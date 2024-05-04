import java.io.*;
import java.util.*;

public class UCS {

    private static Set<String> loadDictionary(String filename) throws IOException {
        Set<String> dictionary = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            dictionary.add(line.trim().toUpperCase());
        }
        reader.close();
        return dictionary;
    }

    private static List<String> getNeighbors(String word, Set<String> dictionary) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'A'; c <= 'Z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    if (dictionary.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            chars[i] = originalChar;
        }
        return neighbors;
    }

    private static List<String> uniformCostSearch(String start, String end, Set<String> dictionary) {
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
                System.out.println("Nodes visited: " + nodesVisited);
                return reconstructPath(cameFrom, current.word);
            }

            for (String neighbor : getNeighbors(current.word, dictionary)) {
                int newCost = current.cost + 1;
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    frontier.add(new Node(neighbor, current.word, newCost));
                    costSoFar.put(neighbor, newCost);
                    cameFrom.put(neighbor, current.word);
                }
            }
        }
        System.out.println("Nodes visited: " + nodesVisited);
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

    static class Node {
        String word;
        String parent;
        int cost;

        Node(String word, String parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
        }
    }

    public static void main(String[] args) {
        try {
            Set<String> dictionary = loadDictionary("words_alpha.txt");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter start word: ");
            String start = scanner.nextLine().toUpperCase();
            System.out.print("Enter end word: ");
            String end = scanner.nextLine().toUpperCase();

            long startTime = System.currentTimeMillis();
            List<String> path = uniformCostSearch(start, end, dictionary);
            long endTime = System.currentTimeMillis();

            if (path.isEmpty()) {
                System.out.println("No path found.");
            } else {
                System.out.println("Path: " + String.join(" -> ", path));
                System.out.println("Execution time: " + (endTime - startTime) + " ms");
            }
        } catch (IOException e) {
            System.out.println("Failed to load dictionary: " + e.getMessage());
        }
    }
}
