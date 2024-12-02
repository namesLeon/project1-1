package App.Games;

import App.Algorithms.ColoringAlgorithm;
import App.Algorithms.RandomGraphGenerator;
import App.GUI.GraphVisualizer;
import App.GUI.SoundFX;
import App.Games.Leaderboards.*;
import App.Main.Main;
import java.awt.*;
import java.util.Random;
import java.util.Stack;
import javax.swing.*;


public class IChangedMyMind {
    private static int upperBoundVertices = 100;
    private static int lowerBoundVertices = 1;
    private static int upperBoundEdges = 100;
    private static int lowerBoundEdges = 0;
    private static GraphVisualizer graphPanel;

    private static String[] vertexOrder; 
    private static int currentVertexIndex; 
    private static Stack<Integer> undoStack = new Stack<>(); 
    

    public static void game() {
        JFrame frame = new JFrame("I Changed My Mind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel backPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JButton hint = new JButton("Hint");
        JButton generateGraph = new JButton("Generate Graph");
        JButton undoButton = new JButton("Undo");
        JButton back = new JButton("Back");
        JButton leaderboard = new JButton("Show Leaderboard");

        backPanel.add(back);
        buttonPanel.add(hint);
        buttonPanel.add(generateGraph);
        buttonPanel.add(undoButton);
        buttonPanel.add(leaderboard);

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));

        frame.add(backPanel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.add(graphPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        back.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            Main.mainMenu();
            frame.dispose();
        });

        hint.addActionListener(e -> {
            SoundFX.playSound("fail.wav");
        });

        generateGraph.addActionListener(e -> {
            SoundFX.playSound("button.wav");
            generateGraph();
            frame.revalidate();
            frame.repaint();
        });

        undoButton.addActionListener(e -> {
            if (!undoStack.isEmpty()) {
                int lastColoredVertex = undoStack.pop();
                graphPanel.uncolorVertex(vertexOrder[lastColoredVertex]);

                currentVertexIndex--;
                SoundFX.playSound("undo.wav");
            } else {
                JOptionPane.showMessageDialog(frame, "No more moves to undo!", "Undo Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        leaderboard.addActionListener(e -> {
            SoundFX.playSound("button.wav");

            showLeaderboard();
        });
    }

    public static void showLeaderboard() {//jieke add this any questions ask me
        //Loading leaderboard data
        Leaderboard.loadLeaderboard();
        //Display the ranking interface
        Leaderboard.showLeaderboard();
    }

    public static void generateGraph() {
        JFrame frame2 = new JFrame();

        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setSize(new Dimension(400, 400));
        frame2.setLayout(new BorderLayout());

        JPanel confirmPanel = new JPanel();

        JButton confirm = new JButton("Confirm");
        confirmPanel.add(confirm);

        JLabel topLabel = new JLabel("Enter number of vertices and edges", JLabel.CENTER);


        JPanel panel2 = new JPanel(new GridLayout(2, 2, 1, 5));
        panel2.add(new JLabel("Vertices:"));
        JTextField textField1 = new JTextField(1);
        panel2.add(textField1);

        panel2.add(new JLabel("Edges:"));
        JTextField textField2 = new JTextField(1);
        panel2.add(textField2);

        frame2.add(confirmPanel, BorderLayout.SOUTH);
        frame2.add(topLabel, BorderLayout.NORTH);


        frame2.add(panel2, BorderLayout.CENTER);
        frame2.setVisible(true);

        confirm.addActionListener(e -> {
            try {
                int numOfVertices = Integer.parseInt(textField1.getText());
                int numOfEdges = Integer.parseInt(textField2.getText());

                if (((numOfVertices < lowerBoundVertices) || (numOfVertices > upperBoundVertices)) || ((numOfEdges < lowerBoundEdges) || (numOfEdges > upperBoundEdges))) {
                    JOptionPane.showMessageDialog(frame2, "Entries are out of bounds.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    RandomGraphGenerator generator = new RandomGraphGenerator(numOfVertices, numOfEdges);
                    ColoringAlgorithm color = new ColoringAlgorithm();

                    String[] vertices = generator.getVertices();

                    String[][] edges = generator.getEdges();


                    vertexOrder = vertices.clone();
                    shuffleArray(vertexOrder);
                    currentVertexIndex = 0;
                    undoStack.clear();

                    graphPanel.updateGraph(vertices, edges);
                    graphPanel.setVertexOrder(vertexOrder);

                    color.clearColors();


                    frame2.dispose();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void shuffleArray(String[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
