//package ru.fisher.VehiclePark.kafka;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Value("${app.kafka.topic}")
//    private String topic;
//
//    public void sendNotification(String message) {
//        kafkaTemplate.send(topic, message);
//    }
//}
