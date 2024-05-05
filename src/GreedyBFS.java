import java.util.*;

public class GreedyBFS {
    private static class Node {
        String word;
        Node parent;
        int depth;
        int heuristic;

        Node(String word, Node parent, int depth, int heuristic) {
            this.word = word;
            this.parent = parent;
            this.depth = depth;
            this.heuristic = heuristic;
        }
    }

    public static SearchResult greedyBestFirstSearch(String start, String end, Set<String> dictionary) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristic));
        Map<String, Integer> visited = new HashMap<>();
        Node startNode = new Node(start, null, 0, Utils.calculateHeuristic(start, end));
        queue.add(startNode);
        int nodesVisited = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                return new SearchResult(reconstructPath(current), nodesVisited);
            }

            for (String neighbor : Utils.getNeighbors(current.word, dictionary)) {
                if (!visited.containsKey(neighbor) || visited.get(neighbor) > current.depth + 1) {
                    visited.put(neighbor, current.depth + 1);
                    queue.add(new Node(neighbor, current, current.depth + 1, Utils.calculateHeuristic(neighbor, end)));
                }
            }
        }
        return new SearchResult(Collections.emptyList(), nodesVisited);
    }

    private static List<String> reconstructPath(Node node) {
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node.word);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
