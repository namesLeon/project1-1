package App.GUI;

// necessary imports
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.UpperBoundAlgorithm;
import App.Txt.TxtInterpretation;
import App.Main.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import java.awt.*;


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
        frame = new JFrame("Basic GUI");

        // application exits when we close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // size of the window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLayout(new BorderLayout());

        // panel to hold components
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        // button that does nothing for now 
        JButton loadGraphButton = new JButton("Load Graph");
        JButton exactButton = new JButton("Run Exact Algorithm");
        JButton upperBoundButton = new JButton("Run Upper Bound Algorithm");
        JButton lowerBoundButton = new JButton("Run Lower Bound Algorithm");
        JButton backButton = new JButton("Back");
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");

        // new text area to display the results
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        
        // add buttons to panel
        panel1.add(loadGraphButton);
        panel1.add(exactButton);
        panel1.add(upperBoundButton);
        panel1.add(lowerBoundButton);
        panel1.add(scrollPane);
        panel2.add(zoomInButton);
        panel2.add(backButton);
        panel2.add(zoomOutButton);

        graphPanel = new GraphVisualizer(new String[0], new String[0][0]);
        graphPanel.setPreferredSize(new Dimension(600, 600));
        
        // add the panel to the window
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);
        
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                Main.mainMenu();
                frame.dispose();
            }
        });

        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                graphPanel.zoomIn();
            }
        });
        
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                graphPanel.zoomOut();
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

            graphPanel.updateGraph(vertices, edges);
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

