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
        Ghost g = new Ghost(GhostType.BLINKY);
        g.start();
        g.updateCoords();
        LOGGER.info(g.x + ", " + g.y + "; " + g.debugCoords());
    }
}