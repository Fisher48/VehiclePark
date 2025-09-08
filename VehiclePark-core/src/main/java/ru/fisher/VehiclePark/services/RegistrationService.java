package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;

@Service
public class RegistrationService {

    private final ManagerRepository managerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        manager.setRole("MANAGER");
        managerRepository.save(manager);
    }

//    @Transactional
//    public void register(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("USER");
//        userRepository.save(user);
//    }
}
