package App.Algorithms;

import java.util.ArrayList;
import java.util.List;

public class ExactAlgorithm {
    private List<List<Integer>> adjacencyList;
    private static final long TIME_LIMIT_MS = 30000; // 30 seconds in milliseconds

    public ExactAlgorithm(String[] vertices, String[][] edges) {
        this.adjacencyList = buildAdjacencyList(vertices, edges);
    }

    public int branchAndBound(String[] vertices, String[][] edges, int[] colors, int upperBound, int lowerBound) {
        long startTime = System.currentTimeMillis(); // Store the start time
        return branchAndBoundRecursive(vertices, edges, colors, upperBound, lowerBound, startTime);
    }

    private int branchAndBoundRecursive(String[] vertices, String[][] edges, int[] colors, int upperBound, int lowerBound, long startTime) {
        if (System.currentTimeMillis() - startTime > TIME_LIMIT_MS) {
            return -1;
        }

        int nextVertex = selectNextVertexDSATUR(colors, adjacencyList);

        if (nextVertex == -1) {
            int currentMaxColor = getMaxColorUsed(colors);
            if (currentMaxColor < upperBound) {
                upperBound = currentMaxColor;
            }
            return upperBound;
        }

        List<Integer> availableColors = getValidColors(nextVertex, colors, upperBound);

        for (int color : availableColors) {
            colors[nextVertex] = color;
            int currentMaxColor = getMaxColorUsed(colors);

            if (currentMaxColor >= upperBound) {
                colors[nextVertex] = 0;
                continue;
            }

            upperBound = branchAndBoundRecursive(vertices, edges, colors, upperBound, lowerBound, startTime);

            if (upperBound == -1) {
                return -1;
            }

            if (upperBound == lowerBound) {
                return lowerBound;
            }

            colors[nextVertex] = 0;
        }

        return upperBound;
    }

    private List<List<Integer>> buildAdjacencyList(String[] vertices, String[][] edges) {
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (String[] edge : edges) {
            int u = Integer.parseInt(edge[0]) - 1;
            int v = Integer.parseInt(edge[1]) - 1;
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);
        }

        return adjacencyList;
    }

    public int selectNextVertexDSATUR(int[] colors, List<List<Integer>> adjacencyList) {
        int maxSaturation = -1;
        int selectedVertex = -1;

        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == 0) {
                int saturation = computeSaturation(i, colors, adjacencyList);
                int degree = adjacencyList.get(i).size();  // or number of colored neighbors
                if (saturation > maxSaturation) {
                    maxSaturation = saturation;
                    selectedVertex = i;
                } else if (saturation == maxSaturation) {
                    // TIE-BREAK: check degree
                    int selectedVertexDegree = adjacencyList.get(selectedVertex).size();
                    if (degree > selectedVertexDegree) {
                        selectedVertex = i;
                    }
                }
            }
        }

        return selectedVertex;
    }

    private int computeSaturation(int vertex, int[] colors, List<List<Integer>> adjacencyList) {
        boolean[] adjacentColors = new boolean[colors.length + 1];

        for (int neighbor : adjacencyList.get(vertex)) {
            if (colors[neighbor] != 0) {
                adjacentColors[colors[neighbor]] = true;
            }
        }

        int saturation = 0;
        for (boolean hasColor : adjacentColors) {
            if (hasColor) {
                saturation++;
            }
        }

        return saturation;
    }

    public List<Integer> getValidColors(int vertexIndex, int[] colors, int upperBound) {
        boolean[] usedColors = new boolean[upperBound + 1];

        for (int neighbor : adjacencyList.get(vertexIndex)) {
            if (colors[neighbor] > 0) {
                usedColors[colors[neighbor]] = true;
            }
        }

        List<Integer> validColors = new ArrayList<>();
        for (int color = 1; color <= upperBound; color++) {
            if (!usedColors[color]) {
                validColors.add(color);
            }
        }

        return validColors;
    }

    public int getMaxColorUsed(int[] colors) {
        int maxColorUsed = 0;
        for (int color : colors) {
            if (color > maxColorUsed) {
                maxColorUsed = color;
            }
        }
        return maxColorUsed;
    }
}
