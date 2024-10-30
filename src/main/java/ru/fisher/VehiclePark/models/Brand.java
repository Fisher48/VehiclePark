package ru.fisher.VehiclePark.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "autopark", name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "type")
    private String type;

    @Column(name = "fuel_tank")
    private int fuelTank;

    @Column(name = "load_capacity")
    private int loadCapacity;

    @Column(name = "number_of_seats")
    private int numberOfSeats;

    @OneToMany(mappedBy = "brand")
    private List<Vehicle> vehicles;

}
