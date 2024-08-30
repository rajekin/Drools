package com.example.decisioncenternotifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionServiceResponse {
    private List<ServiceElement> elements;
    private int totalcount;

    // Getters and Setters

    public List<ServiceElement> getElements() {
        return elements;
    }

    public void setElements(List<ServiceElement> elements) {
        this.elements = elements;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceElement {
        private String id;
        private String internalId;
        private String name;
        private String buildMode;
        private String advancedProperties;

        // Getters and Setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInternalId() {
            return internalId;
        }

        public void setInternalId(String internalId) {
            this.internalId = internalId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBuildMode() {
            return buildMode;
        }

        public void setBuildMode(String buildMode) {
            this.buildMode = buildMode;
        }

        public String getAdvancedProperties() {
            return advancedProperties;
        }

        public void setAdvancedProperties(String advancedProperties) {
            this.advancedProperties = advancedProperties;
        }
    }
}



-------------------
package com.example.decisioncenternotifier;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class DecisionCenterService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "https://your-decision-center-url/decisioncenter-api/v1/decisionservice";
    private final String filePath = "services.txt"; // Path to the file to store service names

    public DecisionCenterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DecisionServiceResponse getDecisionServices() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("username", "password");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUrl, HttpMethod.GET, entity, DecisionServiceResponse.class).getBody();
    }

    public void processDecisionServices(DecisionServiceResponse response) throws Exception {
        Set<String> currentServiceNames = new HashSet<>();
        for (DecisionServiceResponse.ServiceElement element : response.getElements()) {
            String serviceName = element.getName();
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


---------------------------------------------------



package com.example.decisioncenternotifier;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

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
        DecisionServiceResponse response = decisionCenterService.getDecisionServices();
        decisionCenterService.processDecisionServices(response);
    }
}





