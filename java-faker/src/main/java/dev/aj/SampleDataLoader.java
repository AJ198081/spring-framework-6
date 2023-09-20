package dev.aj;

import com.github.javafaker.Faker;
import dev.aj.domain.model.entities.Address;
import dev.aj.domain.model.entities.Person;
import dev.aj.domain.model.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final Faker faker;

    @Override
    public void run(String... args) throws Exception {

        List<Person> people = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> getPerson())
                .toList();

        personRepository.saveAll(people);

    }

    private Person getPerson() {
        return Person.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .email(faker.internet().emailAddress())
                .address(
                        Address.builder()
                                .state(faker.address().streetAddress())
                                .city(faker.address().city())
                                .state(faker.address().state())
                                .zip(faker.address().zipCode())
                                .build()
                )
                .build();
    }
}
