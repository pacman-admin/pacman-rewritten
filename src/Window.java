import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

abstract class Window extends JPanel implements ActionListener/*, ItemListener*/ {
    /*static void open(){
        javax.swing.SwingUtilities.invokeLater(Window::createWindow(new PreferencePane()));
    }
    private static void createWindow(Window what){
        JFrame frame = new JFrame("Settings");
        /*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
    /*Create and set up the content pane.*/
       /* what.setOpaque(true);
        /*content panes must be opaque*/
    //frame.setContentPane(what);
    /*Display the Window.*/
        /*frame.pack();
        frame.setVisible(true);
    }*/
    static JButton createButton(String title, int key, Window window, String command) {
        final JButton button = new JButton(title);
        button.setMnemonic(key);
        button.setEnabled(true);
        button.setActionCommand(command);
        button.addActionListener(window);
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "update":
                if (UpdateMgr.updateAvailable()) {
                    //UpdatePrompt.create();
                }
                break;
            case "launchAbout":
                //AboutWindow.open();
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