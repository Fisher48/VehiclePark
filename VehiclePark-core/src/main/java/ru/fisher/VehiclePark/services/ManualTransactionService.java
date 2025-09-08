package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Vehicle;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Service
@Slf4j
public class ManualTransactionService {

    private final PlatformTransactionManager transactionManager;
    private final BrandService brandService;
    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;
    private final DataSource dataSource;
    private final ModelMapper modelMapper;

    @Autowired
    public ManualTransactionService(PlatformTransactionManager transactionManager, BrandService brandService,
                                    VehicleService vehicleService, EnterpriseService enterpriseService,
                                    DataSource dataSource, ModelMapper modelMapper) {
        this.transactionManager = transactionManager;
        this.brandService = brandService;
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
        this.dataSource = dataSource;
        this.modelMapper = modelMapper;
    }

    public void saveVehicleJdbcTransaction(Vehicle vehicle) {
        String sql = """
                INSERT INTO autopark.vehicle (number, purchase_time, price, mileage,
                                      year_of_car_production, brand_id, enterprise_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // (1) начало транзакции

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, vehicle.getNumber());
                ps.setTimestamp(2, Timestamp.valueOf(vehicle.getPurchaseTime()));
                ps.setInt(3, vehicle.getPrice());
                ps.setInt(4, vehicle.getMileage());
                ps.setInt(5,vehicle.getYearOfCarProduction());
                ps.setLong(6, vehicle.getBrand().getId());
                ps.setLong(7, vehicle.getEnterprise().getId());

                ps.executeUpdate();
            }

            connection.commit(); // (2) подтверждение

            log.info("Машина {} сохранена вручную через JDBC", vehicle.getNumber());

        } catch (SQLException e) {
            log.error("Ошибка при сохранении машины вручную через JDBC: {}", e.getMessage());
            try {
                dataSource.getConnection().rollback(); // (3) откат
            } catch (SQLException ex) {
                log.error("Ошибка rollback: {}", ex.getMessage());
            }
        }
    }

    public void updateVehicleWithManualTransaction(Long vehicleId, VehicleDTO vehicleDTO,
                                                   Long brandId, Long enterpriseId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            log.info("Заходим в транзакцию обновления машины");
            Vehicle existingVehicle = vehicleService.findOne(vehicleId);

            Vehicle updatedVehicle = modelMapper.map(vehicleDTO, Vehicle.class);
            updatedVehicle.setId(vehicleId);

            // Если дата не указана — сохраняем старую
            if (updatedVehicle.getPurchaseTime() == null) {
                updatedVehicle.setPurchaseTime(existingVehicle.getPurchaseTime());
            }

            updatedVehicle.setBrand(brandService.findOne(brandId));
            updatedVehicle.setEnterprise(enterpriseService.findOne(enterpriseId));

            vehicleService.save(updatedVehicle);

            log.debug("Машина: {} обновлена через ручную транзакцию", updatedVehicle.getNumber());
            transactionManager.commit(status);
            log.debug("Транзакция закрыта");
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
            log.error("Ошибка при обновлении данных машины вручную: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
