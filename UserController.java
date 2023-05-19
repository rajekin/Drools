package net.codejava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;



@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, 
                        @RequestParam("password") String password,
                        Model model) {
        if (userService.isValidUser(username, password)) {
            model.addAttribute("successMessage", "Login successful!");
            return "redirect:/brmstools";
        } else {
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "login";
        }
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "home";
        } else {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/brmstools")
    public String brmstools(Model model) {
        List<Server> servers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Raj\\Downloads\\SpringBootForm\\SpringBootForm\\servers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String url = parts[1].trim();
                Server server = new Server(name, url);
                servers.add(server);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("servers", servers);
        return "brmstools";
    }

    @GetMapping("/status")
    public String status(Model model) {
        List<ServerStatus> decisionCenterStatusList = new ArrayList<>();
        List<ServerStatus> ruleExecutionServerStatusList = new ArrayList<>();
        int decisionCenterUpCount = 0;
        int ruleExecutionServerUpCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Raj\\Downloads\\SpringBootForm\\SpringBootForm\\servers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String url = parts[1].trim();
                try {
                    URL serverUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
                    conn.setRequestMethod("HEAD");
                    int responseCode = conn.getResponseCode();
                    boolean isUp = (200 <= responseCode && responseCode <= 399);
                    LocalDateTime lastResStatusCheck = LocalDateTime.now();
                    ServerStatus serverStatus = new ServerStatus(name, isUp, lastResStatusCheck, url);
                    if (url.contains("DecisionCenter")) {
                        decisionCenterStatusList.add(serverStatus);
                        if (isUp) {
                            decisionCenterUpCount++;
                        }
                    } else if (url.contains("res")) {
                        ruleExecutionServerStatusList.add(serverStatus);
                        if (isUp) {
                            ruleExecutionServerUpCount++;
                        }
                    }
                } catch (MalformedURLException e) {
                    // Handle MalformedURLException
                    e.printStackTrace();
                } catch (IOException e) {
                    // Handle IOException
                    boolean isUp = false;
                    LocalDateTime lastResStatusCheck = LocalDateTime.now();
                    ServerStatus serverStatus = new ServerStatus(name, isUp, lastResStatusCheck, url);
                    if (url.contains("DecisionCenter")) {
                        decisionCenterStatusList.add(serverStatus);
                    } else if (url.contains("res")) {
                        ruleExecutionServerStatusList.add(serverStatus);
                    }
                }
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
        int decisionCenterTotalServers = decisionCenterStatusList.size();
        int ruleExecutionServerTotalServers = ruleExecutionServerStatusList.size();
        double decisionCenterUpPercentage = decisionCenterTotalServers > 0 ? (decisionCenterUpCount * 100.0) / decisionCenterTotalServers : 0;
        double ruleExecutionServerUpPercentage = ruleExecutionServerTotalServers > 0 ? (ruleExecutionServerUpCount * 100.0) / ruleExecutionServerTotalServers : 0;
        model.addAttribute("decisionCenterStatusList", decisionCenterStatusList);
        model.addAttribute("ruleExecutionServerStatusList", ruleExecutionServerStatusList);
        model.addAttribute("decisionCenterUpPercentage", decisionCenterUpPercentage);
        model.addAttribute("ruleExecutionServerUpPercentage", ruleExecutionServerUpPercentage);
        return "status";
    }




    
        
    
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        try {
            FileWriter writer = new FileWriter("user_details.txt", true);
            writer.write(username + "," + password + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while saving user details");
            return "register";
        }

        model.addAttribute("success", "User registered successfully");
        return "register";
    }
    
}
