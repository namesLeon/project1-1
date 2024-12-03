package App.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import App.GUI.GraphVisualizer;

public class ColoringAlgorithm {

    // Predefined colors
    private static final Color[] PREDEFINED_COLORS = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE
    };

    // Minimum color distance to ensure new colors are distinct
    private static final int MIN_COLOR_DISTANCE = 50;

    // List to store the colors generated
    private List<Color> colors;

    // Constructor
    public ColoringAlgorithm() {
        colors = new ArrayList<>();
        // Initialize with predefined colors
        for (Color color : PREDEFINED_COLORS) {
            colors.add(color);
        }
    }

    // Method to generate a new random color
    public Color addNewRandomColor() {
        Random rand = new Random();
        int attempts = 0;
        while (attempts < 1000) {
            int r = rand.nextInt(256);
            int g = rand.nextInt(256);
            int b = rand.nextInt(256);
            Color newColor = new Color(r, g, b);

            boolean isDistinct = true;
            for (Color existingColor : colors) {
                if (calculateColorDistance(existingColor, newColor) < MIN_COLOR_DISTANCE) {
                    isDistinct = false;
                    break;
                }
            }

            if (isDistinct) {
                colors.add(newColor);
                return newColor;
            }
            attempts++;
        }
        // If a distinct color wasn't found, return a random color anyway
        Color fallbackColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        colors.add(fallbackColor);
        return fallbackColor;
    }

    // Method to calculate color distance
    private int calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return (int) Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

    // Method to get the number of colors used
    public int getColorCount() {
        return colors.size();
    }

    // Method to get all colors
    public List<Color> getAllColors() {
        return new ArrayList<>(colors);
    }
    public void clearColors() {
        colors.clear();
    }
    // Method to get a color by its index in the list
    public Color getColor(int index) {
        if (index < 0 || index >= colors.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return colors.get(index);
    }
        // Method to assign a new color to a vertex (assuming GraphVisualizer calls this)
    public void assignColorToVertex(String vertex, GraphVisualizer visualizer) {
        Color newColor = addNewRandomColor();
        visualizer.setVertexColor(vertex, newColor); // Set the color for the vertex
    }
}
