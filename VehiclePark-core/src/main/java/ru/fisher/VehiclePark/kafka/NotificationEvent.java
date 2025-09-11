package ru.fisher.VehiclePark.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String eventType;
    private Long managerId;
    private String message;
    private String timestamp;
}
