package com.example.Dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByState(String state);
    List<Person> findByName(String name);
    List<Person> findByAge(int age);
    boolean existsByEmail(String email);
}


