package com.example.Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResponseController {

	@Autowired
	Parser parse;
	
	@GetMapping("/coverage")
    public String showRuleCoverageForm() {
        return "coverage"; // This should match the name of your HTML template file
    }
	
	
	
	
    @GetMapping("/displayResponse")
    public String displayResponse(@RequestParam("decisionService") String decisionService, Model model) {
        String jsonFilePath = "/r.json"; // Replace with your JSON file name
        String jsonString = Parser.readJsonFile(jsonFilePath);
        List<String> businessNames = Parser.extractBusinessNames(jsonString);
        Set<String> firesRulesList =  Parser.generateRulesFired(jsonFilePath);

        List<String> elementsNotInSet = new ArrayList<>();
        for (String element : businessNames) {
            if (!firesRulesList.contains(element)) {
            	 System.out.println("**NOT IN"+element);
                elementsNotInSet.add(element);
            }
        }
    	
    	
    	
    	
        // Replace these with actual data from your API response
        int rulesFired = firesRulesList.size(); // Replace with actual number
        int rulesNotFired = elementsNotInSet.size(); // Replace with actual number
        
        int totalRules = rulesFired + rulesNotFired;
        double percentageCovered = (double) rulesFired / totalRules * 100;
        
        model.addAttribute("decisionServiceName", "Sample Decision Service");
        model.addAttribute("numberOfRulesFired", rulesFired);
        model.addAttribute("numberOfRulesNotFired", rulesNotFired);
        model.addAttribute("rulesFiredList", null);
        model.addAttribute("rulesNotFiredList", null);
        model.addAttribute("percentageCovered", percentageCovered);
        model.addAttribute("businessNames", firesRulesList);
        model.addAttribute("elementsNotInSet", elementsNotInSet);


        
        return "response";
    }
}




