import java.util.Random;

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