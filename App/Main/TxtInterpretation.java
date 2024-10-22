package App.Main;

import java.io.*;
import java.util.*;
import App.Algorithms.ExactAlgorithm;
import App.Algorithms.LowerBoundAlgorithm;
import App.Algorithms.UpperBoundAlgorithm;

class ColEdge {
    int u;
    int v;
}

public class TxtInterpretation {

    public final static boolean DEBUG = true;
    public final static String COMMENT = "//";

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Error! No filename specified.");
            System.exit(0);
        }

        String inputfile = args[0];
        boolean seen[] = null;

        int n = -1; // Number of vertices
        int m = -1; // Number of edges
        ColEdge[] e = null;

        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            String record;

            // Reading vertices and edges
            while ((record = br.readLine()) != null) {
                if (record.startsWith(COMMENT)) continue;
                break;
            }

            // Reading number of vertices
            if (record.startsWith("VERTICES = ")) {
                n = Integer.parseInt(record.substring(11));
                if (DEBUG) System.out.println(COMMENT + " Number of vertices = " + n);
            }

            seen = new boolean[n + 1];

            // Reading number of edges
            record = br.readLine();
            if (record.startsWith("EDGES = ")) {
                m = Integer.parseInt(record.substring(8));
                if (DEBUG) System.out.println(COMMENT + " Expected number of edges = " + m);
            }

            // Initialize edges
            e = new ColEdge[m];

            for (int d = 0; d < m; d++) {
                if (DEBUG) System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String[] data = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }

                e[d] = new ColEdge();
                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                seen[e[d].u] = true;
                seen[e[d].v] = true;

                if (DEBUG) System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);
            }

            String surplus = br.readLine();
            if (surplus != null) {
                if (surplus.length() >= 2) if (DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '" + surplus + "'");
            }

        } catch (IOException ex) {
            System.out.println("Error! Problem reading file " + inputfile);
            System.exit(0);
        }

        for (int x = 1; x <= n; x++) {
            if (!seen[x]) {
                if (DEBUG) System.out.println(COMMENT + " Warning: vertex " + x + " didn't appear in any edge: it will be considered a disconnected vertex on its own.");
            }
        }

        // At this point, we have n vertices and m edges.
        // Now we need to convert the data to a format suitable for the ExactAlgorithm.

        String[] vertices = new String[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = String.valueOf(i + 1);
        }

        String[][] edges = new String[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = String.valueOf(e[i].u);
            edges[i][1] = String.valueOf(e[i].v);
        }

        // Instantiate the algorithms
        ExactAlgorithm exactAlg = new ExactAlgorithm();
        LowerBoundAlgorithm lowBoundAlg = new LowerBoundAlgorithm(); // Empty for now
        UpperBoundAlgorithm uppBoundAlg = new UpperBoundAlgorithm(); // Empty for now

        int upperBound = n; // number of vertices as an upper bound for the chromatic number
        int[] colors = new int[n];

        // Call the exact algorithm
        int exactChroNumber = exactAlg.recursiveBacktracking(vertices, edges, upperBound, colors);
        System.out.println("Using recursive backtracking gives " + exactChroNumber + " as the exact chromatic number");

        // Reset colors for the next algorithm
        colors = new int[n];

        exactChroNumber = exactAlg.branchAndBound(vertices, edges, 0, colors, upperBound);
        System.out.println("Using a branch and bound technique gives " + exactChroNumber + " as the exact chromatic number");

        // The LowerBoundAlgorithm and UpperBoundAlgorithm will be called here when implemented
        // Currently, they are placeholders with no functionality.
    }

    // Methods from ExactAlgorithm.java
    public int recursiveBacktracking(String[] vertices, String[][] edges, int upperBound, int[] colors) {
        int exactChroNumber = 0;

        for (int i = 0; i < colors.length; i++) {
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
        if (vertexIndex == vertices.length) {
            int currentMaxColor = getMaxColorUsed(colors);
            if (currentMaxColor < upperBound) {
                upperBound = currentMaxColor;
            }
            return upperBound;
        }

        for (int color = 1; color <= upperBound; color++) {
            if (isColorValid(vertices, edges, vertexIndex, colors, color)) {
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
        List<String> list = new ArrayList<>();
        for (String[] edge : edges) {
            if (edge[0].equals(vertex)) {
                list.add(edge[1]);
            }
            if (edge[1].equals(vertex)) {
                list.add(edge[0]);
            }
        }

        return list.toArray(new String[0]);
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

    public void lowerBoundMethod() {
        // Placeholder for lower bound algorithm implementation
    }

    public void upperBoundMethod() {
        // Placeholder for upper bound algorithm implementation
    }
}

