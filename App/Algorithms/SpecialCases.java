package App.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
* note from ilgaz
* this class detects special case graphs for which the chromatic number
* can be determined quickly (like tree => 2 colors, bipartite => 2 colors,
* complete => number of vertices etc)
*
* HOW TO USE:
*   int result = SpecialCaseChecker.detectSpecialCase(vertices, edges);
*   if (result != -1) {
*       'result' is the chromatic number for this special case
*   } else {
*        It's not a special case. Use your expensive algorithm.
*   }
*/
public class SpecialCases {

    /**
     * main entry point to detect if the graph is a known special case
     * returns:
     *   -1 if no special case is detected and expensive algorithm
     *      is called
     *   otherwise, the exact chromatic number for that special case
     */


    public static boolean isComplete(String[] vertices, String[][] edges) {

        /**
         * A complete graph is essentially a graph where every vertex
         * are connected by a distinct edge.
         * 
         * For a graph with n vertices,
         * a complete graph must have n(n-1)/2 edges for n vertices
         * 
         * If we find this, the chromatic number for a complete graph is just n, 
         * the number of vertices.
         */


        int n = vertices.length;
        // quick check numerically
        // If # edges < n*(n-1)/2, we can skip deeper checks.

        int requiredEdges = n * (n - 1) / 2;

        if (edges.length != requiredEdges) {
            return false;
        }

        // for further safety we can keep track in a set of pairs
        // to ensure that every pair appears only once

        Set<String> edgeSet = new HashSet<>();

            for (String[] e : edges) { 
            // e[0], e[1] are vertex labels. We'll use them as "1-2", "2-1", etc
            String v1 = e[0];
            String v2 = e[1];
            if (v1.equals(v2)) {
                // A self-loop would break the definition of a simple complete graph
                return false;
                }

            // building a unique string so that the order doesn't matter
            // like edge (1,2) => "1-2"

            try{
                int a = Integer.parseInt(v1);
                int b = Integer.parseInt(v2);
            } catch(NumberFormatException ex){
                System.out.println("The graph input is malformed.");
                return false;
            } 

            int minAB = Math.min(a, b);
            int maxAB = Math.max(a, b);
            edgeSet.add(minAB + "-" + maxAB);
        }

        // now every pair i-j with (i<j) from 1...n should appear in that set

        for (int i = 1; i<=n; i++) {
            for(int j = i+1; j<=n; j++) {
                String pair = i + "-" + j;
                if(!edgeSet.contains(pair)) {
                    return false;
                }
            }
            
        }

        return true;

    }

    public static boolean isBipartite(String[] vertices, String[][] edges) {
        int n = vertices.length;
        int[] color = new int[n];
        Arrays.fill(color, -1); // -1 means "uncolored"
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
            adj.add(new ArrayList<>());
        }

        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        // Attempt coloring from each unvisited vertex.
        for (int start = 0; start < n; start++) {
            if (color[start] == -1) {
                color[start] = 0; // Assign first color arbitrarily
                Queue<Integer> queue = new LinkedList<>();
                queue.add(start);

                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    for (int v : adj.get(u)) {
                        if (color[v] == -1) {
                            // Assign opposite color to the neighbor
                            color[v] = 1 - color[u];
                            queue.add(v);
                        } else if (color[v] == color[u]) {
                            // same color on 2 neighbours
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
