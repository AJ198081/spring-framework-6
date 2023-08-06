package dev.aj.service;

import dev.aj.domain.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer saveNewCustomer(Customer customer);

    Customer updateExistingCustomer(Long id, Customer customer);
}
