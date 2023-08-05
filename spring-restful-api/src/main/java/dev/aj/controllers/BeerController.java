package dev.aj.controllers;

import dev.aj.domain.model.Beer;
import dev.aj.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/beer")
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

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

}
