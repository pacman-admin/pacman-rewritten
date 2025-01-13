import javax.swing.*;
import java.awt.event.KeyEvent;

class PreferencePane extends Window {
    private PreferencePane() {
        title = "Preferences";
    }

    private static void runInit() {
        init(new PreferencePane());
    }

    static void launch() {
        javax.swing.SwingUtilities.invokeLater(PreferencePane::runInit);
    }

    final void main(JPanel p) {
        p.add(createButton("Check for updates", KeyEvent.VK_U, "update"));
    }
}