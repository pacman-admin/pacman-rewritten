import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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