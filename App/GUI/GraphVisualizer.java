package App.GUI;

import App.Algorithms.ColoringAlgorithm;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.*;


public class GraphVisualizer extends JPanel {

    private Map<String, Color> vertexColors; // new hashmap to keep track of each vertex color
    private String[] vertices; // List of vertices
    private String[][] edges;  // List of edges
    private Map<String, Point> vertexPositions; // Map to store vertex positions to avoid overlap
    private double zoomFactor = 1.0; // Default zoom level
    private int panX = 0; // Horizontal pan offset
    private int panY = 0; // Vertical pan offset
    @SuppressWarnings("unused")
    private String[] vertexOrder;
    private String highlightedVertex; // Added for highlighting
    private boolean showColoredGraph = true; // Default of the graph is uncolored
    private VertexClickListener vertexClickListener; // Listener for vertex clicks


    // Variables for dragging
    private String draggedVertex = null;
    private Point lastMousePosition;
    private boolean isDraggingVertex = false;
    private final int DRAG_THRESHOLD = 5; // Pixels

    private boolean restrictClicksToHighlightedVertex = false;
    private boolean isGraphColored = false;

    public void setRestrictClicksToHighlightedVertex(boolean restrict) {
        this.restrictClicksToHighlightedVertex = restrict;
    }

    
    public interface VertexClickListener {
        void vertexClicked(String vertex);
    }

    public void setVertexClickListener(VertexClickListener listener) {
        this.vertexClickListener = listener;
    }

    // Constructor to initialize the graph data
    public GraphVisualizer(String[] vertices, String[][] edges) {
        this.vertexColors = new HashMap<>();
        for (String vertex : vertices) {
            vertexColors.put(vertex, Color.WHITE);
        }
        this.vertices = vertices;
        this.edges = edges;
        this.vertexPositions = new HashMap<>();
        setPreferredSize(new Dimension(600, 600));
        generateVertexPositions();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int adjustedX = (int) ((clickPoint.x - panX) / zoomFactor);
                int adjustedY = (int) ((clickPoint.y - panY) / zoomFactor);
                Point adjustedClickPoint = new Point(adjustedX, adjustedY);
        
                draggedVertex = null;
        
                for (Map.Entry<String, Point> entry : vertexPositions.entrySet()) {
                    String vertex = entry.getKey();
                    Point vertexPos = entry.getValue();
                    int radius = 20;
        
                    if (vertexPos.distance(adjustedClickPoint) < radius) {
                        draggedVertex = vertex;
                        break;
                    }
                }
        
                lastMousePosition = adjustedClickPoint;
                isDraggingVertex = false;
            }
        
            @Override
            public void mouseReleased(MouseEvent e) {
                // It's a click, not a drag
                if (!isDraggingVertex && draggedVertex != null && vertexClickListener != null) {
                    if (!restrictClicksToHighlightedVertex || draggedVertex.equals(highlightedVertex)) {
                        vertexClickListener.vertexClicked(draggedVertex);
                    }
                }
                draggedVertex = null;
                isDraggingVertex = false;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedVertex != null) {
                    Point currentPoint = e.getPoint();
                    int adjustedX = (int) ((currentPoint.x - panX) / zoomFactor);
                    int adjustedY = (int) ((currentPoint.y - panY) / zoomFactor);
                    Point adjustedCurrentPoint = new Point(adjustedX, adjustedY);

                    if (!isDraggingVertex && lastMousePosition.distance(adjustedCurrentPoint) > DRAG_THRESHOLD) {
                        isDraggingVertex = true;
                    }

                    if (isDraggingVertex) {
                        vertexPositions.put(draggedVertex, adjustedCurrentPoint);
                        lastMousePosition = adjustedCurrentPoint;
                        repaint();
                    }
                }
            }
        });
    }

    public void recolorVertex(String vertex, Color color) {
        if (vertexColors.containsKey(vertex) || true) {  // Add to ensure vertex exists or add new
            vertexColors.put(vertex, color);
            repaint();  // Refresh visual representation
        }
    }

    
    public void autoColorGraph(Map<String, Color> coloring) {
        for (Map.Entry<String, Color> entry : coloring.entrySet()) {
            vertexColors.put(entry.getKey(), entry.getValue());
    }
        repaint();
}


public void clearVertexColors() {
    if (vertices == null) {
        return; // No vertices to clear
    }

    for (String vertex : vertices) {
        vertexColors.put(vertex, Color.WHITE); // Reset each vertex to white
    }
    isGraphColored = false; // Mark the graph as uncolored
    repaint();
}



    
    // Method to set a highlighted vertex
    public void highlightVertex(String vertex) {
        this.highlightedVertex = vertex;
        repaint();
    }

    public void setVertexOrder(String[] order){
        this.vertexOrder = order;
    }


    // Method to generate random positions for vertices, ensuring no overlap
    private void generateVertexPositions() {
        Random rand = new Random();
        int margin = 50; // Margin from the edges of the panel
        int radius = 30; // Radius of the vertex circles
        int maxAttempts = 1000; // Maximum number of attempts to find a non-overlapping position

        for (String vertex : vertices) {
            boolean positionFound = false;
            int attempts = 0;
            while (!positionFound && attempts < maxAttempts) {
                int x = margin + rand.nextInt(getWidth() - 2 * margin - radius);
                int y = margin + rand.nextInt(getHeight() - 2 * margin - radius);
                Point newPosition = new Point(x, y);

                boolean overlaps = false;
                for (Point p : vertexPositions.values()) {
                    if (p.distance(newPosition) < 2 * radius) {
                        overlaps = true;
                        break;
                    }
                }

                if (!overlaps) {
                    vertexPositions.put(vertex, newPosition);
                    positionFound = true;
                }
                attempts++;
            }

            if (!positionFound) {
                vertexPositions.put(vertex, new Point(0, 0));
            }
        }
    }

    // Override the paintComponent method to draw vertices and edges
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(panX, panY);
        g2d.scale(zoomFactor, zoomFactor);

        drawEdges(g2d);
        drawVertices(g2d);
    }


    public void autoColorGraphWithAlgorithm(ColoringAlgorithm coloringAlgorithm) {
        if (vertices == null || vertices.length == 0) {
            return; // No vertices to color
        }
    
        List<Color> availableColors = coloringAlgorithm.getAllColors();
        Map<String, Color> coloring = new HashMap<>();
    
        for (String vertex : vertices) {
            Color currentColor = vertexColors.getOrDefault(vertex, Color.WHITE);
    
            if (currentColor.equals(Color.WHITE)) {
                // Determine colors of adjacent vertices
                Set<Color> adjacentColors = new HashSet<>();
    
                for (String[] edge : edges) {
                    if (edge[0].equals(vertex)) {
                        adjacentColors.add(vertexColors.getOrDefault(edge[1], Color.WHITE));
                        adjacentColors.add(coloring.getOrDefault(edge[1], Color.WHITE));
                    } else if (edge[1].equals(vertex)) {
                        adjacentColors.add(vertexColors.getOrDefault(edge[0], Color.WHITE));
                        adjacentColors.add(coloring.getOrDefault(edge[0], Color.WHITE));
                    }
                }
    
                // Assign the first available color that is not in adjacentColors
                for (Color color : availableColors) {
                    if (!adjacentColors.contains(color)) {
                        coloring.put(vertex, color);
                        break;
                    }
                }
            }
        }
    
        // Apply the coloring to the vertices
        for (Map.Entry<String, Color> entry : coloring.entrySet()) {
            vertexColors.put(entry.getKey(), entry.getValue());
        }
    
        isGraphColored = true; // Mark the graph as colored
        repaint(); // Refresh the visual representation
    }
    
    private void drawVertices(Graphics2D g2d) {
        int radius = 20;
        for (String vertex : vertices) {
            Point pos = vertexPositions.get(vertex);
            if (pos == null) continue;

            int x = pos.x - radius / 2;
            int y = pos.y - radius / 2;

            Color vertexColor = showColoredGraph ? vertexColors.getOrDefault(vertex, Color.WHITE) : Color.WHITE;

            if (vertex.equals(highlightedVertex)) {
                // Draw a larger circle to highlight
                g2d.setColor(Color.CYAN);
                g2d.fillOval(x - 5, y - 5, radius + 10, radius + 10);
            }

            g2d.setColor(vertexColor);
            g2d.fillOval(x, y, radius, radius);

            g2d.setColor(Color.BLACK);
            g2d.drawString(vertex, x + radius / 2 - 5, y + radius / 2 + 5);
        }
    }




    // Method to set a vertex's color
    public void setVertexColor(String vertex, Color color) {
        vertexColors.put(vertex, color);
        repaint();
    }

    public void toggleGraphColoring(ColoringAlgorithm coloringAlgorithm) {
        if (isGraphColored) {
            clearVertexColors();
            isGraphColored = false;
        } else {
            autoColorGraphWithAlgorithm(coloringAlgorithm);
            isGraphColored = true;
        }
    }

    // Method to get a vertex's color
    public Color getVertexColor(String vertex) {
        return vertexColors.get(vertex);
    }

    // Method to get the edges
    public String[][] getEdges() {
        return edges;
    }

    private void drawEdges(Graphics2D g2d) {
        if (edges == null) return;
        g2d.setColor(Color.BLACK);
        for (String[] edge : edges) {
            Point pos1 = vertexPositions.get(edge[0]);
            Point pos2 = vertexPositions.get(edge[1]);

            if (pos1 != null && pos2 != null) {
                g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
            }
        }
    }

    public void updateGraph(String[] vertices, String[][] edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.vertexPositions.clear();
        generateVertexPositions();
        repaint();
    }

    public void zoomIn() {
        zoomFactor *= 1.2;
        repaint();
    }

    public void zoomOut() {
        zoomFactor /= 1.2;
        repaint();
    }

    // New method to get vertex count
    public int getVertexCount() {
        return vertices != null ? vertices.length : 0;
    }
    
    // Retrieve all distinct colors used in the graph
    public List<Color> getAllUsedColors() {
    return new ArrayList<>(new HashSet<>(vertexColors.values()));
    }

    public boolean areAllVerticesColored() {
        for (String vertex : vertices) {
            Color color = vertexColors.getOrDefault(vertex, Color.WHITE);
            if (color.equals(Color.WHITE)) {
                return false; // If any vertex is white, the graph is not fully colored
            }
        }
        return true; // All vertices are colored
    }
    
    public void applyMinimalEdgeCrossingAlgorithm(String[] vertices, String[][] edges) {
        Map<String, Point> newPositions = new HashMap<>();
        
        Random rand = new Random();
        int width = getWidth();
        int height = getHeight();
        
        // Initialize positions randomly or on a grid
        for (String vertex : vertices) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            newPositions.put(vertex, new Point(x, y));
        }
    
        // Simple force-directed layout
        for (int iteration = 0; iteration < 10000; iteration++) {  // 100 iterations for optimization
            for (String v1 : vertices) {
                Point position1 = newPositions.get(v1);
                for (String v2 : vertices) {
                    if (!v1.equals(v2)) {
                        Point position2 = newPositions.get(v2);
                        
                        // Apply simple repulsive force
                        int dx = position1.x - position2.x;
                        int dy = position1.y - position2.y;
                        double distance = Math.sqrt(dx * dx + dy * dy);
                        if (distance < 50) distance = 50; // Avoid overlap
                        
                        int forceX = (int) (1000 * dx / distance);
                        int forceY = (int) (1000 * dy / distance);
                        
                        newPositions.put(v1, new Point(position1.x + forceX, position1.y + forceY));
                        newPositions.put(v2, new Point(position2.x - forceX, position2.y - forceY));
                    }
                }
            }
    
            // Apply attraction between connected vertices (edges)
            for (String[] edge : edges) {
                String v1 = edge[0];
                String v2 = edge[1];
                
                Point position1 = newPositions.get(v1);
                Point position2 = newPositions.get(v2);
                
                int dx = position1.x - position2.x;
                int dy = position1.y - position2.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                // Apply simple attraction force (to reduce edge crossing)
                int forceX = (int) (600 * dx / distance);
                int forceY = (int) (600 * dy / distance);
                
                newPositions.put(v1, new Point(position1.x - forceX, position1.y - forceY));
                newPositions.put(v2, new Point(position2.x + forceX, position2.y + forceY));
            }
        }
    
        // Now scale the positions to fit within the bounds of the window
        scaleGraphToFitWindow(newPositions);
    
        // Update the graph panel with new positions after layout
        vertexPositions = newPositions;
        repaint(); // Repaint the graph with new positions
    }
    
    private void scaleGraphToFitWindow(Map<String, Point> newPositions) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        // Find the min/max x and y values to determine the bounding box of the graph
        for (Point p : newPositions.values()) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
    
        // Calculate the scaling factors for the graph to fit within the window
        double scaleX = (double) getWidth() / (maxX - minX);
        double scaleY = (double) getHeight() / (maxY - minY);
        double scale = Math.min(scaleX, scaleY);  // Scale the graph to fit the window
    
        // Apply scaling to the vertex positions
        for (Map.Entry<String, Point> entry : newPositions.entrySet()) {
            Point original = entry.getValue();
            int scaledX = (int) ((original.x - minX) * scale);
            int scaledY = (int) ((original.y - minY) * scale);
            entry.setValue(new Point(scaledX, scaledY));
        }
    }
}    