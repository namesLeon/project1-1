package App.Games;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Scoring {

    private static final String LEADERBOARD_DIR = "App/Games/Leaderboards";

    public static int scoreCalculator(String game, String userName, int chromaticNumber, int timeElapsed) {
        int score = chromaticNumber * timeElapsed;

        storeInLeaderboard(game, userName, score);

        return score;
    }

    public static void storeInLeaderboard(String game, String userName, int score) {
        try {
            File directory = new File(LEADERBOARD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            List<LeaderboardEntry> leaderboard = loadLeaderboard(game);

            leaderboard.add(new LeaderboardEntry(userName, score));

            leaderboard.sort(Comparator.comparingInt(LeaderboardEntry::getScore));

            saveLeaderboard(game, leaderboard);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static List<LeaderboardEntry> loadLeaderboard(String game) throws IOException {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        File file = new File(LEADERBOARD_DIR, game + "_leaderboard.txt");
    
        System.out.println("Attempting to load leaderboard from: " + file.getAbsolutePath());
    
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Read line: " + line);
    
                    String[] parts = line.split(": ");
                    System.out.println("Split parts length: " + parts.length);
                    if (parts.length == 2) {
                        String user = parts[0].trim();
                        System.out.println("Parsed user: " + user);
                        try {
                            int score = Integer.parseInt(parts[1].trim());
                            leaderboard.add(new LeaderboardEntry(user, score));
                            System.out.println("Added entry: " + user + " - " + score);
                        } catch (NumberFormatException ex) {
                            System.out.println("Error parsing score for line: " + line);
                        }
                    } else {
                        System.out.println("Unexpected format in line: " + line);
                    }
                }
            }
        } else {
            System.out.println("File does not exist.");
        }
        return leaderboard;
    }
    

    private static void saveLeaderboard(String game, List<LeaderboardEntry> leaderboard) throws IOException {
        File file = new File(LEADERBOARD_DIR, game + "_leaderboard.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (LeaderboardEntry entry : leaderboard) {
                writer.write(entry.getUserName() + "," + entry.getScore());
                writer.newLine();
            }
        }
    }

    public static class LeaderboardEntry {
        private final String userName;
        private final int score;

        public LeaderboardEntry(String userName, int score) {
            this.userName = userName;
            this.score = score;
        }

        public String getUserName() {
            return userName;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return userName + ": " + score;
        }
    }

    public static List<LeaderboardEntry> getLeaderBoard(String game) {
        try {
            List<LeaderboardEntry> leaderboard = loadLeaderboard(game);
            
            for (LeaderboardEntry entry : leaderboard) {
                System.out.println("Loaded entry: " + entry.getUserName() + " - " + entry.getScore());
            }
            
            return leaderboard;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return Collections.emptyList();
        }
    }
    
}
