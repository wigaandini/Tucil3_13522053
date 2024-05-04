import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AS {
    private static Set<String> dictionary = new HashSet<>();
    private static String startWord, endWord;

    public static void main(String[] args) throws IOException {
        loadDictionary("dictionary.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter start word: ");
        String startWord = scanner.nextLine().toUpperCase();
        System.out.print("Enter end word: ");
        String endWord = scanner.nextLine().toUpperCase();
        
        if (!dictionary.contains(startWord) || !dictionary.contains(endWord)) {
            System.out.println("Start or end word not found in the dictionary.");
            return;
        }

        long startTime = System.nanoTime();
        AStarResult result = findWordLadderAStar(startWord, endWord);
        long endTime = System.nanoTime();

        if (result != null) {
            System.out.println("Path: " + String.join(" -> ", result.path));
            System.out.println("Nodes visited: " + result.nodesVisited);
        } else {
            System.out.println("No path found.");
        }
        System.out.println("Execution time: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void loadDictionary(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line.trim().toUpperCase());
            }
        }
    }

    private static class AStarNode {
        String word;
        int g;
        int h;
        AStarNode parent;

        AStarNode(String word, int g, int h, AStarNode parent) {
            this.word = word;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        int getF() {
            return g + h;
        }
    }

    private static class AStarResult {
        List<String> path;
        int nodesVisited;

        AStarResult(List<String> path, int nodesVisited) {
            this.path = path;
            this.nodesVisited = nodesVisited;
        }
    }

    private static int calculateHeuristic(String word1, String word2) {
        int diff = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    private static List<String> getNeighbors(String word) {
        List<String> neighbors = new ArrayList<>();
        char[] wordArray = word.toCharArray();

        for (int i = 0; i < wordArray.length; i++) {
            char originalChar = wordArray[i];
            for (char c = 'A'; c <= 'Z'; c++) {
                if (c != originalChar) {
                    wordArray[i] = c;
                    String newWord = new String(wordArray);
                    if (dictionary.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            wordArray[i] = originalChar;
        }
        return neighbors;
    }

    private static AStarResult findWordLadderAStar(String start, String end) {
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(AStarNode::getF));
        Map<String, Integer> gScoreMap = new HashMap<>();
        Set<String> closedSet = new HashSet<>();
        int nodesVisited = 0;

        AStarNode startNode = new AStarNode(start, 0, calculateHeuristic(start, end), null);
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
                return new AStarResult(path, nodesVisited);
            }

            closedSet.add(current.word);
            for (String neighbor : getNeighbors(current.word)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeG = current.g + 1;
                if (tentativeG < gScoreMap.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    AStarNode neighborNode = new AStarNode(neighbor, tentativeG, calculateHeuristic(neighbor, end), current);
                    gScoreMap.put(neighbor, tentativeG);
                    openSet.add(neighborNode);
                }
            }
        }
        return null;
    }
}
