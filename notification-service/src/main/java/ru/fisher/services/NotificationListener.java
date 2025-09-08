//package ru.fisher.services;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import ru.fisher.kafka.NotificationEvent;
//
//@Component
//@Slf4j
//public class NotificationListener {
//
//    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
//    public void listen(NotificationEvent event) {
//        System.out.println("Получено уведомление: " + event);
//        // log.info("Received notification for manager {}: {}", event.getManagerId(), event.getMessage());
//        // Здесь можно вызвать REST или сохранить, или передать дальше
//        NotificationEvent notificationEvent = new NotificationEvent(
//                event.getEventType(),
//                event.getManagerId(),
//                event.getMessage(),
//                event.getTimestamp()
//        );
//        System.out.println(notificationEvent);
//    }
//}
