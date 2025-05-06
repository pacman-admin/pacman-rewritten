import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
final class SoundManager {
    private static final Logger LOGGER = LoggerFactory.createLogger(SoundManager.class.getName());
    private static final ExecutorService soundPlayer = newCachedThreadPool();
    private static final ExecutorService wakaHandler = newSingleThreadExecutor();
    private static Clip pauseBeat;
    private static Clip wa;
    private static Clip ka;
    private static boolean wakaToggle = true;

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
        wakaHandler.shutdown();
        LOGGER.info("All resources closed. Goodbye!");
    }

    static void playStartSound() {
        try (InputStream is = SoundManager.class.getResourceAsStream("GAME_START.wav"); Clip clip = AudioSystem.getClip()) {
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(is)));
            while (!clip.isOpen()) ;
            is.close();
            clip.start();
            clip.drain();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            LOGGER.warning("Error while playing start sound\n" + e);
        }
    }

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
            assert is != null;
            c.open(AudioSystem.getAudioInputStream(new BufferedInputStream(is)));
            is.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LOGGER.warning("Error while loading sound\n" + e);
        }
    }

    static void loopWhiteNoise() {
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

        if (wakaToggle) {
            wakaToggle = false;
            wakaHandler.submit(() -> {
                wa.start();
                wa.drain();
                wa.setFramePosition(0);
            });
            return;
        }
        wakaToggle = true;
        wakaHandler.submit(() -> {
            ka.start();
            ka.drain();
            ka.setFramePosition(0);
        });
    }
}