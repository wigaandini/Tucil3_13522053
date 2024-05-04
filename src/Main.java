import java.util.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("                                                               ");
        System.out.println("                                                               ");
        System.out.println("░▒█░░▒█░▒█▀▀▀█░▒█▀▀▄░▒█▀▀▄░░░▒█░░░░█▀▀▄░▒█▀▀▄░▒█▀▀▄░▒█▀▀▀░▒█▀▀▄");
        System.out.println("░▒█▒█▒█░▒█░░▒█░▒█▄▄▀░▒█░▒█░░░▒█░░░▒█▄▄█░▒█░▒█░▒█░▒█░▒█▀▀▀░▒█▄▄▀");
        System.out.println("░▒▀▄▀▄▀░▒█▄▄▄█░▒█░▒█░▒█▄▄█░░░▒█▄▄█▒█░▒█░▒█▄▄█░▒█▄▄█░▒█▄▄▄░▒█░▒█");
        System.out.println("                                                               ");
        System.out.println("              Welcome to Word Ladder Solver!                   ");
        System.out.println("                       13522053                                ");
        System.out.println("                                                               ");
        System.out.println("                                                               ");

        try {
            Set<String> dictionary = DictionaryLoader.loadDictionary("dictionary.txt");
            Scanner scanner = new Scanner(System.in);
            String startWord, endWord;

            while (true) {
                System.out.print("Enter start word: ");
                startWord = scanner.nextLine().toUpperCase();
                System.out.print("Enter end word: ");
                endWord = scanner.nextLine().toUpperCase();

                try {
                    validateWords(startWord, endWord, dictionary);
                    break;
                } catch (WordLadderException e) {
                    System.out.println(e.getMessage());
                }
            }

            List<String> path = Collections.emptyList();
            long startTime = 0, endTime = 0;
            boolean validAlgorithm = false;

            while (!validAlgorithm) {
                System.out.println("\nChoose an algorithm: ");
                System.out.println("1. Uniform Cost Search (UCS)");
                System.out.println("2. Greedy Best First Search (GBFS)");
                System.out.println("3. A* Search\n");

                System.out.print("Enter the chosen algorithm (UCS, GBFS, A*): ");
                String algorithm = scanner.nextLine().trim().toUpperCase();

                startTime = System.currentTimeMillis();
                switch (algorithm) {
                    case "UCS":
                        path = UCS.uniformCostSearch(startWord, endWord, dictionary);
                        validAlgorithm = true;
                        break;
                    case "GBFS":
                        path = GreedyBFS.greedyBestFirstSearch(startWord, endWord, dictionary);
                        validAlgorithm = true;
                        break;
                    case "A*":
                        path = AStar.aStarSearch(startWord, endWord, dictionary);
                        validAlgorithm = true;
                        break;
                    default:
                        System.out.println("Invalid algorithm selected. Please try again.");
                        break;
                }
                endTime = System.currentTimeMillis();
            }

            if (path.isEmpty()) {
                System.out.println("\nNo path found.");
            } else {
                System.out.println("Path:");
                for (int i = 0; i < path.size(); i++) {
                    System.out.print(path.get(i));
                    if (i < path.size() - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println("\n\nExecution time: " + (endTime - startTime) + " ms");
            }

        } catch (IOException e) {
            System.out.println("Failed to load dictionary: " + e.getMessage());
        }
    }

    private static void validateWords(String startWord, String endWord, Set<String> dictionary) throws WordLadderException {
        if (!dictionary.contains(startWord) && !dictionary.contains(endWord)) {
            throw new WordLadderException("Start word and end word not found in dictionary.\n");
        }
        if (!dictionary.contains(startWord)) {
            throw new WordLadderException("Start word not found in dictionary.\n");
        }
        if (!dictionary.contains(endWord)) {
            throw new WordLadderException("End word not found in dictionary.\n");
        }
        if (startWord.length() != endWord.length()) {
            throw new WordLadderException("Start word and end word must be of equal length.\n");
        }
    }
}
