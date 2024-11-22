package App.Algorithms;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoringAlgorithm {

    private static final int COLOR_DIFFERENCE_THRESHOLD = 50; // Minimum RGB difference for distinct colors
    private List<Color> generatedColors; // List to store generated colors

    // Constructor
    public ColoringAlgorithm() {
        generatedColors = new ArrayList<>();
    }

    // Method to generate a distinct random color
    public Color generateDistinctColor() {
        Random random = new Random();
        Color newColor;
        boolean isDistinct;
        do {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            newColor = new Color(red, green, blue);

            // Check if this color is distinct from the previously generated ones
            isDistinct = true;
            for (Color existingColor : generatedColors) {
                if (calculateColorDifference(newColor, existingColor) < COLOR_DIFFERENCE_THRESHOLD) {
                    isDistinct = false;
                    break;
                }
            }
        } while (!isDistinct);

        // Add the new color to the list of generated colors
        generatedColors.add(newColor);
        return newColor;
    }

    // Helper method to calculate the difference between two colors
    private int calculateColorDifference(Color c1, Color c2) {
        int redDiff = Math.abs(c1.getRed() - c2.getRed());
        int greenDiff = Math.abs(c1.getGreen() - c2.getGreen());
        int blueDiff = Math.abs(c1.getBlue() - c2.getBlue());
        return redDiff + greenDiff + blueDiff;
    }

    // Method to get a color by index
    public Color getColorByIndex(int index) {
        // Generate new colors if the requested index exceeds the number of generated colors
        while (index >= generatedColors.size()) {
            generateDistinctColor();
        }
        return generatedColors.get(index);
    }


}



//generate random colors (an easy way would be using rgb numbers)
//check whether or not these colors are to similar to already existing colors (looking at rgb values and checking to see if the minimum diference is met)
//store the colors in a Color[]
//return the color when your method is called with a number