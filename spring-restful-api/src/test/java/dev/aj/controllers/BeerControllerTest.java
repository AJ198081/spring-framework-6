package dev.aj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.enums.BeerStyle;
import dev.aj.domain.model.Beer;
import dev.aj.exception_handling.NotFoundException;
import dev.aj.service.BeerService;
import dev.aj.service.implementations.BeerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@Tag("unit")
@WebMvcTest(controllers = {BeerController.class})
class BeerControllerTest {

    private static List<Beer> listOfBeers;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerService beerService;

    private Beer testBeer;

    @Captor
    private ArgumentCaptor<UUID> uuidArgCaptor;

    private DateTimeFormatter dateTimeFormatter;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {

        listOfBeers = beerService.listBeers();

        listOfBeers.stream()
                .collect(Collectors.toSet());

        testBeer = Beer.builder()
                .version(1)
                .beerName("Test Beer")
                .beerStyle(BeerStyle.ALE)
                .upc("234123")
                .quantityOnHand(1)
                .createdDate(LocalDateTime.now().minusDays(1L))
                .updatedDate(LocalDateTime.now())
                .build();

//        listOfBeersJson = objectMapper.writeValueAsString(listOfBeers);

//        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_PATTERN));
        dateTimeFormatter = DateTimeFormatter.ofPattern(BeerController.DATE_TIME_PATTERN);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listBeers() throws Exception {

        UUID randomUUID = UUID.randomUUID();
        testBeer.setId(randomUUID);

        List<Beer> testBeerList = new ArrayList<>(4);
        testBeerList.add(testBeer);
        testBeerList.addAll(listOfBeers);

        Mockito.when(beerService.listBeers()).thenReturn(testBeerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/beer/all").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.size()", is(4)),
                        MockMvcResultMatchers.jsonPath("$[0].id", is(randomUUID.toString())),
                        MockMvcResultMatchers.jsonPath("$[0].beerStyle", is(BeerStyle.ALE.toString()))
                );
    }

    @Test
    void getBeerById() throws Exception {

        String randomId = UUID.randomUUID().toString();

        Mockito.when(beerService.getBeerById(Mockito.any(UUID.class))).thenAnswer(invocation -> {
            UUID beerId = invocation.getArgument(0, UUID.class);
            testBeer.setId(beerId);
            return testBeer;
        });

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/beer/id/{beerId}", randomId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id", is(randomId)),
                        MockMvcResultMatchers.jsonPath("$.beerStyle", is(BeerStyle.ALE.toString()))
                );
    }

    @Test
    void handlePost() throws Exception {

        UUID randomUUID = UUID.randomUUID();

        Beer newBeer = Beer.builder()
                .beerStyle(BeerStyle.PALE_ALE)
                .beerName("New Beer")
                .upc("23894238")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(10)
                .build();

        Mockito.when(beerService.saveNewBeer(Mockito.any(Beer.class))).then(invocation -> {
            Beer beerToBeSaved = invocation.getArgument(0, Beer.class);
            beerToBeSaved.setId(randomUUID);
            beerToBeSaved.setVersion(1);
            beerToBeSaved.setCreatedDate(LocalDateTime.now());
            beerToBeSaved.setUpdatedDate(LocalDateTime.now());
            return beerToBeSaved;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBeer)))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.id", is(randomUUID.toString())),
                        MockMvcResultMatchers.jsonPath("$.beerStyle", is(BeerStyle.PALE_ALE.toString())),
                        MockMvcResultMatchers.jsonPath("$.quantityOnHand", is(10))
                );
    }

    @Test
    @Disabled
    void handlePut() {
    }

    @Test
    void deleteBeer() throws Exception {
        Beer beer = listOfBeers.get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/beer/{beerId}", beer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        Mockito.verify(beerService).deleteBeerById(uuidArgCaptor.capture());

        Assertions.assertThat(uuidArgCaptor.getValue())
                .isEqualTo(beer.getId());
    }

    @Test
    void handlePatch() throws Exception {
        Beer firstBeer = listOfBeers.get(0);

        firstBeer.setBeerName("New Beer");
        firstBeer.setBeerStyle(BeerStyle.PORTER);
        firstBeer.setUpc("New UPC");
        firstBeer.setPrice(new BigDecimal("5.99"));

        Mockito.when(beerService.patchExistingBeer(Mockito.any(UUID.class), Mockito.any(Beer.class)))
                .then(invocation -> {
                    Beer argument = invocation.getArgument(1, Beer.class);
                    argument.setUpdatedDate(LocalDateTime.now());
                    argument.setVersion(argument.getVersion() + 1);
                    return argument;
                });

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/beer/{beerId}", firstBeer.getId()).content(objectMapper.writeValueAsString(firstBeer))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.version", is(firstBeer.getVersion() + 1)),
                MockMvcResultMatchers.jsonPath("$.id", is(firstBeer.getId().toString())),
                MockMvcResultMatchers.jsonPath("$.createdDate", is(dateTimeFormatter.format(firstBeer.getCreatedDate()))),
                MockMvcResultMatchers.jsonPath("$.updatedDate", not(firstBeer.getUpdatedDate().toString())),
                MockMvcResultMatchers.jsonPath("$.upc", is(firstBeer.getUpc())),
                MockMvcResultMatchers.jsonPath("$.price", is(5.99))
        );
    }

    @Test
    void getBeerByNonExistenceIdThrowsNotFoundException() throws Exception {
        Mockito.when(beerService.getBeerById(ArgumentMatchers.any(UUID.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/beer/id/{beerId}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}