import javax.swing.*;
import java.awt.event.KeyEvent;

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
