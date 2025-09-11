package ru.fisher.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "gps_data", schema = "autopark")
public class GpsData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @JsonBackReference
    private Vehicle vehicle;

    @Column(name = "coordinates", columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point coordinates;

    @Column(name = "timestamp", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Setter
    @Transient
    private Double longitude; // x

    @Setter
    @Transient
    private Double latitude; // y

    public GpsData() {}

    public GpsData(Vehicle vehicle, Point coordinates, Double longitude,
                   Double latitude, LocalDateTime timestamp) {
        this.vehicle = vehicle;
        this.coordinates = coordinates;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return this.coordinates.getX();
    }

    public Double getLatitude() {
        return this.coordinates.getY();
    }

}
