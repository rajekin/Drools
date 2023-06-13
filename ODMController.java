package net.codejava;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class ODMController {

    @GetMapping("/odm")
    public String getOdmPage() {
        return "odm";
    }

    @GetMapping("/trace")
    public String getTraceByDecisionID(@RequestParam("decisionID") String decisionID, Model model) {
        // ODM API endpoint and decision ID parameter
        String apiUrl = "http://odm-api-url/trace/" + decisionID;

        // Basic authentication credentials
        String username = "your-username";
        String password = "your-password";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // Create headers with basic authentication
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedCredentials);

        // Create a request entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send a GET request to the ODM API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

        // Add the API response to the model
        model.addAttribute("response", response.getBody());

        // Return the name of the template to display
        return "odm";
    }
}
