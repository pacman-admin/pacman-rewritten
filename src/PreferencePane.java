import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

class PreferencePane extends Window implements ItemListener {
    private static final Logger LOGGER = LoggerFactory.createLogger(PreferencePane.class.getName());

    private final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

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
        p.add(createCheckbox("Check for updates during startup", KeyEvent.VK_C, Preferences.autoUpdate));
        p.add(createCheckbox("Debug", KeyEvent.VK_D, Preferences.debug));
        p.add(createCheckbox("Mute all sounds", KeyEvent.VK_M, Preferences.mute));
        p.add(createCheckbox("Play Pause Beat when paused", KeyEvent.VK_B, Preferences.playPauseBeat));
        p.add(createButton("Check for updates", KeyEvent.VK_U, "update"));
        //p.add(createCheckbox("", KeyEvent.VK_, Preferences.));
    }

    final JCheckBox createCheckbox(String title, int key, boolean selected) {
        checkBoxes.add(new JCheckBox(title));
        checkBoxes.getLast().setMnemonic(key);
        checkBoxes.getLast().setSelected(selected);
        checkBoxes.getLast().addItemListener(this);
        return checkBoxes.getLast();
    }

    public void itemStateChanged(ItemEvent e) {
        switch(checkBoxes.indexOf(e.getItemSelectable())){
            case 0:
                LOGGER.info("Clicked checkBox 1");
                Preferences.autoUpdate = e.getStateChange() == ItemEvent.SELECTED;
                break;
            case 1:
                LOGGER.info("Clicked checkBox 2");
                Preferences.debug = e.getStateChange() == ItemEvent.SELECTED;
                break;
            case 2:
                LOGGER.info("Clicked checkBox 3");
                if (e.getStateChange() == ItemEvent.SELECTED){
                    Preferences.mute();
                    checkBoxes.get(3).setSelected(false);
                    return;
                }
                Preferences.mute = false;
                break;
            case 3:
                LOGGER.info("Clicked checkBox 4");
                Preferences.playPauseBeat = e.getStateChange() == ItemEvent.SELECTED;
                break;
            default:
                LOGGER.severe("An unknown checkbox was selected/deselected!");
                return;
        }
        Preferences.save();
    }
}