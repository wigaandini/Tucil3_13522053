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
            boolean continuePlaying = true;

            while (continuePlaying) {
                String startWord, endWord;

                while (true) {
                    System.out.print("Enter start word: ");
                    startWord = scanner.nextLine().trim().toUpperCase();

                    System.out.print("Enter end word: ");
                    endWord = scanner.nextLine().trim().toUpperCase();

                    try {
                        validateWords(startWord, endWord, dictionary);
                        break;
                    } catch (WordLadderException e) {
                        System.out.println(e.getMessage());
                    }
                }

                SearchResult result = null;
                long startTime = 0, endTime = 0;
                long memoryBefore = 0, memoryAfter = 0;
                boolean validAlgorithm = false;

                while (!validAlgorithm) {
                    System.out.println("\nChoose an algorithm: ");
                    System.out.println("1. Uniform Cost Search (UCS)");
                    System.out.println("2. Greedy Best First Search (GBFS)");
                    System.out.println("3. A* Search");
                    System.out.println("4. Breadth First Search (BFS)\n");

                    System.out.print("Enter the chosen algorithm (UCS, GBFS, A*, BFS): ");
                    String algorithm = scanner.nextLine().trim().toUpperCase();

                    // Measure memory before execution
                    Runtime runtime = Runtime.getRuntime();
                    runtime.gc();  // Request garbage collection for accurate reading
                    memoryBefore = runtime.totalMemory() - runtime.freeMemory();

                    // Measure start time
                    startTime = System.currentTimeMillis();

                    switch (algorithm) {
                        case "UCS":
                            result = UCS.uniformCostSearch(startWord, endWord, dictionary);
                            validAlgorithm = true;
                            break;
                        case "GBFS":
                            result = GreedyBFS.greedyBestFirstSearch(startWord, endWord, dictionary);
                            validAlgorithm = true;
                            break;
                        case "A*":
                            result = AStar.aStarSearch(startWord, endWord, dictionary);
                            validAlgorithm = true;
                            break;
                        case "BFS":
                            result = BFS.breadthFirstSearch(startWord, endWord, dictionary);
                            validAlgorithm = true;
                            break;
                        default:
                            System.out.println("Invalid algorithm selected. Please try again.");
                            break;
                    }

                    // Measure end time
                    endTime = System.currentTimeMillis();
                    
                    // Measure memory after execution
                    memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                }

                if (result == null || result.getPath().isEmpty()) {
                    System.out.println("\nNo path found.");
                } else {
                    System.out.println("Path length: " + result.getPath().size());
                    System.out.println("Path:");
                    List<String> path = result.getPath();
                    for (int i = 0; i < path.size(); i++) {
                        System.out.print(path.get(i));
                        if (i < path.size() - 1) {
                            System.out.print(" -> ");
                        }
                    }
                    System.out.println("\n\nTotal nodes visited: " + result.getNodesVisited());
                    System.out.println("Execution time: " + (endTime - startTime) + " ms");
                    System.out.println("Memory used: " + (memoryAfter - memoryBefore) + " bytes");
                }

                System.out.print("\nDo you want to continue? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                System.out.println();
                continuePlaying = choice.equals("y");
                if (choice != "y" && choice != "n") {
                    System.out.print("Invalid input. ");
                    continuePlaying = false;
                }
                if (!continuePlaying) {
                    System.out.println("Exiting the Word Ladder. Goodbye!");
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load dictionary: " + e.getMessage());
        }
    }

    public static void validateWords(String startWord, String endWord, Set<String> dictionary) throws WordLadderException {
        if (startWord.length() == 0 || endWord.length() == 0) {
            throw new WordLadderException("Start word and end word must not be empty!\n");
        }
        
        if (!dictionary.contains(startWord) && !dictionary.contains(endWord)) {
            throw new WordLadderException("Start word and end word must be words in English!\n");
        }
        if (!dictionary.contains(startWord)) {
            throw new WordLadderException("Start word must be words in English!\n");
        }
        if (!dictionary.contains(endWord)) {
            throw new WordLadderException("End word must be words in English!\n");
        }
        if (startWord.length() != endWord.length()) {
            throw new WordLadderException("Start word and end word must be of equal length.\n");
        }
    }
}
