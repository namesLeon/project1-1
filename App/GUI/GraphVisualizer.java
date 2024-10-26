package App.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphVisualizer extends JPanel {

    private String[] vertices; // List of vertices
    private String[][] edges;  // List of edges
    private Map<String, Point> vertexPositions; // Map to store vertex positions to avoid overlap
    private double zoomFactor = 1.0; // Default zoom level
    private int panX = 0; // Horizontal pan offset
    private int panY = 0; // Vertical pan offset
    private Point lastMousePosition; // Tracks last mouse position for dragging


    // Constructor to initialize the graph data
    public GraphVisualizer(String[] vertices, String[][] edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.vertexPositions = new HashMap<>();
        setPreferredSize(new Dimension(600, 600)); // Set panel size
        generateVertexPositions(); // Generate random non-overlapping positions for vertices

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculate the difference in mouse position and adjust pan
                int dx = e.getX() - lastMousePosition.x;
                int dy = e.getY() - lastMousePosition.y;
                panX += dx;
                panY += dy;
                lastMousePosition = e.getPoint();
                repaint(); // Repaint to show the panned view
            }
        });
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

                // Ensure no overlap with existing vertices
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

            // If no valid position was found, default to (0, 0)
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

        // First draw edges
        drawEdges(g2d);

        // Then draw vertices
        drawVertices(g2d);
    }

    // Method to draw the vertices
    private void drawVertices(Graphics2D g2d) {
        int radius = 20; // Radius of vertex circles
        for (String vertex : vertices) {
            Point pos = vertexPositions.get(vertex);
            if (pos == null) continue;

            int x = pos.x - radius / 2;
            int y = pos.y - radius / 2;

            // Draw the vertex as a filled circle
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x, y, radius, radius);

            // Draw the vertex label
            g2d.setColor(Color.BLACK);
            g2d.drawString(vertex, x + radius / 2 - 5, y + radius / 2 + 5);
        }
    }

    // Method to draw the edges
    private void drawEdges(Graphics2D g2d) {
        if (edges == null) return;
        g2d.setColor(Color.BLACK);
        for (String[] edge : edges) {
            Point pos1 = vertexPositions.get(edge[0]);
            Point pos2 = vertexPositions.get(edge[1]);

            // Draw a line between the two vertices
            if (pos1 != null && pos2 != null) {
                g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
            }
        }
    }

    // Method to update vertices and edges dynamically
    public void updateGraph(String[] vertices, String[][] edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.vertexPositions.clear(); // Clear old positions
        generateVertexPositions();    // Generate new positions
        repaint();                    // Repaint the component to reflect changes
    }

    public void zoomIn() {
        zoomFactor *= 1.2;
        repaint();
    }

    public void zoomOut() {
        zoomFactor /= 1.2;
        repaint();
    }
}

