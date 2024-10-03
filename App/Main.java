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
            {"c", "d"}
        };
        int upperBound = 4; //number of vertices for an easy reference

        ExactAlgorithm exactAlg = new ExactAlgorithm();
        int exactChroNumber = exactAlg.exactAlgorithm(vertices, edges, upperBound);
        System.out.println(exactChroNumber);
    }
}
