package dev.aj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    private static final String HOST_NAME = "http://localhost:";
    private static HttpHeaders headers;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @LocalServerPort
    private int portNumber;
    private String newCustomerJson;
    private Long savedCustomerId;
    private Customer savedCustomer;

    @BeforeAll
    static void beforeAll() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setUp() {
        newCustomerJson = """
                    {
                        "firstName": "ZJ",
                        "lastName": "B"
                    }
                """;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void getAllCustomers() {
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(HOST_NAME + portNumber + "/customers"));

        ResponseEntity<List<Customer>> exchange = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Customer>>() {
        });

        List<Customer> responseJson = exchange.getBody();

        savedCustomer = responseJson.get(0);
        savedCustomerId = savedCustomer.getId();

        org.junit.jupiter.api.Assertions.assertEquals(3, responseJson.size());
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    void getCustomerById() {
        RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(HOST_NAME + portNumber + "/customers/customer/" + savedCustomerId));
        ResponseEntity<Customer> exchange = restTemplate.exchange(requestEntity, Customer.class);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals(savedCustomerId, exchange.getBody().getId());
    }

    @Test
    void createNewCustomer() {
        RequestEntity<String> requestEntity = new RequestEntity<>(newCustomerJson, headers, HttpMethod.POST, URI.create(HOST_NAME + portNumber + "/customers"));

        ResponseEntity<Customer> exchange = restTemplate.exchange(requestEntity, Customer.class);

        Assertions.assertThat(exchange).extracting(ResponseEntity::getStatusCode, exchangeVar -> exchangeVar.getBody().getFirstName())
                .contains(HttpStatus.OK, "ZJ");

        Assertions.assertThat(exchange.getHeaders().get("Location").get(0))
                .startsWith("/customer/");

    }

    @Test
    void updateAnExistingCustomer() throws JsonProcessingException {

        savedCustomer.setFirstName("AJ");
        savedCustomer.setLastName("Sikh");

        RequestEntity<Object> requestEntity = new RequestEntity<>(objectMapper.writeValueAsString(savedCustomer), headers, HttpMethod.PUT, URI.create(HOST_NAME + portNumber + "/customers/customer/" + savedCustomer.getId()));

        ResponseEntity<Customer> exchange = restTemplate.exchange(requestEntity, Customer.class);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assertions.assertThat(exchange.getBody()).extracting(Customer::getFirstName, Customer::getLastName)
                .contains("AJ", "Sikh");

    }

    @Test
    void deleteAnExistingCustomer() {

        RequestEntity<Object> requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create(HOST_NAME + portNumber + "/customers/customer/" + savedCustomer.getId()));

        var exchange = restTemplate.exchange(requestEntity, ResponseEntity.class);
        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());

    }
}