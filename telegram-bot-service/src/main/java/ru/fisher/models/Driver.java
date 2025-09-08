package ru.fisher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(schema = "autopark", name = "driver")
public class Driver {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Обязательное поле")
    private String name;

    @Column(name = "salary")
    @NotNull(message = "Обязательное поле")
    private Double salary;

    @Column(name = "active")
    //@Transient
    private Boolean isActive = false;

    // Связь "многие ко многим" с автомобилями
    @ManyToMany
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    @JsonIgnore
    private List<Vehicle> vehicles;

    // Связь "один ко многим" с предприятием
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;

    @OneToOne
    @JoinColumn(name = "active_vehicle_id", referencedColumnName = "id")
    //@Transient
    private Vehicle activeVehicle;
}
