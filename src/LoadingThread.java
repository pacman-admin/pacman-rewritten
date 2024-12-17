import java.util.logging.Logger;

final class LoadingThread extends Thread {
    private static final Logger LOGGER = Logger.getLogger(LoadingThread.class.getName());

    LoadingThread() {
        start();
    }

    public void run() {
        Preferences.load();
        if (Preferences.autoUpdate) {
            UpdateMgr.checkForUpdate();
        }
    }
}