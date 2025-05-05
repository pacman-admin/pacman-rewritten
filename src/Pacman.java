/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
final class Pacman extends Entity {
    private final static int stopBuffer = 2;
    private Dir nextDir;

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
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (y <= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH + stopBuffer) {
                    halt();
                    return;
                }
                y -= Preferences.pacSpeed;
                return;
            case Dir.LEFT:
                if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    x -= Preferences.pacSpeed;
                    y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (x <= coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH + stopBuffer) {
                    halt();
                    return;
                }
                x -= Preferences.pacSpeed;
                return;
            case Dir.DOWN:
                if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    y += Preferences.pacSpeed;
                    x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                }
                if (y >= coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH + stopBuffer) {
                    halt();
                    return;
                }
                y += Preferences.pacSpeed;
                return;
            case Dir.RIGHT:
                x += Preferences.pacSpeed;
                coordsX = (x - PacStatic.HALF_CELLWIDTH + (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                    halt();
                }
                y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
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

    void halt() {
        dir = Dir.STOPPED;
        x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
    }

    void freeze() {
        dir = Dir.STOPPED;
        nextDir = Dir.STOPPED;
    }

    void reset() {
        dir = Dir.STOPPED;
        nextDir = Dir.STOPPED;
        x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        coordsX = 1;
        coordsY = 1;
    }
}