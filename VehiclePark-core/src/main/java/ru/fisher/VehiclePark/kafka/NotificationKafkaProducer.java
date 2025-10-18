package ru.fisher.VehiclePark.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Value("${kafka.topic.notifications}")
    private String topic;

    public void sendNotification(Long managerId, String message) {
        NotificationEvent event = new NotificationEvent(
                null, // chatId пока неизвестен
                managerId,
                message,
                LocalDateTime.now().toString()
        );
        kafkaTemplate.send(topic, event);
    }
}
