package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.Map;

import static java.lang.IO.println;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {


    /// Base URL is configured in [RestTemplateBuilderConfig]
    public static final String GET_BEER_PATH = "/api/v1/beer";

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<String> stringResponse = restTemplate.getForEntity(GET_BEER_PATH, String.class);
        println("BeerClientImpl :: stringResponse.getBody() = " + stringResponse.getBody());

        ResponseEntity<Map> mapResponse = restTemplate.getForEntity(GET_BEER_PATH, Map.class);

        ResponseEntity<JsonNode> jsonResponse = restTemplate.getForEntity(GET_BEER_PATH, JsonNode.class);

        jsonResponse.getBody().findPath("content")
                .forEach(
                        node -> println(node.path("beerName").asString())
                );


        // restTemplate.getForEntity() does not work, as it does not support parameterized types due to erasure.
        // so restTemplate.exchange() is used.
        ResponseEntity<RestPageImpl<BeerDTO>> pageResponse = restTemplate.exchange(
                GET_BEER_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<BeerDTO>>() {
                });

        println("pageResponse = " + pageResponse);

        return pageResponse.getBody();
    }
}
