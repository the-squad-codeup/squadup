package pro.squadup.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pro.squadup.models.Game;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Service
public class GameApiService {

    @Value("${igdb.client.id}")
    private String IGDB_CLIENT_ID;

    @Value("${igdb.access.token}")
    private String IGDB_ACCESS_TOKEN;

    @Value("${igdb.api.url}")
    private String IGDB_API_URL;

    @Autowired
    private ApiService apiService;

    public GameApiService() {

    }

    // Searches IGDB API and returns a list of game objects
    // Calls on custom Deserializer to convert JSON to game objects
    public List<Game> searchGames(String query) throws JsonProcessingException {
        String bodyString = (
                "search `" + query + "`; fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name,release_dates.y; limit 500; where category = (0,2,8,9);"
        ).replace('`', '"');

        // Creating httpclient object with timeout parameters
        HttpClient httpClient = apiService.buildHttpClient();

        // Creating webclient object to make API request
        WebClient client = apiService.buildWebClient(httpClient, bodyString);

        // Creating Async Mono object to hold list of objects returned from api call
        Mono<List<Game>> res = client.post()
                .uri(IGDB_API_URL + "games")
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(bodyString))
                .header("Client-ID", IGDB_CLIENT_ID)
                .header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                })
        ;
        // Waiting for async call to resolve to a POJO and return it
        List<Game> games = res.block();
        return games;
    }

}
