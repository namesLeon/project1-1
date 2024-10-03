package App;

import java.util.ArrayList;
import java.util.List;

class ExactAlgorithm {
    public int exactAlgorithm(String[] vertices, String[][] edges, int upperBound) {
        int exactChroNumber = 0;

        int[] colors = new int[vertices.length];

        for(int i = 0; i < colors.length; i++) {
            String[] neighbours = getNeighbours(vertices[i], edges);
            for (int c = 1; c <= upperBound; c++) {
                boolean valid = true;
                for (String neighbour : neighbours) {
                    for (int j = 0; j < vertices.length; j++) {
                        if (vertices[j].equals(neighbour) && colors[j] == c) {
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) break;
                }
                
                if (valid) {
                    colors[i] = c;
                    break;
                }
            }
        }

        int maxColorUsed = 0;
        for (int color : colors) {
            if (color > maxColorUsed) {
                maxColorUsed = color;
            }
        }

        exactChroNumber = maxColorUsed;

        return exactChroNumber;
    }

    public String[] getNeighbours(String vertice, String[][] edges) {
        List<String> list = new ArrayList<String>();

        for(int i = 0; i < edges.length; i++) {
            if(edges[i][0].equals(vertice)) {
                list.add(edges[i][1]);
            }
            if(edges[i][1].equals(vertice)) {
                list.add(edges[i][0]);
            }
        }

        String[] neighbours = list.toArray(new String[list.size()]);

        return neighbours;
    }
}