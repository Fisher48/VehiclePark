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

        int vehicleId = vehicle.getId();

        // Проверка на уникальность номера
        if (vehicleService.getVehicleByNumber(vehicle.getNumber()).isPresent()) {
            Vehicle existingVehicle = vehicleService.getVehicleByNumber(vehicle.getNumber()).get();
            // Убедится, что это не тот же самый автомобиль
            if (existingVehicle.getId() != (vehicleId)) {
                errors.rejectValue("number", "",
                        "Машина с таким номером: " + vehicle.getNumber() + " уже есть");
            }
        }
    }
}
