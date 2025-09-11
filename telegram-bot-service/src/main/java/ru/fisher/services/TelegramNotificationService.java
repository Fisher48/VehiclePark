//package ru.fisher.services;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import ru.fisher.bot.TelegramBotV2;
//import ru.fisher.dto.NotificationEvent;
//import ru.fisher.models.Manager;
//import ru.fisher.repositories.ManagerRepository;
//
//import java.util.Optional;
//
//
//@Service
//@Slf4j
//public class TelegramNotificationService {
//
//    private final TelegramBotV2 telegramBot;
//    private final ManagerRepository managerRepository;
//
//    public TelegramNotificationService(TelegramBotV2 telegramBot, ManagerRepository managerRepository) {
//        this.telegramBot = telegramBot;
//        this.managerRepository = managerRepository;
//    }
//
//    @KafkaListener(topics = "${app.kafka.topic}")
//    public void sendNotification(NotificationEvent notification) {
//        SendMessage message = new SendMessage();
//        Optional<Manager> manager = managerRepository.findById(notification.getManagerId());
//        if (manager.isPresent()) {
//            message.setChatId(manager.get().getChatId().toString());
//            message.setText(notification.getMessage());
//        } else {
//            throw new RuntimeException("Менеджер не найден");
//        }
//        try {
//            telegramBot.execute(message);
//        } catch (TelegramApiException e) {
//            log.error("Ошибка отправки: {}", e.getMessage());
//        }
//    }
//
////    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
////    public void handleNotification(NotificationEvent event) {
////        managerRepository.findById(event.getManagerId())
////                .ifPresent(manager -> {
////                    if (manager.getChatId() != null) {
////                       // String message = formatMessage(event);
////                        telegramBot.sendNotification(manager.getChatId(), event.toString());
////                    }
////                });
////    }
//
//    private String formatMessage(NotificationEvent event) {
//        return String.format("""
//            🔔 %s
//            Тип: %s
//            Время: %s
//            """,
//                event.getMessage(),
//                event.getEventType(),
//                event.getTimestamp()
//        );
//    }
//}
