package App.Algorithms;

import java.util.ArrayList;
import java.util.List;

public class ExactAlgorithm {
    public int recursiveBacktracking(String[] vertices, String[][] edges, int upperBound, int[] colors) {
        int exactChroNumber = 0;

        for(int i = 0; i < colors.length; i++) {
            for (int color = 1; color <= upperBound; color++) {
                if (isColorValid(vertices, edges, i, colors, color)) {
                    colors[i] = color;
                    break;
                }
            }
        }

        exactChroNumber = getMaxColorUsed(colors);

        return exactChroNumber;
    }

    public int branchAndBound(String[] vertices, String[][] edges, int vertexIndex, int[] colors, int upperBound) {
        if(vertexIndex == (vertices.length)) {
            int currentMaxColor = getMaxColorUsed(colors);

            if(currentMaxColor < upperBound) {
                upperBound = currentMaxColor;
            }

            return upperBound;
        }

        for(int color = 1; color <= upperBound; color++) {
            if(isColorValid(vertices, edges, vertexIndex, colors, color)) {
                colors[vertexIndex] = color;

                upperBound = branchAndBound(vertices, edges, vertexIndex + 1, colors, upperBound);

                colors[vertexIndex] = 0;
            }
        }

        return upperBound;
    }

    public boolean isColorValid(String[] vertices, String[][] edges, int vertexIndex, int[] colors, int color) {
        boolean valid = true;
        String[] neighbours = getNeighbours(vertices[vertexIndex], edges);

        for (String neighbour : neighbours) {
            for (int j = 0; j < vertices.length; j++) {
                if (vertices[j].equals(neighbour) && colors[j] == color) {
                    valid = false;
                    break;
                }
            }
            if (!valid) { 
                break;
            }
        }

        return valid;
    }

    public String[] getNeighbours(String vertex, String[][] edges) {
        List<String> list = new ArrayList<String>();

        for(int i = 0; i < edges.length; i++) {
            if(edges[i][0].equals(vertex)) {
                list.add(edges[i][1]);
            }
            if(edges[i][1].equals(vertex)) {
                list.add(edges[i][0]);
            }
        }

        String[] neighbours = list.toArray(new String[list.size()]);

        return neighbours;
    }

    public int getMaxColorUsed(int[] colors) {
        int maxColorUsed = 0;

        for (int color : colors) {
            if (color > maxColorUsed) {
                maxColorUsed = color;
            }
        }

        return maxColorUsed;
    }
}