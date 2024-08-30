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
            StringBuilder emailContent = new StringBuilder("New services have been added:\n");
            for (String newService : newServices) {
                emailContent.append(newService).append("\n");
            }

            // Assuming emailService has a method to send an email
            emailService.sendEmail("recipient@example.com", "New Decision Services Added", emailContent.toString());
        }
    }
}
