import java.util.*;

public class BFS {
    private static class Node {
        String word;
        Node parent;
        int depth;

        Node(String word, Node parent, int depth) {
            this.word = word;
            this.parent = parent;
            this.depth = depth;
        }
    }

    public static SearchResult breadthFirstSearch(String start, String end, Set<String> dictionary) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(new Node(start, null, 0));
        visited.add(start);
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
                return new SearchResult(path, nodesVisited);
            }

            for (String neighbor : Utils.getNeighbors(current.word, dictionary)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Node(neighbor, current, current.depth + 1));
                }
            }
        }
        return new SearchResult(Collections.emptyList(), nodesVisited);
    }
}
