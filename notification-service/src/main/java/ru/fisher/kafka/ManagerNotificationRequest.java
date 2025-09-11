package ru.fisher.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerNotificationRequest {
    private Long managerId;
    private String message;
}
