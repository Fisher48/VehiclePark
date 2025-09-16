package ru.fisher.VehiclePark.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationKafkaProducer notificationKafkaProducer;

    private String loginAndGetToken() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username": "Ivan",
                            "password": "12345"}
                        """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return response.replace("{\"jwt-token\":\"", "").replace("\"}", "");
    }

    @Test
    void whenNotAuthorized_then401() throws Exception {
        mockMvc.perform(get("/api/managers/1/vehicles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAuthorized_then200() throws Exception {
        String token = loginAndGetToken();
        mockMvc.perform(get("/api/managers/1/vehicles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void whenBadCredentials_then401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username": "Ivan",
                            "password": "wrong"}
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenInvalidData_then400() throws Exception {
        String token = loginAndGetToken();
        mockMvc.perform(post("/api/managers/1/vehicles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"invalidField": "wrong"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginAndAccessProtectedEndpoint() throws Exception {
        // 1. Логинимся и получаем токен
        String token = loginAndGetToken(); // Извлекаем токен из ответа
        mockMvc.perform(get("/api/managers/1/vehicles")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(10))); // Тут зависит от твоих тестовых данных
    }
}
