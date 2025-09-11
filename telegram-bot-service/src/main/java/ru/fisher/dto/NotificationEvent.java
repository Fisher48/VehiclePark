package ru.fisher.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long chatId;
    // private String eventType;
    private String message;
    private String timestamp;
}
