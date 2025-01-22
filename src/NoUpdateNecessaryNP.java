import javax.swing.*;

//No Update Necessary Notification Pane
class NoUpdateNecessaryNP extends Window {
    private NoUpdateNecessaryNP() {
        title = "No update needed";
    }

    private static void runInit() {
        init(new NoUpdateNecessaryNP());
    }

    static void launch() {
        javax.swing.SwingUtilities.invokeLater(NoUpdateNecessaryNP::runInit);
    }

    final void main(JPanel p) {
        p.add(new JLabel("You already have the latest version"));
        p.add(new JLabel("Pac-Man version " + PacStatic.VERSION));
    }
}
