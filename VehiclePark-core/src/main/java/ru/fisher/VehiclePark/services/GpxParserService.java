package ru.fisher.VehiclePark.services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.fisher.VehiclePark.exceptions.GpxParsingException;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Vehicle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GpxParserService {

    public List<GpsData> parseGpxFile(Vehicle vehicle, MultipartFile gpxFile, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            String gpxContent = new String(gpxFile.getBytes(), StandardCharsets.UTF_8);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(gpxContent));
            Document doc = builder.parse(is);
            NodeList trackPoints = doc.getElementsByTagName("trkpt");

            List<GpsData> gpsDataList = new ArrayList<>();
            GeometryFactory geometryFactory = new GeometryFactory();

            for (int i = 0; i < trackPoints.getLength(); i++) {
                Element trkpt = (Element) trackPoints.item(i);
                double lat = Double.parseDouble(trkpt.getAttribute("lat"));
                double lon = Double.parseDouble(trkpt.getAttribute("lon"));
                String timeStr = trkpt.getElementsByTagName("time").item(0).getTextContent();
                LocalDateTime pointTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME);

                if (pointTime.isBefore(startTime) || pointTime.isAfter(endTime)) {
                    throw new IllegalArgumentException("GPX содержит точки вне диапазона");
                }

                GpsData data = new GpsData();
                data.setVehicle(vehicle);
                data.setCoordinates(geometryFactory.createPoint(new Coordinate(lon, lat)));
                data.setTimestamp(pointTime);
                gpsDataList.add(data);
            }

            return gpsDataList;
        } catch (Exception e) {
            throw new GpxParsingException("Ошибка парсинга GPX: " + e.getMessage());
        }
    }
}
