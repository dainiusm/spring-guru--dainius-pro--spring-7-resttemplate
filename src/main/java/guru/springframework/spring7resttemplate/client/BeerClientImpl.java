package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import guru.springframework.spring7resttemplate.model.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static java.lang.IO.println;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {


    /// Base URL is configured in [RestTemplateBuilderConfig]
    public static final String GET_BEER_PATH = "/api/v1/beer";

    private final RestTemplateBuilder restTemplateBuilder;


    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public Page<BeerDTO> listBeers() {
        return listBeers(null, null);
    }

    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public Page<BeerDTO> listBeers(Integer pageNumber, Integer pageSize) {
        return listBeers(null, null, false, pageNumber, pageSize);
    }

    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        RestTemplate restTemplate = restTemplateBuilder.build();

//        ResponseEntity<String> stringResponse = restTemplate.getForEntity(GET_BEER_PATH, String.class);
//        println("BeerClientImpl :: stringResponse.getBody() = " + stringResponse.getBody());
//
//        ResponseEntity<Map> mapResponse = restTemplate.getForEntity(GET_BEER_PATH, Map.class);
//
//        ResponseEntity<JsonNode> jsonResponse = restTemplate.getForEntity(GET_BEER_PATH, JsonNode.class);
//
//        jsonResponse.getBody().findPath("content")
//                .forEach(
//                        node -> println(node.path("beerName").asString())
//                );


        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(GET_BEER_PATH);

        if (StringUtils.hasText(beerName)) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }
        if (Boolean.TRUE.equals(showInventory)) {
            uriComponentsBuilder.queryParam("showInventory", true);
        }
        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }


        // restTemplate.getForEntity() does not work, as it does not support parameterized types due to erasure.
        // so restTemplate.exchange() is used.
        ResponseEntity<RestPageImpl<BeerDTO>> pageResponse = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<BeerDTO>>() {
                });

        println("pageResponse = " + pageResponse);

        return pageResponse.getBody();
    }
}
