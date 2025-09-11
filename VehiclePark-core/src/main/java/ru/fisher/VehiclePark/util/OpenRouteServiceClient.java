package ru.fisher.VehiclePark.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenRouteServiceClient {

    private final WebClient webClient;

    @Value("${openrouteservice.api.key}")
    private String key;

    @Autowired
    public OpenRouteServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.
                baseUrl("https://api.openrouteservice.org/v2/directions/driving-car/geojson").build();
    }

    public Mono<String> getRoute(double[] start, double[] end) {
        String requestBody = String.format(
                "{\"coordinates\":[[%.6f,%.6f],[%.6f,%.6f]]}",
                start[1], start[0], end[1], end[0]
        );

        return webClient.post()
                .uri("")
                .header("Authorization", key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
