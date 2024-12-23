package App.Games;

import App.Algorithms.*;
import App.GUI.*;
import App.Main.Main;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class IChangedMyMind {

    private JFrame frame;
    private GraphVisualizer graphPanel;
    private ColoringAlgorithm coloringAlgorithm;
    private List<String> vertexOrder;
    private int currentVertexIndex;
    private Instant startTime;
    private Instant endTime;
    private int hintPenaltySeconds = 0;
    private static Stack<UndoStep> UndoStack = new Stack<>();
    private int vertices;
    private int edges;
    private String[] Vertices;

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
            IChangedMyMind IChangedMyMind = new IChangedMyMind();
            IChangedMyMind.initializeGame();
            frame.dispose();
        });
    }

    private void initializeGame() {
        frame = new JFrame("I Changed My Mind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton generateGraphButton = new JButton("Generate Graph");
        JButton backButton = new JButton("Back");
        JButton showleaderboard = new JButton("Show Leaderboard");
        JButton undoButton= new JButton("Undo");
        JButton giveUpButton = new JButton("Give Up");

        buttonPanel.add(generateGraphButton);
        buttonPanel.add(backButton);
        buttonPanel.add(showleaderboard);
        buttonPanel.add(undoButton);
        buttonPanel.add(giveUpButton);

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));
        graphPanel.setRestrictClicksToHighlightedVertex(true);
        
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

        undoButton.addActionListener(e -> {
            if (!UndoStack.isEmpty()) {
                UndoStep lastStep = UndoStack.pop();
                
                // Restore the previous color of the vertex
                graphPanel.recolorVertex(vertexOrder.get(lastStep.vertexIndex), lastStep.previousColor);
                
                // Move back to the previous vertex
                currentVertexIndex--;
        
                // Highlight the vertex to ensure proper interaction
                if (currentVertexIndex >= 0) {
                    highlightCurrentVertex();
                }
        
                SoundFX.playSound("undo.wav");
            } else {
                JOptionPane.showMessageDialog(frame, "No more moves to undo!", "Undo Error", JOptionPane.WARNING_MESSAGE);
            }
        });


        showleaderboard.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            showLeaderboard();
        });

        giveUpButton.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            giveUp();
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
                int vertices = Integer.parseInt(verticesField.getText());
                int edges = Integer.parseInt(edgesField.getText());

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
                Vertices = generator.getVertices();
                String[][] Edges = generator.getEdges();

                graphPanel.updateGraph(Vertices, Edges);

                coloringAlgorithm = new ColoringAlgorithm();

                vertexOrder = new ArrayList<>(Arrays.asList(Vertices));
                Collections.shuffle(vertexOrder);
                currentVertexIndex = 0;

                startTime = Instant.now();

                highlightCurrentVertex();
                graphPanel.clearVertexColors();

                frame2.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void highlightCurrentVertex() {
        if (currentVertexIndex < vertexOrder.size()) {
            String currentVertex = vertexOrder.get(currentVertexIndex);
            graphPanel.highlightVertex(currentVertex);
        }
    }

    private void handleVertexClick(String vertex) {
        while (true) {
            // Show all color options
            Color selectedColor = showColorSelectionDialog(vertex);
    
            if (selectedColor == null) {
                // Player canceled the dialog
                break;
            }
    
            // Validate the selected color
            Set<Color> invalidColors = getAdjacentColors(vertex);
            if (invalidColors.contains(selectedColor)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "This color is unavailable. Check the colors of the neighboring vertices.",
                        "Invalid Color",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                // Push the current state to UndoStack before changing the color
                Color previousColor = graphPanel.getVertexColor(vertex);
                UndoStack.push(new UndoStep(currentVertexIndex, previousColor, selectedColor));
    
                graphPanel.setVertexColor(vertex, selectedColor);
    
                // Progress to the next vertex
                currentVertexIndex++;
                if (currentVertexIndex < vertexOrder.size()) {
                    highlightCurrentVertex();
                } else {
                    endGame();
                }
                break; // Exit the loop
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
            List<Color> allColors = coloringAlgorithm.getAllColors();
            List<String> colorNames = coloringAlgorithm.getColorNames();
            Map<String, Color> colorOptions = new LinkedHashMap<>();
    
            colorOptions.put("💡 Get Computer Suggestion", null);
    
            colorOptions.put("Generate New Color", null);
    
            for (int i = 0; i < allColors.size(); i++) {
                Color color = allColors.get(i);
                String colorName = colorNames.get(i);
                colorOptions.put(colorName, color);
            }
    
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
                return null;
            }
    
            if (selectedOption.equals("Generate New Color")) {
                Color newColor = coloringAlgorithm.addNewRandomColor();
                JOptionPane.showMessageDialog(
                        frame,
                        "New color generated and added to the selection list.",
                        "New Color Added",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (selectedOption.equals("💡 Get Computer Suggestion")) {
                Color suggestedColor = getComputerSuggestion(vertex);
                String colorName = coloringAlgorithm.getColorName(suggestedColor);
    
                int applySuggestion = JOptionPane.showConfirmDialog(
                        frame,
                        "Suggested color: " + colorName + "\nDo you want to use this color?",
                        "Computer Suggestion",
                        JOptionPane.YES_NO_OPTION
                );
    
                if (applySuggestion == JOptionPane.YES_OPTION) {
                    applyHintPenalty();
                    return suggestedColor;
                }
            } else {
                return colorOptions.get(selectedOption);
            }
        }
    }
    

    private Color getComputerSuggestion(String vertex) {
        Set<Color> invalidColors = getAdjacentColors(vertex);
    
        List<Color> allColors = coloringAlgorithm.getAllColors();
    
        for (Color color : allColors) {
            if (!invalidColors.contains(color)) {
                return color;
            }
        }
    
        Color newColor = coloringAlgorithm.addNewRandomColor();
        return newColor;
    }
    
    private void applyHintPenalty() {
        hintPenaltySeconds += 10;
    }
    

    private void endGame() {
        endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        int totalTimeSeconds = (int)duration.getSeconds() + hintPenaltySeconds;

        int chromaticNumber = coloringAlgorithm.getColorCount();

        String userName = JOptionPane.showInputDialog(frame, "Enter your username for the leaderboard:");

        if (userName != null && !userName.trim().isEmpty()) {
            int score = Scoring.scoreCalculator(chromaticNumber, totalTimeSeconds, vertices, edges);

            String message = String.format("Congratulations! You've colored all vertices.\n" +
            "Time taken: %d seconds\n" +
            "Colors used: %d\n" +
            "Hint penalty: %d seconds\n" +
            "Total time (with penalty): %d seconds",
            duration.getSeconds(),
            chromaticNumber,
            hintPenaltySeconds,
            totalTimeSeconds);
            JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

            Scoring.storeInLeaderboard("RandomOrder", userName, score);
        }

        int option = JOptionPane.showOptionDialog(frame,
                "Would you like to play again?",
                "Play Again",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);

        if (option == JOptionPane.YES_OPTION) {
            generateGraph();
        } else {
            Main.mainMenu();
            frame.dispose();
        }
    }

    public static void showLeaderboard() {
        List<Scoring.LeaderboardEntry> leaderboard = Scoring.getLeaderBoard("RandomOrder");

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

    private static class UndoStep {
        int vertexIndex;
        Color previousColor;
        Color newColor;
    
        UndoStep(int vertexIndex, Color previousColor, Color newColor) {
            this.vertexIndex = vertexIndex;
            this.previousColor = previousColor;
            this.newColor = newColor;
        }
    }

    private void giveUp() {
        for(int i = 0; i < Vertices.length; i++) {
            Color suggestedColor = getComputerSuggestion(Vertices[i]);
            graphPanel.setVertexColor(Vertices[i], suggestedColor);
        }

        JFrame frame2 = new JFrame("Give Up?");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setSize(new Dimension(300, 200));
        frame2.setLayout(new BorderLayout());

        JTextPane text = new JTextPane();
        text.setText("Do you want to play again?");
        text.setEditable(false);

        StyledDocument doc = text.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yes);
        buttonPanel.add(no);

        frame2.add(buttonPanel, BorderLayout.SOUTH);
        frame2.add(text, BorderLayout.CENTER);

        frame2.setVisible(true);

        yes.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            frame2.dispose();
            generateGraph();
        });

        no.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            Main.mainMenu();
            frame2.dispose();
            frame.dispose();
        });
    }
}
