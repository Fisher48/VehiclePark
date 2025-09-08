package ru.fisher.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long chatId;
   // private String eventType;
    private String message;
    private String timestamp;
}
