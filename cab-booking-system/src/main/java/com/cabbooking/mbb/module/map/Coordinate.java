package com.cabbooking.mbb.module.map;

public record Coordinate(double latitude, double longitude, String label) {
    public Coordinate {
        if (label == null || label.isBlank()) {
            label = "Pinned location";
        }
    }

    public static Coordinate of(double latitude, double longitude, String label) {
        return new Coordinate(latitude, longitude, label);
    }

    public boolean isValid() {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }

    public String roundedKey() {
        return String.format("%.4f,%.4f", latitude, longitude);
    }
}
