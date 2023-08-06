package dev.aj.controllers;

import dev.aj.domain.model.Customer;
import dev.aj.service.CustomerService;
import dev.aj.service.implementations.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = {CustomerController.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Customer testCustomer;

    private List<Customer> customers;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .version(1)
                .firstName("AJ")
                .lastName("B")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers = new CustomerServiceImpl().getAllCustomers();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() throws Exception {
        Long randomgLong = new Random().nextLong(100, 345);
        testCustomer.setId(randomgLong);

        Customer anotherTestCustomer = Customer.builder()
                .id(new Random().nextLong())
                .version(1)
                .firstName("DJ")
                .lastName("B")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Mockito.when(customerService.getAllCustomers()).thenReturn(List.of(testCustomer, anotherTestCustomer));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.size()", is(2)),
                        MockMvcResultMatchers.jsonPath("$[0].id", is(randomgLong.intValue())),
                        MockMvcResultMatchers.jsonPath("$[0].firstName", is("AJ")),
                        MockMvcResultMatchers.jsonPath("$[0].lastName", is("B")),
                        MockMvcResultMatchers.jsonPath("$[0].version", is(1))
                );
    }

    @Test
    void getCustomerById() throws Exception {

        Long randomgLong = new Random().nextLong(100, 345);
        testCustomer.setId(randomgLong);

        Mockito.when(customerService.getCustomerById(Mockito.anyLong())).thenReturn(testCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/customer/" + randomgLong).accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id", is(randomgLong.intValue())),
                        MockMvcResultMatchers.jsonPath("$.firstName", is("AJ")),
                        MockMvcResultMatchers.jsonPath("$.lastName", is("B")),
                        MockMvcResultMatchers.jsonPath("$.version", is(1))
                );

    }

    @Test
    @Disabled
    void createNewCustomer() {
    }

    @Test
    @Disabled
    void updateExistingCustomer() {
    }

    @Test
    void deleteExistingCustomer() throws Exception {
        Customer customer = customers.get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/customer/{customerId}", customer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(customerService).deleteExistingCustomer(longArgumentCaptor.capture());

        Assertions.assertThat(longArgumentCaptor.getValue())
                .isEqualTo(customer.getId());

    }
}