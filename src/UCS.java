import java.util.*;

public class UCS {
    private static class Node {
        String word;
        String parent;
        int cost;

        Node(String word, String parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
        }
    }

    public static List<String> uniformCostSearch(String start, String end, Set<String> dictionary) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Map<String, Integer> costSoFar = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        int nodesVisited = 0;

        frontier.add(new Node(start, null, 0));
        costSoFar.put(start, 0);
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                System.out.println("\nTotal nodes visited: " + nodesVisited);
                return reconstructPath(cameFrom, current.word);
            }

            for (String neighbor : Utils.getNeighbors(current.word, dictionary)) {
                int newCost = current.cost + 1;
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    frontier.add(new Node(neighbor, current.word, newCost));
                    costSoFar.put(neighbor, newCost);
                    cameFrom.put(neighbor, current.word);
                }
            }
        }
        return Collections.emptyList();
    }

    private static List<String> reconstructPath(Map<String, String> cameFrom, String current) {
        List<String> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}
