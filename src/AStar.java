import java.util.*;

public class AStar {
    public static SearchResult aStarSearch(String start, String end, Set<String> dictionary) {
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(AStarNode::getF));
        Map<String, Integer> gScoreMap = new HashMap<>();
        Set<String> closedSet = new HashSet<>();
        int nodesVisited = 0;

        AStarNode startNode = new AStarNode(start, 0, Utils.calculateHeuristic(start, end), null);
        openSet.add(startNode);
        gScoreMap.put(start, 0);

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                List<String> path = new ArrayList<>();
                for (AStarNode node = current; node != null; node = node.parent) {
                    path.add(node.word);
                }
                Collections.reverse(path);
                return new SearchResult(path, nodesVisited);
            }

            closedSet.add(current.word);
            for (String neighbor : Utils.getNeighbors(current.word, dictionary)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeG = current.g + 1;
                if (tentativeG < gScoreMap.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    AStarNode neighborNode = new AStarNode(neighbor, tentativeG, Utils.calculateHeuristic(neighbor, end), current);
                    gScoreMap.put(neighbor, tentativeG);
                    openSet.add(neighborNode);
                }
            }
        }
        return new SearchResult(Collections.emptyList(), nodesVisited);
    }
}
