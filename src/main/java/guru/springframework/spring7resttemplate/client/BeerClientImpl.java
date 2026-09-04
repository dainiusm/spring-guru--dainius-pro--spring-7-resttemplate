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

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static java.lang.IO.println;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    /// Base URL is configured in [RestTemplateBuilderConfig]
    public static final String GET_BEER_PATH = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    private final RestTemplateBuilder restTemplateBuilder;


    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();

//        ResponseEntity<BeerDTO> entity = restTemplate.getForEntity(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
//        entity.getStatusCode();
//        entity.getHeaders();
//        entity.getBody();

        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
    }


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


        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

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
        @SuppressWarnings("Convert2Diamond")
        ResponseEntity<RestPageImpl<BeerDTO>> pageResponse = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<BeerDTO>>() {
                });

        println("pageResponse = " + pageResponse);

        return pageResponse.getBody();
    }


    /// -----------------------------------------------------------------------------------------------------------------
    static final boolean if__create_beer_and_get_via_location = true;

    @Override
    public BeerDTO createBeer(BeerDTO beerDto) {
        return if__create_beer_and_get_via_location ?
                __createBeerPostLocationGet(beerDto) :
                __createBeerPost(beerDto);
    }

    /// Receives created resource as POST response body
    BeerDTO __createBeerPost(BeerDTO beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTO> responseEntity = restTemplate.postForEntity(GET_BEER_PATH, beerDto, BeerDTO.class);
        return responseEntity.getBody();
    }

    /// Another implementation of Beer resource creation via POST
    /// Makes 2 calls to the server, used as example of capabilities
    BeerDTO __createBeerPostLocationGet(BeerDTO beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = restTemplate.postForLocation(GET_BEER_PATH, beerDto);
        Objects.requireNonNull(uri, "POST call to " + GET_BEER_PATH + " returned no Location header");
        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public BeerDTO updateBeer(BeerDTO beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.put(GET_BEER_BY_ID_PATH, beerDto, beerDto.getId());

        return getBeerById(beerDto.getId());
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public BeerDTO updateBeerByPatch(UUID beerId, BeerDTO beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.patchForObject(GET_BEER_BY_ID_PATH, beerDto, BeerDTO.class, beerId);

        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
    }


    /// -----------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_PATH, beerId);
    }
}
