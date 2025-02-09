package ru.fisher.VehiclePark.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.exceptions.EnterpriseNotUpdatedException;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.*;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final ManagerRepository managerRepository;
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final GpsDataRepository gpsDataRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository, ManagerRepository managerRepository, VehicleRepository vehicleRepository, TripRepository tripRepository, GpsDataRepository gpsDataRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.managerRepository = managerRepository;
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
        this.gpsDataRepository = gpsDataRepository;
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

        // Проверяем и устанавливаем временную зону
        if (isValidTimeZone(updatedEnterprise.getTimezone())) {
            enterprise.setTimezone(updatedEnterprise.getTimezone());
        } else {
            throw new IllegalArgumentException("Некорректный часовой пояс: " + updatedEnterprise.getTimezone());
        }

        List<Manager> managers = enterpriseRepository.findById(idEnterprise).get().getManagers();

        updatedEnterprise.setManagers(managers);
        updatedEnterprise.setId(idEnterprise);

        enterpriseRepository.save(updatedEnterprise);
    }

    private boolean isValidTimeZone(String timezone) {
        // Проверяем, является ли это валидным идентификатором или смещением
        return Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone) ||
                timezone.matches("UTC[+-]\\d{2}:\\d{2}");
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

        // Удаляем все поездки, связанные с машинами этого предприятия
        for (Vehicle vehicle : enterprise.getVehicles()) {
            for (Trip trip : vehicle.getTrip()) {
                if (trip.getStartGpsData() != null) {
                    gpsDataRepository.delete(trip.getStartGpsData());
                }
                if (trip.getEndGpsData() != null) {
                    gpsDataRepository.delete(trip.getEndGpsData());
                }
            }
            tripRepository.deleteAll(vehicle.getTrip());
        }

        vehicleRepository.deleteAll(enterprise.getVehicles());
        enterpriseRepository.deleteById(idEnterprise);
    }

    public List<Enterprise> findAllForManager(Long id) {
        return enterpriseRepository.findEnterprisesByManagersId(id)
                .stream()
                .sorted(Comparator.comparing(Enterprise::getId))
                .toList();
    }

    public boolean isEnterpriseManagedByManager(Long enterpriseId, Long managerId) {
        return enterpriseRepository.existsByIdAndManagersId(enterpriseId, managerId);
    }

    public String getTimezoneByEnterpriseId(Long enterpriseId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));
        return enterprise.getTimezone();
    }

}
