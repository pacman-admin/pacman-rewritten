import java.io.*;
import java.util.logging.Logger;

/**
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
        SoundManager.closeAll();
        playPauseBeat = false;
        mute = true;
        save();
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