import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

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
abstract class Window extends JPanel implements ActionListener {
    private static final Logger LOGGER = LoggerFactory.createLogger(Window.class.getName());
    String title = "Empty Window";

    Window() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            LOGGER.warning("Error while setting theme to system theme." + e);
        }
        LOGGER.info("Successfully set theme to system theme.");
    }

    static void init(Window what) {
        final JFrame frame = new JFrame(what.title);
        what.realInit();
        frame.setContentPane(what);
        frame.pack();
        frame.setVisible(true);
    }

    abstract void main(JPanel panel);

    private void realInit() {
        LOGGER.info("Opening " + title + "...");
        final JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        LOGGER.info("Creating UI...");
        main(infoPanel);
        infoPanel.add(createButton("About Pac-Man", KeyEvent.VK_A, "about"));
        infoPanel.add(createButton("Donate", KeyEvent.VK_D, "donate"));
        infoPanel.add(new JLabel("pacman@langdonstaab.ca | www.langdonstaab.ca"));
        infoPanel.add(new JLabel("Copyright © 2025 Langdon Staab"));
        add(infoPanel, BorderLayout.LINE_START);
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
                if (!Preferences.autoUpdate) {
                    UpdateMgr.checkForUpdate();
                }
                if (UpdateMgr.getLatestVersion() > PacStatic.VERSION) {
                    UpdateNotificationPane.launch();
                } else {
                    NoUpdateNecessaryNP.launch();
                }
                return;
            case "about":
                InfoPane.launch();
                return;
            case "donate":
                try {
                    Desktop.getDesktop().browse(new URI("https://buymeacoff.ee/langdonstaab"));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException("Error opening popup in browser", ex);
                }
                return;
            case "install":
                try {
                    UpdateMgr.installUpdate();
                } catch (IOException ex) {
                    LOGGER.warning("Could not install update. Encountered an IOException.\nPlease check your internet connection.\n" + ex);
                    JOptionPane.showMessageDialog(null, "Could not download latest jar file.\nPlease check your internet connection.", "Could not install update", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (Exception ex) {
                    LOGGER.warning("Could not install update. \nPlease download the latest version from www.langdonstaab.ca\n" + ex);
                    JOptionPane.showMessageDialog(null, "Could not download laetest jar file.\nPlease manually download the latest version from www.langdonstaab.ca\n"+ex, "Could not install update", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Successfully installed the latest available version!\nPlease restart Pac-Man to use the latest version.", "Update Successful!", JOptionPane.PLAIN_MESSAGE);
        }
    }
}