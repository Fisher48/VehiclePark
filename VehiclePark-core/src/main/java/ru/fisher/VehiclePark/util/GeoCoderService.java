package ru.fisher.VehiclePark.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class GeoCoderService {

    @Value("${yandex.api.key}")
    private String apiKey;

    @Value("${openrouteservice.api.key}")
    private String openRouteApiKey;

    private final ObjectMapper objectMapper;
    private final WebClient webClient;


    /**
     * Получение координат по адресу через OpenRouteService API
     */
    public Map<String, Double> getCoordinatesFromOpenRouteService(String address) {
        try {
            // Выполняем GET-запрос
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.openrouteservice.org")
                            .path("/geocode/search/")
                            .queryParam("api_key", openRouteApiKey)
                            .queryParam("text", address)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Ответ от OpenRouteService API: {}", response);

            // Парсим JSON-ответ
            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");

            if (features.isArray() && !features.isEmpty()) {
                JsonNode geometry = features.get(0).path("geometry");
                double longitude = geometry.path("coordinates").get(0).asDouble();
                double latitude = geometry.path("coordinates").get(1).asDouble();

                Map<String, Double> result = new HashMap<>();
                result.put("lat", latitude);
                result.put("lon", longitude);
                return result;
            } else {
                log.warn("Координаты не найдены для адреса: {}", address);
                return null;
            }
        } catch (Exception e) {
            log.error("Ошибка при вызове OpenRouteService API: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Получение адреса по координатам через Яндекс Геокодер API
     */
    public String getAddressFromYandexGeo(double latitude, double longitude) {
        try {
            // Выполняем GET-запрос
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("geocode-maps.yandex.ru")
                            .path("/1.x/")
                            .queryParam("apikey", apiKey)
                            .queryParam("format", "json")
                            .queryParam("geocode", longitude + "," + latitude)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Ответ от Яндекс Геокодер API: {}", response);

            // Парсим JSON-ответ
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode geoObjectCollection = rootNode.path("response")
                    .path("GeoObjectCollection")
                    .path("featureMember");

            if (geoObjectCollection.isArray() && !geoObjectCollection.isEmpty()) {
                JsonNode geoObject = geoObjectCollection.get(0).path("GeoObject");
                return geoObject.path("metaDataProperty")
                        .path("GeocoderMetaData")
                        .path("text").asText();
            } else {
                log.warn("Адрес не найден для координат: {}, {}", latitude, longitude);
                return "Address not found";
            }
        } catch (Exception e) {
            log.error("Ошибка при вызове Яндекс Геокодера: {}", e.getMessage());
            return "Address not found";
        }
    }

    /**
     * Получение адреса по координатам через OpenRouteService API
     */
    public String getAddressFromOpenRouteService(double latitude, double longitude) {
        log.info("Отправляем запрос на обратное геокодирование: {}",
                "&point.lon=" + longitude + "&point.lat=" + latitude);

        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.openrouteservice.org")
                            .path("/geocode/reverse/")
                            .queryParam("api_key", openRouteApiKey)
                            .queryParam("format", "json")
                            .queryParam("point.lon", longitude)
                            .queryParam("point.lat", latitude)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Ответ от openrouteservice: {}", response);

            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");
            if (features.isArray() && !features.isEmpty()) {
                return features.get(0)
                        .path("properties")
                        .path("label")
                        .asText("Address not found");
            }
        } catch (Exception e) {
            log.error("Ошибка при получении адреса: {}", e.getMessage());
        }
        return "Address not found";
    }

    public Mono<String> getAddressFromOpenRouteServiceReactive(double latitude, double longitude) {
        log.info("Отправляем запрос на обратное геокодирование (реактивно): {}",
                "&point.lon=" + longitude + "&point.lat=" + latitude);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.openrouteservice.org")
                        .path("/geocode/reverse/")
                        .queryParam("api_key", openRouteApiKey)
                        .queryParam("format", "json")
                        .queryParam("point.lon", longitude)
                        .queryParam("point.lat", latitude)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        JsonNode features = root.path("features");
                        if (features.isArray() && !features.isEmpty()) {
                            return Mono.just(features.get(0)
                                    .path("properties")
                                    .path("label")
                                    .asText("Address not found"));
                        } else {
                            return Mono.just("Address not found");
                        }
                    } catch (Exception e) {
                        log.error("Ошибка при разборе JSON: {}", e.getMessage());
                        return Mono.just("Address not found");
                    }
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при получении адреса: {}", e.getMessage());
                    return Mono.just("Address not found");
                });
    }

}
