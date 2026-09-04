package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import guru.springframework.spring7resttemplate.model.RestPageImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(BeerClientImpl.class)
@Import(RestTemplateBuilderConfig.class)
class BeerClientMockTest {

    @Autowired
    BeerClient beerClient;

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testGetBeerById() {
        BeerDTO beerDto = getBeerDto();
        String payload = objectMapper.writeValueAsString(beerDto);

        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(
                        BeerClientImpl.GET_BEER_BY_ID_PATH,
                        beerDto.getId()
                ))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        BeerDTO byId = beerClient.getBeerById(beerDto.getId());

        assertThat(byId)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(beerDto);

    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Test
    void testListBeers() {
        String payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent()).hasSize(1);
        server.verify();
    }


    BeerDTO getBeerDto() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
    }

    RestPageImpl<BeerDTO> getPage() {
        return new RestPageImpl<>(List.of(getBeerDto()), 0, 25, 1);
    }

}
