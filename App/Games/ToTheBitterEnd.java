package App.Games;

import App.GUI.*;
import App.Algorithms.*;
import App.Main.Main;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class ToTheBitterEnd {

    private JFrame frame;
    private GraphVisualizer graphPanel;
    private ColoringAlgorithm coloringAlgorithm;
    private Instant startTime;
    private Instant endTime;
    private int hintPenaltySeconds = 0;
    private int vertices;
    private int edges;

    public static void game() {
        JFrame frame = new JFrame("infoText");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start Game");

        JTextPane infoDump = new JTextPane();
        infoDump.setEditable(false);
        infoDump.setText("Welcome to the Game! \n \n This is an explanation of the game.\n Enjoy!");

        StyledDocument doc = infoDump.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(infoDump);

        frame.add(startButton, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        startButton.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            ToTheBitterEnd ToTheBitterEndGame = new ToTheBitterEnd();
            ToTheBitterEndGame.initializeGame();
            frame.dispose();
        });
    }

    private void initializeGame() {
        frame = new JFrame("To The Bitter End");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton generateGraphButton = new JButton("Generate Graph");
        JButton backButton = new JButton("Back");
        JButton showleaderboard = new JButton("Show Leaderboard");

        buttonPanel.add(generateGraphButton);
        buttonPanel.add(backButton);
        buttonPanel.add(showleaderboard);

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));
        graphPanel.setRestrictClicksToHighlightedVertex(false);

        // Set the vertex click listener
        graphPanel.setVertexClickListener(new GraphVisualizer.VertexClickListener() {
            @Override
            public void vertexClicked(String vertex) {
                handleVertexClick(vertex);
            }
        });

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Action Listeners
        backButton.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            Main.mainMenu();
            frame.dispose();
        });

        generateGraphButton.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            generateGraph();
        });

        showleaderboard.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            showLeaderboard();
        });
    }

    private void generateGraph() {
        JFrame frame2 = new JFrame("Generate Graph");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setSize(new Dimension(300, 200));
        frame2.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JLabel verticesLabel = new JLabel("Number of Vertices:");
        JTextField verticesField = new JTextField();
        JLabel edgesLabel = new JLabel("Number of Edges:");
        JTextField edgesField = new JTextField();

        inputPanel.add(verticesLabel);
        inputPanel.add(verticesField);
        inputPanel.add(edgesLabel);
        inputPanel.add(edgesField);

        JButton confirmButton = new JButton("Confirm");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        frame2.add(inputPanel, BorderLayout.CENTER);
        frame2.add(buttonPanel, BorderLayout.SOUTH);
        frame2.setVisible(true);

        coloringAlgorithm = new ColoringAlgorithm();

        confirmButton.addActionListener(e -> {
            try {
                vertices = Integer.parseInt(verticesField.getText());
                edges = Integer.parseInt(edgesField.getText());

                if (vertices <= 0) {
                    JOptionPane.showMessageDialog(frame2, "Number of vertices must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (edges < vertices - 1) {
                    JOptionPane.showMessageDialog(frame2, "Number of edges must be at least " + (vertices - 1) + " to ensure connectivity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (edges < 0) {
                    JOptionPane.showMessageDialog(frame2, "Number of edges cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxEdges = vertices * (vertices - 1) / 2;
                if (edges > maxEdges) {
                    JOptionPane.showMessageDialog(frame2, "Too many edges for the number of vertices. Maximum possible edges: " + maxEdges, "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RandomGraphGenerator generator = new RandomGraphGenerator(vertices, edges);
                String[] Vertices = generator.getVertices();
                String[][] Edges = generator.getEdges();

                graphPanel.updateGraph(Vertices, Edges);

                // Initialize the coloring algorithm
                coloringAlgorithm = new ColoringAlgorithm();

                // Start the timer
                startTime = Instant.now();

                graphPanel.clearVertexColors();
                frame2.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleVertexClick(String vertex) {
        Color selectedColor = showColorSelectionDialog(vertex);

        if (selectedColor != null) {
            // Validate the selected color
            Set<Color> invalidColors = getAdjacentColors(vertex);
            if (invalidColors.contains(selectedColor)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "This color is unavailable. Check the colors of the neighbouring vertices.",
                        "Invalid Color",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                // Apply the selected color
                graphPanel.setVertexColor(vertex, selectedColor);

                // Check if all vertices are colored
                if (graphPanel.areAllVerticesColored()) {
                    endGame();
                }
            }
        }
    }

    private Set<Color> getAdjacentColors(String vertex) {
        Set<Color> colors = new HashSet<>();
        for (String[] edge : graphPanel.getEdges()) {
            if (edge[0].equals(vertex)) {
                Color color = graphPanel.getVertexColor(edge[1]);
                if (color != null) colors.add(color);
            } else if (edge[1].equals(vertex)) {
                Color color = graphPanel.getVertexColor(edge[0]);
                if (color != null) colors.add(color);
            }
        }
        return colors;
    }

    private Color showColorSelectionDialog(String vertex) {
        while (true) {
            // Get all colors and their names from coloringAlgorithm
            List<Color> allColors = coloringAlgorithm.getAllColors();
            List<String> colorNames = coloringAlgorithm.getColorNames();
    
            // Create a mapping from color names to Color objects
            Map<String, Color> colorOptions = new LinkedHashMap<>();
    
            // Add the "Get Computer Suggestion" option first
            colorOptions.put("💡 Get Computer Suggestion", null);
    
            // Add the "Generate New Color" option
            colorOptions.put("Generate New Color", null);
    
            // Add the rest of the color options
            for (int i = 0; i < allColors.size(); i++) {
                Color color = allColors.get(i);
                String colorName = colorNames.get(i);
                colorOptions.put(colorName, color);
            }
    
            // Show color selection dialog
            Object[] options = colorOptions.keySet().toArray();
            String selectedOption = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select a color for vertex " + vertex + ":",
                    "Color Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
    
            if (selectedOption == null) {
                // Player canceled the dialog
                return null;
            }
    
            if (selectedOption.equals("Generate New Color")) {
                // Generate a new color and add it to coloringAlgorithm
                Color newColor = coloringAlgorithm.addNewRandomColor();
                JOptionPane.showMessageDialog(
                        frame,
                        "New color generated and added to the selection list.",
                        "New Color Added",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // Loop again to refresh the selection list with the new color
            } else if (selectedOption.equals("💡 Get Computer Suggestion")) {
                // Get the suggested color
                Color suggestedColor = getComputerSuggestion(vertex);
                String colorName = coloringAlgorithm.getColorName(suggestedColor);
    
                // Ask the player if they want to use the suggested color
                int applySuggestion = JOptionPane.showConfirmDialog(
                        frame,
                        "Suggested color: " + colorName + "\nDo you want to use this color?",
                        "Computer Suggestion",
                        JOptionPane.YES_NO_OPTION
                );
    
                if (applySuggestion == JOptionPane.YES_OPTION) {
                    // Apply the suggested color and apply penalty
                    applyHintPenalty();
                    return suggestedColor;
                }
                // Otherwise, loop again to let the player choose
            } else {
                // Return the selected color
                return colorOptions.get(selectedOption);
            }
        }
    }

    private void endGame() {
        endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        int totalTimeSeconds = (int) duration.getSeconds() + hintPenaltySeconds;

        int chromaticNumber = coloringAlgorithm.getColorCount();

        String userName = JOptionPane.showInputDialog(frame, "Enter your username for the leaderboard:");
        if (userName != null && !userName.trim().isEmpty()) {
            int score = Scoring.scoreCalculator(chromaticNumber, totalTimeSeconds, vertices, edges);
            JOptionPane.showMessageDialog(frame, String.format(
                "Game Over!\nTime: %d seconds\nColors: %d\nTotal Time: %d seconds", 
                duration.getSeconds(), chromaticNumber, totalTimeSeconds), 
                "Game Over", JOptionPane.INFORMATION_MESSAGE
            );

            Scoring.storeInLeaderboard("ToTheBitterEnd", userName, score);
        }

        int option = JOptionPane.showOptionDialog(frame, "Play again?", "Play Again", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (option == JOptionPane.YES_OPTION) {
            generateGraph();
        } else {
            Main.mainMenu();
            frame.dispose();
        }
    }

    public static void showLeaderboard() {
        List<Scoring.LeaderboardEntry> leaderboard = Scoring.getLeaderBoard("ToTheBitterEnd");

        JFrame frame3 = new JFrame();
        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame3.setSize(new Dimension(800, 800));
        frame3.setLayout(new BorderLayout());

        JLabel label = new JLabel("Leaderboard");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        for (Scoring.LeaderboardEntry entry : leaderboard) {
            sb.append(entry.getUserName()).append(": ").append(entry.getScore()).append("\n");
        }

        textArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton close = new JButton("Close");
        close.addActionListener(e -> frame3.dispose());

        frame3.add(label, BorderLayout.NORTH);
        frame3.add(scrollPane, BorderLayout.CENTER);
        frame3.add(close, BorderLayout.SOUTH);

        frame3.setVisible(true);
    }

    private Color getComputerSuggestion(String vertex) {
        // Get colors of adjacent vertices
        Set<Color> invalidColors = getAdjacentColors(vertex);
    
        // Try to use existing colors to minimize the number of colors used
        List<Color> allColors = coloringAlgorithm.getAllColors();
    
        for (Color color : allColors) {
            if (!invalidColors.contains(color)) {
                // Found a valid color
                return color;
            }
        }
    
        // If no existing color is valid, generate a new color
        Color newColor = coloringAlgorithm.addNewRandomColor();
        return newColor;
    }
    
    private void applyHintPenalty() {
        // Apply a small penalty for using the hint
        hintPenaltySeconds += 10; 
    }
}
