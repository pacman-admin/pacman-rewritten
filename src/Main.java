import java.util.logging.Logger;

/**
 * @author Langdon Staab
 */
public final class Main {
    private static final Logger LOGGER = LoggerFactory.createMasterLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting Pac-Man 14.0...");
        new LoadingThread();
        PreferencePane.launch();
        GameWindow.launch();
    }
}