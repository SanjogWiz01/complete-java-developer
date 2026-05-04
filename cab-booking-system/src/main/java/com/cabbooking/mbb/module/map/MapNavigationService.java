package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapNavigationService {
    private static final double CITY_SPEED_KMPH = 34.0;
    private static final double MEANINGFUL_MOVEMENT_KM = 0.12;

    private final CoordinateParser coordinateParser;
    private final CityGraphMap cityGraphMap;
    private final PathfindingEngine pathfindingEngine;
    private final RouteInstructionDecoder instructionDecoder;
    private final RouteCacheService routeCacheService;
    private final HaversineDistanceCalculator distanceCalculator;

    public MapNavigationService(CoordinateParser coordinateParser,
                                CityGraphMap cityGraphMap,
                                PathfindingEngine pathfindingEngine,
                                RouteInstructionDecoder instructionDecoder,
                                RouteCacheService routeCacheService,
                                HaversineDistanceCalculator distanceCalculator) {
        this.coordinateParser = coordinateParser;
        this.cityGraphMap = cityGraphMap;
        this.pathfindingEngine = pathfindingEngine;
        this.instructionDecoder = instructionDecoder;
        this.routeCacheService = routeCacheService;
        this.distanceCalculator = distanceCalculator;
    }

    public RoutePlan planRoute(String pickupLabel, String dropoffLabel,
                               Double pickupLatitude, Double pickupLongitude,
                               Double dropoffLatitude, Double dropoffLongitude,
                               String vehicleType, boolean offlineAllowed) {
        Coordinate pickup = coordinateParser.resolve(pickupLabel, pickupLatitude, pickupLongitude);
        Coordinate dropoff = coordinateParser.resolve(dropoffLabel, dropoffLatitude, dropoffLongitude);
        String cacheKey = routeCacheService.buildKey(pickup, dropoff, vehicleType, offlineAllowed);
        return routeCacheService.get(cacheKey).orElseGet(() -> buildAndCacheRoute(pickup, dropoff, cacheKey, offlineAllowed));
    }

    public boolean isMeaningfulMovement(Coordinate previous, Coordinate latest) {
        return distanceCalculator.distanceKm(previous, latest) >= MEANINGFUL_MOVEMENT_KM;
    }

    public double directDistanceKm(double pickupLatitude, double pickupLongitude, double dropoffLatitude, double dropoffLongitude) {
        return distanceCalculator.distanceKm(pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude);
    }

    public RouteCacheService.CacheStats cacheStats() {
        return routeCacheService.stats();
    }

    public long handleTrafficSignalChange() {
        return routeCacheService.markTrafficChanged();
    }

    private RoutePlan buildAndCacheRoute(Coordinate pickup, Coordinate dropoff, String cacheKey, boolean offlineAllowed) {
        MapNode startNode = cityGraphMap.nearestNode(pickup);
        MapNode endNode = cityGraphMap.nearestNode(dropoff);
        List<MapNode> graphPath = pathfindingEngine.findPath(cityGraphMap, startNode.id(), endNode.id());
        List<Coordinate> path = stitchEndpoints(pickup, dropoff, graphPath);
        double distanceKm = round(sumDistance(path));
        int durationMinutes = Math.max(1, (int) Math.ceil((distanceKm / CITY_SPEED_KMPH) * 60));
        List<RouteInstruction> instructions = instructionDecoder.decode(path);
        String summary = "A* route via " + startNode.landmark() + " and " + endNode.landmark();

        RoutePlan plan = new RoutePlan(pickup, dropoff, path, instructions, distanceKm, durationMinutes,
                "A_STAR_CITY_GRAPH", summary, cacheKey, false, offlineAllowed);
        return routeCacheService.put(cacheKey, plan);
    }

    private List<Coordinate> stitchEndpoints(Coordinate pickup, Coordinate dropoff, List<MapNode> graphPath) {
        List<Coordinate> path = new ArrayList<>();
        path.add(pickup);
        graphPath.stream()
                .map(MapNode::coordinate)
                .filter(point -> distanceCalculator.distanceKm(path.get(path.size() - 1), point) > 0.05)
                .forEach(path::add);
        if (distanceCalculator.distanceKm(path.get(path.size() - 1), dropoff) > 0.05) {
            path.add(dropoff);
        }
        return path;
    }

    private double sumDistance(List<Coordinate> path) {
        double total = 0;
        for (int index = 1; index < path.size(); index++) {
            total += distanceCalculator.distanceKm(path.get(index - 1), path.get(index));
        }
        return total;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
