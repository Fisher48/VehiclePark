package ru.fisher.VehiclePark.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.exceptions.EnterpriseNotUpdatedException;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.DriverRepository;
import ru.fisher.VehiclePark.repositories.EnterpriseRepository;
import ru.fisher.VehiclePark.repositories.ManagerRepository;
import ru.fisher.VehiclePark.repositories.VehicleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    private final ManagerRepository managerRepository;

    private final VehicleRepository vehicleRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository, ManagerRepository managerRepository, VehicleRepository vehicleRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.managerRepository = managerRepository;
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

    @Transactional(readOnly = true)
    public Enterprise findById(Long id) {
        return enterpriseRepository.findById(id).orElse(null);
    }

    public List<Vehicle> findVehiclesByEnterpriseId(Long id) {
        return vehicleRepository.findVehiclesByEnterpriseId(id);
    }

    @Transactional
    public void save(Enterprise enterprise) {
        enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void save(Enterprise enterprise, Long id) {
        Optional<Manager> foundManager = managerRepository.findById(id);
        if (enterprise.getManagers() == null) {
            enterprise.setManagers(new ArrayList<>());
        }
        enterprise.getManagers().add(foundManager.get());
        enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void update(Long id, Enterprise enterprise) {
        List<Manager> managers = enterpriseRepository.findById(id).get().getManagers();
        enterprise.setManagers(managers);
        enterprise.setId(id);
        enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void update(Long idManager, Long idEnterprise, Enterprise updatedEnterprise) {
        Enterprise enterprise = enterpriseRepository.findById(idEnterprise).get();
        List<Enterprise> managerEnterprises = managerRepository.findById(idManager).get().getEnterprises();

        if (!managerEnterprises.contains(enterprise)) {
            throw new EnterpriseNotUpdatedException("Нет доступа к данному предприятию");
        }

        List<Manager> managers = enterpriseRepository.findById(idEnterprise).get().getManagers();

        updatedEnterprise.setManagers(managers);
        updatedEnterprise.setId(idEnterprise);

        enterpriseRepository.save(updatedEnterprise);
    }

    @Transactional
    public void delete(Long id) {
        enterpriseRepository.deleteById(id);
    }


    @Transactional
    public void delete(Long idManager, Long idEnterprise) {
        // enterprisesRepository.deleteById(id);
        Enterprise enterprise = enterpriseRepository.findById(idEnterprise).get();
        List<Enterprise> managerEnterprises = managerRepository.findById(idManager).get().getEnterprises();

        if (!managerEnterprises.contains(enterprise)) {
            throw new AccessDeniedException("Нет доступа к данному предприятию");
        }

        // Удаление предприятия из списка у всех менеджеров, у которых оно есть
        for (Manager manager : enterprise.getManagers()) {
            manager.getEnterprises().remove(enterprise);
        }
        enterpriseRepository.deleteById(idEnterprise);
    }

    public List<Enterprise> findAllForManager(Long id) {
        return enterpriseRepository.findEnterprisesByManagersId(id);
    }

    public String getTimezoneByEnterpriseId(Long enterpriseId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));
        return enterprise.getTimezone();
    }

}
