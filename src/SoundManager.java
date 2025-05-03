import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newFixedThreadPool;

final class SoundManager {
    private static final Logger LOGGER = LoggerFactory.createLogger(SoundManager.class.getName());
    private static final ThreadPoolExecutor soundPlayer = (ThreadPoolExecutor) newFixedThreadPool(3);
    private static Clip pauseBeat;
    private static Clip wa;
    private static Clip ka;
    private static boolean wakaToggle = true;

    static void closeSounds() {
        try {
            wa.close();
        } catch (Exception e) {
            LOGGER.warning("Error while closing wa.\n" + e);
        }
        try {
            ka.close();
        } catch (Exception e) {
            LOGGER.warning("Error while closing ka.\n" + e);
        }
        try {
            pauseBeat.close();
        } catch (Exception e) {
            LOGGER.warning("Error while closing pauseBeat.\n" + e);
        }
    }

    private static void openClip(Clip c, String filename) {
        InputStream is = SoundManager.class.getResourceAsStream(filename);
        try {
            c.open(AudioSystem.getAudioInputStream(is));
            is.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LOGGER.warning("Error while loading sound\n" + e);
        }
    }
    static void loopWhiteNoise(){
        //loop inaudible white noise to fix Linux sound issues
        soundPlayer.submit(new AsynchronousBlankClipLooper());
    }

    static void play(Sound what) {
        soundPlayer.submit(new AsynchronousSoundPlayer(what));
    }

    static void loopPauseBeat() {
        openClip(pauseBeat, "PAUSE_BEAT.wav");
        pauseBeat.loop(Clip.LOOP_CONTINUOUSLY);
        LOGGER.info("Pause beat started");
    }

    static void preload() {
        try {
            wa = AudioSystem.getClip();
            ka = AudioSystem.getClip();
            pauseBeat = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            Preferences.mute();
            throw new RuntimeException("Line Unavailable!" + e, e);
        }
        openClip(wa, "DOT_1.wav");
        openClip(ka, "DOT_2.wav");
    }

    static void waka() {
        soundPlayer.submit(() -> {
            if (wakaToggle) {
                wa.setFramePosition(0);
                wa.start();
                wakaToggle = false;
                return;
            }
            ka.setFramePosition(0);
            ka.start();
            wakaToggle = true;
        });
    }

    static void stopPauseBeat() {
        pauseBeat.close();
        LOGGER.info("Pause beat stopped");
    }

    static void closeAll() {
        System.out.println("Closing resources...");
        System.err.println("Closing resources...");
        LOGGER.info("Closing resources...");
        closeSounds();
        soundPlayer.shutdown();
        LOGGER.info("All resources closed. Goodbye!");
    }

    static void playStartSound() {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            LOGGER.warning("Cannot play game start sound, line unavailable!");
            return;
        }
        try {
            InputStream is = SoundManager.class.getResourceAsStream("GAME_START.wav");
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(is));
            LOGGER.info("Waiting for start clip to open...");
            while (!clip.isOpen()) ;
            LOGGER.info("Starting start sound...");
            clip.start();
            LOGGER.info("Start sound playing...");
            clip.drain();
            clip.close();
            LOGGER.info("Start sound finished.");
            return;
        } catch (LineUnavailableException e) {
            LOGGER.warning("Line Unavailable!\n" + e);
        } catch (IOException e) {
            LOGGER.warning("IOException while opening audio stream!\n" + e);
        } catch (UnsupportedAudioFileException e) {
            LOGGER.warning("Unsupported audio type!\n" + e);
        }
        clip.close();
    }
}