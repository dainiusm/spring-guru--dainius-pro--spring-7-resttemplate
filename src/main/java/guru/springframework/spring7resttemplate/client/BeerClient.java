package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

public interface BeerClient {

    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(Integer pageNumber, Integer pageSize);

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
}
