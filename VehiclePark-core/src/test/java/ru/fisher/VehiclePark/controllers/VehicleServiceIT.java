package ru.fisher.VehiclePark.controllers;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.VehicleService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
class VehicleServiceIT {

    @Autowired
    private VehicleService vehicleService;

    @Test
    void shouldFindAllVehicles() {
        List<Vehicle> vehicles = vehicleService.findAll();
        assertThat(vehicles).hasSizeGreaterThanOrEqualTo(25);
    }

    @Test
    void shouldFindVehicleByNumber() {
        String vehicleNumber = "С015ЕВ65";
        Optional<Vehicle> vehicle = vehicleService.findVehicleByNumber(vehicleNumber);
        assertThat(vehicle.get().getNumber()).isEqualTo(vehicleNumber);
    }
}
