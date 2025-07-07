import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static java.lang.Float.parseFloat;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
final class UpdateMgr {
    private static final Logger LOGGER = LoggerFactory.createLogger(UpdateMgr.class.getName());
    private static float latestVersion = 14f;

    static void checkForUpdate() {
        LOGGER.info("Checking for update...");
        try {
            URI versionF = new URI("https://raw.githubusercontent.com/pacman-admin/pacman-rewritten/refs/heads/master/version");
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
            UpdateNotificationPane.launch();
        }
    }

    static float getLatestVersion() {
        return latestVersion;
    }
}