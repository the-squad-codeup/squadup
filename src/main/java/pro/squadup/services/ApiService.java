package pro.squadup.services;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ApiService {

    public ApiService() {

    }

    // Sets up timeout rules
    public HttpClient buildHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .responseTimeout(Duration.ofMillis(30000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS)));
    }

    // Builds the WebClient
    public WebClient buildWebClient(HttpClient httpClient, String bodyString) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
