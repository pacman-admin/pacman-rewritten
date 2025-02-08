import javax.sound.sampled.LineUnavailableException;
import java.util.logging.Logger;

final class LoadingThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.createLogger(LoadingThread.class.getName());


    LoadingThread() {
        //LOGGER.info("Starting concurrent startup Thread...");
        start();
    }

    public void run() {
        //LOGGER.info("Thread started.");
        Preferences.load();
        if (Preferences.autoUpdate) {
            UpdateMgr.checkForUpdate();
        }

        if(Preferences.mute){
            return;
        }
        try {
            SoundManager.preloadWaka();
        } catch (LineUnavailableException e) {
            LOGGER.warning("Error while loading 'waka' sounds! All sounds have been muted.\n"+e);
            Preferences.mute = true;
        }
        //LOGGER.info("Thread finished.");
    }
}