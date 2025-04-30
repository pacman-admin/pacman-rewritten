import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newFixedThreadPool;

final class SoundManager {
    private static final Logger LOGGER = LoggerFactory.createLogger(SoundManager.class.getName());
    //private static final ExecutorService soundPlayer = newCachedThreadPool();
    private static final ThreadPoolExecutor soundPlayer = (ThreadPoolExecutor) newFixedThreadPool(2);
    private static Clip pauseBeat;
    private static Clip wa;
    private static Clip ka;
    private static boolean wakaToggle = true;

    static void closeSounds() {
        pauseBeat.close();
        wa.close();
        ka.close();
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

    static void play(Sound what) {
        soundPlayer.submit(new AsynchronousSoundPlayer(what));
    }

    static void loopPauseBeat() {
        openClip(pauseBeat, "PAUSE_BEAT.wav");
        pauseBeat.loop(Clip.LOOP_CONTINUOUSLY);
        LOGGER.info("Pause beat started");
    }

    static void preloadStartSound() {
        LOGGER.info("Playing Start Sound...");
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            LOGGER.warning("Cannot play game start sound, line unavailable!");
            return;
        }
        try {
            InputStream is = SoundManager.class.getResourceAsStream("GAME_START.wav");
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) {
                LOGGER.info("Waiting for clip to open...");
            }
            clip.start();
            while (!clip.isActive()) ;
            while (clip.getFramePosition() == 0) {
                LOGGER.info("Waiting for first frame to play...");
            }
            clip.close();
            LOGGER.info("Start sound preloaded!");
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

    @SuppressWarnings("StatementWithEmptyBody")
    static void preload() {
        try {
            wa = AudioSystem.getClip();
            ka = AudioSystem.getClip();
            pauseBeat = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            Preferences.mute();
            throw new RuntimeException("Line Unavailable!" + e, e);
        }
        while (soundPlayer.getActiveCount() > 1) ;
        openClip(wa, "DOT_1.wav");
        wa.start();
        while (!wa.isActive()) ;
        while (wa.available() < 96000) ;
        while (wa.getFramePosition() == 0) ;
        wa.stop();
        while (soundPlayer.getActiveCount() > 1) ;
        openClip(ka, "DOT_2.wav");
        ka.start();
        while (!ka.isActive()) ;
        while (ka.available() < 96000) ;
        while (ka.getFramePosition() == 0) ;
        ka.stop();
        new Thread(() -> {
            while (soundPlayer.getActiveCount() > 1) ;
            soundPlayer.submit(new AsynchronousSoundPreloader(Sound.DEATH));
            while (soundPlayer.getActiveCount() > 1) ;
            soundPlayer.submit(new AsynchronousSoundPreloader(Sound.EXTRA_LIFE));
            while (soundPlayer.getActiveCount() > 1) ;
            soundPlayer.submit(new AsynchronousSoundPreloader(Sound.FRUIT));
            while (soundPlayer.getActiveCount() > 1) ;
            soundPlayer.submit(new AsynchronousSoundPreloader(Sound.PAUSE));
        }).start();
    }

    static void waka() {
        if (wakaToggle) {
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
        pauseBeat.close();
        LOGGER.info("Pause beat stopped");
    }

    static void closeAll() {
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
            InputStream is = new BufferedInputStream(SoundManager.class.getResourceAsStream("GAME_START.wav"));
            clip.open(AudioSystem.getAudioInputStream(is));
            while (!clip.isOpen()) ;
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