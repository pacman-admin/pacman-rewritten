import processing.core.PApplet;
import processing.core.PImage;

import java.util.logging.Logger;

/**
 * @author Langdon Staab
 * Rewriten 2025
 */
public final class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    private Ghost[] ghosts;
    private boolean first = true;
    private boolean first1 = true;
    //private boolean paused;
    private int startMillis;
    private int time;
    private int pauseUntil;
    private Pellet[] pellets = new Pellet[78];

    //private PImage maze_white;
    //private PImage currentFruitSprite;
    //private FruitSpriteContainer fruitSprites;
    private PImage maze_blue;

    public static void main(String[] ignored) {
        //SoundManager.preloadStartSound();
        Runtime.getRuntime().addShutdownHook(new Thread(SoundManager::closeAll));
        PApplet.main(new String[]{"GameWindow"});
        new LoadingThread();
        LOGGER.info("Pac-Man started");
    }

    public void settings() {
        size(PacStatic.CELLWIDTH * 13 * Preferences.scale, PacStatic.CELLWIDTH * 13 * Preferences.scale);
    }

    private void calcTime() {
        time = millis() - startMillis;
        Math.sqrt(Math.pow(2, 4));
    }

    public void setup() {
        ghosts = new Ghost[]{new Ghost(GhostType.BLINKY), new Ghost(GhostType.INKY), new Ghost(GhostType.PINKY)};
        maze_blue = loadImage("maze_blue.png");
        image(maze_blue, 0, 0);
        imageMode(CENTER);
        //fruitSprites = new FruitSpriteContainer();
        noStroke();
        int pelletCount = 0;
        for (int i = 0; i < PacStatic.MAP_DESIGN.length; i++) {
            for (int j = 0; j < PacStatic.MAP_DESIGN[0].length; j++) {
                if (PacStatic.MAP_DESIGN[i][j]) {
                    if (i > 1 || j > 1) {
                        pellets[pelletCount] = new Pellet(j, i);
                        pelletCount++;
                    }
                }
            }
        }
        LOGGER.info("Bla" + time);
        LOGGER.info("# of pellets: " + pelletCount);
        //maze_white = loadImage("maze_white.png");
    }

    void updateWindowSize() {
        surface.setResizable(true);
        size(PacStatic.CELLWIDTH * 13 * Preferences.scale, PacStatic.CELLWIDTH * 13 * Preferences.scale);
        surface.setResizable(false);
        PacStatic.scaleWasChanged = false;
    }

    private void startGame() {
        pauseUntil = time + 4500;
        if (!first || Preferences.mute) {
            return;
        }
        SoundManager.playStartSound();
    }

    public void draw() {
        if (PacStatic.scaleWasChanged) {
            updateWindowSize();
        }
        if (first) {
            startMillis = millis();
            calcTime();
            LOGGER.info("Game started.");
            startGame();
            first = false;
        }

        /*if (paused) {
            return;
        }*/
        calcTime();
        image(maze_blue, PacStatic.CANVAS_CENTRE, PacStatic.CANVAS_CENTRE);
        if (time < pauseUntil) {
            return;
        }
        if (first1) {
            //new LoadingThread();
            first1 = false;
            for (Ghost g : ghosts) {
                g.start();
            }
            return;
        }
        drawPellets();

        drawGhosts();
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
            switch (g.dir) {
                case Dir.UP -> image(g.sprites.up[whichSprite], g.x, g.y);
                case Dir.RIGHT -> image(g.sprites.right[whichSprite], g.x, g.y);
                case Dir.DOWN -> image(g.sprites.down[whichSprite], g.x, g.y);
                case Dir.LEFT -> image(g.sprites.left[whichSprite], g.x, g.y);
            }
        }
    }

    private final class Ghost extends Entity {
        final GameWindow.GhostSpriteContainer sprites;
        private final GhostType t;
        private Dir dir;
        private int coordsX;
        private int coordsY;

        Ghost(GhostType gT) {
            t = gT;
            sprites = new GameWindow.GhostSpriteContainer(gT);
            reset();
        }

        void reset() {
            dir = Dir.STOPPED;
        }

        void start() {
            switch (t) {
                case GhostType.BLINKY:
                    x = PacStatic.CELLWIDTH * 5 + PacStatic.HALF_CELLWIDTH;
                    y = PacStatic.CELLWIDTH * 5 + PacStatic.HALF_CELLWIDTH;
                    break;
                case GhostType.INKY:
                    x = PacStatic.CELLWIDTH * 6 + PacStatic.HALF_CELLWIDTH;
                    y = PacStatic.CELLWIDTH * 10 + PacStatic.HALF_CELLWIDTH;
                    break;
                case GhostType.PINKY:
                    x = PacStatic.CELLWIDTH * 10 + PacStatic.HALF_CELLWIDTH;
                    y = PacStatic.CELLWIDTH * 9 + PacStatic.HALF_CELLWIDTH;
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

    private final class GhostSpriteContainer extends Thread {
        final PImage[] up = new PImage[2];
        final PImage[] right = new PImage[2];
        final PImage[] down = new PImage[2];
        final PImage[] left = new PImage[2];
        final GhostType t;

        GhostSpriteContainer(GhostType t) {
            this.t = t;
            start();
        }

        public void run() {
            switch (t) {
                case GhostType.BLINKY:
                    LOGGER.info("Loading sprites for Blinky...");
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
                    LOGGER.info("Loading sprites for Inky...");
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
                    LOGGER.info("Loading sprites for Pinky...");
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

    private class Pellet extends Entity {
        private boolean eaten;

        private Pellet(int cellCol, int cellRow) {
            x = cellCol * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            y = cellRow * PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
            reset();
        }

        void eat() {
            eaten = true;
        }

        void show() {
            if (!eaten) {
                circle(x * Preferences.scale, y * Preferences.scale, 14 * Preferences.scale);
            }
        }

        void reset() {
            eaten = false;
        }
    }


    private final class FruitSpriteContainer extends Thread {
        final PImage[] fruit = new PImage[8];

        FruitSpriteContainer() {
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
}