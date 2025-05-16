import java.io.*;
import java.util.logging.Logger;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
class Preferences {
    private static final Logger LOGGER = LoggerFactory.createLogger(Preferences.class.getName());
    static boolean autoUpdate = true;
    static boolean debug = false;
    static boolean mute = false;
    static boolean playPauseBeat = true;
    static int scale = 1;
    static int pacSpeed = 3;
    static int ghostSpeed = 2;

    static void mute() {
        SoundManager.closeSounds();
        playPauseBeat = false;
        mute = true;
        save();
    }

    static void unMute() {
        new Thread(() -> {
            SoundManager.preload();
            mute = false;
            save();
        }).start();
    }

    static void load() {
        try {
            ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(PacStatic.PATH + "/PacPreferences.dat"));
            Container savedSettings = (Container) oIS.readObject();
            autoUpdate = savedSettings.autoUpdate;
            debug = savedSettings.debug;
            mute = savedSettings.mute;
            playPauseBeat = savedSettings.playPauseBeat;
            scale = savedSettings.scale;
            pacSpeed = savedSettings.pacSpeed;
            ghostSpeed = savedSettings.ghostSpeed;
            oIS.close();
        } catch (IOException e) {
            LOGGER.warning("Could not load settings. Perhaps the file does not exist.");
            save();
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Encountered an unexpected ClassNotFoundException while attempting to load Preferences from disk.");
        }
    }

    static void save() {
        try {
            ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(PacStatic.PATH + "/PacPreferences.dat"));
            oOS.writeObject(new Container());
            oOS.close();
        } catch (IOException e) {
            LOGGER.warning("Could not save Preferences to disk. Perhaps your home directory (~) is not writable.");
            LOGGER.warning(e.toString());
        }
    }

    private final static class Container implements Serializable {
        private final boolean autoUpdate = Preferences.autoUpdate;
        private final boolean debug = Preferences.debug;
        private final boolean mute = Preferences.mute;
        private final boolean playPauseBeat = Preferences.playPauseBeat;
        private final int scale = Preferences.scale;
        private final int pacSpeed = Preferences.pacSpeed;
        private final int ghostSpeed = Preferences.ghostSpeed;
    }
}