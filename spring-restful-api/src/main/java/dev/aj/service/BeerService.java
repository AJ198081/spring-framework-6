package dev.aj.service;

import dev.aj.domain.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

   List<Beer> listBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    Beer updateOrCreateNewBeer(Beer beer);

    void deleteBeerById(UUID beerId);

    Beer patchExistingBeer(UUID beerId, Beer beer);
}
