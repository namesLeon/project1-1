package App.Main;

import App.GUI.*;
import App.Games.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int targetDimension = (int) (2.0 / 3.0 * screenHeight);
        ImageIcon image = new ImageIcon("App/Images/graphColoring.png");
        Image img = image.getImage();
        Image scaledImg = img.getScaledInstance(targetDimension, targetDimension, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);


        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        JPanel exitPanel = new JPanel();

        JButton BasicGUI = new JButton("SandBox");
        JButton ICMM = new JButton("I Changed My Mind");
        JButton RO = new JButton("Random Order");
        JButton TTBE = new JButton("To The Bitter End");
        JButton Exit = new JButton("Exit");

        buttonPanel.add(BasicGUI);
        buttonPanel.add(RO);
        buttonPanel.add(ICMM);
        buttonPanel.add(TTBE);
        exitPanel.add(Exit);

        frame.add(imageLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(exitPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        BasicGUI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                BasicGUI basicGUI = new BasicGUI();
                basicGUI.createShowGUI();
                frame.dispose();
            }
        });

        RO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                RandomOrder.game();
                frame.dispose();
            }
        });

        ICMM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav"); // Play success sound instead of fail
                IChangedMyMind.game(); // Launch the "I Changed My Mind" game mode
            }
        });
        

        TTBE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                ToTheBitterEnd.game();
                frame.dispose();
            }
        });

        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                frame.dispose();
            }
        });
    }
}
