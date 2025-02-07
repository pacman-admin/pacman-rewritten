import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

final class SoundManager {
    private static final Logger LOGGER = LoggerFactory.createLogger(SoundManager.class.getName());
    private static Clip pauseBeat;

    private static Clip getClip(String filename) {
        InputStream is = SoundManager.class.getResourceAsStream(filename);
        assert is != null;
        try (AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(is))) {
            final Clip clip = AudioSystem.getClip();
            clip.open(in);
            is.close();
            in.close();
            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    static void play(Sound what) {
        Clip clip;
        switch (what) {
            case Sound.WA:
                clip = getClip("DOT_1.wav");
                break;
            case Sound.KA:
                clip = getClip("DOT_2.wav");
                break;
            case Sound.GAME_START:
                clip = getClip("GAME_START.wav");
                break;
            case Sound.DEATH:
                clip = getClip("DEATH.wav");
                break;
            case Sound.EXTRA_LIFE:
                clip = getClip("EXTRA_LIFE.wav");
                break;
            case Sound.PAUSE:
                clip = getClip("PAUSE.wav");
                break;
            case Sound.FRUIT:
                clip = getClip("FRUIT.wav");
                break;
            default:
                LOGGER.severe("Attempt to play nonexistent sound");
                return;
        }
        clip.start();
        clip.close();
    }

    static void loopPauseBeat() {
        pauseBeat = getClip("PAUSE_BEAT.wav");
        pauseBeat.loop(Clip.LOOP_CONTINUOUSLY);
    }

    static void stopPauseBeat() {
        pauseBeat.stop();
        pauseBeat.close();
    }
}
