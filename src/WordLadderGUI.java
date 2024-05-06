import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class WordLadderGUI extends JFrame {
    private JTextField startWordField;
    private JTextField endWordField;
    private JComboBox<String> algorithmBox;
    private JTextArea outputArea;
    private Set<String> dictionary;
    private final Color softPink = new Color(194, 125, 150);
    private final Font boldFont = new Font("Verdana", Font.BOLD, 14);
    private final Color backgroundColor = new Color(255, 245, 249);

    public WordLadderGUI() {
        setTitle("Word Ladder Solver");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            dictionary = DictionaryLoader.loadDictionary("dictionary.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to load dictionary!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setOpaque(true);
        inputPanel.setBackground(backgroundColor);

        JLabel startLabel = new JLabel("Start Word:");
        startLabel.setFont(boldFont);
        startLabel.setForeground(softPink);
        startWordField = new JTextField();
        startWordField.setFont(boldFont);

        JLabel endLabel = new JLabel("End Word:");
        endLabel.setFont(boldFont);
        endLabel.setForeground(softPink);
        endWordField = new JTextField();
        endWordField.setFont(boldFont);

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setFont(boldFont);
        algoLabel.setForeground(softPink);
        algorithmBox = new JComboBox<>(new String[]{"UCS", "Greedy BFS", "A*"});
        algorithmBox.setFont(boldFont);

        inputPanel.add(startLabel);
        inputPanel.add(startWordField);
        inputPanel.add(endLabel);
        inputPanel.add(endWordField);
        inputPanel.add(algoLabel);
        inputPanel.add(algorithmBox);

        JButton searchButton = new JButton("Search!");
        searchButton.setFont(boldFont);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(softPink);
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setMaximumSize(new Dimension(100, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findWordLadderPath();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); 
        buttonPanel.add(searchButton);

        outputArea = new JTextArea(10, 40);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputArea.setEditable(false);
        outputArea.setFont(boldFont);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setForeground(softPink);
        outputArea.setBackground(backgroundColor);
        outputArea.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(backgroundColor);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void findWordLadderPath() {
        String startWord = startWordField.getText().trim().toUpperCase();
        String endWord = endWordField.getText().trim().toUpperCase();
        String algorithm = (String) algorithmBox.getSelectedItem();

        try {
            Main.validateWords(startWord, endWord, dictionary);
        } catch (WordLadderException e) {
            outputArea.setText(e.getMessage());
            return;
        }

        SearchResult result = null;
        long startTime = System.currentTimeMillis();
        switch (algorithm) {
            case "UCS":
                result = UCS.uniformCostSearch(startWord, endWord, dictionary);
                break;
            case "Greedy BFS":
                result = GreedyBFS.greedyBestFirstSearch(startWord, endWord, dictionary);
                break;
            case "A*":
                result = AStar.aStarSearch(startWord, endWord, dictionary);
                break;
        }
        long endTime = System.currentTimeMillis();

        if (result == null || result.getPath().isEmpty()) {
            outputArea.setText("\nNo path found.");
        } else {
            StringBuilder output = new StringBuilder();
            output.append("Path length: ").append(result.getPath().size()).append("\nPath: \n");
            List<String> path = result.getPath();
            for (int i = 0; i < path.size(); i++) {
                output.append(path.get(i));
                if (i < path.size() - 1) {
                    output.append(" -> ");
                }
            }
            output.append("\n\nTotal nodes visited: ").append(result.getNodesVisited());
            output.append("\nExecution time: ").append(endTime - startTime).append(" ms");
            outputArea.setText(output.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordLadderGUI gui = new WordLadderGUI();
            gui.setVisible(true);
        });
    }
}
