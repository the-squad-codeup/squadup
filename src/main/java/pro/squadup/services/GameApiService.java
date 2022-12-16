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

//    @Autowired
//    ApiService apiService;

    public GameApiService() {

    }



//    public void searchGames(String query) throws IOException {
//
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println("Inside searchGames");
//        String bodyString = (
//                "search `" + query + "`; fields name,cover.image_id,genres.name,platforms.name;"
//        ).replace('`', '"');
//        System.out.println("Body String: " + bodyString);
//        System.out.println("Client ID Header: " + IGDB_CLIENT_ID);
//        System.out.println("Authorization Header: " + "Bearer " + IGDB_ACCESS_TOKEN);
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(MediaType.get("text/plain;charset=UTF-8"), bodyString);
////        RequestBody formBody = new FormBody.Builder()
////                .add("search", query)
////                .add("fields", "name,cover.image_id,genres.name,platforms.name")
////                .build();
//        mapper.writeValueAsString(body);
//        Request request = new Request.Builder()
//                .url(IGDB_API_URL + "games")
//                .addHeader("Client-ID", IGDB_CLIENT_ID)
//                .addHeader("Authorization", "Bearer " + IGDB_ACCESS_TOKEN)
//                .addHeader("Content-Type", "text/plain")
//                .post(body)
//                .build();
//
//        mapper.writeValueAsString(request);
//
//        Call call = client.newCall(request);
////        Response response = call.execute();
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                } else {
//                    ObjectMapper mapper = new ObjectMapper();
//                    System.out.println(mapper.writeValueAsString(response));
//                }
//            }
//        });
//
////        ObjectMapper mapper = new ObjectMapper();
////        System.out.println(mapper.writeValueAsString(response));
//    }

    public List<Object> searchGames(String query) throws JsonProcessingException {
        System.out.println("Inside searchGames. Query: " + query);
        String bodyString = (
                "search `" + query + "`; fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name; where category = 0;"
        ).replace('`', '"');
        System.out.println("Body String: " + bodyString);
        // Initialize http client and web client
//        HttpClient httpClient = apiService.buildHttpClient();
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));


//        WebClient client = apiService.buildWebClient(httpClient);
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        // Defining method as POST
//        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();

        // Setting request URL
//        WebClient.RequestBodySpec bodySpec = uriSpec.uri(IGDB_API_URL + "games");

        // Setting  body
//        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(bodyString);

        // Setting headers
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Client-ID", IGDB_CLIENT_ID);
//        httpHeaders.add("Authorization", "Bearer " + IGDB_ACCESS_TOKEN);
//        headersSpec.header("Client-ID", IGDB_CLIENT_ID);
//        headersSpec.header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN);

//        WebClient.ResponseSpec responseSpec = headersSpec.header(
//                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
//                .acceptCharset(StandardCharsets.UTF_8)
//                .ifNoneMatch("*")
//                .ifModifiedSince(ZonedDateTime.now())
//                .retrieve();

        // Sending Request and Receiving Response
//        Mono<String> res = headersSpec.exchangeToMono(response -> {
//            if (response.statusCode().equals(HttpStatus.OK)) {
//                System.out.println("Status OK");
//                return response.bodyToMono(String.class);
//            } else if (response.statusCode().is4xxClientError()) {
//                System.out.println("4xx Error");
//                return Mono.just("Error response");
//            } else {
//                System.out.println("Exception");
//                return response.createException()
//                        .flatMap(Mono::error);
//            }
//        });
//        Mono<String> res = headersSpec.retrieve()
//                .bodyToMono(String.class);

        Mono<List<Object>> res = client.post()
                .uri(IGDB_API_URL + "games")
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(bodyString))
                .header("Client-ID", IGDB_CLIENT_ID)
                .header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {});

        List<Object> objects = res.block();





//        ObjectMapper mapper = new ObjectMapper();
//        Set<Game> games = new HashSet<>();
//        for(Object object : objects) {
//            games.add()
//            System.out.println(mapper.writeValueAsString(object));
//        }
        return objects;
    }

    public Game addGame(long igdbId) {

    }
}
