import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class DecisionCenterService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "https://your-decision-center-url/decisioncenter-api/v1/decisionservice";
    private final String filePath = "services.txt"; // Path to the file to store service names

    public DecisionCenterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getDecisionServices() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("username", "password");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
    }

    public void processDecisionServices(ResponseEntity<String> response) throws Exception {
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode elements = root.get("elements");
        int totalCount = root.get("totalcount").asInt();

        Set<String> currentServiceNames = new HashSet<>();
        for (JsonNode element : elements) {
            String serviceName = element.get("name").asText();
            currentServiceNames.add(serviceName);
        }

        Set<String> previousServiceNames = loadPreviousServiceNames();
        Set<String> newServices = new HashSet<>(currentServiceNames);
        newServices.removeAll(previousServiceNames);

        if (!newServices.isEmpty()) {
            // Send an email with the new service names
            for (String newService : newServices) {
                System.out.println("New Service Added: " + newService);
            }
            updateServiceNamesFile(currentServiceNames);
        }
    }

    private Set<String> loadPreviousServiceNames() throws IOException {
        Set<String> serviceNames = new HashSet<>();
        if (Files.exists(Paths.get(filePath))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    serviceNames.add(line);
                }
            }
        }
        return serviceNames;
    }

    private void updateServiceNamesFile(Set<String> serviceNames) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String serviceName : serviceNames) {
                writer.write(serviceName);
                writer.newLine();
            }
        }
    }
}


--------------------


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class ScheduledTask {
    private final DecisionCenterService decisionCenterService;
    private final EmailService emailService;

    public ScheduledTask(DecisionCenterService decisionCenterService, EmailService emailService) {
        this.decisionCenterService = decisionCenterService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000) // Check every 60 seconds
    public void checkForNewServices() throws Exception {
        ResponseEntity<String> response = decisionCenterService.getDecisionServices();
        decisionCenterService.processDecisionServices(response);

        // Logic to detect new services and call emailService.sendEmail(...)
        Set<String> newServices = decisionCenterService.getNewServices(response);
        if (!newServices.isEmpty()) {
            String subject = "New Service(s) Added to Decision Center";
            String body = "The following new services were added:\n" + String.join("\n", newServices);
            emailService.sendEmail("recipient@example.com", subject, body);
        }
    }
}


---------------------------------

spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

