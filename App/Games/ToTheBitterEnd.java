package App.Games;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import App.GUI.GraphVisualizer;
import App.GUI.SoundFX;
import App.Main.Main;

public class ToTheBitterEnd {

    private static int upperBoundVertices = 100;
    private static int lowerBoundVertices = 0;
    private static int upperBoundEdges = 100;
    private static int lowerBoundEdges = 0;
    private static GraphVisualizer graphPanel;

    public static void game() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel backPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JButton hint = new JButton("hint");
        JButton generateGraph = new JButton("generate graph");
        JButton back = new JButton("back");

        backPanel.add(back);
        buttonPanel.add(hint);
        buttonPanel.add(generateGraph);

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
                //TODO implement hint functionality
            }
        });

        generateGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                generateGraph();
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public static void generateGraph() {
        JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(new Dimension(400, 400));
        frame2.setLayout(new BorderLayout());

        JPanel confirmPanel = new JPanel();

        JButton confirm = new JButton("confirm");

        confirmPanel.add(confirm);

        JLabel topLabel = new JLabel("Give number of vertices and edges", JLabel.CENTER);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 4, 1, 5));

        panel2.add(new JLabel(""));
        panel2.add(new JLabel("vertices"));
    
        JTextField textField1 = new JTextField(1);
        panel2.add(textField1);

        panel2.add(new JLabel(""));
        panel2.add(new JLabel(""));
        panel2.add(new JLabel("edges"));

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

                    if(((numOfVertices < lowerBoundVertices) || (numOfVertices > upperBoundVertices)) || ((numOfEdges < lowerBoundEdges) || (numOfEdges > upperBoundEdges))) {
                        JOptionPane.showMessageDialog(frame2, "At least one of these entries is out of bounds.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                    //TODO call generateGraph to get String[] vertices and String[][] edges once it is written
                    // String[] vertices = generateGraph.getVertices();
                    // String[][] edges = generateGraph.getEdges();

                    String[] test = {"a", "b", "c", "d"};
                    String[][] test2 = {{"a", "b"}, {"a", "c"}, {"b", "d"}, {"c", "d"}};

                    graphPanel.updateGraph(test, test2); // to be replaced with vertices and edges

                    frame2.dispose();
                    }

                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame2, "Please enter valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
