package App.Algorithms;


import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColoringAlgorithm {

    private Map<String, Color> vertexColors;  // Stores the assigned colors for each vertex
    private String[] vertices;               // List of vertices
    private String[][] edges;                // List of edges
    private Color[] availableColors;         // Array of available colors for coloring

    // Constructor
    public ColoringAlgorithm(String[] vertices, String[][] edges) {
        this.vertices = vertices;
        this.edges = edges;

        // Initialize vertexColors
        this.vertexColors = new HashMap<>();

        // Define a set of colors for use (you can extend this if needed)
        this.availableColors = new Color[]{
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
            Color.CYAN, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY, Color.DARK_GRAY
        };
    }

    /**
     * Executes the graph coloring algorithm.
     * Assigns colors to vertices such that no two adjacent vertices have the same color.
     */
    public void executeColoring() {
        for (String vertex : vertices) {
            Set<Color> usedColors = getColorsOfAdjacentVertices(vertex); // Get colors used by adjacent vertices
            Color chosenColor = chooseAvailableColor(usedColors);        // Choose a color not in the used set
            vertexColors.put(vertex, chosenColor);                       // Assign the chosen color
        }
    }

    /**
     * Retrieves the colors of all vertices adjacent to the given vertex.
     *
     * @param vertex The vertex for which to retrieve adjacent colors
     * @return A set of colors used by adjacent vertices
     */
    private Set<Color> getColorsOfAdjacentVertices(String vertex) {
        Set<Color> usedColors = new HashSet<>();
        for (String[] edge : edges) {
            if (edge[0].equals(vertex)) {
                Color neighborColor = vertexColors.get(edge[1]);
                if (neighborColor != null) {
                    usedColors.add(neighborColor);
                }
            } else if (edge[1].equals(vertex)) {
                Color neighborColor = vertexColors.get(edge[0]);
                if (neighborColor != null) {
                    usedColors.add(neighborColor);
                }
            }
        }
        return usedColors;
    }

    /**
     * Chooses an available color that is not in the set of used colors.
     *
     * @param usedColors A set of colors already used by adjacent vertices
     * @return A color that can be used for the current vertex
     */
    private Color chooseAvailableColor(Set<Color> usedColors) {
        for (Color color : availableColors) {
            if (!usedColors.contains(color)) {
                return color; // Return the first color that isn't already used
            }
        }
        // If all colors are used (unlikely given the size of availableColors), fallback to a default
        return Color.BLACK; // Default color in case of unexpected issues
    }

    /**
     * Returns the color assigned to a given vertex.
     *
     * @param vertex The vertex whose color is to be retrieved
     * @return The color assigned to the vertex
     */
    public Color getVertexColor(String vertex) {
        return vertexColors.getOrDefault(vertex, Color.WHITE); // Default to WHITE if not yet assigned
    }

    /**
     * Main method for testing the coloring algorithm.
     */
    public static void main(String[] args) {
        // Example graph: vertices and edges
        String[] vertices = {"A", "B", "C", "D", "E"};
        String[][] edges = {
            {"A", "B"}, {"A", "C"}, {"B", "C"}, {"B", "D"}, {"C", "E"}
        };

        // Instantiate the algorithm and execute coloring
        ColoringAlgorithm algorithm = new ColoringAlgorithm(vertices, edges);
        algorithm.executeColoring();

        // Output the colors assigned to each vertex
        for (String vertex : vertices) {
            Color color = algorithm.getVertexColor(vertex);
            System.out.println("Vertex " + vertex + " is colored with: " + color.toString());
        }
    }

    /**
     * Returns the entire map of vertex-color assignments (for external use).
     *
     * @return A map of vertex-color assignments
     */
    public Map<String, Color> getVertexColors() {
        return vertexColors;
    }
}




//generate random colors (an easy way would be using rgb numbers)
//check whether or not these colors are to similar to already existing colors (looking at rgb values and checking to see if the minimum diference is met)
//store the colors in a Color[]
//return the color when your method is called with a number