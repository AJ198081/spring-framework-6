package dev.aj.controllers;

import dev.aj.domain.model.Customer;
import dev.aj.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("Request received to fetch all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/customer/{customerId}")
    public Customer getCustomerById(@PathVariable(value = "customerId") Long id) {
        log.info("Request received to fetch customer Id - {}", id);
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/customer/" + savedCustomer.getId());

        return new ResponseEntity<>(savedCustomer, httpHeaders, HttpStatus.OK);
    }

    @PutMapping(path = "/customer/{customerId}")
    public ResponseEntity<Customer> updateExistingCustomer(@PathVariable(value = "customerId") Long id, @RequestBody Customer customer) {

        Customer updatedCustomer = customerService.updateExistingCustomer(id, customer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);

    }


}
