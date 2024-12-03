package App.Games;

import App.GUI.*;
import App.Algorithms.*;
import App.Main.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

/*
Random Order is now playable;
TODO: Implement hint system (Have to figure that one yet)
DISCLAIMER: If I messed someone's code up, hmu and we'll figure something out. (I tried my best to not do so :/)
Change Log:

Main.java: Changed the button sound when pressing Random Order game mode;

RandomOrder.java: Implemented game logic;
The order of the vertices in which the player has to color is randomly generated and then fixed;
The current vertex that has to be colored is highlighted with a cyan thick border;
The player can drag all vertices regardless of whether it's highlighted or not (dragging is prioritized over clicking);
When choosing a color, the player can make the following choices:
1) If they are available, choose one of the six predetermined colors
2) If not, generate a new color;
In that sense, the game automatically checks the colors of the neighboring vertices and blocks the player from using them;
Upon coloring the last vertex, the player is prompted to write their username;
The player's username and score are displayed and then stored into the leaderboard file;
After that, the player is asked whether they want to play again;

GraphVisualizer.java: Added support for vertex highlighting;
Implemented vertex click listener interface (to enable communication from GraphVisualizer back to the RandomOrder game logic when a vertex is clicked);
Restricted player interaction to the highlighted vertex (to enforce the rule that the player must color vertices in a specific order);
Minor changes to vertex dragging functionality;
Some other minor changes;

RandomGraphGenerator.java: Ensured that the generated graph is connected (this is a requirement in the project manual);
Added an error prompt for an edge case;

ColoringAlgorithm.java: Fixed the algorithm to properly create and add new colors. (I remember this was working fine but somehow it got broken, idk)
*/

public class RandomOrder {

    private JFrame frame;
    private GraphVisualizer graphPanel;
    private ColoringAlgorithm coloringAlgorithm;
    private List<String> vertexOrder;
    private int currentVertexIndex;
    private Instant startTime;

    public static void game() {
        RandomOrder randomOrderGame = new RandomOrder();
        randomOrderGame.initializeGame();
    }

    private void initializeGame() {
        frame = new JFrame("Random Order Game");
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
                int numOfVertices = Integer.parseInt(verticesField.getText());
                int numOfEdges = Integer.parseInt(edgesField.getText());

                if (numOfVertices <= 0) {
                    JOptionPane.showMessageDialog(frame2, "Number of vertices must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (numOfEdges < numOfVertices - 1) {
                    JOptionPane.showMessageDialog(frame2, "Number of edges must be at least " + (numOfVertices - 1) + " to ensure connectivity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (numOfEdges < 0) {
                    JOptionPane.showMessageDialog(frame2, "Number of edges cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxEdges = numOfVertices * (numOfVertices - 1) / 2;
                if (numOfEdges > maxEdges) {
                    JOptionPane.showMessageDialog(frame2, "Too many edges for the number of vertices. Maximum possible edges: " + maxEdges, "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RandomGraphGenerator generator = new RandomGraphGenerator(numOfVertices, numOfEdges);
                String[] vertices = generator.getVertices();
                String[][] edges = generator.getEdges();

                graphPanel.updateGraph(vertices, edges);

                // Initialize the coloring algorithm
                coloringAlgorithm = new ColoringAlgorithm();

                // Randomly shuffle the vertex order
                vertexOrder = new ArrayList<>(Arrays.asList(vertices));
                Collections.shuffle(vertexOrder);
                currentVertexIndex = 0;

                // Start the timer
                startTime = Instant.now();

                // Highlight the first vertex
                highlightCurrentVertex();

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
        // Get colors of adjacent vertices
        Set<Color> invalidColors = getAdjacentColors(vertex);

        // Present color options, excluding invalid colors
        Color selectedColor = showColorSelectionDialog(invalidColors, vertex);

        if (selectedColor != null) {
            // Update the color
            graphPanel.setVertexColor(vertex, selectedColor);

            // Move to the next vertex
            currentVertexIndex++;
            if (currentVertexIndex < vertexOrder.size()) {
                highlightCurrentVertex();
            } else {
                endGame();
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

    private Color showColorSelectionDialog(Set<Color> invalidColors, String vertex) {
        // Define available colors
        Map<String, Color> colorOptions = new LinkedHashMap<>();
        colorOptions.put("Red", Color.RED);
        colorOptions.put("Green", Color.GREEN);
        colorOptions.put("Blue", Color.BLUE);
        colorOptions.put("Yellow", Color.YELLOW);
        colorOptions.put("Orange", Color.ORANGE);

        // Remove invalid colors
        colorOptions.values().removeAll(invalidColors);

        if (colorOptions.isEmpty()) {
            // Allow player to pick a new color
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "No available colors left. Would you like to use a new color?",
                    "No Colors Available",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                Color newColor = coloringAlgorithm.addNewRandomColor();
                return newColor;
            } else {
                // Player chose not to use a new color
                return null;
            }
        }

        // Show color selection dialog
        Object[] options = colorOptions.keySet().toArray();
        String selectedColorName = (String) JOptionPane.showInputDialog(
                frame,
                "Select a color for vertex " + vertex + ":",
                "Color Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedColorName != null) {
            return colorOptions.get(selectedColorName);
        }
        return null;
    }

    private void endGame() {
        // Stop the timer
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        int timeElapsed = (int) duration.getSeconds();

        // Calculate the chromatic number (number of unique colors used)
        Set<Color> usedColors = new HashSet<>();
        for (String vertex : vertexOrder) {
            Color color = graphPanel.getVertexColor(vertex);
            if (color != null) {
                usedColors.add(color);
            }
        }
        int chromaticNumber = usedColors.size();

        // Prompt for username
        String userName = JOptionPane.showInputDialog(frame, "Enter your username for the leaderboard:");

        if (userName != null && !userName.trim().isEmpty()) {
            // Calculate the score
            int score = Scoring.scoreCalculator("RandomOrder", userName, chromaticNumber, timeElapsed);

            // Congratulate the player
            JOptionPane.showMessageDialog(frame,
                    "Congratulations, " + userName + "!\nYour score: " + score,
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);

            // Store the score
            Scoring.storeInLeaderboard("RandomOrder", userName, score);
        }

        // Ask to play again or exit
        int option = JOptionPane.showOptionDialog(frame,
                "Would you like to play again?",
                "Play Again",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);

        if (option == JOptionPane.YES_OPTION) {
            // Restart the game
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
}
