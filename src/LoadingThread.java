import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
            PacStatic.prevHighScore = Math.max(0, Integer.parseInt(reader.readLine()));
            LOGGER.info("Saved high score");
        } catch (FileNotFoundException e) {
            LOGGER.warning("Error while saving high score\n" + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferencePane.launch();
        LOGGER.info("Thread finished!");
    }
}