import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.lang.Math.PI;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
    static float CHOMP_SPEED = (float) (PI / 36);

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