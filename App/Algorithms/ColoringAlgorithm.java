package App.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoringAlgorithm {

    private static final int COLOR_DIFFERENCE_THRESHOLD = 50; // Minimum RGB difference to avoid similarity
    private List<Color> colorList;

    public ColoringAlgorithm() {
        this.colorList = new ArrayList<>();
    }

    /**
     * Generates a random color, ensuring it's sufficiently different
     * from existing colors in the list.
     */
    public Color generateRandomColor() {
        Random random = new Random();
        Color newColor;
        boolean isTooSimilar;

        do {
            // Generate a random color
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            newColor = new Color(red, green, blue);

            // Check similarity with existing colors
            isTooSimilar = false;
            for (Color existingColor : colorList) {
                if (calculateColorDifference(existingColor, newColor) < COLOR_DIFFERENCE_THRESHOLD) {
                    isTooSimilar = true;
                    break;
                }
            }

        } while (isTooSimilar);

        // Add the new color to the list and return it
        colorList.add(newColor);
        return newColor;
    }

    /**
     * Returns a color from the list by index.
     * If the index is invalid, returns null.
     */
    public Color getColor(int index) {
        if (index >= 0 && index < colorList.size()) {
            return colorList.get(index);
        }
        return null; // Invalid index
    }

    /**
     * Calculates the difference between two colors based on their RGB values.
     */
    private int calculateColorDifference(Color c1, Color c2) {
        int redDiff = Math.abs(c1.getRed() - c2.getRed());
        int greenDiff = Math.abs(c1.getGreen() - c2.getGreen());
        int blueDiff = Math.abs(c1.getBlue() - c2.getBlue());
        return redDiff + greenDiff + blueDiff;
    }

    /**
     * Main method for testing the algorithm.
     */
    public static void main(String[] args) {
        ColoringAlgorithm algorithm = new ColoringAlgorithm();

        // Generate 10 random colors
        for (int i = 0; i < 10; i++) {
            Color color = algorithm.generateRandomColor();
            System.out.println("Generated Color " + i + ": " + color);
        }

        // Retrieve colors by index
        System.out.println("Color at index 2: " + algorithm.getColor(2));
        System.out.println("Color at index 5: " + algorithm.getColor(5));
    }
}





//generate random colors (an easy way would be using rgb numbers)
//check whether or not these colors are to similar to already existing colors (looking at rgb values and checking to see if the minimum diference is met)
//store the colors in a Color[]
//return the color when your method is called with a number