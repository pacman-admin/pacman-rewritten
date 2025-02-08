import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * @author Langdon Staab
 */
public final class Main {
    private static final Logger LOGGER = LoggerFactory.createMasterLogger(Main.class.getName());

    public static void main(String[] args) {
        //SoundManager.play(Sound.FRUIT);
        LOGGER.info("Starting Pac-Man 14.0...");
        new LoadingThread();
        GameWindow.launch();
        //SoundManager.play(Sound.EXTRA_LIFE);
        final Timer t = new Timer(true);
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SoundManager.waka();
            }
        },2000,200);
        //SoundManager.loopPauseBeat();
        //SoundManager.play(Sound.);
    }
}