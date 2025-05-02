final class Pacman extends Entity {
    private Dir nextDir;

    void move() {
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
                y -= Preferences.pacSpeed;
                coordsY = (y + PacStatic.HALF_CELLWIDTH - 1 - (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                    halt();
                }
                x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                return;
            case Dir.LEFT:
                x -= Preferences.pacSpeed;
                coordsX = (x + PacStatic.HALF_CELLWIDTH - 3 - (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                    halt();
                }
                y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                return;
            case Dir.DOWN:
                y += Preferences.pacSpeed;
                coordsY = (y - PacStatic.HALF_CELLWIDTH + (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                if (!PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                    halt();
                }
                x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
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
    void freeze(){
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