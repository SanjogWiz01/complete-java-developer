package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class CoordinateParser {
    private final Map<String, Coordinate> knownStops = Map.ofEntries(
            Map.entry("lakeside", Coordinate.of(28.2096, 83.9856, "Lakeside, Pokhara")),
            Map.entry("phewa lake", Coordinate.of(28.2143, 83.9576, "Phewa Lake")),
            Map.entry("airport", Coordinate.of(28.2009, 83.9821, "Pokhara International Airport")),
            Map.entry("pokhara international airport", Coordinate.of(28.2009, 83.9821, "Pokhara International Airport")),
            Map.entry("sarangkot", Coordinate.of(28.2439, 83.9483, "Sarangkot")),
            Map.entry("bindhyabasini", Coordinate.of(28.2333, 83.9833, "Bindhyabasini Temple")),
            Map.entry("bindhyabasini temple", Coordinate.of(28.2333, 83.9833, "Bindhyabasini Temple")),
            Map.entry("prithvi chowk", Coordinate.of(28.2130, 83.9973, "Prithvi Chowk")),
            Map.entry("mahendra pul", Coordinate.of(28.2154, 83.9896, "Mahendra Pul")),
            Map.entry("damside", Coordinate.of(28.1998, 83.9726, "Damside")),
            Map.entry("old bus park", Coordinate.of(28.2144, 83.9879, "Old Bus Park")),
            Map.entry("matepani", Coordinate.of(28.2100, 84.0128, "Matepani")),
            Map.entry("bagar", Coordinate.of(28.2358, 83.9952, "Bagar"))
    );

    public Coordinate resolve(String label, Double latitude, Double longitude) {
        if (isValid(latitude, longitude)) {
            return Coordinate.of(latitude, longitude, label);
        }
        return parseKnownStop(label).orElse(Coordinate.of(28.2096, 83.9856, "Lakeside, Pokhara"));
    }

    public Optional<Coordinate> parseKnownStop(String label) {
        if (label == null || label.isBlank()) {
            return Optional.empty();
        }
        String normalized = label.toLowerCase(Locale.ROOT).replace(",", " ").replaceAll("\\s+", " ").trim();
        if (knownStops.containsKey(normalized)) {
            return Optional.of(knownStops.get(normalized));
        }
        return knownStops.entrySet().stream()
                .filter(entry -> normalized.contains(entry.getKey()) || entry.getKey().contains(normalized))
                .min(Comparator.comparingInt(entry -> Math.abs(entry.getKey().length() - normalized.length())))
                .map(Map.Entry::getValue);
    }

    private boolean isValid(Double latitude, Double longitude) {
        return latitude != null && longitude != null
                && latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }
}
