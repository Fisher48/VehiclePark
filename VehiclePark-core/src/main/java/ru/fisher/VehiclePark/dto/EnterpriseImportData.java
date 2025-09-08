package ru.fisher.VehiclePark.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseImportData {

    @Id
    private Long id;
    private String name;
    private String city;
    private String timezone;
}
