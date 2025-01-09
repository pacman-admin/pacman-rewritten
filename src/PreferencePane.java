import javax.swing.*;
import java.util.logging.Logger;

class PreferencePane extends Window {
    private static final Logger LOGGER = LoggerFactory.createLogger(PreferencePane.class.getName());

    PreferencePane() {
        title = "Preferences";
    }

    private static void runInit() {
        init(new PreferencePane());
    }

    static void launch() {

        javax.swing.SwingUtilities.invokeLater(PreferencePane::runInit);
    }

    final void main(JPanel p) {
    }
}