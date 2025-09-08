package ru.fisher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.model.Manager;


import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUsername(String username);

    Optional<Manager> findManagerByChatId(Long chatId);

    Optional<Manager> findByUsernameAndPassword(String username, String password);

}
