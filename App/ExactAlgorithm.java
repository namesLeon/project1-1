package App;

import java.util.ArrayList;
import java.util.List;

class ExactAlgorithm {
    public int exactAlgorithm(String[] vertices, String[][] edges, int upperBound) {
        if (vertices == null || vertices.length == 0) {
            return 0; //hadle an empty vrtices array
        }
        if(edges == null) {
            edges = new String[0][0]; //handle a null case edges matrix
        }

        int[] colors = new int[vertices.length]; //initialising array that keeps track of the amount of colors that have been chosen

        for(int i = 0; i < colors.length; i++) {
            String[] neighbours = getNeighbours(vertices[i], edges);
            for (int c = 1; c <= upperBound; c++) {
                boolean valid = true;
                for (String neighbour : neighbours) {
                    for (int j = 0; j < vertices.length; j++) {
                        if (vertices[j].equals(neighbour) && colors[j] == c) { //all these loops and if statements basically just check if any neighbours have the same color
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) break; //if a neighbour with the same color is detected don't continue checking
                }
                
                if (valid) { //if no neighbour with the same color is detected assign that color
                    colors[i] = c;
                    break; //if the vertex has a color don't check for new colors
                }
            }
        }

        int maxColorUsed = 0;
        for (int color : colors) {
            if (color > maxColorUsed) { //checking the amount of colors used
                maxColorUsed = color;
            }
        }

        return maxColorUsed;
    }

    public String[] getNeighbours(String vertex, String[][] edges) {
        List<String> list = new ArrayList<String>(); //using a list because it's easier to work with in this scenario

        for(int i = 0; i < edges.length; i++) { //checking if the given vertex appears in an edge, if so add the other vertex of said edge to the list
            if(edges[i][0].equals(vertex)) {
                list.add(edges[i][1]);
            }
            if(edges[i][1].equals(vertex)) {
                list.add(edges[i][0]);
            }
        }

        String[] neighbours = list.toArray(new String[list.size()]); //turning list into an array

        return neighbours;
    }
}