package dev.aj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.aj.domain.model.Beer;
import dev.aj.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/beer")
public class BeerController {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSS";

    private final BeerService beerService;
    private final ObjectMapper objectMapper;

    public BeerController(BeerService beerService, Jackson2ObjectMapperBuilder builder) {

        this.beerService = beerService;

        this.objectMapper = builder
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .dateFormat(new SimpleDateFormat(DATE_TIME_PATTERN))
                .build();
    }

    @GetMapping(path = "/all")
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(path = "/id/{id}")
    public Beer getBeerById(@PathVariable(value = "id") UUID uuid) {
        return beerService.getBeerById(uuid);
    }

    @PostMapping
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/id/" + savedBeer.getId());

        return new ResponseEntity<Beer>(savedBeer, httpHeaders, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Beer> handlePut(@RequestBody Beer beer) {
        return new ResponseEntity<>(beerService.updateOrCreateNewBeer(beer), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{beerId}")
    public ResponseEntity<HttpStatus> deleteBeer(@PathVariable(value = "beerId") UUID beerId) {
        beerService.deleteBeerById(beerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{beerId}")
    public ResponseEntity<Beer> patchBeer(@PathVariable UUID beerId, @RequestBody Beer beer) {
        Beer patchedBeer = beerService.patchExistingBeer(beerId, beer);
        return new ResponseEntity<>(patchedBeer, HttpStatus.OK);
    }

}
