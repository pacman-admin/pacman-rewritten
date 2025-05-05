import static java.lang.Math.PI;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
final class Pacman extends Entity {
    float mouthOpenAngle;
    private Dir nextDir;
    private boolean mouthOpening;

    void chomp() {
        if (mouthOpening) {
            mouthOpenAngle += PacStatic.CHOMP_SPEED;
            if (mouthOpenAngle >= PI) {
                mouthOpening = false;
            }
            return;
        }
        mouthOpenAngle -= PacStatic.CHOMP_SPEED;
        if (mouthOpenAngle <= 3 * PI / 4) {
            mouthOpening = true;
        }
    }

    void move() {
        coordsX = Math.round((float) x / PacStatic.CELLWIDTH + 0.5f) - 1;
        coordsY = Math.round((float) y / PacStatic.CELLWIDTH + 0.5f) - 1;
        switch (nextDir) {
            case Dir.UP:
                if (PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    dir = Dir.UP;
                    break;
                }
                break;
            case Dir.LEFT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    dir = Dir.LEFT;
                    break;
                }
                break;
            case Dir.DOWN:
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    dir = Dir.DOWN;
                    break;
                }
                break;
            case Dir.RIGHT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
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
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (y <= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
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
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (x <= coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                x -= Preferences.pacSpeed;
                chomp();
                return;
            case Dir.DOWN:
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    y += Preferences.pacSpeed;
                    chomp();
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (y >= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
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
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (x >= coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH) {
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
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

    void freeze() {
        dir = Dir.STOPPED;
        nextDir = Dir.STOPPED;
    }

    void reset() {
        mouthOpenAngle = (float) PI;
        x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        coordsX = 1;
        coordsY = 1;
        dir = Dir.STOPPED;
        nextDir = Dir.STOPPED;
        mouthOpening = true;
        mouthOpenAngle = (float) PI;
    }
}