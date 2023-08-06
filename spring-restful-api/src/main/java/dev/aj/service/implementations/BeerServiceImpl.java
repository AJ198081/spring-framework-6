package dev.aj.service.implementations;

import dev.aj.domain.enums.BeerStyle;
import dev.aj.domain.model.Beer;
import dev.aj.service.BeerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BeerServiceImpl implements BeerService {

    public static final String CREATED_DATE = "createdDate";
    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeers() {
        return beerMap.values().stream().toList();
    }

    @Override
    public Beer getBeerById(UUID id) {
        return beerMap.getOrDefault(id, new Beer());
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        beer.setId(UUID.randomUUID());
        beer.setVersion(1);
        beer.setCreatedDate(LocalDateTime.now());
        beer.setUpdatedDate(LocalDateTime.now());
        beerMap.put(beer.getId(), beer);
        return beer;
    }

    @Override
    public Beer updateOrCreateNewBeer(Beer beer) {

        Beer savedBeer = null;

        if (!Objects.isNull(beer.getId()) && beerMap.containsKey(beer.getId())) {
            savedBeer = this.updateBeer(beer);
        } else {
            savedBeer = this.saveNewBeer(beer);
        }

        return savedBeer;
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public Beer patchExistingBeer(UUID beerId, Beer beer) {
        Beer patchedBeer = null;
        if (beerMap.containsKey(beerId)) {
            patchedBeer = patchBeerObject(beerId, beer);
        }
        return patchedBeer;
    }

    private Beer patchBeerObject(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);

        existingBeer.setVersion(existingBeer.getVersion() + 1);
        existingBeer.setUpdatedDate(LocalDateTime.now());

        if (StringUtils.isBlank(beer.getBeerName())){
            existingBeer.setBeerName(beer.getBeerName());
        }

        if (StringUtils.isBlank(beer.getUpc())) {
            existingBeer.setUpc(beer.getUpc());
        }

        if (!Objects.isNull(beer.getPrice())) {
            existingBeer.setPrice(beer.getPrice());
        }

        if (Arrays.stream(BeerStyle.values()).anyMatch(beerStyle -> beerStyle.equals(beer.getBeerStyle()))) {
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }

        return existingBeer;
    }

    private Beer updateBeer(Beer beerToBeUpdated) {
        UUID beerToBeUpdatedId = beerToBeUpdated.getId();
        Beer beerFromRepo = beerMap.get(beerToBeUpdatedId);

        LocalDateTime createdDate = beerFromRepo.getCreatedDate();
        beerToBeUpdated.setCreatedDate(createdDate);

        beerToBeUpdated.setUpdatedDate(LocalDateTime.now());

         beerMap.put(beerToBeUpdatedId, beerToBeUpdated);

        return beerMap.get(beerToBeUpdatedId);
    }

}
