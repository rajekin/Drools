xample.java
+30
-23
Original file line number	Diff line number	Diff line change
@@ -1,28 +1,35 @@
private static StatementFields setStatementFields(String field1, String field3, String field4,
                                                  DareRestrictRequest inputRest, StatementFields statementFields) {
    if (field1 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField1().equals(field1)) {
            statementFields.setField1(null);
        } else {
            statementFields.setField1(field1);
        }
    }
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

    if (field3 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField3().equals(field3)) {
            statementFields.setField3(null);
        } else {
            statementFields.setField3(field3);
        }
    }
public class JsonFileUtil {

    if (field4 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField4().equals(field4)) {
            statementFields.setField4(null);
        } else {
            statementFields.setField4(field4);
    /**
     * Converts a Java object to pretty-printed JSON and saves it as a file
     * with an incremented filename (e.g. scenario1.json, scenario2.json).
     *
     * @param obj      the object to convert
     * @param baseName the base name of the file (e.g., \"scenario\")
     * @param folder   the output folder (e.g., \"output\")
     */
    public static void convertObjectToJsonFile(Object obj, String baseName, String folder) {
        try {
            File directory = new File(folder);
            if (!directory.exists()) directory.mkdirs();
            int counter = 1;
            File file;
            do {
                file = new File(directory, baseName + counter + \".json\");
                counter++;
            } while (file.exists());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, obj);
            System.out.println(\"✅ Saved: \" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
