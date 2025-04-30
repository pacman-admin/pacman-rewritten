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

    @Override
    public void run() {
        final InputStream is = switch (s) {
            case Sound.DEATH -> AsynchronousSoundPreloader.class.getResourceAsStream("DEATH.wav");
            case Sound.EXTRA_LIFE -> AsynchronousSoundPreloader.class.getResourceAsStream("EXTRA_LIFE.wav");
            case Sound.PAUSE -> AsynchronousSoundPreloader.class.getResourceAsStream("PAUSE.wav");
            case Sound.FRUIT -> AsynchronousSoundPreloader.class.getResourceAsStream("FRUIT.wav");
            default -> throw new RuntimeException("Attempt to preload unknown sound");
        };
        if (is == null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("InputStream is null!\n" + e, e);
            }
            throw new RuntimeException("InputStream is null!");
        }
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) ;
            clip.start();
            clip.drain();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}