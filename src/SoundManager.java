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
 * Copyright (c) 2025 Langdon Staab <pacman@langdonstaab.ca>
 * <p>
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
        LOGGER.info("Closing resources...");
        System.out.println("Closing resources...");
        closeSounds();
        soundPlayer.shutdown();
        wakaHandler.shutdown();
        System.out.println("All resources closed");
    }

    static void playStartSound() {
        LOGGER.info("Starting playback of game start sound...");
        try (InputStream is = SoundManager.class.getResourceAsStream("GAME_START.wav"); Clip clip = AudioSystem.getClip()) {
            assert is != null;
            clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(is)));
            while (!clip.isOpen()) ;
            is.close();
            clip.start();
            LOGGER.info("Start sound is playing...");
            clip.drain();
            LOGGER.info("Start sound finished");
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
        if(Preferences.playPauseBeat) {
            openClip(pauseBeat, "PAUSE_BEAT.wav");
            pauseBeat.loop(Clip.LOOP_CONTINUOUSLY);
            LOGGER.info("Pause beat started");
        }
    }

    static void preload() {
        try {
            wa = AudioSystem.getClip();
            ka = AudioSystem.getClip();
            pauseBeat = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            Preferences.mute();
            throw new RuntimeException("Line Unavailable" + e, e);
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