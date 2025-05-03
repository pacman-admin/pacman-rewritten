/**
 * @author Langdon Staab
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
abstract class Pickup {
    final int x, y;
    private boolean eaten;

    Pickup(int cellCol, int cellRow) {
        x = cellCol * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        y = cellRow * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        reset();
    }

    private void eat() {
        eaten = true;
        increaseScore();
    }

    abstract void increaseScore();

    abstract void handleDraw();

    void show() {
        if (!eaten) {
            handleDraw();
        }
    }

    void checkIfBeingEaten(double pacX, double pacY) {
        if (!eaten) {
            if (Math.hypot(pacX - x, pacY - y) < PacStatic.HALF_CELLWIDTH) {
                eat();
            }
        }
    }

    void reset() {
        eaten = false;
    }
}