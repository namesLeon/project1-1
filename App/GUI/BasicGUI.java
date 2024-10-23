package App.GUI;

// necessary imports
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.UpperBoundAlgorithm;
import App.Txt.TxtInterpretation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.IOException;


public class BasicGUI {

    // instance variables for graph data (optional at this point)
    private String[] vertices;
    private String[][] edges;
    // declaring result area as an instance variable to make it accessible to other methods
    private JTextArea resultArea;
    
    // method to create and display the gui
    public void createShowGUI() {

        // create a new window
        JFrame frame = new JFrame("project1-1");

        // application exits when we close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // size of the window
        frame.setSize(600, 800);

        // panel to hold components
        JPanel panel = new JPanel();

        // button that does nothing for now 
        JButton loadGraphButton = new JButton("Load Graph");
        JButton exactButton = new JButton("Run Exact Algorithm");
        JButton upperBoundButton = new JButton("Run Upper Bound Algorithm");
        JButton lowerBoundButton = new JButton("Run Lower Bound Algorithm");

        // new text area to display the results
        JTextArea resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        
        // add buttons to panel
        panel.add(loadGraphButton);
        panel.add(exactButton);
        panel.add(upperBoundButton);
        panel.add(lowerBoundButton);
        panel.add(scrollPane);
        
        // add the panel to the window
        frame.add(panel);
        
        // make the window visible
        frame.setVisible(true);

        // action listener for load graph button
        loadGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo: create a load graph method and implement action listener later
                System.out.println("load Graph button clicked.");
            }
        });

        // action listener for exact alg. button
        exactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runExactAlgorithm();
                System.out.println("exact Algorithm button clicked.");
            }
        });
        
        // action listener for upper bound alg. button
        upperBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runUpperBoundAlgorithm();
                System.out.println("upper bound algorithm button clicked.");
            }
        });

        // action listener for lower bound alg. button
        lowerBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runLowerBoundAlgorithm();
                System.out.println("lower bound algorithm button clicked.");
            }
        });

    }

    // helper methods

    private void loadGraphFromFile(File file) {
        // logic to read graph from file
        TxtInterpretation txtInt = new TxtInterpretation();
    }

    private void runExactAlgorithm(){
        if(vertices == null || edges == null) {
            System.out.println("load a graph first");
            resultArea.append("load a graph first");
            return;
        }

        ExactAlgorithm exactAlg = new ExactAlgorithm();
        int upperBound = vertices.length; // assuming upper bound is the number of vertices
        int[] colors = new int[vertices.length];

        int exactChroNumber = exactAlg.recursiveBacktracking(vertices, edges, upperBound, colors);
        System.out.println("exact chromatic number: " + exactChroNumber);    
    }

    private void runUpperBoundAlgorithm(){
        if(vertices == null || edges == null) {
            System.out.println("load a graph first");
            return;
        }

        UpperBoundAlgorithm upperAlg = new UpperBoundAlgorithm();
        // upper bound algorithm not implemented yet
    }

    private void runLowerBoundAlgorithm(){
        if(vertices == null || edges == null) {
            System.out.println("load a graph first");
            return;
        }

        LowerBoundAlgorithm lowerAlg = new LowerBoundAlgorithm();
        // lower bound algorithm not implemented yet
    }

}

