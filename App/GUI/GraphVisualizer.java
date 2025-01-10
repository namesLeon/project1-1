package App.GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.*;


public class GraphVisualizer extends JPanel {

    private Map<String, Color> vertexColors; // new hashmap to keep track of each vertex color
    private String[] vertices; // List of vertices
    private String[][] edges;  // List of edges
    private Map<String, Point> vertexPositions; // Map to store vertex positions to avoid overlap
    private double zoomFactor = 1.0; // Default zoom level
    private int panX = 0; // Horizontal pan offset
    private int panY = 0; // Vertical pan offset
    private String[] vertexOrder;
    private String highlightedVertex; // Added for highlighting
    private boolean showColoredGraph = true; // Default of the graph is uncolored


    // Variables for dragging
    private String draggedVertex = null;
    private Point lastMousePosition;
    private boolean isDraggingVertex = false;
    private final int DRAG_THRESHOLD = 5; // Pixels

    private boolean restrictClicksToHighlightedVertex = false;

    public void setRestrictClicksToHighlightedVertex(boolean restrict) {
        this.restrictClicksToHighlightedVertex = restrict;
    }

    private VertexClickListener vertexClickListener;

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
                if (vertexClickListener != null) {
                    if (!restrictClicksToHighlightedVertex || (draggedVertex.equals(highlightedVertex))) {
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
        } else {
            throw new IllegalArgumentException("Vertex " + vertex + " not found.");
        }
    }

    public void clearVertexColors() {
        for (String vertex : vertices) {
            vertexColors.put(vertex, Color.WHITE); // Reset each vertex to the default color
        }
        repaint();
    }
    
    // Method to set a highlighted vertex
    public void highlightVertex(String vertex) {
        this.highlightedVertex = vertex;
        repaint();
    }

    // Helper method to handle vertex clicks
    private void handleVertexClick(String vertex) {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
        String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Orange"};

        String selectedColorName = (String) JOptionPane.showInputDialog(
                this,
                "Choose a color for vertex " + vertex + ":",
                "Select Color",
                JOptionPane.PLAIN_MESSAGE,
                null,
                colorNames,
                colorNames[0]
        );

        if (selectedColorName != null) {
            for (int i = 0; i < colorNames.length; i++) {
                if (selectedColorName.equals(colorNames[i])) {
                    vertexColors.put(vertex, colors[i]);
                    break;
                }
            }
            repaint();
        }
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

    public void toggleGraphColoring(){
        showColoredGraph = !showColoredGraph; // Toggle the flag
        repaint();
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
                return false;
            }
        }
        return true;
    }
}
