package ru.fisher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fisher.bot.TelegramBotV2;
import ru.fisher.dto.NotificationEvent;

@Service
@Slf4j
public class TelegramNotificationListener {

    private final TelegramBotV2 telegramBot;

    public TelegramNotificationListener(TelegramBotV2 telegramBot) {
        this.telegramBot = telegramBot;
    }

    @KafkaListener(topics = "${kafka.topic.telegram-notifications}", groupId = "telegram-group")
    public void listen(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            NotificationEvent notification = mapper.readValue(message, NotificationEvent.class);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(notification.getChatId());
            sendMessage.setText(notification.getMessage());

            telegramBot.execute(sendMessage);
            log.info("Отправлено сообщение в Telegram для chatId={}", notification.getChatId());

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из Kafka: {}", message, e);
        }
    }

//    @KafkaListener(topics = "${kafka.topic.telegram-notifications}", groupId = "telegram-group")
//    public void listen(String message) throws TelegramApiException {
//        log.info("Уведомление из notification-service: {}", message);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonNode node = mapper.readTree(message);
//            long chatId = node.get("chatId").asLong();
//            String text = node.get("message").asText();
//
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(chatId);
//            sendMessage.setText(text);
//            telegramBot.execute(sendMessage);
//        } catch (JsonProcessingException e) {
//            log.error("Invalid Kafka JSON: {}", message, e);
//        }
//    }
}
