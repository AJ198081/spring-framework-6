package dev.aj.controller;

import dev.aj.domain.model.entities.Person;
import dev.aj.domain.model.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping("")
    public ResponseEntity<Iterable<Person>> getPeople() {
        Iterable<Person> all = personRepository.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/findByName")
    public ResponseEntity<Person> getPersonByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
