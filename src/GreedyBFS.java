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

    public static List<String> greedyBestFirstSearch(String start, String end, Set<String> dictionary) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristic));
        Map<String, Integer> visited = new HashMap<>();
        queue.add(new Node(start, null, 0, Utils.calculateHeuristic(start, end)));
        int nodesVisited = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                List<String> path = new ArrayList<>();
                while (current != null) {
                    path.add(current.word);
                    current = current.parent;
                }
                Collections.reverse(path);
                System.out.println("\nTotal nodes visited: " + nodesVisited);
                return path;
            }

            for (String neighbor : Utils.getNeighbors(current.word, dictionary)) {
                if (!visited.containsKey(neighbor) || visited.get(neighbor) > current.depth + 1) {
                    visited.put(neighbor, current.depth + 1);
                    queue.add(new Node(neighbor, current, current.depth + 1, Utils.calculateHeuristic(neighbor, end)));
                }
            }
        }
        return Collections.emptyList();
    }
}
