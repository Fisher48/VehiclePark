package ru.fisher.VehiclePark.services;


import jakarta.ws.rs.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;

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

    @Transactional
    public void addChatIdToManager(Manager manager, Long chatId) {
        manager.setChatId(chatId);
        managerRepository.save(manager);
    }

    public Optional<Manager> getManagerByChatId(Long chatId) {
        return managerRepository.findManagerByChatId(chatId);
    }

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

    @Transactional
    public void updateManagerChatId(Long managerId, Long chatId) {
        // Сначала отвязываем этот chatId от других менеджеров
        managerRepository.clearChatIdFor(chatId);

        // Привязываем chatId к текущему менеджеру
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Менеджер не найден"));
        manager.setChatId(chatId);
        managerRepository.save(manager);
    }

}
