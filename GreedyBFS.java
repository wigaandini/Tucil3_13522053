import java.io.*;
import java.util.*;

public class GreedyBFS {
    private Set<String> dictionary;
    private String startWord;
    private String endWord;

    public GreedyBFS(String startWord, String endWord) {
        this.startWord = startWord;
        this.endWord = endWord;
        this.dictionary = new HashSet<>();
    }

    public void loadDictionary(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() == startWord.length()) {
                dictionary.add(line.toUpperCase());
            }
        }
        reader.close();
    }

    public List<String> findLadder() {
        long startTime = System.currentTimeMillis();
        if (!dictionary.contains(startWord) || !dictionary.contains(endWord)) {
            System.out.println("Invalid start or end word.");
            return null;
        }

        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristic));
        Map<String, Integer> visited = new HashMap<>();
        queue.add(new Node(startWord, null, 0, heuristic(startWord)));

        int nodesVisited = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesVisited++;
        
            if (current.word.equals(endWord)) {
                long endTime = System.currentTimeMillis();
                List<String> path = new ArrayList<>();
                while (current != null) {
                    path.add(current.word);
                    current = current.parent;
                }
                Collections.reverse(path);
                System.out.println("Path (steps): " + path);
                System.out.println("Number of steps in the path: " + path.size());
                System.out.println("Total nodes visited during the search: " + nodesVisited);
                System.out.println("Execution time: " + (endTime - startTime) + " ms");
                return path;
            }
        
            for (String neighbor : getNeighbors(current.word)) {
                if (!visited.containsKey(neighbor) || visited.get(neighbor) > current.depth + 1) {
                    visited.put(neighbor, current.depth + 1);
                    queue.add(new Node(neighbor, current, current.depth + 1, heuristic(neighbor)));
                }
            }
        }        

        return null;
    }

    private int heuristic(String word) {
        int differences = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != endWord.charAt(i)) {
                differences++;
            }
        }
        return differences;
    }

    private List<String> getNeighbors(String word) {
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter start word: ");
        String startWord = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter end word: ");
        String endWord = scanner.nextLine().trim().toUpperCase();

        GreedyBFS ladder = new GreedyBFS(startWord, endWord);
        try {
            ladder.loadDictionary("dictionary.txt");
            ladder.findLadder();
        } catch (IOException e) {
            System.err.println("Failed to load the dictionary: " + e.getMessage());
        }
    }
}