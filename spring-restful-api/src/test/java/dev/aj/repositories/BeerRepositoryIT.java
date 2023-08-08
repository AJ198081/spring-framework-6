package dev.aj.repositories;

import dev.aj.domain.enums.BeerStyle;
import dev.aj.domain.model.Beer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryIT {

    @Autowired
    private BeerRepository beerRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSaveBeer() {
        Beer beerToBeSaved = Beer.builder()
                .beerName("My Beer for Repo")
                .beerStyle(BeerStyle.PORTER)
                .build();

        Beer savedBeer = beerRepository.save(beerToBeSaved);

        Assertions.assertNotNull(savedBeer.getId());
        Assertions.assertAll("Asserting saved beers attributes",
                () -> Assertions.assertEquals(beerToBeSaved.getBeerStyle(), savedBeer.getBeerStyle()),
                () -> Assertions.assertEquals(beerToBeSaved.getBeerName(), savedBeer.getBeerName())
        );
    }
}