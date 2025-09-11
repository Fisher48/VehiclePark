package ru.fisher.VehiclePark.bot;

import lombok.Getter;
import lombok.Setter;
import ru.fisher.VehiclePark.models.Period;
import ru.fisher.VehiclePark.models.ReportType;

import java.time.LocalDateTime;

@Getter
@Setter
public class BotSession {
    private ReportType type;
    private Long targetId;
    private Period period;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Step currentStep = Step.START;

    public enum Step {
        START, TYPE_SELECTED, ID_ENTERED, PERIOD_SELECTED, START_DATE_ENTERED, END_DATE_ENTERED, READY
    }
}

