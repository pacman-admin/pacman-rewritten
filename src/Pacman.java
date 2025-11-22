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
final class Pacman extends Entity {
    float mouthOpenAngle;
    int lives;
    boolean frozen;
    private Dir nextDir;
    private boolean mouthOpening;
    private boolean dying;

    boolean isAlive() {
        return !(dying || frozen);
    }

    private void doDieAnimation() {
        mouthOpenAngle -= PacStatic.CHOMP_SPEED / 2;
        if (mouthOpenAngle <= 0) {
            lives--;
            frozen = true;
        }
    }

    void chomp() {
        if (!mouthOpening) {
            mouthOpenAngle += PacStatic.CHOMP_SPEED;
            mouthOpening = mouthOpenAngle >= PI;
            return;
        }
        mouthOpenAngle -= PacStatic.CHOMP_SPEED;
        if (mouthOpenAngle <= 2 * PI / 3) {
            mouthOpening = false;
        }
    }

    void beginDeathAnimation() { dying |= !frozen;
    }

    void move() {
        if (dying) {
            doDieAnimation();
            return;
        }
        coordsX = Math.round((float) x / PacStatic.CELLWIDTH + 0.5f) - 1;
        coordsY = Math.round((float) y / PacStatic.CELLWIDTH + 0.5f) - 1;
        switch (nextDir) {
            case Dir.UP:
                if (PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    dir = Dir.UP;
                    break;
                }
                break;
            case Dir.LEFT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    dir = Dir.LEFT;
                    break;
                }
                break;
            case Dir.DOWN:
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    dir = Dir.DOWN;
                    break;
                }
                break;
            case Dir.RIGHT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    dir = Dir.RIGHT;
                    break;
                }
                break;
            default:
                return;
        }
        switch (dir) {
            case Dir.UP:
                if (PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    y -= Preferences.pacSpeed;
                    chomp();
                    return;
                }
                if (y <= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                y -= Preferences.pacSpeed;
                chomp();
                return;
            case Dir.LEFT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    x -= Preferences.pacSpeed;
                    chomp();
                    return;
                }
                if (x <= coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                x -= Preferences.pacSpeed;
                chomp();
                return;
            case Dir.DOWN:
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    y += Preferences.pacSpeed;
                    chomp();
                    return;
                }
                if (y >= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                y += Preferences.pacSpeed;
                chomp();
                return;
            case Dir.RIGHT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                    x += Preferences.pacSpeed;
                    chomp();
                    return;
                }
                if (x >= coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                x += Preferences.pacSpeed;
                chomp();
        }
    }

    void up() {
        nextDir = Dir.UP;
    }

    void right() {
        nextDir = Dir.RIGHT;
    }

    void down() {
        nextDir = Dir.DOWN;
    }

    void left() {
        nextDir = Dir.LEFT;
    }

    void reset() {
        mouthOpenAngle = (float) PI;
        x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        coordsX = 1;
        coordsY = 1;
        dying = false;
        dir = Dir.STOPPED;
        nextDir = Dir.STOPPED;
        mouthOpening = true;
        frozen = false;
    }
}