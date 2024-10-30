package ru.fisher.VehiclePark.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle", schema = "autopark")
public class Vehicle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotNull(message = "Название машины должно быть указано")
    @NotBlank
    @Size(max = 100, message = "Название должно быть максимум 100 символов")
    private String name;

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

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

}
