import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

final class AsynchronousSoundPreloader implements Runnable {
    private final Sound s;

    AsynchronousSoundPreloader(Sound which) {
        s = which;
    }

    @Override
    public void run() {
        final InputStream is = switch (s) {
            case Sound.DEATH -> AsynchronousSoundPreloader.class.getResourceAsStream("DEATH.wav");
            case Sound.EXTRA_LIFE -> AsynchronousSoundPreloader.class.getResourceAsStream("EXTRA_LIFE.wav");
            case Sound.PAUSE -> AsynchronousSoundPreloader.class.getResourceAsStream("PAUSE.wav");
            case Sound.FRUIT -> AsynchronousSoundPreloader.class.getResourceAsStream("FRUIT.wav");
            default -> throw new RuntimeException("Attempt to preload unknown sound");
        };
        try (Clip clip = AudioSystem.getClip();) {
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) ;
            clip.start();
            while (!clip.isActive()) ;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}