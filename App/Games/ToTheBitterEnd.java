package App.Games;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import App.GUI.SoundFX;
import App.Main.Main;

public class ToTheBitterEnd {
    public static void game() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel backPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JButton hint = new JButton("hint");
        JButton generateGraph = new JButton("generate graph");
        JButton back = new JButton("back");

        backPanel.add(back);
        buttonPanel.add(hint);
        buttonPanel.add(generateGraph);

        frame.add(backPanel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.setVisible(true);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("button.wav");
                Main.mainMenu();
                frame.dispose();
            }
        });

        hint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundFX.playSound("fail.wav");
                //TODO implement hint functionality
            }
        });

        generateGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed
        });
    }

    public void generateGraph() {

    }
}
