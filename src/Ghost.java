import java.util.Random;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
class Ghost extends Entity {
    private final GhostType t;
    private final Random random = new Random();

    Ghost(GhostType gT) {
        t = gT;
        reset();
    }

    void reset() {
        dir = Dir.STOPPED;
    }

    boolean isTouching(double pacX, double pacY) {
        //return false;
        return Math.hypot(pacX - x, pacY - y) < PacStatic.HALF_CELLWIDTH;
    }

    void start() {
        switch (t) {
            case GhostType.BLINKY:
                coordsX = 5;
                coordsY = 5;
                break;
            case GhostType.INKY:
                coordsX = 6;
                coordsY = 10;
                break;
            case GhostType.PINKY:
                coordsX = 10;
                coordsY = 9;
                break;
        }
        x = PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH;
        y = PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH;
        dir = Dir.UP;
    }

    private void changeDir() {
        switch (random.nextInt() % 4) {
            case 0 -> {
                if (PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    dir = Dir.UP;
                    move();
                    return;
                }
                changeDir();
            }
            case 1 -> {
                if (PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                    dir = Dir.RIGHT;
                    move();
                    return;
                }
                changeDir();
            }
            case 2 -> {
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    dir = Dir.DOWN;
                    move();
                    return;
                }
                changeDir();
            }
            case 3 -> {
                if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    dir = Dir.LEFT;
                    move();
                    return;
                }
                changeDir();
            }
        }
    }

    void move() {
        switch (dir) {
            case Dir.UP:
                if (y < PacStatic.CELLWIDTH) y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                y -= Preferences.ghostSpeed;
                coordsY = (y + PacStatic.HALF_CELLWIDTH - 1 - (Preferences.ghostSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    changeDir();
                }
                if (x < (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                    x += Preferences.ghostSpeed;
                }
                if (x > (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                    x -= Preferences.ghostSpeed;
                }
                return;
            case Dir.LEFT:
                if (x < PacStatic.CELLWIDTH) x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                x -= Preferences.ghostSpeed;
                coordsX = (x + PacStatic.HALF_CELLWIDTH - 3 - (Preferences.ghostSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    changeDir();
                }
                if (y < (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                    y += Preferences.ghostSpeed;
                }
                if (y > (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                    y -= Preferences.ghostSpeed;
                }
                return;
            case Dir.DOWN:
                if (y > PacStatic.CELLWIDTH * 12) y = (int) (PacStatic.CELLWIDTH * 11.5f);
                y += Preferences.ghostSpeed;
                coordsY = (y - PacStatic.HALF_CELLWIDTH + (Preferences.ghostSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    changeDir();
                }
                if (x < (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                    x += Preferences.ghostSpeed;
                }
                if (x > (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                    x -= Preferences.ghostSpeed;
                }
                return;
            case Dir.RIGHT:
                if (x > PacStatic.CELLWIDTH * 12) x = (int) (PacStatic.CELLWIDTH * 11.5f);
                x += Preferences.ghostSpeed;
                coordsX = (x - PacStatic.HALF_CELLWIDTH + (Preferences.ghostSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                    changeDir();
                }
                if (y < (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                    y += Preferences.ghostSpeed;
                }
                if (y > (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                    y -= Preferences.ghostSpeed;
                }
        }
    }
}