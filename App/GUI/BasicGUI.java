package App.GUI;

import App.Algorithms.ColoringAlgorithm;
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.RandomGraphGenerator;
import App.Algorithms.SpecialCases;  // Added import
import App.Algorithms.UpperBoundAlgorithm;
import App.Main.Main;
import App.Txt.TxtInterpretation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import App.Algorithms.ColoringAlgorithm;

public class BasicGUI {

    // Instance variables for graph data
    private String[] vertices;
    private String[][] edges;
    private int upperBound;
    private int lowerBound;
    // declaring result area as an instance variable to make it accessible to other methods
    private JTextArea resultArea;
    private JFrame frame;
    private GraphVisualizer graphPanel;
    
    // Added for dragging
    private boolean isDraggingVertex = false;
    private String draggedVertex = null;
    private Point lastMousePosition = null;
    private double zoomFactor = 1.0;
    private int panX = 0;
    private int panY = 0;
    private static final int DRAG_THRESHOLD = 5; // Adjust this value as needed
    private Map<String, Point> vertexPositions = new HashMap<>();
    ColoringAlgorithm coloringAlgorithm = new ColoringAlgorithm();
    
    public void infoScreen() {
        JFrame frame = new JFrame("infoText");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start");

        JTextPane infoDump = new JTextPane();
        infoDump.setEditable(false);
        infoDump.setText("Welcome to the SandBox! \n \n Here you can create graphs and colour or drag them around as you wish, without pesky timers or orders to bother you. At the top of the screen there will be a couple buttons; there is the load graph button to load a graph from a special type of file, the generate graph button the generate a random graph with a by you specified numbers of vertices and edges and lastly the exact, upper bound and lower bound algorithm buttons which show you the exact chromatic number, the upper bound or the lower bound for the currently loaded graph. Finally there are the result area and the graph box which show the afore mentioned numbers and graphs.");

        StyledDocument doc = infoDump.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(10, doc.getLength(), center, false);

        int height = infoDump.getHeight();
        int width = frame.getWidth();
        infoDump.setSize(width,height);

        frame.add(startButton, BorderLayout.SOUTH);
        frame.add(infoDump, BorderLayout.CENTER);

        BasicGUI BasicGUI = new BasicGUI();
        BasicGUI.createShowGUI();

        frame.setVisible(true);

        startButton.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            frame.dispose();
        });
    }


    // Method to create and display the GUI
    public void createShowGUI() {
        // create a new window
        frame = new JFrame("Basic GUI");

        // application exits when we close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // size of the window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

         // panel to hold components

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        // Added button for generating random graph 
        JButton loadGraphButton = new JButton("Load Graph");
        JButton exactButton = new JButton("Run Exact Algorithm");
        JButton upperBoundButton = new JButton("Run Upper Bound Algorithm");
        JButton lowerBoundButton = new JButton("Run Lower Bound Algorithm");
        JButton backButton = new JButton("Back");
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");
        JButton toggleColorButton = new JButton("Toggle Graph Coloring");
        JButton randomGraphButton = new JButton("Generate Random Graph"); 


        // new text area to display the results
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);


        // add buttons to panel
        panel1.add(loadGraphButton);
        panel1.add(exactButton);
        panel1.add(upperBoundButton);
        panel1.add(lowerBoundButton);
        panel1.add(scrollPane);
        panel2.add(zoomInButton);
        panel2.add(backButton);
        panel2.add(zoomOutButton);
        panel1.add(toggleColorButton);
        panel1.add(randomGraphButton);  // Added button to panel

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));

        // add the panel to the window
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);
        
        // make the window visible
        frame.setVisible(true);

        /**
         * I noticed that GraphVisualizer provides the method setVertexClickListener
         * but there is no code in BasicGUI that sets a listener 
         * for vertex clicks
         * 
         * I think this is why coloring doesn't work in Sandbox mode.
           */
        graphPanel.setVertexClickListener(new GraphVisualizer.VertexClickListener() {
            @Override
            public void vertexClicked(String vertex) {
                handleVertexClick(vertex);
            }
        });
        

        // Action listener for load graph button
        loadGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
                SoundFX.playSound("button.wav");
                                //todo: create a load graph method and implement action listener later

                JFileChooser fileChooser = new JFileChooser();

                File defaultDirectory = new File("project-1.1/App/Txt/Graphs");
                fileChooser.setCurrentDirectory(defaultDirectory);

                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    loadGraphFromFile(selectedFile);
                } else {
                    resultArea.append("file selection cancelled.");
                }
            }
        });

        toggleColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");

                // Check if the graph is already colored
                if (!graphPanel.areAllVerticesColored()) {
                    // Color the graph using the autoColorGraphWithAlgorithm (similar to giveUp method)
                    ColoringAlgorithm coloringAlgorithm = new ColoringAlgorithm();
                    graphPanel.autoColorGraphWithAlgorithm(coloringAlgorithm);
                } else {
                    // Clear the graph's colors
                    graphPanel.clearVertexColors();
                }
            }
        });

        // Action listener for exact algorithm button
        exactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                runExactAlgorithm();
                System.out.println("exact Algorithm 2 button clicked.");
            }
        });

        // Action listener for upper bound algorithm button
        upperBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
               
                runUpperBoundAlgorithm();
                System.out.println("upper bound algorithm button clicked.");
            }
        });

        // Action listener for lower bound algorithm button
        lowerBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                runLowerBoundAlgorithm();
                System.out.println("lower bound algorithm button clicked.");
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                Main.mainMenu();
                frame.dispose();
            }
        });

        // Action listener for zoom in button
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                graphPanel.zoomIn();
            }
        });

        // Action listener for zoom out button
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                graphPanel.zoomOut();
            }
        });

        // Action listener for random graph button
        randomGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                generateGraph();  // Added action listener for random graph generation
            }
        });

        // Mouse listener for dragging
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int adjustedX = (int) ((clickPoint.x - panX) / zoomFactor);
                int adjustedY = (int) ((clickPoint.y - panY) / zoomFactor);
                Point adjustedClickPoint = new Point(adjustedX, adjustedY);

                draggedVertex = null;
                isDraggingVertex = false; //resetting the dragging flag

                for (Map.Entry<String, Point> entry : vertexPositions.entrySet()) {
                    String vertex = entry.getKey();
                    Point vertexPos = entry.getValue();
                    int radius = 20;

                    if (vertexPos.distance(adjustedClickPoint) < radius) {
                        draggedVertex = vertex;
                        break;
                    }
                }
                lastMousePosition = adjustedClickPoint;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedVertex = null;
                isDraggingVertex = false; // Reset dragging flag
            }
        });

        // Mouse motion listener for dragging
        graphPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedVertex != null) {
                    Point currentPoint = e.getPoint();
                    int adjustedX = (int) ((currentPoint.x - panX) / zoomFactor);
                    int adjustedY = (int) ((currentPoint.y - panY) / zoomFactor);
                    Point adjustedCurrentPoint = new Point(adjustedX, adjustedY);

                    if (!isDraggingVertex && lastMousePosition.distance(adjustedCurrentPoint) > DRAG_THRESHOLD) {
                        isDraggingVertex = true;
                    }

                    if (isDraggingVertex) {
                        vertexPositions.put(draggedVertex, adjustedCurrentPoint);
                        lastMousePosition = adjustedCurrentPoint;
                        graphPanel.repaint();  // Repaint the graph to reflect the new positions
                    }
                }
            }
        });
    }

    
    private void handleVertexClick(String vertex) {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
        String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Orange"};

        String selectedColorName = (String) JOptionPane.showInputDialog(
            frame,
            "Choose a color for vertex " + vertex + ":",
            "Select Color",
            JOptionPane.PLAIN_MESSAGE,
            null,
            colorNames,
            colorNames[0]
        );

        if(selectedColorName != null) {
            for(int i=0; i<colorNames.length; i++) {
                if(selectedColorName.equals(colorNames[i])) {
                    graphPanel.setVertexColor(vertex, colors[i]);
                    break;
                }
            }
            graphPanel.repaint();
        }
    }
    

    // Helper methods

    /**
     * I made minimal changes to the loadGraphFromFile:
     * 
     * the click listener now connects vertex clicks to the handleVertexClick method
     * 
     * no changes in updateGraph call
     * no changes in ui updates
     * no changes in logic
     * 
     */
    private void loadGraphFromFile(File file) {

        TxtInterpretation txtInt = new TxtInterpretation();
        // getting vertices and edges
        vertices = txtInt.getVertices(file.getAbsolutePath());
        edges = txtInt.getEdges(file.getAbsolutePath());

        if(vertices != null && edges != null) {
            upperBound = -1; // reset
            // display in result area
            resultArea.append("Graph loaded from file: " + file.getName() + "\n");
            resultArea.append("Number of vertices: " + vertices.length + "\n");
            resultArea.append("Number of edges: " + edges.length + "\n\n");

            graphPanel.updateGraph(vertices, edges);

            graphPanel.setVertexClickListener(new GraphVisualizer.VertexClickListener() {
                @Override
                public void vertexClicked(String vertex) {
                    handleVertexClick(vertex);
                }
            });

            frame.revalidate();
            frame.repaint();
        } else {
            resultArea.append("Failed to load graph from file " + file.getName() + "\n");
        }
    }

    private void runExactAlgorithm() {
        if(vertices == null || edges == null) {
            resultArea.append("Please load a graph first.\n");
            return;
        }
        if(SpecialCases.isComplete(vertices, edges)) {
            int ans = vertices.length;
            resultArea.append("Graph is complete. Chromatic number is " + ans + ".\n");
            return;
        }
        if(SpecialCases.isBipartite(vertices, edges)) {
            resultArea.append("Graph is bipartite. Chromatic number is 2.\n");
            return;
        }
        ExactAlgorithm exactAlg = new ExactAlgorithm(vertices, edges);
        if(upperBound == -1) {
            upperBound = UpperBoundAlgorithm.UpperBound(vertices, edges);
        }
        if(lowerBound == -1) {
            lowerBound = LowerBoundAlgorithm.LowerBound(vertices, edges);
        }
        int[] colors = new int[vertices.length];

        int exactChroNumber = exactAlg.branchAndBound(vertices, edges, colors, upperBound, lowerBound);
        if(exactChroNumber == -1) {
            SoundFX.playSound("fail.wav");
            resultArea.append("The calculation took more than 30 seconds, \n" + "Here is the upper bound: " + upperBound + "\n" + "Here is the lower bound: " + lowerBound + "\n");
        }
        else {
            resultArea.append("Exact chromatic number: " + exactChroNumber + ".\n");
        }
    }


private void generateRandomGraph() { 
    Scanner scanner = new Scanner(System.in);
    int numVertices = scanner.nextInt(); 
    int numEdges = scanner.nextInt(); 
    scanner.close();
    RandomGraphGenerator randomGraphGenerator = new RandomGraphGenerator(numVertices, numEdges); 
    vertices = randomGraphGenerator.getVertices(); 
    edges = randomGraphGenerator.getEdgesNormalized(); 
    
    vertexPositions.clear(); 
    Random rand = new Random(); 
    for (String vertex : vertices) { 
        int x = rand.nextInt(graphPanel.getWidth()); 
        int y = rand.nextInt(graphPanel.getHeight()); 
        vertexPositions.put(vertex, new Point(x, y)); } 
        
    resultArea.append("Random graph generated with " + numVertices + " vertices and " + numEdges + " edges.\n"); 
    graphPanel.updateGraph(vertices, edges); frame.revalidate(); // Revalidate the frame to reflect the changes 
    frame.revalidate();
    frame.repaint(); 
    }

    private void runUpperBoundAlgorithm(){
        if(vertices == null || edges == null) {
            resultArea.append("Please load a graph first.\n");
            return;
        }

        upperBound = UpperBoundAlgorithm.UpperBound(vertices, edges);
        resultArea.append("Upper bound: " + upperBound + ".\n");
    }

    private void runLowerBoundAlgorithm(){
        if(vertices == null || edges == null) {
            resultArea.append("Please load a graph first .\n");
            return;
        }

        lowerBound = LowerBoundAlgorithm.LowerBound(vertices, edges);
        resultArea.append("Lower bound: " + lowerBound + ".\n");
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

        ColoringAlgorithm coloringAlgorithm = new ColoringAlgorithm();

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
                String[] Vertices = generator.getVertices();
                String[][] Edges = generator.getEdgesNormalized(); // Use normalized edges

                graphPanel.updateGraph(Vertices, Edges);

                frame2.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

