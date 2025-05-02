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