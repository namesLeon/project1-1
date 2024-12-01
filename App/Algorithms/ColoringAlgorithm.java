package App.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoringAlgorithm {

    // Predefined colors: Red, Green, Blue, Yellow, Orange
    private static final Color[] PREDEFINED_COLORS = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE
    };

    // Minimum color distance to ensure random colors are not too similar to existing ones
    private static final int MIN_COLOR_DISTANCE = 50;

    // List to store the colors generated
    private List<Color> colors;

    private int minColorDistance;

    // Constructor to initialize the list of colors
    public ColoringAlgorithm() {
        this.minColorDistance = minColorDistance;
        colors = new ArrayList<>();
    }

    // Method to generate a random color
    private Color generateRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256); // Red value between 0 and 255
        int g = rand.nextInt(256); // Green value between 0 and 255
        int b = rand.nextInt(256); // Blue value between 0 and 255
        return new Color(r, g, b); // Return a new Color object
    }

    // Method to calculate the Euclidean distance between two colors
    private int calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return (int) Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

    // Method to generate and add a new random color to the list, ensuring it's not too similar to existing ones
    public Color addNewRandomColor() {
        int attempts = 0;
        while (attempts < 1000) {
            Color newColor = generateRandomColor(); // Declare inside the loop
            boolean isSimilar = false;
            
            // Check if the new color is too similar to any of the existing colors
            for (Color existingColor : colors) {
                if (calculateColorDistance(existingColor, newColor) < MIN_COLOR_DISTANCE) {
                    isSimilar = true;
                    break;
                }
            }
            
            if (!isSimilar) {
                colors.add(newColor); // Add the valid color to the list
                return newColor; // Return the new color
            }
            attempts++;
        }
        throw new RuntimeException("Could not generate a sufficiently distinct color.");
    }
    

    // Method to add a predefined color by index
    public Color addPredefinedColor(int index) {
        if (index >= 0 && index < PREDEFINED_COLORS.length) {
            Color selectedColor = PREDEFINED_COLORS[index];
            colors.add(selectedColor);
            return selectedColor;
        } else {
            return null; // Return null if the index is out of bounds
        }
    }

    // Method to get a color by its index in the list
    public Color getColor(int index) {
        if (index < 0 || index >= colors.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return colors.get(index);
    }
    

    // Method to get the total number of colors generated
    public int getColorCount() {
        return colors.size();
    }

    public List<Color> getAllColors() {
        return new ArrayList<>(colors); // Return a copy to prevent external modification
    }

    public void clearColors() {
        colors.clear();
    }

    public String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }    
}

