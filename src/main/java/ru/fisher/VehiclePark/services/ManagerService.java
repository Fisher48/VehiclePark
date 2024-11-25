package ru.fisher.VehiclePark.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.ManagerRepository;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager findOne(Long id) {
        Optional<Manager> foundManager = managerRepository.findById(id);
        return foundManager.orElse(null);
    }

    public Manager findByUsername(String username) {
        Optional<Manager> foundManager = managerRepository.findByUsername(username);
        return foundManager.orElse(null);
    }

}
