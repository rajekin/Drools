package com.example.Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/intake")
    public String showIntakeForm(Model model) {
        model.addAttribute("person", new Person());
        return "intakeForm";
    }

    @PostMapping("/intake")
    public String submitIntakeForm(@ModelAttribute Person person, Model model) {
        boolean decisionServiceExists = personRepository.existsByDecisionServiceAndVersion(person.getDecisionService(), person.getVersion());

       

        if (decisionServiceExists) {
            model.addAttribute("errorMessage", "Decision Service with this version already exists");
            model.addAttribute("person", person);
            return "intakeForm";
        }
        
        personRepository.save(person);
        model.addAttribute("successMessage", "Person saved successfully");
        return "redirect:/details/" + person.getId();
    }



   
    @GetMapping("/search")
    public String showSearchPage(@RequestParam(required = false) String searchType,
                                 @RequestParam(required = false) String searchValue,
                                 Model model) {
        List<Person> persons = new ArrayList<>();
        if ("estimatedDueDate".equals(searchType)) {
            persons = personRepository.findByEstimatedDueDate(searchValue);
        } else if ("decisionService".equals(searchType)) {
            persons = personRepository.findByDecisionService(searchValue);
        }

        model.addAttribute("persons", persons);
        return "searchPage";
    }

    
    
    @GetMapping("/details/{id}")
    public String showDetailsPage(@PathVariable Long id, Model model) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            return "detailsPage"; // Create a details page HTML for displaying the person's details
        } else {
            // Handle case when person with given ID is not found
            return "redirect:/search";
        }
    }
}
