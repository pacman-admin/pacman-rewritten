import java.util.logging.Logger;

final class LoadingThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.createLogger(LoadingThread.class.getName());

    LoadingThread() {
        //LOGGER.info("Starting concurrent startup Thread...");
        start();
    }

    public void run() {
        LOGGER.info("Thread started.");
        Preferences.load();
        LOGGER.info("Loaded saved Preferences from filesystem.");
        if (!Preferences.mute) {
            SoundManager.preload();
        }
        /*if (Preferences.autoUpdate) {
            UpdateMgr.checkForUpdate();
        }*/
        //PreferencePane.launch();
        LOGGER.info("Thread finished.");
    }
}