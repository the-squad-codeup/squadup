package pro.squadup.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pro.squadup.models.Game;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GameApiService {

    @Value("${igdb.client.id}")
    private String IGDB_CLIENT_ID;

    @Value("${igdb.access.token")
    private String IGDB_ACCESS_TOKEN;

    @Value("${igdb.api.url}")
    private String IGDB_API_URL;

    @Autowired
    ApiService apiService;

    public GameApiService() {

    }



    public void searchGames(String query) throws JsonProcessingException {
        // Initialize http client and web client
        HttpClient httpClient = apiService.buildHttpClient();
        WebClient client = apiService.buildWebClient(httpClient);

        // Defining method as POST
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();

        // Setting request URL
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(IGDB_API_URL + "games");

        // Setting  body
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue((
                "search `" + query + "`; fields name,cover.image_id,age_ratings.rating,age_ratings.category,genres.name,platforms.name;"
        ).replace('`', '"'));

        // Setting headers
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Client-ID", IGDB_CLIENT_ID);
//        httpHeaders.add("Authorization", "Bearer " + IGDB_ACCESS_TOKEN);
        headersSpec.header("Client-ID", IGDB_CLIENT_ID);
        headersSpec.header("Authorization", "Bearer " + IGDB_ACCESS_TOKEN);
        headersSpec.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Sending Request and Receiving Response
        Mono<String> res = headersSpec.exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(String.class);
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.just("Error response");
            } else {
                return response.createException()
                        .flatMap(Mono::error);
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(res));
    }
}
