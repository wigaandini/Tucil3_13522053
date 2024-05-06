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
    private JPanel outputPanel;
    private JScrollPane outputScrollPane;
    private Set<String> dictionary;
    private final Color softPink = new Color(194, 125, 150);
    private final Font verdanaFont = new Font("Verdana", Font.BOLD, 14);
    private final Color backgroundColor = new Color(255, 245, 249);

    public WordLadderGUI() {
        setTitle("Word Ladder Solver");
        setSize(700, 600);
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
        startLabel.setFont(verdanaFont);
        startLabel.setForeground(softPink);
        startWordField = new JTextField();
        startWordField.setFont(verdanaFont);

        JLabel endLabel = new JLabel("End Word:");
        endLabel.setFont(verdanaFont);
        endLabel.setForeground(softPink);
        endWordField = new JTextField();
        endWordField.setFont(verdanaFont);

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setFont(verdanaFont);
        algoLabel.setForeground(softPink);
        algorithmBox = new JComboBox<>(new String[]{"UCS", "Greedy BFS", "A*", "BFS"});
        algorithmBox.setFont(verdanaFont);

        inputPanel.add(startLabel);
        inputPanel.add(startWordField);
        inputPanel.add(endLabel);
        inputPanel.add(endWordField);
        inputPanel.add(algoLabel);
        inputPanel.add(algorithmBox);

        JButton searchButton = new JButton("Search!");
        searchButton.setFont(verdanaFont);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(softPink);
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findWordLadderPath();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        buttonPanel.add(searchButton);

        outputPanel = new JPanel(new BorderLayout());
        outputPanel.setOpaque(true);
        outputPanel.setBackground(backgroundColor);

        outputScrollPane = new JScrollPane(outputPanel);
        outputScrollPane.setOpaque(true);
        outputScrollPane.setBackground(backgroundColor);
        outputScrollPane.setPreferredSize(new Dimension(680, 300));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(outputScrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createWordGridPanel(List<String> path, String endWord) {
        int wordLength = path.get(0).length();
        JPanel gridPanel = new JPanel(new GridLayout(path.size(), wordLength, 5, 5));
        gridPanel.setOpaque(true);
        gridPanel.setBackground(backgroundColor);

        for (String word : path) {
            for (int i = 0; i < word.length(); i++) {
                JLabel label = new JLabel(String.valueOf(word.charAt(i)), SwingConstants.CENTER);
                label.setFont(new Font("Verdana", Font.BOLD, 18));
                label.setOpaque(true);

                if (i < endWord.length() && word.charAt(i) == endWord.charAt(i)) {
                    label.setBackground(new Color(194, 125, 150));
                    label.setForeground(Color.BLACK);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                }

                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                gridPanel.add(label);
            }
        }

        return gridPanel;
    }

    private void findWordLadderPath() {
        String startWord = startWordField.getText().trim().toUpperCase();
        String endWord = endWordField.getText().trim().toUpperCase();
        String algorithm = (String) algorithmBox.getSelectedItem();
    
        try {
            Main.validateWords(startWord, endWord, dictionary);
        } catch (WordLadderException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            case "BFS":
                result = BFS.breadthFirstSearch(startWord, endWord, dictionary);
                break;
        }
        long endTime = System.currentTimeMillis();
    
        outputPanel.removeAll();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS)); 
    
        JPanel wordGridPanel;
        if (result == null || result.getPath().isEmpty()) {
            wordGridPanel = new JPanel();
            wordGridPanel.setOpaque(true);
            wordGridPanel.setBackground(backgroundColor);
            wordGridPanel.add(new JLabel("No path found."));
        } else {
            wordGridPanel = createWordGridPanel(result.getPath(), endWord);
        }
    
        wordGridPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        outputPanel.add(wordGridPanel);
    
        StringBuilder stats = new StringBuilder();
        stats.append("<html><body>")
            .append("<p style='font-family:Verdana, sans-serif;'>Path length: ").append(result != null ? result.getPath().size() : 0).append("</p>")
            .append("<p style='font-family:Verdana, sans-serif;'>Total nodes visited: ").append(result != null ? result.getNodesVisited() : 0).append("</p>")
            .append("<p style='font-family:Verdana, sans-serif;'>Execution time: ").append(endTime - startTime).append(" ms</p>")
            .append("</body></html>");
    
        JEditorPane statsPane = new JEditorPane();
        statsPane.setContentType("text/html");
        statsPane.setEditable(false);
        statsPane.setText(stats.toString());
        statsPane.setBackground(backgroundColor);
        statsPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        statsPane.setAlignmentX(Component.CENTER_ALIGNMENT); 
        outputPanel.add(statsPane);
    
        int resultHeight = 80 + 30 * (result != null ? result.getPath().size() : 0);
        outputScrollPane.setPreferredSize(new Dimension(680, Math.min(resultHeight, 500)));
    
        outputPanel.revalidate();
        outputPanel.repaint();
        this.pack();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordLadderGUI gui = new WordLadderGUI();
            gui.setVisible(true);
        });
    }
}
