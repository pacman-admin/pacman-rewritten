import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

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
final class LoadingThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.createLogger(LoadingThread.class.getName());

    LoadingThread() {
        start();
    }

    public void run() {
        Preferences.load();
        LOGGER.info("Loaded saved Preferences from filesystem.");
        if (!Preferences.mute) {
            SoundManager.preload();
        }
        if (Preferences.autoUpdate) {
            UpdateMgr.checkForUpdate();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PacStatic.PATH + "/highscore.txt"))) {
            PacStatic.highScore = Math.max(0, Integer.parseInt(reader.readLine()));
            LOGGER.info("Saved high score");
        } catch (FileNotFoundException e) {
            LOGGER.warning("Error while saving high score\n" + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Thread finished!");
    }
}