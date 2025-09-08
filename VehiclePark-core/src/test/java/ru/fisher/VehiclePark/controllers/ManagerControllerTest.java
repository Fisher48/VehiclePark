package ru.fisher.VehiclePark.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.exceptions.VehicleNotFoundException;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.VehicleService;


import java.time.LocalDateTime;
import java.util.function.BooleanSupplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "Ivan")
@Transactional
@Rollback
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VehicleService vehicleService;
    @MockBean
    private NotificationKafkaProducer notificationKafkaProducer;


    @Test
    void testIndexEnterprises() throws Exception {
        this.mockMvc.perform(get("/managers/enterprises"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("enterprises"))
                .andExpect(content().string(containsString("Предприятия")));
    }

    @Test
    void updateEnterprise_WithValidData_ShouldRedirect() throws Exception {
        mockMvc.perform(put("/managers/enterprises/{enterpriseId}", 1)
                        .param("name", "Название из интеграционного теста")
                        .param("timezone", "Europe/Moscow")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/enterprises"));
    }

    @Test
    void testIndexVehicles() throws Exception {
        this.mockMvc.perform(get("/managers/enterprises/{enterpriseId}/vehicles", 1))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(content().string(containsString("Список автомобилей")));
    }

    @Test
    void testCreateVehicleWithInvalidData() throws Exception {
        this.mockMvc.perform(post("/managers/enterprises/{enterpriseId}/vehicles/new", 1)
                        .param("brandId", "1")
                        .param("number", "")
                        .param("yearOfCarProduction", "1800")
                        .param("price", "-100")
                        .param("purchaseTime", LocalDateTime.now().toString())
                        .param("mileage", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("vehicles/new"))
                .andExpect(model().attributeHasErrors("vehicle"))
                .andExpect(model().attributeHasFieldErrors(
                        "vehicle", "number", "yearOfCarProduction", "price"))
                .andExpect(model().attributeHasFieldErrorCode("vehicle", "number", "NotEmpty"))
                .andExpect(model().attributeHasFieldErrorCode("vehicle", "price", "Min"))
                .andExpect(model().attributeHasFieldErrorCode("vehicle", "yearOfCarProduction", "Min"))
                .andExpect(content().string(containsString("Обязательное поле")))
                .andExpect(content().string(containsString("Допускается добавление ТС старше 1950 г.")))
                .andExpect(content().string(containsString("must be greater than or equal to 0")));
    }

    @Test
    void testCreateVehicleWithValidData() throws Exception {
        this.mockMvc.perform(post("/managers/enterprises/{enterpriseId}/vehicles/new", 1)
                        .param("brandId", "1")
                        .param("number", "О023НО48")
                        .param("yearOfCarProduction", "2025")
                        .param("price", "9999999")
                        .param("purchaseTime", LocalDateTime.now().toString())
                        .param("mileage", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/enterprises/1/vehicles"));
    }

    @Test
    @WithMockUser(username = "Ivan", roles = "MANAGER")
    void deleteVehicle_ShouldRedirect() throws Exception {
        // Предположим, что машина с ID=1 существует и принадлежит предприятию 1
        Long enterpriseId = 1L;
        Long vehicleId = 1L;

        mockMvc.perform(delete("/managers/enterprises/{enterpriseId}/vehicles/{vehicleId}",
                        enterpriseId, vehicleId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/enterprises/" + enterpriseId + "/vehicles"));

        // Проверяем, что машина действительно удалена
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.findOne(vehicleId);
        });
    }



//    @Test
//    @WithMockUser(username = "NotOwner", roles = "MANAGER")
//    void deleteVehicle_WithoutAccess_ShouldReturnForbidden() throws Exception {
//        // Создаем машину, принадлежащую другому менеджеру
//        Vehicle vehicle = vehicleService.findOne(7000L);
//
//        mockMvc.perform(delete("/managers/enterprises/1/vehicles/{vehicleId}",
//                        vehicle.getId())
//                        .with(csrf()))
//                .andExpect(status().isForbidden());
//    }


//    @Test
//    void uploadGpx_WithValidFile_ShouldSuccess() throws Exception {
//        MockMultipartFile file = new MockMultipartFile(
//                "gpxFile",
//                "test.gpx",
//                "text/xml",
//                "<gpx></gpx>".getBytes()
//        );
//
//        mockMvc.perform(multipart("/managers/enterprises/1/vehicles/1/trips/upload")
//                        .file(file)
//                        .param("startTime", LocalDateTime.now().toString())
//                        .param("endTime", LocalDateTime.now().plusHours(1).toString())
//                        .with(csrf()))
//                .andExpect(status().isOk());
//    }

    @Test
    void generateReport_ShouldReturnValidModel() throws Exception {
        // Настройка мока
        doNothing().when(notificationKafkaProducer).sendNotification(anyLong(), anyString());

        mockMvc.perform(post("/managers/reports/generate")
                        .param("reportType", "VEHICLE_MILEAGE")
                        .param("vehicleNumber", "С015ЕВ65")
                        .param("startDate", LocalDateTime.now().minusYears(5).toString())
                        .param("endDate", LocalDateTime.now().toString())
                        .param("period", "DAY")
                        .with(csrf()))
                .andExpect(model().attributeExists("report"))
                .andExpect(view().name("reports/view"));

        // Проверка, что метод был вызван
        verify(notificationKafkaProducer, times(1)).sendNotification(anyLong(), anyString());
    }


}