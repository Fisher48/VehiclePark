package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.services.ImportService;


@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping(value = "/enterprise", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importEnterpriseData(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("format") String format) {
        try {
            // Обработка файла через сервис
            importService.importEnterpriseData(file, format);
            return ResponseEntity.ok("Импорт данных успешно выполнен!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при импорте данных: " + e.getMessage());
        }
    }
}
