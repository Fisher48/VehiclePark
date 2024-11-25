package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.repositories.DriverRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DriverService {

    private final DriverRepository driverRepository;
    private final EnterpriseService enterpriseService;

    @Autowired
    public DriverService(DriverRepository driverRepository, EnterpriseService enterpriseService) {
        this.driverRepository = driverRepository;
        this.enterpriseService = enterpriseService;
    }

    public List<Driver> findAvailableDrivers() {
        return driverRepository.findByVehiclesIsNull();
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver findOne(Long id) {
        Optional<Driver> foundVehicle = driverRepository.findById(id);
        return foundVehicle.orElseThrow(
                () -> new ResourceNotFoundException("Driver with " + id + " id, not exists"));
    }

    @Transactional
    public void save(Driver driver) {
        driverRepository.save(driver);
    }

    @Transactional
    public void saveAll(List<Driver> drivers) {
        driverRepository.saveAll(drivers);
    }

    @Transactional
    public void update(Long id, Driver driver) {
        driver.setId(id);
        driverRepository.save(driver);
    }

    @Transactional
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

}
