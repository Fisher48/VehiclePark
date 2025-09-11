package ru.fisher.VehiclePark.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeoJSONResponse {

    @JsonProperty("type")
    private String type = "FeatureCollection";

    @JsonProperty("features")
    private List<Feature> features;

    public GeoJSONResponse(List<Feature> features) {
        this.features = features;
    }

    @Data
    public static class Feature {

        @JsonProperty("type")
        private String type = "Feature";

        @JsonProperty("geometry")
        private Geometry geometry;

        @JsonProperty("properties")
        private Properties properties;

        public Feature(Geometry geometry, Properties properties) {
            this.geometry = geometry;
            this.properties = properties;
        }
    }

    @Data
    public static class Geometry {

        @JsonProperty("type")
        private String type = "Point";

        @JsonProperty("coordinates")
        private List<Double> coordinates; // Список для координат

        public Geometry(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    @Data
    public static class Properties {

        @JsonProperty("timestamp")
        private String timestamp;

        public Properties(String timestamp) {
            this.timestamp = timestamp;
        }
    }

}
