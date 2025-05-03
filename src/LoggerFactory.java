import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Langdon Staab
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
final class LoggerFactory {
    private final static Logger LOGGER = createLogger(LoggerFactory.class);

    static Logger createLogger(String className) {
        final Logger newLogger = Logger.getLogger(className);
        newLogger.setLevel(Level.INFO);
        return newLogger;
    }

    static Logger createLogger(Class who) {
        return createLogger(who.getName());
    }

    static Logger createMasterLogger(String className) {
        final Logger masterLogger = createLogger(className);
        //masterLogger.addHandler(new ConsoleHandler());
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.setLevel(Level.WARNING);
            masterLogger.addHandler(fileHandler);
            LOGGER.info("Successfully created master Logger.");
        } catch (IOException e) {
            masterLogger.severe("Could not create FileHandler. \nError messages will not be written to a log file! \n(Because this message won't be written to a log and there is usually no console, this message is pretty useless)");
        }
        return masterLogger;
    }
}