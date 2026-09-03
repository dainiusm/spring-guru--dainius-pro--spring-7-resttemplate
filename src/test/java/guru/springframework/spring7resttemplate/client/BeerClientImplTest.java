package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static java.lang.IO.println;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void testListBeers() {
        Page<BeerDTO> beersResponse = beerClient.listBeers();

        println("beersResponse = " + beersResponse);

//        List<BeerDTO> beersList = beersResponse.getContent();
//
//        String result = beersList.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", "));
//
//        println("BeerClientImplTest || result = " + result);
    }

}