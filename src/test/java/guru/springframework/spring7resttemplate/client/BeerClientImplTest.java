package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;

import static org.assertj.core.api.Assertions.assertThat;

import guru.springframework.spring7resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.lang.IO.println;

@SpringBootTest
class BeerClientImplTest {

    public static final int FIRST_PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 20;

    @Autowired
    BeerClient beerClient;

    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersByNameAndByStyleWithInventory() {
        Page<BeerDTO> beersResponse = beerClient.listBeers("Moon", BeerStyle.PILSNER, true, FIRST_PAGE_NUMBER, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSize(2);
        assertThat(beers.getFirst().getQuantityOnHand()).isNotNull(); // there should be inventory returned

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersByNameAndByStyle() {
        Page<BeerDTO> beersResponse = beerClient.listBeers("Moon", BeerStyle.PILSNER, null, FIRST_PAGE_NUMBER, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSize(2);
        assertThat(beers.getFirst().getQuantityOnHand()).isNull(); // there should be no inventory returned

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersByName() {
        Page<BeerDTO> beersResponse = beerClient.listBeers("Moon", null, null, FIRST_PAGE_NUMBER, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSize(3);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersALL() {
        Page<BeerDTO> beersResponse = beerClient.listBeers(FIRST_PAGE_NUMBER, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSize(PAGE_SIZE);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }

    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersALLDefault() {
        Page<BeerDTO> beersResponse = beerClient.listBeers();
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSize(25);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersSameRequestHasSameElements() {
        Page<BeerDTO> beersResponse = beerClient.listBeers(FIRST_PAGE_NUMBER, PAGE_SIZE);
        Page<BeerDTO> beersResponse2 = beerClient.listBeers(FIRST_PAGE_NUMBER, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();
        assertThat(beersResponse2).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        List<BeerDTO> beers2 = beersResponse2.getContent();
        assertThat(beers).hasSize(PAGE_SIZE);
        assertThat(beers2).hasSize(PAGE_SIZE);

        assertThat(beers).containsExactlyElementsOf(beers2);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeersPaging() {
        Page<BeerDTO> beersResponse = beerClient.listBeers(FIRST_PAGE_NUMBER, PAGE_SIZE);
        Page<BeerDTO> beersResponse2 = beerClient.listBeers(FIRST_PAGE_NUMBER + 1, PAGE_SIZE);
        assertThat(beersResponse).isNotNull();
        assertThat(beersResponse2).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        List<BeerDTO> beers2 = beersResponse2.getContent();
        assertThat(beers).hasSize(PAGE_SIZE);
        assertThat(beers2).hasSize(PAGE_SIZE);

        assertThat(beers).doesNotContainAnyElementsOf(beers2);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }

}
