package dev.aj.domain.model.repositories;

import dev.aj.domain.model.entities.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query("select p from Person p where p.firstName = ?1 and p.lastName = ?2")
    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);
}
