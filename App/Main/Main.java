package App.Main;

import App.Algorithms.*;
import App.GUI.*;
import App.Txt.*;

/* The plan for now is to have Main.java call the GUI, which then in turn can call upon the Algorithms which then call TxtInterpretation.java */
public class Main {
    @SuppressWarnings("unused") //To get rid of the temporary annoying unused errors
    public static void main(String[] args) {
        String[] vertices = {"a", "b", "c", "d"};
        String[][] edges = {
            {"a", "b"}, 
            {"a", "c"}, 
            {"b", "d"},
            {"c", "d"}
        };

        //I made the first example a square because it's easy to understand

        int upperBound = vertices.length; //number of vertices for an easy reference
        int[] colors = new int [vertices.length];

        ExactAlgorithm exactAlg = new ExactAlgorithm();
        LowerBoundAlgorithm lowBoundAlg = new LowerBoundAlgorithm();
        UpperBoundAlgorithm uppBoundAlg = new UpperBoundAlgorithm();
        BasicGUI basicGUI = new BasicGUI();
        TxtInterpretation txt = new TxtInterpretation();

        basicGUI.createShowGUI();

        int exactChroNumber = exactAlg.recursiveBacktracking(vertices, edges, upperBound, colors);
        System.out.println("Using recursive backtracking gives " + exactChroNumber + " as the exact chromatic number");

        colors = new int [vertices.length];

        exactChroNumber = exactAlg.branchAndBound(vertices, edges, 0, colors, upperBound);
        System.out.println("Using a branching and bounding technique gives " + exactChroNumber + " as the exact chromatic number");
    }
}
