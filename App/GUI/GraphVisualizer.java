package App.GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
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
    private Point lastMousePosition; // Tracks last mouse position for dragging
    private String draggedVertex = null;

    // new instance variables added to fix the vertex coloring
    private boolean isDragging = false;
    private Point pressPoint; // to store the initial press point
    private static final int DRAG_THRESHOLD = 5; // threshold to distinguish between click and drag

    // Constructor to initialize the graph data
    public GraphVisualizer(String[] vertices, String[][] edges) {
        this.vertexColors = new HashMap<>();
        for (String vertex : vertices) {
            vertexColors.put(vertex, Color.WHITE); // start each vertex as white
        }
        this.vertices = vertices;
        this.edges = edges;
        this.vertexPositions = new HashMap<>();
        setPreferredSize(new Dimension(600, 600)); // Set panel size
        generateVertexPositions(); // Generate random non-overlapping positions for vertices

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDragging = false; // Reset dragging flag
                pressPoint = e.getPoint();
                lastMousePosition = e.getPoint();

                boolean vertexFound = false;
                for (Map.Entry<String, Point> entry : vertexPositions.entrySet()) {
                    String vertex = entry.getKey();
                    Point position = entry.getValue();
                    int radius = 20;

                    if (pressPoint.distance(position) < radius) {
                        draggedVertex = vertex;
                        vertexFound = true;
                        break;
                    }
                }

                if (!vertexFound) {
                    draggedVertex = null;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // stop dragging when mouse is released
                if (!isDragging && draggedVertex != null) {
                    // it was a click, not a drag
                    handleVertexClick(draggedVertex);
                    repaint();
                }
                draggedVertex = null;
                isDragging = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                Point currentPoint = e.getPoint();

                if (draggedVertex != null) {
                    if (!isDragging && pressPoint.distance(currentPoint) > DRAG_THRESHOLD) {
                        isDragging = true;
                    }

                    if (isDragging) {
                        // we need to update the position of the dragged vertex
                        vertexPositions.put(draggedVertex, currentPoint);
                        repaint();
                    }
                } else {
                    // panning
                    int dx = currentPoint.x - lastMousePosition.x;
                    int dy = currentPoint.y - lastMousePosition.y;
                    panX += dx;
                    panY += dy;
                    lastMousePosition = currentPoint;
                    repaint(); // Repaint to show the panned view

                }
            }
        });
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

            Color vertexColor = vertexColors.getOrDefault(vertex, Color.WHITE);

            g2d.setColor(vertexColor);
            g2d.fillOval(x, y, radius, radius);

            g2d.setColor(Color.BLACK);
            g2d.drawString(vertex, x + radius / 2 - 5, y + radius / 2 + 5);
        }
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
}
