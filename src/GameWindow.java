import processing.core.PApplet;

import java.util.logging.Logger;

/**
 * @author Langdon Staab
 * Rewriten 2025
 */
public final class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    public static void launch() {
        LOGGER.info("Pac-Man started");
        PApplet.main(new String[]{"GameWindow"});
    }
}