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