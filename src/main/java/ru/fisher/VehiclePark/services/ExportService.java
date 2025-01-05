package ru.fisher.VehiclePark.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.dto.ExportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Enterprise;

import java.io.StringWriter;
import java.util.List;

@Service
public class ExportService {

    private final ObjectMapper objectMapper;

    public ExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Экспорт в JSON
    public String exportToJson(Enterprise enterprise,
                               List<VehicleDTO> vehicles,
                               List<TripDTO> trips) throws Exception {
        // Wrap data into an export structure
        var exportData = new ExportDTO(enterprise, vehicles, trips);
        return objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(exportData);
    }

    // Экспорт в CSV
    public String exportToCsv(Enterprise enterprise,
                              List<VehicleDTO> vehicles,
                              List<TripDTO> trips) throws Exception {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write enterprise details
            csvWriter.writeNext(new String[]{"Enterprise ID", "Enterprise Name", "City", "Timezone"});
            csvWriter.writeNext(new String[]{String.valueOf(
                    enterprise.getId()),
                    enterprise.getName(),
                    enterprise.getCity(),
                    enterprise.getTimezone()});

            // Write vehicle details
            csvWriter.writeNext(new String[]{"Vehicle ID", "Brand ID", "Number", "Price", "Year", "Mileage"});
            for (VehicleDTO vehicle : vehicles) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(vehicle.getId()),
                        vehicle.getBrand().getId().toString(),
                        vehicle.getNumber(),
                        String.valueOf(vehicle.getPrice()),
                        String.valueOf(vehicle.getYearOfCarProduction()),
                        String.valueOf(vehicle.getMileage())
                });
            }

            // Write trip details
            csvWriter.writeNext(new String[]{"Trip ID", "Start Time", "End Time", "Start Point", "End Point", "Duration"});
            for (TripDTO trip : trips) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(trip.getId()),
                        trip.getStartTime(),
                        trip.getEndTime(),
                        trip.getStartPointAddress(),
                        trip.getEndPointAddress(),
                        String.valueOf(trip.getDuration())
                });
            }
        }
        return writer.toString();
    }
}
