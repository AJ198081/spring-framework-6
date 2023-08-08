package dev.aj.repositories;

import dev.aj.domain.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryIT {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSaveCustomer() {
        Customer customer = Customer.builder()
                .firstName("AJ")
                .lastName("B")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertAll("Asserting on saved Customer",
                () -> Assertions.assertNotNull(savedCustomer.getId()),
                () -> Assertions.assertNotNull(savedCustomer.getCreatedDate()),
                () -> Assertions.assertNotNull(savedCustomer.getLastModifiedDate()),
                () -> Assertions.assertEquals(customer.getFirstName(), savedCustomer.getFirstName()),
                () -> Assertions.assertEquals(customer.getLastName(), savedCustomer.getLastName())
                );
    }
}