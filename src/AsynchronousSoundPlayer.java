import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

final class AsynchronousSoundPlayer implements Runnable {
    private final Sound s;

    AsynchronousSoundPlayer(Sound which) {
        s = which;
    }

    public void run() {
        try (InputStream is = switch (s) {
            case Sound.DEATH -> AsynchronousSoundPlayer.class.getResourceAsStream("DEATH.wav");
            case Sound.EXTRA_LIFE -> AsynchronousSoundPlayer.class.getResourceAsStream("EXTRA_LIFE.wav");
            case Sound.PAUSE -> AsynchronousSoundPlayer.class.getResourceAsStream("PAUSE.wav");
            case Sound.FRUIT -> AsynchronousSoundPlayer.class.getResourceAsStream("FRUIT.wav");
            default -> throw new RuntimeException("Attempt to preload unknown sound");
        }; Clip clip = AudioSystem.getClip()) {
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) ;
            clip.start();
            clip.drain();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}