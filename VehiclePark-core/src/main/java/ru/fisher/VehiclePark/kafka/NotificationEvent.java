package ru.fisher.VehiclePark.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long chatId;
    private Long managerId;
    private String message;
    private String timestamp;
}
