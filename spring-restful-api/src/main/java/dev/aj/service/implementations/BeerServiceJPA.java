package dev.aj.service.implementations;

import dev.aj.domain.model.Beer;
import dev.aj.exception_handling.NotFoundException;
import dev.aj.repositories.BeerRepository;
import dev.aj.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;

    @Override
    public List<Beer> listBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Beer getBeerById(UUID id) {
        Optional<Beer> optionalBeer = beerRepository.findById(id);
        return optionalBeer.orElseThrow(() -> new NotFoundException("Unable to find id " + id));
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public Beer updateOrCreateNewBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }

    @Override
    public Beer patchExistingBeer(UUID beerId, Beer beer) {
        return null;
    }
}
