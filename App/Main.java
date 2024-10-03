package App;

//This is the main file of the program, mainly used by
//the code lead, to call the other files and have them all work together

public class Main {
    public static void main(String[] args) {
        String[] vertices;
        String[][] edges;
        int upperBound;
        
        ExactAlgorithm exactAlg = new ExactAlgorithm();
        int exactChroNumber = exactAlg.exactAlgorithm(vertices, edges, upperBound);
    }
}
