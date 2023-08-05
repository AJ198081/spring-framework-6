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

    Map<Long, Customer> customersMap;

    public CustomerServiceImpl() {
        this.customersMap = new HashMap<>();

        Random random = new Random();

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
}
