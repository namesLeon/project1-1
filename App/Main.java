package App;

//This is the main file of the program, mainly used by
//the code lead, to call the other files and have them all work together
public class Main {
    public static void main(String[] args) {
        String[] vertices = {"a", "b", "c", "d"};
        String[][] edges = {
            {"a", "b"}, 
            {"a", "c"}, 
            {"b", "d"},
            {"c", "d"} //test 
        };

        //I made the first example a square because it's easy to understand

        int upperBound = vertices.length; //number of vertices for an easy reference
        int[] colors = new int [vertices.length];

        ExactAlgorithm exactAlg = new ExactAlgorithm();

        int exactChroNumber = exactAlg.recursiveBacktracking(vertices, edges, upperBound, colors);
        System.out.println("Using recursive backtracking gives " + exactChroNumber + " as the exact chromatic number");

        colors = new int [vertices.length];

        exactChroNumber = exactAlg.branchAndBound(vertices, edges, 0, colors, upperBound);
        System.out.println("Using a branching and bounding technique gives " + exactChroNumber + " as the exact chromatic number");
    }
}
