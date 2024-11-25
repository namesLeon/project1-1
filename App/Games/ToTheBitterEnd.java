package App.Games;

import App.Algorithms.ColoringAlgorithm;
import App.GUI.GraphVisualizer;
import App.GUI.SoundFX;
import App.Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ToTheBitterEnd {

    private static int upperBoundVertices = 100;
    private static int lowerBoundVertices = 0;
    private static int upperBoundEdges = 100;
    private static int lowerBoundEdges = 0;
    private static GraphVisualizer graphPanel;
    private static Instant startTime;
    private static Instant endTime;

    private static ColoringAlgorithm coloringAlgorithm;

    public static void game() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel backPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JButton hint = new JButton("Hint");
        JButton generateGraph = new JButton("Generate Graph");
        JButton back = new JButton("Back");
        JButton leaderboard = new JButton("Show Leaderboard");
        JButton submit = new JButton("Submit Solution");

        backPanel.add(back);
        buttonPanel.add(hint);
        buttonPanel.add(generateGraph);
        buttonPanel.add(leaderboard);
        buttonPanel.add(submit);

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));

        frame.add(backPanel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                Main.mainMenu();
                frame.dispose();
            }
        });

        hint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("fail.wav");
                // TODO: implement hint functionality
            }
        });

        generateGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                generateGraph();
                startTime = Instant.now(); // Start timer when graph is generated
                frame.revalidate();
                frame.repaint();

                // Instantiate ColoringAlgorithm when graph is generated
                coloringAlgorithm = new ColoringAlgorithm(graphPanel.getVertexCount());
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                endTime = Instant.now(); // Stop timer when submit button is pressed
                Duration timeTaken = Duration.between(startTime, endTime);

                boolean isCorrect = checkGraphColoring();
                showResult(timeTaken, isCorrect);
            }
        });
    }

    private static boolean checkGraphColoring() {
        //check the validity of the current graph coloring
        boolean isCorrect = true;

        for (int i = 0; i < graphPanel.getVertexCount(); i++) {
            String currentColor = coloringAlgorithm.getColor(i);
            if (currentColor == null) {
                isCorrect = false;
                break;
            }

            Set<String> unavailableColors = coloringAlgorithm.getUnavailableColors(i);
            if (unavailableColors.contains(currentColor)) {
                isCorrect = false;
                break;
            }
        }

        return isCorrect;
    }

    private static void showResult(Duration timeTaken, boolean isCorrect) {
        long seconds = timeTaken.getSeconds();
        long millis = timeTaken.toMillisPart();
        String message = isCorrect ? "You won!" : "You lost.";
        JOptionPane.showMessageDialog(null, message + " Time taken: " + seconds + " seconds and " + millis + " milliseconds.");
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

    public static void generateGraph() {
        JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setSize(new Dimension(400, 400));
        frame2.setLayout(new BorderLayout());

        JPanel confirmPanel = new JPanel();

        JButton confirm = new JButton("Confirm");

        confirmPanel.add(confirm);

        JLabel topLabel = new JLabel("Give number of vertices and edges", JLabel.CENTER);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 4, 1, 5));

        panel2.add(new JLabel(""));
        panel2.add(new JLabel("Vertices"));

        JTextField textField1 = new JTextField(1);
        panel2.add(textField1);

        panel2.add(new JLabel(""));
        panel2.add(new JLabel(""));
        panel2.add(new JLabel("Edges"));

        JTextField textField2 = new JTextField(1);
        panel2.add(textField2);

        panel2.add(new JLabel(""));

        frame2.add(confirmPanel, BorderLayout.SOUTH);
        frame2.add(topLabel, BorderLayout.NORTH);
        frame2.add(panel2, BorderLayout.CENTER);

        frame2.setVisible(true);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numOfVertices = Integer.parseInt(textField1.getText());
                    int numOfEdges = Integer.parseInt(textField2.getText());

                    if (((numOfVertices < lowerBoundVertices) || (numOfVertices > upperBoundVertices))
                            || ((numOfEdges < lowerBoundEdges) || (numOfEdges > upperBoundEdges))) {
                        JOptionPane.showMessageDialog(frame2, "At least one of these entries is out of bounds.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // TODO: Replace with real graph generation logic
                        String[] test = {"a", "b", "c", "d"};
                        String[][] test2 = {{"a", "b"}, {"a", "c"}, {"b", "d"}, {"c", "d"}};

                        graphPanel.updateGraph(test, test2); // to be replaced with vertices and edges
                        frame2.dispose();
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
