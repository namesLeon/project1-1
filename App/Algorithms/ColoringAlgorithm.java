package App.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoringAlgorithm {

    // Minimum color distance to ensure random colors are not too similar to existing ones
    private static final int MIN_COLOR_DISTANCE = 50;

    // List to store the colors generated
    private List<Color> colors;

    // Constructor to initialize the list of colors
    public ColoringAlgorithm() {
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
        Color newColor = null;  // Declare the newColor variable here
        boolean isSimilar = true;

        // Keep generating random colors until we find one that's sufficiently distinct
        while (isSimilar) {
            newColor = generateRandomColor();
            isSimilar = false;
            
            // Check if the new color is too similar to any of the existing colors
            for (Color existingColor : colors) {
                if (calculateColorDistance(existingColor, newColor) < MIN_COLOR_DISTANCE) {
                    isSimilar = true;  // If too similar, stop and regenerate
                    break;
                }
            }
        }
        
        // Add the valid color to the list and return it
        colors.add(newColor);
        return newColor;
    }

    // Method to get a color by its index in the list
    public Color getColor(int index) {
        if (index >= 0 && index < colors.size()) {
            return colors.get(index);
        } else {
            return null;  // Return null if index is out of bounds
        }
    }

    // Method to get the total number of colors generated
    public int getColorCount() {
        return colors.size();
    }
}





//generate random colors (an easy way would be using rgb numbers)
//check whether or not these colors are to similar to already existing colors (looking at rgb values and checking to see if the minimum diference is met)
//store the colors in a Color[]
//return the color when your method is called with a number