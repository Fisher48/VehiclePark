package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Manager;
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


    public Page<Driver> findAllForManager(Long managerId, Integer page, Integer size) {

        log.info("Received page: " + page + ", itemsPerPage: " + size);
        List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
        List<Driver> drivers = new ArrayList<>();
        for (Enterprise enterprise : enterprises) {
            drivers.addAll(driverRepository.findDriversByEnterpriseId(enterprise.getId()));
        }

//    	Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
//	    return new PageImpl<Driver>(drivers, pageable, drivers.size());

        int pageNumber = (page != null) ? Math.max(page - 1, 0) : 0;
        Pageable pageable = PageRequest.of(pageNumber, size);
        int pageSize = (size != null) ? size : drivers.size(); // Установим размер страницы равным количеству результатов, если size = null
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), drivers.size());

        return new PageImpl<>(drivers.subList(start, end), pageable, drivers.size());
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
}
