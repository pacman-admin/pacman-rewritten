import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import static java.lang.Float.parseFloat;

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
final class UpdateMgr {
    private static final Logger LOGGER = LoggerFactory.createLogger(UpdateMgr.class.getName());
    private static float latestVersion = PacStatic.VERSION;

    static void checkForUpdate() {
        LOGGER.info("Checking for update...");
        try {
            URI versionF = new URI("https://raw.githubusercontent.com/pacman-admin/pacman-rewritten/refs/heads/master/version");
            BufferedReader in = new BufferedReader(new InputStreamReader(versionF.toURL().openStream()));
            String s = in.readLine();
            in.close();
            latestVersion = parseFloat(s);
            LOGGER.info("Latest version is: " + latestVersion);
            LOGGER.info("Latest version is: " + s);

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
            LOGGER.info("An update is available");
            //create update window
            UpdateNotificationPane.launch();
        }
    }

    static float getLatestVersion() {
        return latestVersion;
    }

    static void installUpdate() throws IOException, URISyntaxException {
        InputStream in = new URI("https://raw.githubusercontent.com/pacman-admin/GNU-Linux-downloads/refs/heads/main/Pac-Man_redo.jar").toURL().openStream();
        Files.copy(new BufferedInputStream(in), Paths.get(getAppPath()), StandardCopyOption.REPLACE_EXISTING);
    }

    private static String getAppPath() {
        try {
            return new File(PacStatic.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}