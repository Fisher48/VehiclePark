//package ru.fisher.services;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import ru.fisher.kafka.NotificationEvent;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class NotificationProcessor {
//    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
//    private final ManagerRepository managerRepository;
//
//    @KafkaListener(
//            topics = "${app.kafka.topics}",
//            groupId = "${spring.kafka.consumer.group-id}"
//    )
//    public void processNotification(NotificationEvent event) {
//        log.info("Received event for manager: {}", event.getManagerId());
//        kafkaTemplate.send(, event)
//        kafkaTemplate.send(
//                "${app.kafka.topics.telegram-notifications}", telegramMsg
//        );
//
//    }
//}
//
//
