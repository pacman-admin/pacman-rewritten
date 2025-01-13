import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static java.lang.Float.parseFloat;

final class UpdateMgr {
    private static final Logger LOGGER = LoggerFactory.createLogger(UpdateMgr.class.getName());
    private static float latestVersion = 14f;

    static void checkForUpdate() {
        LOGGER.info("Checking for update...");
        try {
            URI versionF = new URI("https://raw.githubusercontent.com/pacman-admin/pacmancode/newmaster/Pac-Man_VERSION.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(versionF.toURL().openStream()));
            latestVersion = parseFloat(in.readLine());
            in.close();
        } catch (IOException e) {
            LOGGER.warning("Encountered an IOException while checking for an update. Please check your internet connection.");
            LOGGER.warning(e.toString());
            return;
        } catch (URISyntaxException e) {
            LOGGER.warning("Encountered a URISyntaxException while checking for update.");
            LOGGER.warning(e.toString());
            return;
        }
        if (PacStatic.VERSION < latestVersion) {
            LOGGER.info("An update is available!");
            //create update window
        }
    }

    public static boolean updateAvailable() {
        return latestVersion > PacStatic.VERSION;
    }

    public static float getLatestVersion() {
        return latestVersion;
    }
}