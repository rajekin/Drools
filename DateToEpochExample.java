import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.Base64;

public class RestRegressionRunner {

    private static final String INPUT_FOLDER = "input_jsons";
    private static final String OUTPUT_FOLDER = "responses";
    private static final String ENDPOINT_URL = "https://your-endpoint.com/api"; // Replace with actual endpoint
    private static final String USERNAME = "yourUsername"; // Replace with actual username
    private static final String PASSWORD = "yourPassword"; // Replace with actual password

    private static List<Long> responseTimes = new ArrayList<>();
    private static List<String> failedRequests = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(OUTPUT_FOLDER));

            List<Path> jsonFiles = Files.list(Paths.get(INPUT_FOLDER))
                                        .filter(p -> p.toString().endsWith(".json"))
                                        .collect(Collectors.toList());

            for (Path path : jsonFiles) {
                String json = new String(Files.readAllBytes(path));
                String fileName = path.getFileName().toString();

                try {
                    long start = System.nanoTime();
                    String response = postJson(json);
                    long end = System.nanoTime();
                    long durationMs = (end - start) / 1_000_000;

                    responseTimes.add(durationMs);

                    // Save response to output folder
                    Path outputFile = Paths.get(OUTPUT_FOLDER, "response_" + fileName);
                    Files.write(outputFile, response.getBytes());

                    System.out.printf("Processed %s in %d ms%n", fileName, durationMs);

                } catch (Exception e) {
                    failedRequests.add(fileName);
                    System.err.printf("Failed to process %s: %s%n", fileName, e.getMessage());
                }
            }

            summarizeResults();

        } catch (IOException e) {
            System.err.println("Error setting up input/output folders: " + e.getMessage());
        }
    }

    private static String postJson(String json) throws IOException {
        URL url = new URL(ENDPOINT_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        // Add Basic Auth header
        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static void summarizeResults() {
        System.out.println("\n=== Regression Summary ===");
        double avgTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        System.out.printf("Total Requests: %d%n", responseTimes.size() + failedRequests.size());
        System.out.printf("Successful: %d%n", responseTimes.size());
        System.out.printf("Failed: %d%n", failedRequests.size());
        System.out.printf("Average Response Time: %.2f ms%n", avgTime);
        if (!failedRequests.isEmpty()) {
            System.out.println("Failed Files: " + failedRequests);
        }
    }
}
