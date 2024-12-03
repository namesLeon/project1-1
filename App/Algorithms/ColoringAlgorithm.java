package App.Algorithms;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import App.GUI.GraphVisualizer;

public class ColoringAlgorithm {
    private Map<String, Color> vertexColors;

    public ColoringAlgorithm() {
        vertexColors = new HashMap<>();
    }

    // Set the vertex color
    public void setColor(String vertex, Color color) {
        vertexColors.put(vertex, color);
    }

    // Get the color of the vertex
    public Color getColor(String vertex) {
        return vertexColors.getOrDefault(vertex, Color.WHITE);
    }

    // Clear All Colors
    public void clearColors() {
        vertexColors.clear();
    }

    // Check if the color of the current vertex is legal
    public boolean isValidColor(String vertex, String[][] edges, Color color) {
        for (String[] edge : edges) {
            if (edge[0].equals(vertex) && color.equals(vertexColors.get(edge[1]))) {
                return false;
            }
            if (edge[1].equals(vertex) && color.equals(vertexColors.get(edge[0]))) {
                return false;
            }
        }
        return true;
    }
}
