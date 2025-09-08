package ru.fisher.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(schema = "autopark", name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @JsonBackReference
    private List<Vehicle> vehicles;

}
