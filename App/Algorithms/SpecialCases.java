package App.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

        /* 
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

            int a = 0;
            int b = 0;

            try{
                a = Integer.parseInt(v1);
                b = Integer.parseInt(v2);
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
        */
        else{
            return true;
        }

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

    public static boolean isCycle(String[] vertices, String[][] edges) {
        int n = vertices.length;
        if (n < 3) {
            // A cyclic graph requires at least 3 vertices
            return false;
        }
    
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    
        // Check each edge and fill in the adjacency list
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1; // Convert to 0-based index
            int v = Integer.parseInt(edge[1]) - 1;
            if (u == v) {
                // Self-loops break the cyclic graph definition
                return false;
            }
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
    
        // Check that the degree of each vertex is exactly 2
        for (int i = 0; i < n; i++) {
            if (adj.get(i).size() != 2) {
                return false;
            }
        }
    
        // Use DFS（Depth First Search） to check if it is a connected cycle
        boolean[] visited = new boolean[n];
        int start = 0;
        int parent = -1;
        int visitedCount = 0;
    
        // DFS
        if (!dfsCycle(adj, start, visited, parent)) {
            return false;
        }
    
        // Check if all nodes are visited
        for (boolean v : visited) {
            if (v) {
                visitedCount++;
            }
        }
    
        // If connected and the number of visited vertices is equal to the number of vertices in the graph
        return visitedCount == n;
    }
    
    private static boolean dfsCycle(List<List<Integer>> adj, int node, boolean[] visited, int parent) {
        visited[node] = true;
    
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                // If not visited, recursively check neighbors
                if (!dfsCycle(adj, neighbor, visited, node)) {
                    return false;
                }
            } else if (neighbor != parent) {
                // If it has been visited and is not the parent node, it means there is a duplicate edge (non-loop edge)
                return false;
            }
        }
        return true;
    }

    public static boolean isStarGraph(String[] vertices, String[][] edges) {
        int n = vertices.length;
        int[] degree = new int[n];
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            degree[u]++;
            degree[v]++;
        }
    
        int centerCount = 0, leafCount = 0;
        for (int d : degree) {
            if (d == n - 1) centerCount++;
            else if (d == 1) leafCount++;
            else return false;
        }
        return centerCount == 1 && leafCount == n - 1;
    }

    public static boolean isPathGraph(String[] vertices, String[][] edges) {
        int n = vertices.length;
        int[] degree = new int[n];
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            degree[u]++;
            degree[v]++;
        }
    
        int endCount = 0;
        for (int d : degree) {
            if (d == 1) endCount++;
            else if (d != 2) return false;
        }
        return endCount == 2; // Path graph must have exactly 2 endpoints
    }
    
    public static boolean isTree(String[] vertices, String[][] edges) {
        int n = vertices.length;
        if (edges.length != n - 1) return false; // Tree must have n-1 edges
    
        boolean[] visited = new boolean[n];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
    
        if (!dfsTree(adj, 0, visited, -1)) return false; // Detect cycles
    
        // Check if all vertices are visited
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }
    
    private static boolean dfsTree(List<List<Integer>> adj, int node, boolean[] visited, int parent) {
        visited[node] = true;
    
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                if (!dfsTree(adj, neighbor, visited, node)) return false;
            } else if (neighbor != parent) {
                return false; // Cycle detected
            }
        }
        return true;
    }
    
    public static boolean isWheelGraph(String[] vertices, String[][] edges) {
        int n = vertices.length;
        if (n < 4) return false;
    
        int[] degree = new int[n];
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            degree[u]++;
            degree[v]++;
        }
    
        int center = -1;
        for (int i = 0; i < n; i++) {
            if (degree[i] == n - 1) {
                if (center != -1) return false; // Only one center allowed
                center = i;
            }
        }
        if (center == -1) return false;
    
        // Check if remaining vertices form a cycle
        List<String> cycleVertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i != center) cycleVertices.add(String.valueOf(i + 1));
        }
        return isCycle(cycleVertices.toArray(new String[0]), edges);
    }
    
    public static boolean isDisconnected(String[] vertices, String[][] edges) {
        int n = vertices.length;
        boolean[] visited = new boolean[n];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    
        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
    
        // Use BFS or DFS to traverse
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;
    
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }
    
        // Check if all vertices are visited
        for (boolean v : visited) {
            if (!v) return true; // Graph is disconnected
        }
        return false;
    }
    
    
}
