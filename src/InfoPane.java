import javax.swing.*;

public class InfoPane extends Window {
    private InfoPane() {
        title = "About Pac-Man";
    }

    private static void runInit() {
        init(new InfoPane());
    }

    static void launch() {
        javax.swing.SwingUtilities.invokeLater(InfoPane::runInit);
    }

    final void main(JPanel p) {
        p.add(new JLabel("Pac-Man version " + PacStatic.VERSION));
        p.add(new JLabel("Coded by Langdon Staab"));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        p.add(new JLabel(""));
    }
}
