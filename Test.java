package com.example.Dashboard;

public class Test {
	
	
	@Controller
	public class YourController {
	    @Autowired
	    private DecisionService decisionService;

	    @GetMapping("/yourPage")
	    public String yourPage(Model model) {
	        List<String> decisionServices = decisionService.getAllDecisionServices();
	        model.addAttribute("decisionServices", decisionServices);
	        return "yourPage"; // Replace with the actual name of your Thymeleaf page
	    }
	}

	
	<select id="decisionService" name="decisionService">
    <option th:each="service : ${decisionServices}" th:value="${service}" th:text="${service}"></option>
</select>


}
