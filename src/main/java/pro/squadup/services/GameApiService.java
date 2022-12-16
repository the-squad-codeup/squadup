package pro.squadup.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
//import okhttp3.*;
import okhttp3.MediaType.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pro.squadup.models.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    public List<Object> searchGames(String query) throws JsonProcessingException {
        System.out.println("Inside searchGames. Query: " + query);
        String bodyString = (
                "search `" + query + "`; fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name; where category = 0;"
        ).replace('`', '"');
        System.out.println("Body String: " + bodyString);

        // Creating httpclient object with timeout parameters
        HttpClient httpClient = apiService.buildHttpClient();

        // Creating webclient object to make API request
        WebClient client = apiService.buildWebClient(httpClient, bodyString);

        // Creating Async Mono object to hold list of objects returned from api call
        Mono<List<Object>> res = client.post()
                .uri(IGDB_API_URL + "games")
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(bodyString))
                .header("Client-ID", IGDB_CLIENT_ID)
                .header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {});

        // Waiting for async call to resolve to a POJO
        List<Object> objects = res.block();

        return objects;
    }

    public Game addGame(long igdbId) {
        System.out.println("Inside addGame. Body String: ");
        String bodyString = (
                "fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name; where category = 0; where id = " + igdbId + ";"
                );
        System.out.println(bodyString);
        // Creating httpclient object with timeout parameters
        HttpClient httpClient = apiService.buildHttpClient();

        // Creating webclient object to make API request
        WebClient client = apiService.buildWebClient(httpClient, bodyString);

        // Creating Async Mono object to hold list of objects returned from api call
        Mono<Game> res = client.post()
                .uri(IGDB_API_URL + "games")
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(bodyString))
                .header("Client-ID", IGDB_CLIENT_ID)
                .header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .retrieve()
                .bodyToMono(Game.class);

        // Waiting for async call to resolve to a POJO
        return res.block();
    }
}
