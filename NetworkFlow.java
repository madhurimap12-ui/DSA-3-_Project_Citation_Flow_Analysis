import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NetworkFlow {

    private final CitationGraph citationGraph;

    public NetworkFlow(CitationGraph citationGraph) {
        this.citationGraph = citationGraph;
    }

    public int maximumFlow(String source, String sink) {
        Map<String, ArrayList<String>> graph = citationGraph.getGraph();
        if (!graph.containsKey(source) || !graph.containsKey(sink)) {
            throw new IllegalArgumentException("Source and sink must be paper IDs in the graph");
        }
        if (source.equals(sink)) {
            throw new IllegalArgumentException("Source and sink must be different papers");
        }

        Map<String, Map<String, Integer>> residualCapacity = new HashMap<>();
        Map<String, Set<String>> residualNeighbours = new HashMap<>();
        for (String paper : graph.keySet()) {
            residualCapacity.put(paper, new HashMap<>());
            residualNeighbours.put(paper, new HashSet<>());
        }

        for (Map.Entry<String, ArrayList<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                residualCapacity.get(from).merge(to, 1, Integer::sum);
                residualCapacity.get(to).putIfAbsent(from, 0);
                residualNeighbours.get(from).add(to);
                residualNeighbours.get(to).add(from);
            }
        }

        int maximumFlow = 0;
        Map<String, String> parent = new HashMap<>();
        while (findAugmentingPath(source, sink, residualCapacity, residualNeighbours, parent)) {
            int pathCapacity = Integer.MAX_VALUE;
            for (String paper = sink; !paper.equals(source); paper = parent.get(paper)) {
                String previous = parent.get(paper);
                pathCapacity = Math.min(pathCapacity,
                        residualCapacity.get(previous).get(paper));
            }

            for (String paper = sink; !paper.equals(source); paper = parent.get(paper)) {
                String previous = parent.get(paper);
                residualCapacity.get(previous).put(paper,
                        residualCapacity.get(previous).get(paper) - pathCapacity);
                residualCapacity.get(paper).merge(previous, pathCapacity, Integer::sum);
            }
            maximumFlow += pathCapacity;
        }

        return maximumFlow;
    }

    private boolean findAugmentingPath(
            String source,
            String sink,
            Map<String, Map<String, Integer>> residualCapacity,
            Map<String, Set<String>> residualNeighbours,
            Map<String, String> parent) {
        parent.clear();
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String current = queue.remove();
            for (String neighbour : residualNeighbours.get(current)) {
                if (residualCapacity.get(current).getOrDefault(neighbour, 0) > 0
                        && visited.add(neighbour)) {
                    parent.put(neighbour, current);
                    if (neighbour.equals(sink)) {
                        return true;
                    }
                    queue.add(neighbour);
                }
            }
        }
        return false;
    }
}