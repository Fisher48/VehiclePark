package ru.fisher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.fisher.kafka.ManagerNotificationRequest;
import ru.fisher.kafka.NotificationEvent;
import ru.fisher.model.Manager;
import ru.fisher.repositories.ManagerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class NotificationKafkaListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate;
    private final ManagerRepository managerRepository;

    @Value("${kafka.topic.telegram-notifications}")
    private String telegramTopic;

    @Value("${vehiclepark-core.url}")
    private String vehicleparkCoreUrl; // http://vehiclepark-core:8080

    @Autowired
    public NotificationKafkaListener(KafkaTemplate<String, String> kafkaTemplate, ManagerRepository managerRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.managerRepository = managerRepository;
        this.restTemplate = new RestTemplate();
    }

    @KafkaListener(topics = "${kafka.topic.notifications}", groupId = "notification-group")
    public void consume(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ManagerNotificationRequest request = mapper.readValue(message, ManagerNotificationRequest.class);

            Optional<Manager> managerOpt = managerRepository.findById(request.getManagerId());
            if (managerOpt.isEmpty()) {
                log.warn("Manager with id={} not found", request.getManagerId());
                return;
            }

            String newMsg = request.getMessage() + " " + managerOpt.get().getUsername() + " выгрузил отчет "
                    + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);

            long chatId = managerOpt.get().getChatId();
            NotificationEvent outgoing = new NotificationEvent(
                    chatId,
                    newMsg,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
            String out = mapper.writeValueAsString(outgoing);

            kafkaTemplate.send(telegramTopic, out);

        } catch (Exception e) {
            log.error("Ошибка при обработке уведомления из Kafka: {}", message, e);
        }
    }
}

//    @KafkaListener(topics = "${kafka.topic.notifications}", groupId = "notification-group")
//    public void consume(String message) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree(message);
//        long managerId = node.get("managerId").asLong();
//        String msg = node.get("message").asText();
//
//        // Запрос chatId
////        ResponseEntity<String> response = restTemplate.getForEntity(
////                vehicleparkCoreUrl + "/api/managers/" + managerId + "/chat-id",
////                String.class
////        );
//        Optional<Manager> manager = managerRepository.findById(managerId);
//        String out = String.format("{\"chatId\": %s, \"message\": \"%s\"}", manager.get().getChatId(), msg);
//        kafkaTemplate.send(telegramTopic, out);
//
//        // Отправка в telegram-bot-service
////        if (response.getStatusCode().is2xxSuccessful()
////                && response.getBody() != null
////                && response.getBody().matches("\\d+")) {
////            String chatId = response.getBody();
////            String out = String.format("{\"chatId\": %s, \"message\": \"%s\"}", chatId, msg);
////            log.info("Sending to Kafka: {}", out);
////            kafkaTemplate.send(telegramTopic, out);
////        } else {
////            log.error("Failed to get valid chatId: status={}, body={}", response.getStatusCode(), response.getBody());
////        }
//    }
//}
