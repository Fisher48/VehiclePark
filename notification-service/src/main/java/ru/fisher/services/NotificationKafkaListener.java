package ru.fisher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.kafka.NotificationEvent;
import ru.fisher.model.Manager;
import ru.fisher.repositories.ManagerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class NotificationKafkaListener {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final ManagerRepository managerRepository;

    @Value("${kafka.topic.telegram-notifications}")
    private String telegramTopic;

    @Autowired
    public NotificationKafkaListener(KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                                     ManagerRepository managerRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.managerRepository = managerRepository;
    }

    @KafkaListener(topics = "${kafka.topic.notifications}", groupId = "notification-group")
    public void consume(NotificationEvent request) {
        // 1. Ищем chatId по managerId
        Manager manager = managerRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        // 2. Формируем enriched NotificationEvent
        NotificationEvent event = new NotificationEvent(
                manager.getChatId(),
                manager.getId(),
                "Менеджер: " + manager.getUsername() + " " + request.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        );

        kafkaTemplate.send(telegramTopic, event);
    }
}
