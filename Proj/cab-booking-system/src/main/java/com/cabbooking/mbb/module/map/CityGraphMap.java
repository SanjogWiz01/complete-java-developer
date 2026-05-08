package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CityGraphMap {
    private final HaversineDistanceCalculator distanceCalculator;
    private final Map<String, MapNode> nodes;
    private final Map<String, List<MapEdge>> edges;

    public CityGraphMap(HaversineDistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
        this.nodes = createNodes();
        this.edges = createEdges();
    }

    public MapNode node(String id) {
        return nodes.get(id);
    }

    public List<MapEdge> neighbors(String nodeId) {
        return edges.getOrDefault(nodeId, List.of());
    }

    public MapNode nearestNode(Coordinate coordinate) {
        return nodes.values().stream()
                .min(Comparator.comparingDouble(node -> distanceCalculator.distanceKm(coordinate, node.coordinate())))
                .orElseThrow(() -> new IllegalStateException("City graph has no nodes"));
    }

    public double heuristicKm(String fromNodeId, String toNodeId) {
        return distanceCalculator.distanceKm(node(fromNodeId).coordinate(), node(toNodeId).coordinate());
    }

    private Map<String, MapNode> createNodes() {
        Map<String, MapNode> graphNodes = new HashMap<>();
        put(graphNodes, "LAKESIDE", "Lakeside", 28.2096, 83.9856);
        put(graphNodes, "PHEWA", "Phewa Lake", 28.2143, 83.9576);
        put(graphNodes, "DAMSIDE", "Damside", 28.1998, 83.9726);
        put(graphNodes, "AIRPORT", "Pokhara International Airport", 28.2009, 83.9821);
        put(graphNodes, "PRITHVI", "Prithvi Chowk", 28.2130, 83.9973);
        put(graphNodes, "MAHENDRA", "Mahendra Pul", 28.2154, 83.9896);
        put(graphNodes, "BINDHYABASINI", "Bindhyabasini Temple", 28.2333, 83.9833);
        put(graphNodes, "BAGAR", "Bagar", 28.2358, 83.9952);
        put(graphNodes, "SARANGKOT", "Sarangkot", 28.2439, 83.9483);
        put(graphNodes, "MATEPANI", "Matepani", 28.2100, 84.0128);
        return Map.copyOf(graphNodes);
    }

    private Map<String, List<MapEdge>> createEdges() {
        Map<String, List<MapEdge>> graphEdges = new HashMap<>();
        connect(graphEdges, "PHEWA", "LAKESIDE", 1.0);
        connect(graphEdges, "LAKESIDE", "DAMSIDE", 1.1);
        connect(graphEdges, "DAMSIDE", "AIRPORT", 1.3);
        connect(graphEdges, "LAKESIDE", "MAHENDRA", 0.9);
        connect(graphEdges, "MAHENDRA", "PRITHVI", 0.8);
        connect(graphEdges, "PRITHVI", "AIRPORT", 1.5);
        connect(graphEdges, "MAHENDRA", "BINDHYABASINI", 2.1);
        connect(graphEdges, "BINDHYABASINI", "BAGAR", 1.3);
        connect(graphEdges, "BINDHYABASINI", "SARANGKOT", 4.1);
        connect(graphEdges, "PRITHVI", "MATEPANI", 1.6);
        connect(graphEdges, "MATEPANI", "AIRPORT", 2.3);
        return graphEdges.entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), List.copyOf(entry.getValue())), HashMap::putAll);
    }

    private void put(Map<String, MapNode> graphNodes, String id, String landmark, double latitude, double longitude) {
        graphNodes.put(id, new MapNode(id, landmark, Coordinate.of(latitude, longitude, landmark)));
    }

    private void connect(Map<String, List<MapEdge>> graphEdges, String from, String to, double trafficWeight) {
        double distance = distanceCalculator.distanceKm(node(from).coordinate(), node(to).coordinate());
        graphEdges.computeIfAbsent(from, ignored -> new ArrayList<>()).add(new MapEdge(from, to, distance, trafficWeight));
        graphEdges.computeIfAbsent(to, ignored -> new ArrayList<>()).add(new MapEdge(to, from, distance, trafficWeight));
    }
}
