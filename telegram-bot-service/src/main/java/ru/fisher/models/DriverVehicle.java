package ru.fisher.models;

import jakarta.persistence.*;

@Entity
@Table(schema = "autopark", name = "driver_vehicle") // Укажите схему, если необходимо
public class DriverVehicle {

    @Id
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;


    @Id
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;


}
