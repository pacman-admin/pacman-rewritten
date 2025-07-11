import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
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
        p.add(createButton("Install Update", KeyEvent.VK_I, "install"));
    }
}
