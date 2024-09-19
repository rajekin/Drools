import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Define the base path to the resources folder
            String basePath = "src/main/resources/request";
            
            // Ensure the directory exists
            File directory = new File(basePath);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the request folder if it doesn't exist
            }
            
            // Create example objects
            List<MyClass> objects = List.of(
                new MyClass("Alice", 25),
                new MyClass("Bob", 30),
                new MyClass("Charlie", 35)
            );
            
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Loop through the list of objects and write each to a dynamically named JSON file
            for (MyClass object : objects) {
                // Generate a timestamp for the file name
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                
                // Create the file name with the timestamp
                String fileName = basePath + "/req_" + timestamp + ".json"; 
                
                // Convert the object to JSON and write to the file in src/main/resources/request
                objectMapper.writeValue(new File(fileName), object);
                
                // Optional: Wait for a small interval to ensure unique timestamps
                Thread.sleep(1);
            }
            
            System.out.println("JSON files created in src/main/resources/request folder.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MyClass {
    private String name;
    private int age;

    public MyClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters (if needed)
}
