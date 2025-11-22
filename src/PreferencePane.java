import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
class PreferencePane extends Window implements ItemListener, ChangeListener {
    private static final Logger LOGGER = LoggerFactory.createLogger(PreferencePane.class.getName());
    private final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private final ArrayList<JSlider> sliders = new ArrayList<>();

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
        p.add(createSlider(4, Preferences.scale));
        p.add(new JLabel("Scale (enlarge size of everything)"));
        p.add(createSlider(10, Preferences.pacSpeed));
        p.add(new JLabel("Pac-Man's speed"));
        p.add(createSlider(10, Preferences.ghostSpeed));
        p.add(new JLabel("The Ghosts' speed"));
        p.add(createButton("Check for updates", KeyEvent.VK_U, "update"));
    }

    final JCheckBox createCheckbox(String title, int key, boolean selected) {
        checkBoxes.add(new JCheckBox(title));
        checkBoxes.getLast().setMnemonic(key);
        checkBoxes.getLast().setSelected(selected);
        checkBoxes.getLast().addItemListener(this);
        return checkBoxes.getLast();
    }

    final JSlider createSlider(int limit, int val) {
        sliders.add(new JSlider(JSlider.HORIZONTAL, 1, limit, val));
        sliders.getLast().addChangeListener(this);
        sliders.getLast().setMinorTickSpacing(1);
        sliders.getLast().setPaintTicks(true);
        return sliders.getLast();
    }

    public void itemStateChanged(ItemEvent e) {
        switch (checkBoxes.indexOf((JCheckBox) e.getItemSelectable())) {
            case 0:
                LOGGER.fine("Clicked checkBox 1");
                Preferences.autoUpdate = e.getStateChange() == ItemEvent.SELECTED;
                break;
            case 1:
                LOGGER.fine("Clicked checkBox 2");
                Preferences.debug = e.getStateChange() == ItemEvent.SELECTED;
                break;
            case 2:
                LOGGER.fine("Clicked checkBox 3");
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Preferences.mute();
                    checkBoxes.get(3).setSelected(false);
                    return;
                }
                Preferences.unMute();
                break;
            case 3:
                LOGGER.fine("Clicked checkBox 4");
                Preferences.playPauseBeat = e.getStateChange() == ItemEvent.SELECTED;
                break;
            default:
                LOGGER.severe("An unknown checkbox was selected/deselected!");
                return;
        }
        Preferences.save();
    }

    public void stateChanged(ChangeEvent e) {
        final JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            switch (sliders.indexOf(source)) {
                case 0:
                    Preferences.scale = source.getValue();
                    PacStatic.scaleWasChanged = true;
                    break;
                case 1:
                    Preferences.pacSpeed = source.getValue();
                    break;
                case 2:
                    Preferences.ghostSpeed = source.getValue();
                    break;
                default:
                    LOGGER.severe("An unknown slider was adjusted!");
                    return;
            }
            Preferences.save();
        }
    }
}