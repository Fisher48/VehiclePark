package ru.fisher.VehiclePark.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {

    @NotEmpty(message = "Имя не должно быть пустым")
    private String username;

    private String password;

}
