package App.Main;

import App.GUI.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("App/Images/graphColoring.png");
        Image img = image.getImage();
        Image scaledImg = img.getScaledInstance(800, 800, Image.SCALE_SMOOTH);
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

        //frame.add(imageLabel, BorderLayout.NORTH);
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
                SoundFX.playSound("fail.wav");
                //TODO make RandomOrder.java
            }
        });

        ICMM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("fail.wav");
                //TODO make IChangedMyMind.java
            }
        });

        TTBE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("fail.wav");
                //TODO make ToTheBitterEnd.java
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
