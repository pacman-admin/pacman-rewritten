import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.lang.Math.PI;

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
class PacStatic {
    final static float VERSION = 14.4f;
    final static String PATH = System.getProperty("user.home");
    final static int CELLWIDTH = 32;
    final static int HALF_CELLWIDTH = 16;
    final static int CANVAS_CENTRE = (int) (CELLWIDTH * 6.5);
    final static boolean[][] MAP_DESIGN = {{false, false, false, false, false, false, false, false, false, false, false, false, false}, {false, true, false, true, true, true, true, true, true, true, true, true, false}, {false, true, true, true, false, false, false, false, false, false, false, true, false}, {false, true, false, true, true, true, true, true, true, true, false, true, false}, {false, true, false, true, false, false, false, false, false, true, false, true, false}, {false, true, true, true, true, true, true, true, true, true, true, true, false}, {false, true, false, true, false, true, false, false, true, false, true, true, false}, {false, true, false, true, false, true, false, true, true, false, true, true, false}, {false, true, false, true, true, true, true, false, true, false, false, true, false}, {false, true, false, false, false, true, false, false, true, false, true, true, false}, {false, true, true, true, true, true, true, false, true, false, true, true, false}, {false, true, true, false, false, false, true, true, true, false, true, true, false}, {false, false, false, false, false, false, false, false, false, false, false, false, false}};
    static int highScore = 0;
    static boolean scaleWasChanged = false;
    static final float CHOMP_SPEED = (float) (PI / 36);

    static void saveHighScore() {
        try (PrintWriter writer = new PrintWriter(PacStatic.PATH + "/highscore.txt")) {
            writer.println(PacStatic.highScore);
            System.out.println("Saved high score");
        } catch (FileNotFoundException e) {
            System.err.println("Error while saving high score\n" + e);
        }
    }

    static int getFruitID(int level) {
        switch (level) {
            case 3 -> {
                return 2;
            }
            case 4, 5 -> {
                return 3;
            }
            case 6, 7 -> {
                return 4;
            }
            case 8, 9 -> {
                return 5;
            }
            case 10, 11 -> {
                return 6;
            }
        }
        if (level > 11) {
            return 7;
        }
        return level;
    }

    static int getFruitValue(int ID) {
        switch (ID) {
            case 0 -> {
                return 100;
            }
            case 1 -> {
                return 300;
            }
            case 2 -> {
                return 500;
            }
            case 3 -> {
                return 700;
            }
            case 4 -> {
                return 1000;
            }
            case 5 -> {
                return 2000;
            }
            case 6 -> {
                return 3000;
            }
        }
        return 5000;
    }
}