package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RouteInstructionDecoder {
    private final HaversineDistanceCalculator distanceCalculator;

    public RouteInstructionDecoder(HaversineDistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public List<RouteInstruction> decode(List<Coordinate> path) {
        if (path.size() < 2) {
            return List.of(new RouteInstruction(1, "Stay near " + path.get(0).label(), path.get(0).label(), 0));
        }

        List<RouteInstruction> instructions = new ArrayList<>();
        instructions.add(new RouteInstruction(1, "Start from " + path.get(0).label(), path.get(0).label(), 0));
        for (int index = 1; index < path.size(); index++) {
            Coordinate previous = path.get(index - 1);
            Coordinate current = path.get(index);
            double segmentKm = distanceCalculator.distanceKm(previous, current);
            String direction = directionLabel(previous, current);
            String text = index == path.size() - 1
                    ? "Arrive at " + current.label() + " via the " + direction + " side"
                    : "Continue " + direction + " toward " + current.label() + " for " + String.format("%.1f", segmentKm) + " km";
            instructions.add(new RouteInstruction(index + 1, text, current.label(), round(segmentKm)));
        }
        return instructions;
    }

    private String directionLabel(Coordinate previous, Coordinate current) {
        double latDiff = current.latitude() - previous.latitude();
        double lngDiff = current.longitude() - previous.longitude();
        if (Math.abs(latDiff) > Math.abs(lngDiff)) {
            return latDiff >= 0 ? "north" : "south";
        }
        return lngDiff >= 0 ? "east" : "west";
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
