package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Manager;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Modifying
    @Query("UPDATE Manager m SET m.chatId = NULL WHERE m.chatId = :chatId")
    void clearChatIdFor(@Param("chatId") Long chatId);

    Optional<Manager> findByUsername(String username);

    Optional<Manager> findManagerByChatId(Long chatId);

    Optional<Manager> findByUsernameAndPassword(String username, String password);

}
