package ru.fisher.VehiclePark.controllers;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.VehicleService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//@SpringBootTest
//@WebAppConfiguration
////@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(SpringExtension.class)

//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = {"/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
@Rollback
class VehicleServiceIT {

    @Autowired
    private VehicleService vehicleService;

    @Test
    void shouldFindAllVehicles() {
        List<Vehicle> vehicles = vehicleService.findAll();
        assertThat(vehicles).hasSizeGreaterThanOrEqualTo(15000);
    }

    @Test
    void shouldFindVehicleByNumber() {
        String vehicleNumber = "С015ЕВ65";
        Optional<Vehicle> vehicle = vehicleService.findVehicleByNumber(vehicleNumber);
        assertThat(vehicle.get().getNumber()).isEqualTo(vehicleNumber);
    }
}
