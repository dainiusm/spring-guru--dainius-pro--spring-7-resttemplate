package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.lang.IO.println;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void testListBeers() {
        Page<BeerDTO> beersResponse = beerClient.listBeers();
        assertThat(beersResponse).isNotNull();

        List<BeerDTO> beers = beersResponse.getContent();
        assertThat(beers).hasSizeGreaterThan(0);

        println("beersResponse = " + beersResponse);
        println("beers = " + beers);
    }

}