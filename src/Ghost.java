
public final class Ghost extends Entity {
    private final GhostType t;
    private Dir dir;
    private int coordsX;
    private int coordsY;

    Ghost(GhostType gT) {
        t = gT;
        reset();
    }

    void reset() {
        dir = Dir.STOPPED;
    }

    void start() {
        switch (t) {
            case GhostType.BLINKY:
                x = 67;
                y = 89;
                break;
            case GhostType.INKY:
                x = 45;
                y = 67;
                break;
            case GhostType.PINKY:
                x = 36;
                y = 95;
                break;
        }
        dir = Dir.UP;
    }

    void stop() {
        reset();
    }

    void updateCoords() {
        coordsX = x / (PacStatic.CELLWIDTH * Preferences.scale);
        coordsY = y / (PacStatic.CELLWIDTH * Preferences.scale);
        switch (dir) {
            case Dir.UP:
                /*if (y % (PacStatic.CELLWIDTH * Preferences.scale) == 0 ){
                    return;
                }*/
                coordsY++;
                return;
            case Dir.LEFT:
                /*if (x % (PacStatic.CELLWIDTH * Preferences.scale) == 0 ){
                    return;
                }*/
                coordsX++;
        }
    }

    String debugCoords() {
        return coordsX + ", " + coordsY;
    }
}
