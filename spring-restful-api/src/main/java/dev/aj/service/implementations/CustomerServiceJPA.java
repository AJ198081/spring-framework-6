package dev.aj.service.implementations;

import dev.aj.domain.model.Customer;
import dev.aj.repositories.CustomerRepository;
import dev.aj.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public Customer getCustomerById(Long id) {
        return null;
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        return null;
    }

    @Override
    public Customer updateExistingCustomer(Long id, Customer customer) {
        return null;
    }

    @Override
    public void deleteExistingCustomer(Long id) {

    }
}
