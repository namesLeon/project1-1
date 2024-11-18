package App.Algorithms;

import java.util.*;

public class RandomGraphGenerator {

    private String[] vertices;
    private String[][] edges;

    public RandomGraphGenerator(int numVertices, int numEdges) {
        vertices = new String[numVertices];
        edges = new String[numEdges][2];

        for (int i = 0; i < numVertices; i++) {
            vertices[i] = String.valueOf(i + 1);
        }

        Set<String> edgeSet = new HashSet<>();
        Random random = new Random();
        int edgeCount = 0;

        while (edgeCount < numEdges) {
            int vertex1 = random.nextInt(numVertices);
            int vertex2 = random.nextInt(numVertices);

            if (vertex1 != vertex2) {
                String edge = vertices[vertex1] + "-" + vertices[vertex2];
                String reverseEdge = vertices[vertex2] + "-" + vertices[vertex1];

                if (!edgeSet.contains(edge) && !edgeSet.contains(reverseEdge)) {
                    edgeSet.add(edge);
                    edges[edgeCount][0] = vertices[vertex1];
                    edges[edgeCount][1] = vertices[vertex2];
                    edgeCount++;
                }
            }
        }
    }

    public String[] getVertices() {
        return vertices;
    }

    public String[][] getEdges() {
        return edges;
    }

    public void printGraph() {
        System.out.println("Vertices:");
        System.out.println(Arrays.toString(vertices));

        System.out.println("Edges:");
        for (String[] edge : edges) {
            System.out.println(Arrays.toString(edge));
        }
    }
}

