import javax.swing.*;

public class UpdateNotificationPane extends Window {
    private UpdateNotificationPane() {
        title = "A New Update is Available!";
    }

    private static void runInit() {
        init(new UpdateNotificationPane());
    }

    static void launch() {
        javax.swing.SwingUtilities.invokeLater(UpdateNotificationPane::runInit);
    }

    final void main(JPanel p) {
        p.add(new JLabel("A new Version of Pac-Man has been released!"));
        p.add(new JLabel("Current Version: " + PacStatic.VERSION));
        p.add(new JLabel("New Version: " + UpdateMgr.getLatestVersion()));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
    }
}
