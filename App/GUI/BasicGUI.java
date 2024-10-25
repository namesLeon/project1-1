package App.GUI;

// necessary imports
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.UpperBoundAlgorithm;
import App.Txt.TxtInterpretation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;


public class BasicGUI {

    // instance variables for graph data (optional at this point)
    private String[] vertices;
    private String[][] edges;
    private int upperBound;
    private int lowerBound;
    // declaring result area as an instance variable to make it accessible to other methods
    private JTextArea resultArea;
    private JFrame frame;
    private GraphVisualizer graphPanel;
    
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
        resultArea = new JTextArea(15, 40);
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
                resultArea.setText("");
                SoundFX.playSound("button.wav");
                //todo: create a load graph method and implement action listener later
                JFileChooser fileChooser = new JFileChooser();

                File defaultDirectory = new File("App/Txt/Graphs");
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

        // action listener for exact alg. button
        exactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                runExactAlgorithm();
                System.out.println("exact Algorithm 2 button clicked.");
            }
        });
        
        // action listener for upper bound alg. button
        upperBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                runUpperBoundAlgorithm();
                System.out.println("upper bound algorithm button clicked.");
            }
        });

        // action listener for lower bound alg. button
        lowerBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                runLowerBoundAlgorithm();
                System.out.println("lower bound algorithm button clicked.");
            }
        });

    }

    // helper methods

    private void loadGraphFromFile(File file) {
        // logic to read graph from file
        TxtInterpretation txtInt = new TxtInterpretation();
        // getting vertices and edges
        vertices = txtInt.getVertices(file.getAbsolutePath());
        edges = txtInt.getEdges(file.getAbsolutePath());

        if(vertices!=null && edges!=null){
            upperBound=-1; // reset
            
            // display in result area
            resultArea.append("graph loaded from file: " + file.getName() + "\n");
            resultArea.append("number of vertices: " + vertices.length + "\n");
            resultArea.append("number of edges: " + edges.length + "\n\n");

        } 
        
        if (vertices != null && edges != null) {
            graphPanel = new GraphVisualizer(vertices, edges);
            frame.add(graphPanel); // Add the visualizer to the frame
            frame.revalidate();    // Revalidate the frame to reflect the changes
            frame.repaint();       // Repaint the frame to show the graph
        }
        
        else {
            resultArea.append("failed to load graph from file " + file.getName() + "\n");
        }


    }

    private void runExactAlgorithm(){
        if(vertices == null || edges == null) {
            resultArea.append("please load a graph first \n.");
            return;
        }

        ExactAlgorithm exactAlg = new ExactAlgorithm(vertices, edges);
        upperBound = UpperBoundAlgorithm.UpperBound(vertices, edges);
        lowerBound = LowerBoundAlgorithm.LowerBound(vertices, edges);
        int[] colors = new int[vertices.length];

        int exactChroNumber = exactAlg.branchAndBound(vertices, edges, colors, upperBound, lowerBound);
        if(exactChroNumber == -1) {
            SoundFX.playSound("fail.wav");
            resultArea.append("the calculation took more than 30 seconds \n" + "here is the upper bound " + upperBound + "\n" + "here is the lower bound " + lowerBound + "\n");
        }
        else {
            resultArea.append("exact chromatic number: " + exactChroNumber + "\n");
        }
    }

    private void runUpperBoundAlgorithm(){
        if(vertices == null || edges == null) {
            resultArea.append("load a graph first \n");
            return;
        }

        upperBound = UpperBoundAlgorithm.UpperBound(vertices, edges);
        resultArea.append("upper bound: " + upperBound + "\n");
    }

    private void runLowerBoundAlgorithm(){
        if(vertices == null || edges == null) {
            resultArea.append("load a graph first \n");
            return;
        }

        lowerBound = LowerBoundAlgorithm.LowerBound(vertices, edges);
        resultArea.append("lower bound: " + lowerBound + "\n");
    }

}

