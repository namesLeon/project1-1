import java.io.*;
import java.util.*;

class ColEdge {
    int u;
    int v;
}

class ExactAlgorithm {
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
        if (vertexIndex == (vertices.length)) {
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

    private boolean isColorValid(String[] vertices, String[][] edges, int vertexIndex, int[] colors, int color) {
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

    private String[] getNeighbours(String vertex, String[][] edges) {
        List<String> list = new ArrayList<>();
        for (String[] edge : edges) {
            if (edge[0].equals(vertex)) {
                list.add(edge[1]);
            } else if (edge[1].equals(vertex)) {
                list.add(edge[0]);
            }
        }
        return list.toArray(new String[0]);
    }

    private int getMaxColorUsed(int[] colors) {
        int maxColorUsed = 0;
        for (int color : colors) {
            if (color > maxColorUsed) {
                maxColorUsed = color;
            }
        }
        return maxColorUsed;
    }
}

// Placeholder for Lower Bound Algorithm
class LowerBoundAlgorithm {
    // Add your algorithm implementation here if needed
}

// Placeholder for Upper Bound Algorithm
class UpperBoundAlgorithm {
    // Add your algorithm implementation here if needed
}

public class ReadGraph {

    public final static boolean DEBUG = true;
    public final static String COMMENT = "//";

    private String[] vertices;   // Instance variable for vertices
    private String[][] edges;    // Instance variable for edges

    // Read graph from file
    public void readGraphFromFile(String inputfile) {
        boolean seen[] = null;

        int n = -1; // Number of vertices
        int m = -1; // Number of edges
        ColEdge[] e = null;

        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            String record;

            while ((record = br.readLine()) != null) {
                if (record.startsWith(COMMENT)) continue;
                break;
            }

            if (record.startsWith("VERTICES = ")) {
                n = Integer.parseInt(record.substring(11));
                if (DEBUG) System.out.println(COMMENT + " Number of vertices = " + n);
            }

            seen = new boolean[n + 1];

            record = br.readLine();
            if (record.startsWith("EDGES = ")) {
                m = Integer.parseInt(record.substring(8));
                if (DEBUG) System.out.println(COMMENT + " Expected number of edges = " + m);
            }

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

        vertices = new String[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = String.valueOf(i + 1);
        }

        edges = new String[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = String.valueOf(e[i].u);
            edges[i][1] = String.valueOf(e[i].v);
        }
    }

    // Get vertices array
    public String[] getVertices() {
        return vertices;
    }

    // Get edges array
    public String[][] getEdges() {
        return edges;
    }

    public static void main(String[] args) {
        String inputFile = "path/to/your/inputfile.txt";

        // Instantiate ReadGraph and call its method to read the graph
        ReadGraph readGraph = new ReadGraph();
        readGraph.readGraphFromFile(inputFile);

        // Get the vertices and edges
        String[] vertices = readGraph.getVertices();
        String[][] edges = readGraph.getEdges();

        // Now proceed with calling the algorithms using the arrays obtained from ReadGraph
        int upperBound = vertices.length; // number of vertices as an upper bound
        int[] colors = new int[vertices.length];

        ExactAlgorithm exactAlg = new ExactAlgorithm();
        LowerBoundAlgorithm lowBoundAlg = new LowerBoundAlgorithm();
        UpperBoundAlgorithm uppBoundAlg = new UpperBoundAlgorithm();

        int exactChroNumber = exactAlg.recursiveBacktracking(vertices, edges, upperBound, colors);
        System.out.println("Using recursive backtracking gives " + exactChroNumber + " as the exact chromatic number");

        // Reset colors for the next algorithm
        colors = new int[vertices.length];

        exactChroNumber = exactAlg.branchAndBound(vertices, edges, 0, colors, upperBound);
        System.out.println("Using a branching and bounding technique gives " + exactChroNumber + " as the exact chromatic number");
    }
}
