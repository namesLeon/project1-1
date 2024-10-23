package App.GUI;

// necessary imports
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.UpperBoundAlgorithm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;




public class BasicGUI {

    // instance variables for graph data (optional at this point)
    private String[] vertices;
    private String[][] edges;
    
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
        //todo: complete this method later
    }

    private void runExactAlgorithm(){
        //todo: complete this method later
        ExactAlgorithm exactAlg = new ExactAlgorithm();
    }

    private void runUpperBoundAlgorithm(){
        //todo: complete this method later
        UpperBoundAlgorithm upperAlg = new UpperBoundAlgorithm();
    }

    private void runLowerBoundAlgorithm(){
        //todo: complete this method later
        LowerBoundAlgorithm lowerAlg = new LowerBoundAlgorithm();

    }

}

