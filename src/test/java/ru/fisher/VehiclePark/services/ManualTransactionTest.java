package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Vehicle;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManualTransactionTest {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ManualTransactionService manualTransactionService;

    @Test
    void updateVehicleInManualTransactionTest() {
        Random random = new Random();
        int newPrice = random.nextInt(100000) + 1000;
        int newMileage = random.nextInt(100000) + 1000;
        Vehicle vehicle = vehicleService.findOne(15034L);
        VehicleDTO vehicleDTO = vehicleService.convertToVehicleDTO(vehicle);
        log.info("Цена до изменения: {}", vehicle.getPrice());
        log.info("Пробег до изменения: {}", vehicle.getMileage());
        vehicleDTO.setPrice(newPrice);
        vehicleDTO.setMileage(newMileage);
        manualTransactionService.
                updateVehicleWithManualTransaction(15034L, vehicleDTO, 1L, 1L);

        Vehicle updatedVehicle = vehicleService.findOne(15034L);
        assertEquals(updatedVehicle.getMileage(), newMileage);
        assertEquals(updatedVehicle.getPrice(), newPrice);
        log.info("Цена после изменения: {}", updatedVehicle.getPrice());
        log.info("Пробег после изменения: {}", updatedVehicle.getMileage());
    }

    @Test
    void testSaveVehicleJdbcTransaction() {
        Vehicle vehicle = new Vehicle();
        vehicle.setNumber("JDBC-002");
        vehicle.setPurchaseTime(LocalDateTime.now());
        vehicle.setPrice(55555);
        vehicle.setMileage(1234);
        vehicle.setYearOfCarProduction(2020);
        vehicle.setBrand(brandService.findOne(1L));
        vehicle.setEnterprise(enterpriseService.findOne(1L));

        manualTransactionService.saveVehicleJdbcTransaction(vehicle);

        Optional<Vehicle> newVehicle = vehicleService.findVehicleByNumber("JDBC-002");

        assertEquals(newVehicle.get().getNumber(), vehicle.getNumber());
    }

}
