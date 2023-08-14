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
        // Check if a person with the same email already exists
    	boolean personExists = personRepository.existsByEmail(person.getEmail());
        if (personExists) {
            model.addAttribute("errorMessage", "Person with this email already exists");
            model.addAttribute("person", person); // Keep the user input for the form
            return "intakeForm";
        }
        
        personRepository.save(person);
        model.addAttribute("successMessage", "Person saved successfully");
        return "redirect:/search?personId=" + person.getId();
    }


    @GetMapping("/search")
    public String showSearchPage(@RequestParam(required = false) String searchType,
                                 @RequestParam(required = false) String searchValue,
                                 Model model) {
        List<Person> persons = new ArrayList<>();
        if (searchType != null && searchValue != null && !searchValue.isEmpty()) {
            if ("state".equals(searchType)) {
                persons = personRepository.findByState(searchValue);
            } else if ("name".equals(searchType)) {
                persons = personRepository.findByName(searchValue);
            } else if ("age".equals(searchType)) {
                try {
                    int age = Integer.parseInt(searchValue);
                    persons = personRepository.findByAge(age);
                } catch (NumberFormatException e) {
                    // Invalid age input, do nothing
                }
            }
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
