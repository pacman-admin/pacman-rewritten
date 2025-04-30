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


    //private PImage maze_white;
    //private PImage currentFruitSprite;
    // private FruitSpriteContainer fruitSprites;
    private PImage maze_blue;

    public static void main(String[] ignored) {
        SoundManager.preloadStartSound();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Closing resources...");
            SoundManager.closeAll();
        }));
        PApplet.main(new String[]{"GameWindow"});
        LOGGER.info("Pac-Man started");
    }

    public void settings() {
        size(PacStatic.CELLWIDTH * 13 * Preferences.scale, PacStatic.CELLWIDTH * 13 * Preferences.scale);
    }

    private void calcTime() {
        time = millis() - startMillis;
        Math.sqrt(Math.pow(2,4));
    }

    public void setup() {
        ghosts = new Ghost[]{new Ghost(GhostType.BLINKY), new Ghost(GhostType.INKY), new Ghost(GhostType.PINKY)};
        maze_blue = loadImage("maze_blue.png");
        image(maze_blue, 0, 0);
        //fruitSprites = new FruitSpriteContainer();
        noStroke();
        int i = 0;
        for (boolean[] row : PacStatic.MAP_DESIGN){
            for (boolean open : row){
                if (open){

                }
            }
        }
        LOGGER.info("Bla" + -time);
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
        if (!first) {
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
        image(maze_blue, 0, 0);
        if (time < pauseUntil) {
            return;
        }
        if (first1) {
            new LoadingThread();
            first1 = false;
            for (Ghost g : ghosts) {
                g.start();
            }
            return;
        }
        drawGhosts();
        if (frameCount % 3600 == 3599) {
            SoundManager.play(Sound.DEATH);
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

    class Ghost extends Entity {
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
                    x = PacStatic.CELLWIDTH * Preferences.scale * 5;
                    y = PacStatic.CELLWIDTH * Preferences.scale * 5;
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