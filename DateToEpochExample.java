import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonFileToObjectExample {
    public static void main(String[] args) {
        // Specify the path to the JSON file
        File file = new File("data.json");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert JSON file to Java object
            Person person = objectMapper.readValue(file, Person.class);
            System.out.println(person); // Outputs: Person{name='John Doe', age=30, email='john.doe@example.com'}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
