package ru.fisher.VehiclePark.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


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

}