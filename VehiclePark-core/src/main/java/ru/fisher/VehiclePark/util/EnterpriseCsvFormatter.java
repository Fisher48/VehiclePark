package ru.fisher.VehiclePark.util;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.dto.ExportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Enterprise;

import java.io.StringWriter;
import java.util.List;

@Component
public class EnterpriseCsvFormatter implements ExportFormatter<ExportDTO> {

    @Override
    public String getFormat() {
        return "csv";
    }

    @Override
    public String format(ExportDTO data) throws Exception {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            Enterprise enterprise = data.getEnterprise();
            List<VehicleDTO> vehicles = data.getVehicles();
            List<TripDTO> trips = data.getTrips();

            csvWriter.writeNext(new String[]
                    {"Enterprise ID", "Name", "City", "Timezone"});
            csvWriter.writeNext(new String[]{
                    String.valueOf(enterprise.getId()),
                    enterprise.getName(),
                    enterprise.getCity(),
                    enterprise.getTimezone()});

            csvWriter.writeNext(new String[]
                    {"Vehicle ID", "Brand", "Number", "Price", "Year", "Mileage"});
            for (var v : vehicles) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(v.getId()),
                        v.getBrand().getId().toString(),
                        v.getNumber(),
                        String.valueOf(v.getPrice()),
                        String.valueOf(v.getYearOfCarProduction()),
                        String.valueOf(v.getMileage())
                });
            }

            csvWriter.writeNext(new String[]
                    {"Trip ID", "Start Time", "End Time", "Start", "End", "Duration", "Mileage"});
            for (var t : trips) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(t.getId()),
                        t.getStartTime(),
                        t.getEndTime(),
                        t.getStartPointAddress(),
                        t.getEndPointAddress(),
                        String.valueOf(t.getDuration()),
                        String.valueOf(t.getMileage())
                });
            }
        }
        return writer.toString();
    }
}
