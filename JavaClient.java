/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2020. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.json.java.JSONObject;

/**
 *  The JavaClient class defines an authenticated Java client to execute requests by using the REST API.
 *  The JavaClient class executes two RuleSets, one is a decision service built with decision engine and another one built with classic rule engine.
 *  The JavaClient class also produces a simplified execution trace, as well as remove the RuleApps from RES for cleanup.
 */
public class JavaClient {
	protected CloseableHttpClient m_client;
	protected HttpClientContext m_context;
	private String m_host;
	private String m_port;

	/**
	 * This constructor defines the preemptive basic HTTP authentication.
	 */
	public JavaClient() {
		initProperties();
		HttpHost targetHost = new HttpHost(getServerName(), getServerPortNumber(), "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials("resAdmin", "resAdmin"));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		this.m_context = HttpClientContext.create();
		this.m_context.setCredentialsProvider(credsProvider);
		this.m_context.setAuthCache(authCache);		

		this.m_client = HttpClientBuilder.create().build(); 
	}

	/**
	 * Executes  a ruleset asking for all traces. 
	 * Two files are created in the output directory one containing the full trace another with a simplified execution trace.
	 * The parameters are:        
	 *       - the RuleApp name
	 *       - the ruleset name without extension. 
	 *       - the output file name. Two files are created in the output directory: 
	 *            <outputFile>.txt for the full execution trace and <outputFile>exec.txt for the simplified trace
	 *       - the input parameters file name including the extension as params.xml. The parameters are written in XML.
	 *       - the execution id
	 */
    public void execute(String ruleapp, String ruleset, String outputFile, String paramsFile, String id) {
		String parameters = getParametersFromFile(paramsFile);
		String check =  canExecute(ruleapp, parameters, paramsFile);
		if (check == null) {
			String capruleapp =  ruleapp.substring(0,1).toUpperCase() + ruleapp.substring(1);
			String capruleset =  ruleset.substring(0,1).toUpperCase() + ruleset.substring(1);
			// Parameters  definitions
			String rulesetParams = "<par:Request xmlns:par=\"http://www.ibm.com/rules/decisionservice/" + capruleapp + "/"
					+ capruleset + "/param\" xmlns:trac=\"http://www.ibm.com/rules/decisionservice/tracefilter\">"
			  	    + "<par:DecisionID>"
			        + id + "</par:DecisionID>" 
					+  "<trac:decisionTraceFilter><trac:all>true</trac:all></trac:decisionTraceFilter>"
				    +  parameters
				    + "</par:Request>";
			// Execute a ruleset
			String response = executePostMethod(getExecutionServiceUrl(ruleapp, ruleset), rulesetParams, null);
			// Write the full trace and the simplified trace in the output directory.
			File outfile = writeTrace(response, outputFile);
			writeTrace(getExecutionTrace(outfile, ruleset, parameters), getExecutionFileName(outputFile));
		} else
			System.out.println(check);
	}
    
	/**
	 * Checks if the ruleapp named ruleAppName already exists and if the parameters were read
	 * @return null when the execution can be done else a message with the reason why it cannot execute.
	 */
	private String canExecute(String ruleAppName, String parameters,
			String paramsFile) {
		String urlRuleApp = getRestAuthenticatedUrl() + "/ruleapps/"
				+ ruleAppName + "/1.0";
		JSONObject response = executeJSONGetMethod(urlRuleApp);
		if (response == null) {
			return "ruleapp " + ruleAppName + "doesn't exist. Deploy it first.";
		}
		if (parameters == null)
			return "Cannot read the parameters from data/" + paramsFile;
		return null;
	}

	/**
	 * Reads the parameters from a file.
	 * The parameter is:
	 *       - paramsFile the name with the extension of the file containing the parameters.
	 *       The file is in the data directory in the jar.
	 * @return null when the parameters cannot be read. Otherwise, the method returns the content of the file as a string.
	 */
	private String getParametersFromFile(String paramsFile) {
		try {
			InputStream params = getClass().getResourceAsStream(
					"../data/" + paramsFile);
			byte[] b = new byte[params.available()];
			params.read(b);
			return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Displays two files 
	 * The parameters are:        
	 *       - first file taken in the output directory
	 *       - second file taken in the output directory
	 *       - all a boolean indicating if the complete files are displayed or just the differences 
	 *       - parallel a boolean indicating if the files are displayed side by side or sequentially. 
	 */
	public void displayFiles(String file1, String file2, boolean all,
			boolean parallel) {
		// Build the arrays of strings to be displayed
		FileInputStream is1;
		ArrayList<String> lines1 = new ArrayList<String>();
		List<String> lines2 = new ArrayList<String>();
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		int length = 0;
		if (all)
			System.out.println(" *** Full Execution trace: ");
		else
			System.out.println(" *** Execution trace differences: ");
		try {
			is1 = new FileInputStream("output/" + file1);
			FileInputStream is2 = new FileInputStream("output/" + file2);
			reader1 = new BufferedReader(new InputStreamReader(
					is1));
			reader2 = new BufferedReader(new InputStreamReader(
					is2));
			String line1 = reader1.readLine();
			String line2 = reader2.readLine();
			while (line1 != null) {
				if (line2 == null) {
					// remove the space at the end of the line
					lines1.add(line1.replaceAll("\\s+$", ""));
					lines2.add("");
				} else if (all || !line1.equals(line2)) {
					lines1.add(line1.replaceAll("\\s+$", ""));
					lines2.add(line2);
					if (line1.length() > length)
						length = line1.length();
				}
				line1 = reader1.readLine();
				line2 = reader2.readLine();
			}
			while (line2 != null) {
				lines2.add(line2);
				lines1.add("");
				line2 = reader2.readLine();
			}
			length = length + 2;
			// Print the files
			if (parallel) {
				for (int i = 0; i < lines1.size(); i++) {
					System.out.print(lines1.get(i));
					for (int j = 0; j < length - lines1.get(i).length(); j++)
						System.out.print(" ");
					System.out.println("|  " + lines2.get(i));
				}
				System.out.println(" \n");
			} else {
				for (int i = 0; i < lines1.size(); i++) {
					System.out.println(lines1.get(i));
				}
				System.out.println(" \n");
				for (int i = 0; i < lines2.size(); i++) {
					System.out.println(lines2.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader1.close();
				reader2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    

	/**
	 * Removes the RuleApp and XOM from the execution server database.
	 * The parameters are:        
	 *       - the ruleapp name
	 *       - the xom name
	 */
	public void clean(String ruleAppName, String xomName) {
		String urlRuleApp = getRestAuthenticatedUrl() + "/ruleapps/"
				+ ruleAppName + "/1.0";
		String urlThisXom = getRestAuthenticatedUrl() + "/v1/xoms/"
				+ xomName;
		String urllLibrary = getRestAuthenticatedUrl() + "/libraries/"
				+ ruleAppName + "_1.0/1.0";
		// Deletes the rule application
		executeDeleteMethod(urlRuleApp);
		// Deletes the XOM
		if (xomName != null && !xomName.isEmpty())
		   executeDeleteMethod(urlThisXom + "/1.0");
		// Delete the library ref
		executeDeleteMethod(urllLibrary);
	}

	/**
	 * Gives the usage of the class.
	 */
	public void usage() {
		System.out.println("Usage:    JavaClient Execute <id> <ruleappName> <rulesetName> <paramFile> <outputFile>");
		System.out.println("Or     JavaClient Display <file1> <file2>  <parallel|sequential> [all] ");
		System.out.println("Or     JavaClient Clean <ruleappName> [xomName]");
	}


	/** Main: instantiates a JavaClient instance and checks the arguments to call the appropriate API.
	 *  
	 */
	public static void main(String[] args) {
		// Initialization gives credentials
		JavaClient client = new JavaClient();
		if (args.length == 0 || args.length > 6) {
			client.usage();
			return;
		}
		String scenarioName = args[0];
		
		if (scenarioName.equals("Execute")) {
			String id = args[1];
			String ruleapp = args[2];
			String ruleset = args[3];
			String paramFilename = args[4];
			String outputFilename = args[5];
			client.execute(ruleapp, ruleset, outputFilename, paramFilename, id);
		}
		if (scenarioName.equals("Display")) {
			if (args.length > 5) { 
				client.usage();
				return;
			}
			String file1 = args[1];
			String file2 = args[2];
			boolean parallel = true;
			if (args[3].equals("sequential"))
				parallel = false;
			boolean all = false;
			if (args.length > 4 && args[4].equals("all")) all = true;
			client.displayFiles(file1, file2, all, parallel);
		}
		if (scenarioName.equals("Clean")) {
			if (args.length > 4) { 
				client.usage();
				return;
			}
			String ruleAppName = args[1];
			String xom = null;
			if (args.length > 2)
			  xom = args[2];
			client.clean(ruleAppName, xom);
		}
		client.closeHttpClient();
	}
	

	/******************* Utilities 
	 */

	/**
	 * @return the name of the output file for the simplified execution trace
	 */
	private String getExecutionFileName(String fileName) {
		return fileName + "exec";
	}
	
	
	/**
	 * Close HttpClient instance
	 */
	public void closeHttpClient(){
		if(m_client != null){
			try {
				m_client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Builds the simplified execution trace by parsing with DOM the full XML trace obtained by calling the REST API
	 * The parameters are:        
	 *      - the name of the output file containing the full trace 
	 *      - the ruleset name that was executed
	 *      - the input parameters used 
	 * @return the simplified execution trace
	 */
	private String getExecutionTrace(File outfile, String ruleset, String parameters) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new FileInputStream(outfile), "UTF-8");
			Node trace = doc.getDocumentElement()
					.getElementsByTagName("decisionTrace").item(0);
			NodeList tracec = trace.getChildNodes();
			Node executionEvents = null;
			Node totalRulesFired = null;
			Node totalTasksExecuted = null;
			Node outputString = null;
			int i = 0;
			while (i < tracec.getLength() && executionEvents == null) {
				Node child = tracec.item(i);
				if (child.getNodeName().equals("executionEvents")) {
					executionEvents = child;
				}
				if (child.getNodeName().equals("totalRulesFired")) {
					totalRulesFired = child;
				}
				if (child.getNodeName().equals("totalTasksExecuted")) {
					totalTasksExecuted = child;
				}
				if (child.getNodeName().equals("outputString")) {
					outputString = child;
				}
				i++;
			}
			String result = "* Ruleset " + ruleset + "\n"
					+ "* Parameters\n" + parameters
			        + "* " + totalRulesFired.getTextContent()
					+ " rules fired.\n  " + totalTasksExecuted.getTextContent()
					+ " tasks executed. \n" + getOutputString(outputString) 
					+ exploreExecutionEvents(executionEvents, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * Parses and builds the display of the output string node.
	 * The parameter is:        
	 *      - the node of the outputString
	 * @return the output string to be displayed in the simplified trace
	 */
	private String getOutputString(Node outputString) {
		NodeList list = outputString.getChildNodes();
		return  "* Output \n" + list.item(0).getNodeValue();
	}


	/**
	 * Parses and builds the display of the execution events node.
	 * This function is recursive since the taskEvent may contain executionevents node.
	 * The parameters are:        
	 *      - the node of the execution events
	 *      - the indentation string
	 * @return the execution events to be displayed in the simplified trace
	 */
	private String exploreExecutionEvents(Node event, String indent) {
		String result = "";
		NodeList events = event.getChildNodes();
		int i = 0;
		while (i < events.getLength()) {
			Node node = events.item(i);
			if (node.getNodeName().equals("taskEvent")) {
				result = result + exploreTask(node, indent);
			}
			if (node.getNodeName().equals("ruleEvent")) {
				result = result + exploreRule(node, indent);
			}
			i++;
		}
		return result;
	}

	/**
	 * Parses and builds the display of the rule event node.
	 * The parameters are:        
	 *      - the node of the rule event
	 *      - the indentation string
	 * @return the rule event to be displayed in the simplified trace
	 */
	private String  exploreRule(Node node, String indent) {
		NodeList childs = node.getChildNodes();
		int i = 0;
		while (i < childs.getLength()) {
			Node child = childs.item(i);
			if (child.getNodeName().equals("ruleInformation"))
				return writeRule(child, indent);
			i++;
		}
		return "";
	}

	/**
	 * Parses and builds the display of the task event node.
	 * The parameters are:        
	 *      - the node of the task event
	 *      - the indentation string
	 * @return the task event to be displayed in the simplified trace
	 */
	private String exploreTask(Node task, String indent) {
		String result = "";
		NodeList taskChilds = task.getChildNodes();
		int i = 0;
		while (i < taskChilds.getLength()) {
			Node info = taskChilds.item(i);
			if (info.getNodeName().equals("taskInformation"))
				result = result + writeTask(info, indent);
			if (info.getNodeName().equals("executionEvents"))
				result = result + exploreExecutionEvents(info, indent + "   ");
			i++;
		}
		return result;
	}

	/**
	 * Parses and displays the task information node.
	 * The parameters are:        
	 *      - the node of the task information
	 *      - the indentation string
	 * @return the task information to be displayed in the simplified trace.
	 */
	private String writeTask(Node info, String indent) {
		String result = "";
		String begin = "-";
		if (indent.isEmpty())
			begin = "*";
		NodeList taskChilds = info.getChildNodes();
		int i = 0;
		while (i < taskChilds.getLength()) {
			Node c = taskChilds.item(i);
			if (c.getNodeName().equals("businessName")) {
				NodeList list = c.getChildNodes();
				return indent + begin + " Task: " + list.item(0).getNodeValue() + "\n";
			}
			i++;
		}
		return result;
	}
	/**
	 * Parses and displays the rule information node.
	 * The parameters are:        
	 *      - the node of the rule information
	 *      - the indentation string
	 * @return the rule information to be displayed in the simplified trace.
	 */
	private String  writeRule(Node info, String indent) {
		String bname = "";
		String dtOrRule = "";
		NodeList taskChilds = info.getChildNodes();
		int i = 0;
		while (i < taskChilds.getLength()) {
			Node c = taskChilds.item(i);
			if (c.getNodeName().equals("businessName")) {
				NodeList list = c.getChildNodes();
				bname = list.item(0).getNodeValue();
			}
			if (c.getNodeName().equals("properties")) {
				dtOrRule = getRuleType(c);
				}
			i++;
		}
		return indent + "- " + dtOrRule + ": " + bname+ "\n";
	}

	/**
	 * Parses the rule information node to get if it is a DT or not.
	 * The parameter is:        
	 *      - the node of the rule information
	 * @return DT or Rule
	 */
	private String getRuleType(Node node) {
		NodeList childs = node.getChildNodes();
		int i = 0;
		while (i < childs.getLength()) {
			Node c = childs.item(i);
			if (c.getNodeName().equals("property")) {
				String name = ((Element)c).getAttribute("name");
				if (name != null && name.equals("ilog.rules.dt")&& c.getChildNodes().getLength() !=0)
					return "DT";
				}
			i++;
		}
       return "Rule";
	}

	/**
	 * Writes a string into a file.
	 * The parameters are:        
	 *      - the string to write
	 *      - the fileName to create to put the string in. The file is located in the output directory.
	 *      The extension is txt.
	 * @return DT or Rule
	 */
	private File writeTrace(String response, String fileName) {
		fileName = "output/" + fileName + ".txt";
		BufferedWriter writer = null;
	    File file = new File(fileName);
		try {
		      // creates the file
		      file.createNewFile();
		    writer = new BufferedWriter
		    	    (new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()),"UTF-8"));
		    writer.write( response);
		}
		catch ( IOException e){
		}
		finally {
		    try {
		        if ( writer != null)
		        writer.close( );
		        return file;
		    }
		    catch ( IOException e) {
		    }
		}		
		return null;
	}

	/**
	 * Get methods
	 */
	/*
	 * Builds the URL to the REST API with an existing authentication.
	 */
	protected String getRestAuthenticatedUrl() {
		return "http://" + getServerName() + ":" + getServerPort()
				+ "/res/apiauth";
	}

	protected String getExecutionServiceUrl(String ruleapp, String ruleset) {
		return "http://" + getServerName() + ":" + getServerPort()
				+ "/DecisionService/rest/v1/" + ruleapp +"/1.0/" + ruleset +"/1.0";
	}

	protected String getServerName() {
		return m_host;
	}

	protected String getServerPort() {
		return m_port;
	}

	protected int getServerPortNumber() {
		return new Integer(getServerPort()).intValue();
	}
	/**
	 * Execution of HTTP methods 
	 */
	protected String executePostMethod(String url, String stringBody,
			byte[] byteBody) {
		HttpPost postMethod = new HttpPost(url);
		try {
			if (stringBody != null){
				StringEntity params = new StringEntity(stringBody, "UTF-8");
				params.setContentType("text/xml");
				params.setContentEncoding("UTF-8");
				postMethod.setEntity(params);
			}
			else{
				ByteArrayEntity param = new ByteArrayEntity(byteBody);
				postMethod.setEntity(param);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// Executes the method.
			CloseableHttpResponse response =  m_client.execute(postMethod, m_context);
			try{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT && statusCode != HttpStatus.SC_CREATED ) {
					System.err.println("Method failed: " + response.getStatusLine());
				}
				HttpEntity entity = response.getEntity();
				if(entity != null){
					return EntityUtils.toString(entity, "UTF-8");
				}
				
			}finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Releases the connection.
			postMethod.releaseConnection();
		}
		return null;
	}

	protected void executeDeleteMethod(String url) {
		executeMethod(url, new HttpDelete(url), false);
	}

	protected JSONObject executeJSONGetMethod(String url) {
		String jsonUrl = url + "?accept=json";
		return executeMethod(jsonUrl, new HttpGet(jsonUrl), true);
	}

	protected JSONObject executeMethod(String url, HttpUriRequest method,
			boolean json) {
		JSONObject responseObject = null;
		try {
			// Executes the method.
			CloseableHttpResponse response =  m_client.execute(method, m_context);
			try{
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT && statusCode != HttpStatus.SC_CREATED ) {
					System.err.println("Method failed: " + response.getStatusLine());
				}
				HttpEntity entity = response.getEntity();
				if(entity != null){
					InputStream stream = entity.getContent();
					if (json) {
						try {
							responseObject = JSONObject.parse(stream);
						} catch (Exception e) {
							// The stream does not contain any JSON object.
							responseObject = null;
						}
					} 
				}
			}finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} 
		return responseObject;

	}

	/**
	 * Initialization of properties 
	 */
	private void initProperties() {
		try {
			ResourceBundle rb = ResourceBundle.getBundle("build");
			m_port = rb.getString("server.port");
			m_host = "localhost";
		} catch (MissingResourceException e) {
			System.out.println("No bundle found. Use the default port and host.");
			m_port = "9080";
			m_host = "localhost";
		}
	}
}
