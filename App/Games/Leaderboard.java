package App.Games;
import javax.swing.*;
import java.util.*;
import java.awt.BorderLayout;

public class Leaderboard {
    private static final int MAX_ENTRIES = 10; // Limit the number of displayed rankings
    private static final List<PlayerScore> scores = new ArrayList<>();

    // Add new score to leaderboard
    public static void addScore(String playerName, int score) {
        scores.add(new PlayerScore(playerName, score));
        scores.sort(Comparator.comparingInt(PlayerScore::getScore).reversed()); // Sort by score descending
        if (scores.size() > MAX_ENTRIES) {
            scores.remove(scores.size() - 1); // Keep the top 10
        }
    }

    // Show leaderboard
    public static void showLeaderboard() {
        JFrame leaderboardFrame = new JFrame("Leaderboard");
        leaderboardFrame.setSize(400, 300);
        leaderboardFrame.setLayout(new BorderLayout());

        String[] columnNames = {"Rank", "Player", "Score"};
        Object[][] data = new Object[scores.size()][3];
        for (int i = 0; i < scores.size(); i++) {
            data[i][0] = (i + 1); // rank
            data[i][1] = scores.get(i).getName();
            data[i][2] = scores.get(i).getScore();
        }

        JTable leaderboardTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        leaderboardFrame.add(scrollPane, BorderLayout.CENTER);

        leaderboardFrame.setVisible(true);
    }

    public static void loadLeaderboard() {
        // Sample data loading
        addScore("Alice", 100);
        addScore("Bob", 90);
        addScore("Charlie", 85);
    }

    // Clear leaderboard
    public static void clearLeaderboard() {
        scores.clear();
    }

    // Inner class representing the player's score
    private static class PlayerScore {
        private final String name;
        private final int score;

        public PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
