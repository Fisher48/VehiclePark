package ru.fisher.VehiclePark.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@EqualsAndHashCode
@Table(name = "vehicle", schema = "autopark")
public class Vehicle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Гос. номер должен быть указан")
    @Column(name = "number")
    private String number;

    @Column(name = "price")
    @NotNull(message = "Цена должна быть указана")
    @Min(value = 1, message = "Цена должна быть больше чем 0")
    private int price;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "year_of_car_production")
    @NotNull(message = "Год производства должен быть указан")
    @Min(value = 1950, message = "Год выпуска должен быть больше чем 1950")
    private int yearOfCarProduction;

    @Column(name = "purchase_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime purchaseTime; // Дата покупки (в UTC)

    // Связь "один ко многим" с брендом
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // Связь "многие ко многим" с водителями
    @ManyToMany(mappedBy = "vehicles")
    @JsonIgnore
    private List<Driver> drivers;

    // Поле для активного водителя
    @OneToOne(mappedBy = "activeVehicle")
    @JsonIgnore
    @JoinColumn(name = "activeVehicle")
    private Driver activeDriver;

    // Связь "один ко многим" с предприятием
    @ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GpsData> gpsData;

}
