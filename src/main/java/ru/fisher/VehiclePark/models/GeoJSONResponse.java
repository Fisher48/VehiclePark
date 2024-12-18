package ru.fisher.VehiclePark.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GeoJSONResponse {

    @JsonProperty("type")
    private String type = "FeatureCollection";

    @JsonProperty("features")
    private List<Feature> features;

    public GeoJSONResponse(List<Feature> features) {
        this.features = features;
    }

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

    public static class Geometry {
        @JsonProperty("type")
        private String type = "Point";

        @JsonProperty("coordinates")
        private List<Double> coordinates;

        public Geometry(Double longitude, Double latitude) {
            this.coordinates = List.of(longitude, latitude); // GeoJSON использует порядок [долгота, широта]
        }
    }

    public static class Properties {
        @JsonProperty("timestamp")
        private String timestamp;

        public Properties(String timestamp) {
            this.timestamp = timestamp;
        }
    }

}
