import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonFileUtil {

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
}
