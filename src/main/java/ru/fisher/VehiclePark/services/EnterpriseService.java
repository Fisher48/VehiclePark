package ru.fisher.VehiclePark.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.DriverRepository;
import ru.fisher.VehiclePark.repositories.EnterpriseRepository;
import ru.fisher.VehiclePark.repositories.VehicleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    private final VehicleRepository vehicleRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository, VehicleRepository vehicleRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public List<Enterprise> findAll() {
        return enterpriseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Enterprise findOne(Long id) {
        Optional<Enterprise> foundVehicle = enterpriseRepository.findById(id);
        return foundVehicle.orElseThrow(
                () -> new ResourceNotFoundException("Enterprise with " + id + " id, not exists"));
    }

    public List<Vehicle> findVehiclesByEnterpriseId(Long id) {
        return vehicleRepository.findVehiclesByEnterpriseId(id);
    }

    @Transactional
    public void save(Enterprise enterprise) {
        enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void update(Long id, Enterprise enterprise) {
        enterprise.setId(id);
        enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void delete(Long id) {
        enterpriseRepository.deleteById(id);
    }

}
