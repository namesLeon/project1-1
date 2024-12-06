package App.GUI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundFX {
    public static void playSound(String soundFile) {
        try {
            File file = new File("project-1.1/App/GUI/Sounds/" + soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
