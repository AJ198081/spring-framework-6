package dev.aj.controllers;

import dev.aj.domain.model.Customer;
import dev.aj.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
