package App.Algorithms;

import java.util.*;

public class LowerBoundAlgorithm {

    // Optimized Largest Clique Bound using Bron-Kerbosch Algorithm
    public static int largestCliqueBound(int[][] adjMatrix) {
        Set<Integer> allNodes = new HashSet<>();
        for (int i = 0; i < adjMatrix.length; i++) allNodes.add(i);
        return bronKerbosch(adjMatrix, new HashSet<>(), allNodes, new HashSet<>());
    }

    // Bron-Kerbosch recursive algorithm for finding the maximum clique size
    private static int bronKerbosch(int[][] adjMatrix, Set<Integer> R, Set<Integer> P, Set<Integer> X) {
        if (P.isEmpty() && X.isEmpty()) return R.size();
        
        int maxSize = 0;
        for (Integer v : new HashSet<>(P)) {
            Set<Integer> neighbors = new HashSet<>();
            for (int i = 0; i < adjMatrix.length; i++) {
                if (adjMatrix[v][i] == 1) neighbors.add(i);
            }

            Set<Integer> newR = new HashSet<>(R); newR.add(v);
            Set<Integer> newP = new HashSet<>(P); newP.retainAll(neighbors);
            Set<Integer> newX = new HashSet<>(X); newX.retainAll(neighbors);
            
            maxSize = Math.max(maxSize, bronKerbosch(adjMatrix, newR, newP, newX));
            P.remove(v); X.add(v);
        }
        return maxSize;
    }

    // Generate adjacency matrix from vertices and edges
    public static int[][] createAdjMatrix(String[] vertices, String[][] edges) {
        int n = vertices.length;
        Map<String, Integer> vertexIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            vertexIndexMap.put(vertices[i], i);
        }

        int[][] adjMatrix = new int[n][n];
        for (String[] edge : edges) {
            int u = vertexIndexMap.get(edge[0]);
            int v = vertexIndexMap.get(edge[1]);
            adjMatrix[u][v] = 1;
            adjMatrix[v][u] = 1; // Undirected graph
        }
        return adjMatrix;
    }

    public static int LowerBound(String[] vertices, String[][] edges) {
        int[][] adjMatrix = createAdjMatrix(vertices, edges);
        int lowerBound = largestCliqueBound(adjMatrix);

        return lowerBound;
    }
}