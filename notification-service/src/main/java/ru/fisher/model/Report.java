package ru.fisher.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "report", schema = "autopark")
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ReportType title;

    @Enumerated(EnumType.STRING)
    private Period period; // "DAY", "MONTH", "YEAR"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
