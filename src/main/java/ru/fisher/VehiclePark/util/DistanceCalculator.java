package ru.fisher.VehiclePark.util;

import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.models.GpsData;

import java.util.List;

@Component
public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Вычисление расстояния между двумя точками (широта и долгота) в километрах.
     *
     * @param lat1 Широта первой точки.
     * @param lon1 Долгота первой точки.
     * @param lat2 Широта второй точки.
     * @param lon2 Долгота второй точки.
     * @return Расстояние в километрах.
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public static double calculateMileageFromGpx(List<GpsData> gpsDataList) {
        double totalDistance = 0.0;

        for (int i = 1; i < gpsDataList.size(); i++) {
            GpsData prevPoint = gpsDataList.get(i - 1);
            GpsData currPoint = gpsDataList.get(i);

            // Расчет расстояния между двумя точками
            totalDistance += calculateDistance(
                    prevPoint.getLatitude(),
                    prevPoint.getLongitude(),
                    currPoint.getLatitude(),
                    currPoint.getLongitude()
            );
        }

        return totalDistance;
    }


}
