package com.example.Dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByDecisionServiceAndVersion(String decisionService, String version);
    List<Person> findByEstimatedDueDate(String estimatedDueDate);
    List<Person> findByDecisionService(String decisionService);
}
