//package ru.fisher.VehiclePark.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import ru.fisher.VehiclePark.dto.MileageReportDTO;
//import ru.fisher.VehiclePark.dto.VehicleDTO;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//@ActiveProfiles("test")
//class ReactiveControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    void streamVehicles_shouldReturnFlux() {
//        webTestClient.get()
//                .uri("/reactive/stream/vehicles")
//                .accept(MediaType.TEXT_EVENT_STREAM)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
//                .expectBodyList(VehicleDTO.class)
//                .hasSize(0);
//    }
//
//    @Test
//    void generateVehicleReport_shouldReturnMono() {
//        webTestClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/reactive/stream/reports")
//                        .queryParam("vehicleId", 1)
//                        .build())
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(MileageReportDTO.class);
//    }
//}