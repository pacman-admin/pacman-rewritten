import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Langdon Staab
 * Rewriten 2025
 */

public final class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    private final Pellet[] pellets = new Pellet[78];
    private Ghost[] ghosts;
    private boolean first = true;
    private boolean first1 = true;
    private int pelletsEaten;
    private int score = 0;
    //private boolean paused;
    private int level = 2;
    private int startMillis;
    private int time;
    private int pauseUntil;
    //private PImage maze_white;
    private PImage maze_blue;
    private Pacman pacman = new Pacman();

    public static void main(String[] ignored) {
        //SoundManager.preloadStartSound();
        Runtime.getRuntime().addShutdownHook(new Thread(SoundManager::closeAll));
        PApplet.main(new String[]{"GameWindow"});
        new LoadingThread();
        LOGGER.info("Pac-Man started");
    }

    public void settings() {
        size(PacStatic.CELLWIDTH * 13 * Preferences.scale, PacStatic.CELLWIDTH * 13 * Preferences.scale);
        noSmooth();
    }

    public void keyPressed() {
        switch (keyCode) {
            case UP, 87 -> pacman.up();
            case RIGHT, 68 -> pacman.right();
            case DOWN, 83 -> pacman.down();
            case LEFT, 65 -> pacman.left();
        }
    }

    private void calcTime() {
        time = millis() - startMillis;
    }

    public void setup() {
        noStroke();
        maze_blue = loadImage("maze_blue.png");
        image(maze_blue, 0, 0);
        ghosts = new Ghost[]{new Ghost(GhostType.BLINKY), new Ghost(GhostType.INKY), new Ghost(GhostType.PINKY)};
        int pelletCount = 0;
        for (int i = 1; i < PacStatic.MAP_DESIGN.length - 1; i++) {
            for (int j = 1; j < PacStatic.MAP_DESIGN[0].length - 1; j++) {
                if (PacStatic.MAP_DESIGN[i][j]) {
                    if ((i > 1 || j > 1) && (j != 7 || i != 1)) {
                        pellets[pelletCount] = new Pellet(j, i);
                        pelletCount++;
                    }
                }
            }
        }
        pellets[77] = new Fruit(7, 1);
        imageMode(CENTER);
        rectMode(CENTER);
        //maze_white = loadImage("maze_white.png");
    }

    private void startGame() {
        pauseUntil = time + 4500;
        if (!first || Preferences.mute) {
            return;
        }
        SoundManager.playStartSound();
    }

    public void draw() {
        if (first) {
            startMillis = millis();
            calcTime();
            startGame();
            first = false;
            LOGGER.info("Game started!");
        }

        /*if (paused) {
            return;
        }*/
        calcTime();
        image(maze_blue, PacStatic.CANVAS_CENTRE, PacStatic.CANVAS_CENTRE);
        /*if (time < pauseUntil) {
            return;
        }*/
        if (first1) {
            first1 = false;
            for (Ghost g : ghosts) {
                g.start();
            }
            return;
        }
        if (pelletsEaten > 76) {
            level++;

        }
        drawPellets();
        drawGhosts();
        pacman.move();
        pacman.show();
    }

    private void drawPellets() {
        fill(250, 185, 176);
        for (Pellet pellet : pellets) {
            pellet.show();
        }
    }

    private void drawGhosts() {
        final int whichSprite = (frameCount % 50 < 26) ? 0 : 1;
        for (Ghost g : ghosts) {
            g.move();
            switch (g.dir) {
                case Dir.UP -> image(g.sprites.up[whichSprite], g.x, g.y);
                case Dir.RIGHT -> image(g.sprites.right[whichSprite], g.x, g.y);
                case Dir.DOWN -> image(g.sprites.down[whichSprite], g.x, g.y);
                case Dir.LEFT -> image(g.sprites.left[whichSprite], g.x, g.y);
            }
        }
    }

    private final class Ghost extends Entity {
        private final GameWindow.GhostSpriteContainer sprites;
        private final GhostType t;
        Random random = new Random();

        private Ghost(GhostType gT) {
            t = gT;
            sprites = new GameWindow.GhostSpriteContainer(gT);
            reset();
        }

        void reset() {
            dir = Dir.STOPPED;
        }

        private void start() {
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

        private void move() {
            switch (dir) {
                case Dir.UP:
                    if (y < PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH)
                        y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    y -= Preferences.ghostSpeed;
                    coordsY = (y + PacStatic.HALF_CELLWIDTH - 3) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                        changeDir();
                    }
                    if (x < (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                        x++;
                    }
                    if (x > (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                        x--;
                    }
                    return;
                case Dir.LEFT:
                    if (x < PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH)
                        x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    x -= Preferences.ghostSpeed;
                    coordsX = (x + PacStatic.HALF_CELLWIDTH - 3) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                        changeDir();
                    }
                    if (y < (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                        y++;
                    }
                    if (y > (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                        y--;
                    }
                    return;
                case Dir.DOWN:
                    if (y > PacStatic.CELLWIDTH * 11.5f) y = (int) (PacStatic.CELLWIDTH * 11.5f);
                    y += Preferences.ghostSpeed;
                    coordsY = (y - PacStatic.HALF_CELLWIDTH) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                        changeDir();
                    }
                    if (x < (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                        x++;
                    }
                    if (x > (PacStatic.CELLWIDTH * coordsX + PacStatic.HALF_CELLWIDTH)) {
                        x--;
                    }
                    return;
                case Dir.RIGHT:
                    if (x > PacStatic.CELLWIDTH * 11.5f) x = (int) (PacStatic.CELLWIDTH * 11.5f);
                    x += Preferences.ghostSpeed;
                    coordsX = (x - PacStatic.HALF_CELLWIDTH) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                        changeDir();
                    }
                    if (y < (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                        y++;
                    }
                    if (y > (PacStatic.CELLWIDTH * coordsY + PacStatic.HALF_CELLWIDTH)) {
                        y--;
                    }
            }
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
    }

    private class Pacman extends Entity {
        private Dir nextDir;

        @Override
        void reset() {
            dir = Dir.STOPPED;
            nextDir = Dir.STOPPED;
            x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            coordsX = 1;
            coordsY = 1;
        }

        private void show() {
            //fill(255, 255, 128+32);
            fill(255, 64, 64);
            //ellipse(x * Preferences.scale, y * Preferences.scale, 28 * Preferences.scale, 28 * Preferences.scale);
            rect(x * Preferences.scale, y * Preferences.scale, PacStatic.CELLWIDTH, PacStatic.CELLWIDTH);
        }

        private void move() {
            switch (nextDir) {
                case Dir.UP:
                    if (PacStatic.MAP_DESIGN[coordsY - 1][coordsX]) {
                        dir = nextDir;
                        break;
                    }
                    break;
                case Dir.LEFT:
                    if (PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                        dir = nextDir;
                        break;
                    }
                    break;
                case Dir.DOWN:
                    if (PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                        dir = nextDir;
                        break;
                    }
                    break;
                case Dir.RIGHT:
                    if (PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                        dir = nextDir;
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
                    //x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                case Dir.LEFT:
                    x -= Preferences.pacSpeed;
                    coordsX = (x + PacStatic.HALF_CELLWIDTH - 4 - (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY][coordsX - 1]) {
                        halt();
                    }
                    //y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                case Dir.DOWN:
                    y += Preferences.pacSpeed;
                    coordsY = (y - PacStatic.HALF_CELLWIDTH + (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY + 1][coordsX]) {
                        halt();
                    }
                    //x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                    return;
                case Dir.RIGHT:
                    x += Preferences.pacSpeed;
                    coordsX = (x - PacStatic.HALF_CELLWIDTH + (Preferences.pacSpeed / 3)) / PacStatic.CELLWIDTH;
                    if (!PacStatic.MAP_DESIGN[coordsY][coordsX + 1]) {
                        halt();
                    }
                    //y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            }
        }

        private void up() {
            nextDir = Dir.UP;
        }

        private void right() {
            nextDir = Dir.RIGHT;
        }

        private void down() {
            nextDir = Dir.DOWN;
        }

        private void halt() {
            //nextDir = Dir.STOPPED;
            dir = Dir.STOPPED;
            //x = coordsX * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            //y = coordsY * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        }

        private void left() {
            nextDir = Dir.LEFT;
        }
    }

    private class Pellet {
        final int x, y;
        private boolean eaten;

        private Pellet(int cellCol, int cellRow) {
            x = cellCol * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            y = cellRow * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            reset();
        }

        private void eat() {
            eaten = true;
            increaseScore();
        }

        void increaseScore() {
            pelletsEaten++;
            score += 10;
        }

        void handleDraw() {
            ellipse(x * Preferences.scale, y * Preferences.scale, 14 * Preferences.scale, 14 * Preferences.scale);
        }

        private void show() {
            if (!eaten) {
                handleDraw();
            }
        }

        void reset() {
            eaten = false;
        }
    }

    private final class Fruit extends Pellet {
        private static int typeID = 0;
        private final FruitSpriteContainer sprites;

        private Fruit(int cellCol, int cellRow) {
            super(cellCol, cellRow);
            sprites = new FruitSpriteContainer();
        }

        void increaseScore() {
            score += PacStatic.getFruitValue(typeID);
        }

        void reset() {
            super.reset();
            typeID = PacStatic.getFruitID(level);
        }

        void handleDraw() {
            image(sprites.fruit[typeID], x * Preferences.scale, y * Preferences.scale);
        }
    }

    private final class FruitSpriteContainer extends Thread {
        private final PImage[] fruit = new PImage[8];

        private FruitSpriteContainer() {
            start();
        }

        public void run() {
            fruit[0] = loadImage("cherry.png");
            fruit[1] = loadImage("strawberry.png");
            fruit[2] = loadImage("orange.png");
            fruit[3] = loadImage("apple.png");
            fruit[4] = loadImage("melon.png");
            fruit[5] = loadImage("galaxian.png");
            fruit[6] = loadImage("bell.png");
            fruit[7] = loadImage("key.png");
        }
    }

    private final class GhostSpriteContainer extends Thread {
        private final PImage[] up = new PImage[2];
        private final PImage[] right = new PImage[2];
        private final PImage[] down = new PImage[2];
        private final PImage[] left = new PImage[2];
        private final GhostType t;

        private GhostSpriteContainer(GhostType t) {
            this.t = t;
            start();
        }

        public void run() {
            switch (t) {
                case GhostType.BLINKY:
                    //LOGGER.info("Loading sprites for Blinky...");
                    up[0] = loadImage("ghost/blinky/up.png");
                    up[1] = loadImage("ghost/blinky/up2.png");
                    right[0] = loadImage("ghost/blinky/right.png");
                    right[1] = loadImage("ghost/blinky/right2.png");
                    down[0] = loadImage("ghost/blinky/down.png");
                    down[1] = loadImage("ghost/blinky/down2.png");
                    left[0] = loadImage("ghost/blinky/left.png");
                    left[1] = loadImage("ghost/blinky/left2.png");
                    LOGGER.info("Loaded Blinky sprites!");
                    return;
                case GhostType.INKY:
                    //LOGGER.info("Loading sprites for Inky...");
                    up[0] = loadImage("ghost/inky/up.png");
                    up[1] = loadImage("ghost/inky/up2.png");
                    right[0] = loadImage("ghost/inky/right.png");
                    right[1] = loadImage("ghost/inky/right2.png");
                    down[0] = loadImage("ghost/inky/down.png");
                    down[1] = loadImage("ghost/inky/down2.png");
                    left[0] = loadImage("ghost/inky/left.png");
                    left[1] = loadImage("ghost/inky/left2.png");
                    LOGGER.info("Loaded Inky sprites!");
                    return;
                case GhostType.PINKY:
                    //LOGGER.info("Loading sprites for Pinky...");
                    up[0] = loadImage("ghost/pinky/up.png");
                    up[1] = loadImage("ghost/pinky/up2.png");
                    right[0] = loadImage("ghost/pinky/right.png");
                    right[1] = loadImage("ghost/pinky/right2.png");
                    down[0] = loadImage("ghost/pinky/down.png");
                    down[1] = loadImage("ghost/pinky/down2.png");
                    left[0] = loadImage("ghost/pinky/left.png");
                    left[1] = loadImage("ghost/pinky/left2.png");
                    LOGGER.info("Loaded Pinky sprites!");
            }
        }
    }
}