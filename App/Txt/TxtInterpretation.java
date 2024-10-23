package App.Txt;
import App.Txt.TxtInterpretation;
import java.io.*;

class ColEdge {
    int u;
    int v;
}

public class TxtInterpretation{

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

        // Initialize vertices array
        vertices = new String[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = String.valueOf(i + 1);
        }

        // Initialize edges matrix
        edges = new String[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = String.valueOf(e[i].u);
            edges[i][1] = String.valueOf(e[i].v);
        }
    }

    // Method to return vertices array
    public String[] getVertices() {
        return vertices;
    }

    // Method to return edges matrix
    public String[][] getEdges() {
        return edges;
    }
}
