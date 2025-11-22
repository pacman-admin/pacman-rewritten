import java.io.*;
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