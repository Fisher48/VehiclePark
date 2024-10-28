package ru.fisher.VehiclePark.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.VehicleService;

@Component
public class VehicleValidator implements Validator {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleValidator(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Vehicle.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vehicle vehicle = (Vehicle) target;

        if (vehicleService.getVehicleByNumber(vehicle.getNumber()).isPresent()) {
            errors.rejectValue("number","",
                    "Машина с таким номером: " + vehicle.getNumber() + " уже есть");
        }
    }
}
