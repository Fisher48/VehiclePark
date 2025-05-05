package ru.fisher.VehiclePark.services;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.ManagerRepository;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Manager findOne(Long id) {
        Optional<Manager> foundManager = managerRepository.findById(id);
        return foundManager.orElse(null);
    }

    public Manager findByUsername(String username) {
        Optional<Manager> foundManager = managerRepository.findByUsername(username);
        return foundManager.orElse(null);
    }

//    public Manager authenticate(String username, String password) {
//        return managerRepository.findByUsernameAndPassword(username, password)
//                .orElseThrow(() -> new RuntimeException("Неверный логин или пароль"));
//    }

    // ✅ Добавляем метод для Telegram-бота:
    public Manager authenticate(String username, String password) {
        Optional<Manager> optionalManager = managerRepository.findByUsername(username);

        if (optionalManager.isEmpty()) {
            throw new RuntimeException("Менеджер не найден");
        }

        Manager manager = optionalManager.get();

        if (!passwordEncoder.matches(password, manager.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return manager;
    }

}
