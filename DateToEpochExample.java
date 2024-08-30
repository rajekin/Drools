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
        Set<String> newServices = decisionCenterService.processDecisionServices(response);

        if (!newServices.isEmpty()) {
            String emailContent = buildEmailContent(response.getElements(), newServices);
            emailService.sendEmail("recipient@example.com", "New Decision Services Added", emailContent);
        }
    }

    private String buildEmailContent(Set<String> existingServices, Set<String> newServices) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<h3>New Decision Services Added</h3>");
        emailContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
        emailContent.append("<tr><th>Service Name</th><th>Status</th></tr>");

        // Add existing services
        for (String service : existingServices) {
            emailContent.append("<tr><td>").append(service).append("</td><td>Existing</td></tr>");
        }

        // Add new services
        for (String newService : newServices) {
            emailContent.append("<tr><td>").append(newService).append("</td><td>New</td></tr>");
        }

        emailContent.append("</table>");
        return emailContent.toString();
    }
}
