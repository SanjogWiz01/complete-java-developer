package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Component
public class PathfindingEngine {
    public List<MapNode> findPath(CityGraphMap graph, String startNodeId, String goalNodeId) {
        PriorityQueue<NodeScore> openSet = new PriorityQueue<>(Comparator.comparingDouble(NodeScore::priority));
        Map<String, String> previous = new HashMap<>();
        Map<String, Double> bestCost = new HashMap<>();

        bestCost.put(startNodeId, 0.0);
        openSet.add(new NodeScore(startNodeId, graph.heuristicKm(startNodeId, goalNodeId)));

        while (!openSet.isEmpty()) {
            String current = openSet.poll().nodeId();
            if (current.equals(goalNodeId)) {
                return reconstructPath(graph, previous, current);
            }

            for (MapEdge edge : graph.neighbors(current)) {
                double candidateCost = bestCost.get(current) + edge.distanceKm();
                if (candidateCost < bestCost.getOrDefault(edge.toNodeId(), Double.MAX_VALUE)) {
                    previous.put(edge.toNodeId(), current);
                    bestCost.put(edge.toNodeId(), candidateCost);
                    double priority = candidateCost + graph.heuristicKm(edge.toNodeId(), goalNodeId);
                    openSet.add(new NodeScore(edge.toNodeId(), priority));
                }
            }
        }

        return List.of(graph.node(startNodeId), graph.node(goalNodeId));
    }

    private List<MapNode> reconstructPath(CityGraphMap graph, Map<String, String> previous, String current) {
        List<MapNode> path = new ArrayList<>();
        path.add(graph.node(current));
        while (previous.containsKey(current)) {
            current = previous.get(current);
            path.add(0, graph.node(current));
        }
        return path;
    }

    private record NodeScore(String nodeId, double priority) {
    }
}
