import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

abstract class Window extends JPanel implements ActionListener/*, ItemListener*/ {
    private static final Logger LOGGER = LoggerFactory.createLogger(Window.class.getName());
    private final static JLabel INFO = new JLabel("pacman@langdonstaab.ca | www.langdonstaab.ca");
    private final static JLabel COPYRIGHT = new JLabel("Copyright © 2025 Langdon Staab");
    String title = "Empty Window";
    Window() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            LOGGER.warning("Error while setting theme to system theme." + e);
            //ex.printStackTrace();
        }
        LOGGER.info("Successfully set theme to system theme.");
    }

    static void init(Window what) {
        final JFrame frame = new JFrame(what.title);
        what.realInit();
        frame.setContentPane(what);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    abstract void main(JPanel panel);

    private void realInit() {
        LOGGER.info("Opening " + title + "...");
        final JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        LOGGER.info("Creating UI...");
        main(infoPanel);
        infoPanel.add(createButton("About Pac-Man", KeyEvent.VK_A, "about"));
        infoPanel.add(createButton("Donate", KeyEvent.VK_D, "donate"));
        infoPanel.add(INFO);
        infoPanel.add(COPYRIGHT);
        add(infoPanel, BorderLayout.LINE_START);
        //setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    final JButton createButton(String text, int key, String command) {
        final JButton b = new JButton(text);
        b.setMnemonic(key);
        b.setActionCommand(command);
        b.addActionListener(this);
        return b;
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "update":
                if (UpdateMgr.updateAvailable()) {
                    //UpdatePrompt.create();
                }
                break;
            case "about":
                InfoPane.launch();
                break;
            case "donate":
                try {
                    Desktop.getDesktop().browse(new URI("https://buymeacoff.ee/langdonstaab"));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException("Error opening popup in browser", ex);
                }
                break;
        }
    }
}