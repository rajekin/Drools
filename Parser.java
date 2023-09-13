package com.example.Dashboard;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
public class Parser {
	
//	    public static void main(String[] args) {
//	        // Define the JSON file path within the resources directory
//	        String jsonFilePath = "/r.json"; // Replace with your JSON file name
//
//	        // Read the JSON file as a String
//	        String jsonString = readJsonFile(jsonFilePath);
//
//	        // Parse the JSON and extract the desired values
//	        List<String> businessNames = extractBusinessNames(jsonString);
//
//	        // Print the extracted business names
//	        for (String businessName : businessNames) {
//	            System.out.println(businessName);
//	        }
//	        
//	        Set<String> rulesFired =  generateRulesFired(jsonFilePath);
//	     // Print the extracted rule names
//	        for (String ruleFired : rulesFired) {
//	            System.out.println(ruleFired);
//	        }
//	        
//	        List<String> elementsNotInSet = new ArrayList<>();
//	        for (String element : businessNames) {
//	            if (!rulesFired.contains(element)) {
//	            	 System.out.println("**NOT IN"+element);
//	                elementsNotInSet.add(element);
//	            }
//	        }
//	        System.out.println(elementsNotInSet.size());
//	    }
	        
	        
	    // Function to read a JSON file from the resources directory
	    public static String readJsonFile(String filePath) {
	        StringBuilder jsonContent = new StringBuilder();

	        try (InputStream inputStream = Parser.class.getResourceAsStream(filePath);
	             Scanner scanner = new Scanner(inputStream)) {

	            while (scanner.hasNextLine()) {
	                jsonContent.append(scanner.nextLine());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return jsonContent.toString();
	    }

	    // Function to extract "ilog.rules.business_name" values from JSON
	    public static List<String> extractBusinessNames(String jsonString) {
	        List<String> businessNames = new ArrayList<>();

	        JSONArray jsonArray = new JSONArray(jsonString);
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            if (jsonObject.has("ruleset")) {
	                JSONObject ruleset = jsonObject.getJSONObject("ruleset");
	                if (ruleset.has("rulesInformation")) {
	                    JSONObject rulesInformation = ruleset.getJSONObject("rulesInformation");
	                    for (String key : rulesInformation.keySet()) {
	                        JSONObject ruleInfo = rulesInformation.getJSONObject(key);
	                        if (ruleInfo.has("businessName")) {
	                            String businessName = ruleInfo.getString("businessName");
	                            businessNames.add(businessName);
	                        }
	                        
	                    }
	                }
	            }
	        }

	        return businessNames;
	    }
	    
	    
	    
	    public static Set<String> extractRuleNames(String jsonString) {
	        Set<String> ruleNames = new HashSet<>();

	        JSONArray jsonArray = new JSONArray(jsonString);
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            if (jsonObject.has("execution")) {
	                JSONObject execution = jsonObject.getJSONObject("execution");
	            if (execution.has("events")) {
	                JSONArray eventsArray = execution.getJSONArray("events");
	                for (int j = 0; j < eventsArray.length(); j++) {
	                    JSONObject event = eventsArray.getJSONObject(j);
	                    if (event.has("type") && event.getString("type").equals("rule") && event.has("name")) {
	                        String ruleName = event.getString("name");
	                        ruleNames.add(ruleName);
	                    }
	                }
	            }
	        }
	      }     
	        return ruleNames;
	    
	    }
	    
	    
	    
	    
	    /**
		 * @param jsonFilePath
		 */
		public static HashSet<String> generateRulesFired(String jsonFilePath) {
			// Read the JSON file as a String
			String jsonString = readJsonFile(jsonFilePath);
			 HashSet<String> ruleNames = new HashSet<>();
	      try {
			 JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
	     
			// Check if "execution" node exists
			if (jsonObject.has("execution")) {
			    // Get the "execution" object
			    JSONObject executionObject = jsonObject.getJSONObject("execution");
			    // Check if "events" array exists within the "execution" object
			    if (executionObject.has("events")) {
			        // Get the "events" array
			        JSONArray eventsArray = executionObject.getJSONArray("events");
			        // Create a HashSet to store "name" values for "eventType" = "rule"
			       
			        // Parse the "events" array recursively and add "name" values to the HashSet
			        parseEvents(eventsArray, ruleNames);
			        // Print the HashSet
			        System.out.println("Rule Names: " + ruleNames);
			    } else {
			        System.out.println("No 'events' array found within 'execution' object.");
			    }
			} else {
			    System.out.println("No 'execution' object found.");
			}
			}
	      } catch (Exception e) {
			e.printStackTrace();
	      }
		return ruleNames;
		}
	    
	    
		 public static void parseEvents(JSONArray eventsArray, HashSet<String> ruleNames) {
		        for (int i = 0; i < eventsArray.length(); i++) {
		            JSONObject eventObject = eventsArray.getJSONObject(i);

		            // Extract data from the eventObject as needed
		            String eventType = eventObject.getString("type");
		            String eventName = eventObject.getString("name");
		            // Check if the eventType is "rule"
		            if ("rule".equals(eventType)) {
		                // Add the "name" to the HashSet
		                ruleNames.add(eventName);
		            }
		            // Check if there are nested "events" arrays
		            if (eventObject.has("events")) {
		                JSONArray nestedEventsArray = eventObject.getJSONArray("events");
		                // Recursively parse the nested "events" array
		                parseEvents(nestedEventsArray, ruleNames);
		            }
		        }
		    }
		
	    
	}



