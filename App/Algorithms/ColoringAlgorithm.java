package App.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import App.GUI.GraphVisualizer;
import java.util.Set;

public class ColoringAlgorithm {

    private static final Color[] PREDEFINED_COLORS = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE
    };
    private static final String[] PREDEFINED_COLOR_NAMES = {
        "Red", "Green", "Blue", "Yellow", "Orange"
    };

    private static final int MIN_COLOR_DISTANCE = 50;

    private List<Color> colors;
    private List<String> colorNames;
    

    private int minColorDistance;

    public ColoringAlgorithm() {
        colors = new ArrayList<>();
        colorNames = new ArrayList<>();
        for (int i = 0; i < PREDEFINED_COLORS.length; i++) {
            colors.add(PREDEFINED_COLORS[i]);
            colorNames.add(PREDEFINED_COLOR_NAMES[i]);
        }
    }

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
                int generatedColorCount = getGeneratedColorCount() + 1;
                String colorName = "Generated Color " + generatedColorCount;
                colorNames.add(colorName);
                return newColor;
            }
            attempts++;
        }
        Color fallbackColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        colors.add(fallbackColor);
        int generatedColorCount = getGeneratedColorCount() + 1;
        String colorName = "Generated Color " + generatedColorCount;
        colorNames.add(colorName);
        return fallbackColor;
    }

    private int calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return (int) Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

    private int getGeneratedColorCount() {
        return colorNames.size() - PREDEFINED_COLOR_NAMES.length;
    }

    public List<Color> getAllColors() {
        return new ArrayList<>(colors);
    }

    public List<String> getColorNames() {
        return new ArrayList<>(colorNames);
    }

    public String getColorName(Color color) {
        int index = colors.indexOf(color);
        if (index != -1) {
            return colorNames.get(index);
        }
        return "Unknown Color";
    }
    public void clearColors() {
        colors.clear();
    }

    public String getColor(int index) {
        if (index < 0 || index >= colors.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return colors.get(index);
    }
    
    public int getColorCount() {
        return colors.size();
    }

    public String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }    
}