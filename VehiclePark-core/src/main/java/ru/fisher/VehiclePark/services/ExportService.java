package ru.fisher.VehiclePark.services;

import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.dto.ExportDTO;
import ru.fisher.VehiclePark.util.ExportFormatter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final Map<String, ExportFormatter<ExportDTO>> formatters;

    @SuppressWarnings("unchecked")
    public ExportService(List<ExportFormatter<?>> formatterList) {
        this.formatters = formatterList.stream()
                .filter(Objects::nonNull)
                .map(f -> (ExportFormatter<ExportDTO>) f)
                .collect(Collectors.toMap(ExportFormatter::getFormat,f -> f));
    }

    public String export(ExportDTO exportDTO, String format) throws Exception {
        ExportFormatter<ExportDTO> formatter = formatters.get(format.toLowerCase());
        if (formatter == null) {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
        return formatter.format(exportDTO);
    }
}
