import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CombineJsonFiles {
    public static void main(String[] args) {
        // Folder containing individual JSON files
        String inputFolder = "path/to/your/json/folder";
        // Output file that Postman Runner can use
        String outputFile = "path/to/output/data.json";

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode combinedArray = mapper.createArrayNode();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputFolder), "*.json")) {
            for (Path entry : stream) {
                JsonNode json = mapper.readTree(entry.toFile());
                combinedArray.add(json);
            }

            // Write combined array to data.json
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), combinedArray);
            System.out.println("Combined JSON written to: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
