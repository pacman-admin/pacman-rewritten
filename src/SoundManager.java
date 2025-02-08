import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

final class SoundManager {
    private static final Logger LOGGER = LoggerFactory.createLogger(SoundManager.class.getName());
    private static Clip pauseBeat;
    private static Clip wa;
    private static Clip ka;
    private static boolean wakaToggle = true;

    private static Clip getClip(String filename) throws LineUnavailableException {
        InputStream is = SoundManager.class.getResourceAsStream(filename);
        try {
            final Clip clip = AudioSystem.getClip();
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            is.close();
            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LOGGER.warning("Error while loading sound\n" + e);
        }
        return AudioSystem.getClip();
    }

    private static void playClip(String filename) {
        InputStream is = SoundManager.class.getResourceAsStream(filename);
        //LOGGER.warning("\n"+e);
        try {
            Clip clip = AudioSystem.getClip();
            assert clip != null;
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            clip.start();
            is.close();
            LOGGER.info("Clip started");
            while (clip.isActive()) ;
            LOGGER.info("Clip ended");
            clip.close();
            return;
        } catch (LineUnavailableException e) {
            LOGGER.warning("Line Unavailable!\n" + e);
        } catch (IOException e) {
            LOGGER.warning("IOException while opening audio stream!\n" + e);
        } catch (UnsupportedAudioFileException e) {
            LOGGER.warning("Unsupported audio type!\n" + e);
        }
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void play(Sound what) {
        switch (what) {
            case Sound.WA:
                playClip("DOT_1.wav");
                break;
            case Sound.KA:
                playClip("DOT_2.wav");
                break;
            case Sound.GAME_START:
                playClip("GAME_START.wav");
                break;
            case Sound.DEATH:
                playClip("DEATH.wav");
                break;
            case Sound.EXTRA_LIFE:
                playClip("EXTRA_LIFE.wav");
                break;
            case Sound.PAUSE:
                playClip("PAUSE.wav");
                break;
            case Sound.FRUIT:
                playClip("FRUIT.wav");
                break;
            default:
                LOGGER.severe("Attempt to play nonexistent sound");
        }
    }

    static void loopPauseBeat() {
        try {
            pauseBeat = getClip("PAUSE_BEAT.wav");
        } catch (LineUnavailableException e) {
            LOGGER.warning("Can't play pause beat. Line unavailable\n" + e);
            return;
        }
        pauseBeat.loop(Clip.LOOP_CONTINUOUSLY);
        LOGGER.info("Pause beat started");
    }

    static void preloadWaka() throws LineUnavailableException {
        wa = getClip("DOT_1.wav");
        ka = getClip("DOT_2.wav");
    }

    static void waka() {
        if (wakaToggle){
            wa.setFramePosition(0);
            wa.start();
            wakaToggle = false;
            return;
        }
        ka.setFramePosition(0);
        ka.start();
        wakaToggle = true;
    }

    static void stopPauseBeat() {
        pauseBeat.stop();
        LOGGER.info("Pause beat stopped");
        pauseBeat.close();
    }
    static void closeAll(){
        pauseBeat.stop();
        wa.stop();
        ka.stop();
        pauseBeat.close();
        wa.close();
        ka.close();
    }
}
