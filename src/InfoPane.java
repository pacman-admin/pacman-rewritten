import javax.swing.*;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
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
        p.add(new JLabel(""));
        p.add(new JLabel("This program makes use of the Processing core library,"));
        p.add(new JLabel("licensed under the LGPL v2.1"));
        p.add(new JLabel(""));
        p.add(new JLabel("This program is licensed under the MIT (aka. Expat) license"));
        p.add(new JLabel(""));
        p.add(new JLabel("Be aware that this game, in all its simplicity, is surprisingly addictive"));
    }
}
