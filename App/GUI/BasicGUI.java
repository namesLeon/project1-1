package App.GUI;

// necessary imports

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicGUI {

    // method to create and display the gui
    public void createShowGUI() {

        // create a new window
        JFrame frame = new JFrame("project1-1");

        // application exits when we close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // size of the window
        frame.setSize(400, 300);

        // panel to hold components
        JPanel panel = new JPanel();

        // button that does nothing for now 
        JButton exactButton = new JButton("Run Exact Algorithm");
        JButton upperBoundButton = new JButton("Run Upper Bound Algorithm");
        JButton lowerBoundButton = new JButton("Run Lower Bound Algorithm");
        JButton loadGraphButton = new JButton("Load Graph");

        // todo: button action listeners

        // add buttons to panel

        panel.add(loadGraphButton);
        panel.add(exactButton);
        panel.add(upperBoundButton);
        panel.add(lowerBoundButton);

        // add the panel to the window
        frame.add(panel);

        // make the window visible
        frame.setVisible(true);
    }
}

