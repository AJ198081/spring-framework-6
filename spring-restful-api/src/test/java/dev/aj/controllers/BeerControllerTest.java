package dev.aj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.enums.BeerStyle;
import dev.aj.domain.model.Beer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static dev.aj.domain.enums.BeerStyle.LAGER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeerControllerTest {

    private static HttpHeaders httpHeaders;
    private static String newBeerJson;

    private String beerUrl;
    private UUID savedBeerUuid;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int portNumber;
    private Beer savedBeer;

    @BeforeAll
    static void beforeAll() {

        newBeerJson = """
                    {
                      "version": 1,
                      "beerName": "Kensington City",
                      "beerStyle": "LAGER",
                      "upc": "123456789",
                      "quantityOnHand": 100,
                      "price": 14.99
                    }
                """;

        httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    void setUp() {
        beerUrl = "http://localhost:" + portNumber + "/beer";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void List_Beers() throws JsonProcessingException {
        RequestEntity<Object> requestEntityForAllBeers = new RequestEntity<>(HttpMethod.GET, URI.create(beerUrl.concat("/all")));

        ResponseEntity<List<Beer>> exchange = restTemplate.exchange(requestEntityForAllBeers, new ParameterizedTypeReference<>() {
        });

        List<Beer> body = exchange.getBody();
        savedBeer = body.get(0);
        savedBeerUuid = body.stream().map(Beer::getId).findFirst().orElseThrow();

        Assertions.assertThat(exchange)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody())
                .hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(2)
    void Get_Beer_By_Id() {
        RequestEntity<Object> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create(beerUrl.concat("/id/" + savedBeerUuid.toString())));

        ResponseEntity<Beer> responseEntity = restTemplate.exchange(requestEntity, Beer.class);

        Assertions.assertThat(responseEntity)
                .extracting(ResponseEntity::getStatusCode, beerResponseEntity -> beerResponseEntity.getBody().getId())
                .contains(HttpStatus.OK, savedBeerUuid);
    }

    @Test
    @Order(3)
    void Handle_Put() throws JsonProcessingException {
        Beer beer = objectMapper.readValue(newBeerJson, Beer.class);
        beer.setId(savedBeerUuid);
        beer.setBeerName("AJ Special");
        beer.setQuantityOnHand(500);
        beer.setVersion(2);
        RequestEntity<String> requestEntity = new RequestEntity<>(objectMapper.writeValueAsString(beer), httpHeaders, HttpMethod.PUT, URI.create(beerUrl));

        ResponseEntity<Beer> responseEntity = restTemplate.exchange(requestEntity, Beer.class);

        Assertions.assertThat(responseEntity)
                .extracting(ResponseEntity::getBody).extracting(Beer::getVersion, Beer::getId, Beer::getQuantityOnHand)
                .contains(2, savedBeerUuid, 500);
    }

    @Test
    @Order(4)
    void Handle_Post() throws JsonProcessingException {
        RequestEntity<String> beerRequest = new RequestEntity<>(newBeerJson, httpHeaders, HttpMethod.POST, URI.create(beerUrl));

        ResponseEntity<Beer> responseEntity = restTemplate.exchange(beerRequest, Beer.class);

        Beer savedBeer = responseEntity.getBody();
        String location = responseEntity.getHeaders().get("Location").stream().findFirst().orElseThrow();

        org.assertj.core.api.Assertions.assertThat(savedBeer).extracting(Beer::getBeerStyle, Beer::getUpc, Beer::getQuantityOnHand)
                .contains(LAGER, "123456789", 100);

        Assertions.assertThat(location).startsWith("/id/");
    }

    @Test
    void deleteExistingBeer() {
        RequestEntity<Object> requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create(beerUrl + "/" + savedBeer.getId()));

        ResponseEntity<ResponseEntity> exchange = restTemplate.exchange(requestEntity, ResponseEntity.class);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    @Disabled(value = "Patch is a non-standard method, hence being left alone, can use PUT operation")
    void patchExistingBeer() throws JsonProcessingException {
        savedBeer.setBeerStyle(BeerStyle.PORTER);
        savedBeer.setPrice(new BigDecimal("20.99"));
        savedBeer.setUpc("989898");
        savedBeer.setBeerName("Flash");
        RequestEntity<String> requestEntity = new RequestEntity<>(objectMapper.writeValueAsString(savedBeer), HttpMethod.PATCH, URI.create(beerUrl + "/" + savedBeer.getId()));

        ResponseEntity<Beer> exchange = restTemplate.exchange(requestEntity, Beer.class);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assertions.assertThat(exchange.getBody())
                .extracting(Beer::getBeerStyle, Beer::getPrice, Beer::getUpc, Beer::getBeerName)
                .contains(BeerStyle.PORTER, 20.99, "989898", "Flash");
    }
}