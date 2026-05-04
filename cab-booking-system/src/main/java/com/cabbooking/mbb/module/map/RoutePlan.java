package com.cabbooking.mbb.module.map;

import java.util.List;
import java.util.stream.Collectors;

public record RoutePlan(
        Coordinate pickup,
        Coordinate dropoff,
        List<Coordinate> path,
        List<RouteInstruction> instructions,
        double distanceKm,
        int durationMinutes,
        String algorithm,
        String routeSummary,
        String cacheKey,
        boolean cacheHit,
        boolean offlineFallbackAvailable) {

    public RoutePlan {
        path = List.copyOf(path);
        instructions = List.copyOf(instructions);
    }

    public RoutePlan withCacheHit(boolean cacheHit) {
        return new RoutePlan(pickup, dropoff, path, instructions, distanceKm, durationMinutes,
                algorithm, routeSummary, cacheKey, cacheHit, offlineFallbackAvailable);
    }

    public String instructionText() {
        return instructions.stream()
                .map(instruction -> instruction.sequence() + ". " + instruction.maneuver())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public List<List<Double>> latLngPairs() {
        return path.stream()
                .map(point -> List.of(point.latitude(), point.longitude()))
                .toList();
    }
}
