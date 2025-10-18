package ru.fisher.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.fisher.VehiclePark.kafka.NotificationEvent;
import ru.fisher.bot.TelegramBotV2;

@Service
@Slf4j
public class TelegramNotificationListener {

    private final TelegramBotV2 telegramBot;

    public TelegramNotificationListener(TelegramBotV2 telegramBot) {
        this.telegramBot = telegramBot;
    }

    @KafkaListener(topics = "${kafka.topic.telegram-notifications}", groupId = "telegram-group")
    public void listen(String message) {
        log.info("Уведомление из notification-service: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            NotificationEvent notification = mapper.readValue(message, NotificationEvent.class);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(notification.getChatId());
            sendMessage.setText(notification.getMessage() + " " + notification.getTimestamp());

            telegramBot.execute(sendMessage);
            log.info("✅ Отправлено сообщение в Telegram для chatId={}", notification.getChatId());

        } catch (Exception e) {
            log.error("❌ Ошибка при обработке сообщения из Kafka: {}", message, e);
        }
    }
}
