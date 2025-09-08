package ru.fisher.VehiclePark.models;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "autopark", name = "trip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_gps_data_id", nullable = false)
    private GpsData startGpsData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_gps_data_id", nullable = false)
    private GpsData endGpsData;

    @Column(name = "mileage", nullable = false, precision = 10, scale = 2)
    private BigDecimal mileage; // Расстояние в км

    @Transient // Опционально, если используете в коде
    private Duration duration;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<GpsData> gpsPoints = new ArrayList<>();

    @PostLoad
    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.duration = Duration.between(startTime, endTime);
        }
    }
}
