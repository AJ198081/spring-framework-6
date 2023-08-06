package dev.aj.service.implementations;

import dev.aj.domain.model.Customer;
import dev.aj.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Random random;
    Map<Long, Customer> customersMap;

    public CustomerServiceImpl() {
        this.customersMap = new HashMap<>();

        random = new Random();

        Customer customer1 = Customer.builder()
                .id(random.nextLong(100, 5000))
                .version(1)
                .firstName("AJ")
                .lastName("B")
                .createdDate(LocalDateTime.now().minusDays(7L))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(random.nextLong(100, 5000))
                .version(1)
                .firstName("D")
                .lastName("S")
                .createdDate(LocalDateTime.now().minusDays(7L))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(random.nextLong(100, 5000))
                .version(1)
                .firstName("R")
                .lastName("B")
                .createdDate(LocalDateTime.now().minusDays(7L))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customersMap.put(customer1.getId(), customer1);
        customersMap.put(customer2.getId(), customer2);
        customersMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customersMap.values().stream().toList();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customersMap.getOrDefault(id, new Customer());
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        customer.setId(random.nextLong(5000, 10000));
        customer.setCreatedDate(LocalDateTime.now());
        customer.setLastModifiedDate(LocalDateTime.now());
        customer.setVersion(1);

        customersMap.put(customer.getId(), customer);

        return customersMap.get(customer.getId());
    }

    @Override
    public Customer updateExistingCustomer(Long id, Customer customer) {
        return customersMap.containsKey(id) ? updateCustomer(id, customer) : saveNewCustomer(customer);
    }

    private Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customersMap.get(id);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        return existingCustomer;
    }

}
