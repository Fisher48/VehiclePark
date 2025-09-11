package ru.fisher.VehiclePark.services;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.dto.DriverDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.jpa.DriverRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DriverService {

    private final DriverRepository driverRepository;
    private final EnterpriseService enterpriseService;
    private final ModelMapper modelMapper;

    @Autowired
    public DriverService(DriverRepository driverRepository, EnterpriseService enterpriseService, ModelMapper modelMapper) {
        this.driverRepository = driverRepository;
        this.enterpriseService = enterpriseService;
        this.modelMapper = modelMapper;
    }

    public List<Driver> findAvailableDrivers() {
        return driverRepository.findByVehiclesIsNull();
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver findOne(Long id) {
        Optional<Driver> foundDriver = driverRepository.findById(id);
        return foundDriver.orElseThrow(
                () -> new NotFoundException("Driver with " + id + " id, not exists"));
    }

    public Page<Driver> findAll(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public Page<Driver> findAllForManager(Long id, Pageable pageable) {
        List<Enterprise> enterprises = enterpriseService.findAllForManager(id);
        List<Driver> drivers = new ArrayList<>();

        for (Enterprise enterprise : enterprises) {
            drivers.addAll(driverRepository.findDriversByEnterpriseId(enterprise.getId()));
        }

        return new PageImpl<>(drivers);
    }

    @Cacheable(value = "allDriversForManager", key = "{#managerId, #page, #size}")
    public Page<Driver> findAllForManager(Long managerId, Integer page, Integer size) {
        log.info("Received page: " + page + ", itemsPerPage: " + size);
        List<Long> enterpriseIds =
                enterpriseService.findAllForManager(managerId)
                .stream()
                .map(Enterprise::getId).toList();

        Pageable pageable = PageRequest.of(
                page != null ? Math.max(page - 1, 0) : 0,
                size != null ? size : 10 // Можно задать дефолтный размер страницы
        );

        return driverRepository.findByEnterpriseIdIn(enterpriseIds, pageable);
    }

    public Driver findByActiveVehicleId(Long activeVehicleId) {
        Optional<Driver> foundVehicle = driverRepository.findByActiveVehicleId(activeVehicleId);
        return foundVehicle.orElseThrow(
                () -> new ResourceNotFoundException("Driver with " + activeVehicleId + " id, not exists"));
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

    public List<Driver> findAllForManager(Long id) {
        List<Enterprise> enterprises = enterpriseService.findAllForManager(id);
        List<Driver> drivers = new ArrayList<>();

        for (Enterprise enterprise : enterprises) {
            drivers.addAll(driverRepository.findDriversByEnterpriseId(enterprise.getId()));
        }

        return drivers;
    }

    public List<Driver> findAllByManager(Manager manager) {
        return driverRepository.findAllByEnterpriseIn(manager.getEnterprises());
    }

    public DriverDTO convertToDriverDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }
}
