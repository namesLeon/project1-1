package App.Algorithms;

import java.util.*;

public class RandomGraphGenerator {

    private String[] vertices;
    private String[][] edges;

    public RandomGraphGenerator(int numVertices, int numEdges) {
        if (numEdges < numVertices - 1) {
            throw new IllegalArgumentException("Number of edges must be at least numVertices - 1 to ensure connectivity.");
        }
        if (numEdges > numVertices * (numVertices - 1) / 2) {
            throw new IllegalArgumentException("Too many edges for the number of vertices.");
        }

        vertices = new String[numVertices];
        List<String[]> edgeList = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            vertices[i] = String.valueOf(i + 1);
        }

        // Ensure connectivity by creating a spanning tree
        List<String> vertexList = new ArrayList<>(Arrays.asList(vertices));
        Collections.shuffle(vertexList);
        Random random = new Random();

        for (int i = 1; i < vertexList.size(); i++) {
            String v1 = vertexList.get(i);
            // Randomly connect to any of the previously connected vertices
            String v2 = vertexList.get(random.nextInt(i));
            edgeList.add(new String[]{v1, v2});
        }

        Set<String> edgeSet = new HashSet<>();
        for (String[] edge : edgeList) {
            String edgeStr = edge[0] + "-" + edge[1];
            String reverseEdgeStr = edge[1] + "-" + edge[0];
            edgeSet.add(edgeStr);
            edgeSet.add(reverseEdgeStr); // Store both directions to prevent duplicates
        }

        int edgeCount = edgeList.size();

        // Add remaining edges randomly
        while (edgeCount < numEdges) {
            int index1 = random.nextInt(numVertices);
            int index2 = random.nextInt(numVertices);

            if (index1 != index2) {
                String v1 = vertices[index1];
                String v2 = vertices[index2];
                String edgeStr = v1 + "-" + v2;
                String reverseEdgeStr = v2 + "-" + v1;

                if (!edgeSet.contains(edgeStr) && !edgeSet.contains(reverseEdgeStr)) {
                    edgeSet.add(edgeStr);
                    edgeSet.add(reverseEdgeStr);
                    edgeList.add(new String[]{v1, v2});
                    edgeCount++;
                }
            }
        }

        // Convert edgeList to edges array
        edges = new String[edgeList.size()][2];
        for (int i = 0; i < edgeList.size(); i++) {
            edges[i][0] = edgeList.get(i)[0];
            edges[i][1] = edgeList.get(i)[1];
        }
    }

    public String[] getVertices() {
        return vertices;
    }

    public String[][] getEdges() {
        return edges;
    }
    public String[][] getEdgesNormalized() {
        String[][] normalizedEdges = new String[edges.length][2];
        for (int i=0;i<edges.length;i++) {
            normalizedEdges[i][0] = String.valueOf(Integer.parseInt(edges[i][0])); 
            normalizedEdges[i][1] = String.valueOf(Integer.parseInt(edges[i][1]));
        }
        return normalizedEdges;
    }
}
