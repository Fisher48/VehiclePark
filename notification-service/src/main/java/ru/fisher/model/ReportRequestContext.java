package ru.fisher.model;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class ReportRequestContext {
    private BotState state = BotState.START;
    private ReportType type;       // vehicle, enterprise, total
    private String vehicleNumber;     // гос. номер авто
    private String enterpriseName;
    private Period period;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
