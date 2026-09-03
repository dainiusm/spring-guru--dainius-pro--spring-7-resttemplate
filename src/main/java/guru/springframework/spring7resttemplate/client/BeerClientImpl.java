package guru.springframework.spring7resttemplate.client;

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

    public static final String BEER_GET_URL = "http://localhost:8080/api/v1/beer";
    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<String> stringResponse = restTemplate.getForEntity(BEER_GET_URL, String.class);
        println("BeerClientImpl :: stringResponse.getBody() = " + stringResponse.getBody());

        ResponseEntity<Map> mapResponse = restTemplate.getForEntity(BEER_GET_URL, Map.class);

        ResponseEntity<JsonNode> jsonResponse = restTemplate.getForEntity(BEER_GET_URL, JsonNode.class);

        jsonResponse.getBody().findPath("content")
                .forEach(
                        node -> println(node.path("beerName").asString())
                );

        ResponseEntity<RestPageImpl<BeerDTO>> pageResponse = restTemplate.exchange(
                BEER_GET_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<BeerDTO>>() {
                });

        println("pageResponse = " + pageResponse);

        return pageResponse.getBody();
    }
}
