import processing.core.PApplet;
import processing.core.PImage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Copyright 2024-2025 Langdon Staab
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Langdon Staab
 */
public final class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    private static int minScoreExtraLife = 1000;
    private final Pickup[] pellets = new Pickup[78];
    private final Pacman pacman = new Pacman();
    private ShowableGhost[] ghosts;
    private boolean first = true;
    private int pelletsEaten;
    private int score;
    private boolean scoreIncreased = false;
    private int level;
    private PImage currentMazeImg;
    private PImage maze_white;
    private PImage maze_blue;
    private int lives;
    private int spriteNum;
    private boolean awaitingStart = true;

    public static void main(String[] ignored) {
        //SoundManager.preloadStartSound();
        SoundManager.loopWhiteNoise();
        PApplet.main(new String[]{"GameWindow"});
        new LoadingThread();
        Runtime.getRuntime().addShutdownHook(new Thread(SoundManager::closeAll));
        Runtime.getRuntime().addShutdownHook(new Thread(PacStatic::saveHighScore));
        LOGGER.info("Pac-Man started");
    }

    public void settings() {
        size(PacStatic.CELLWIDTH * 13 * Preferences.scale, PacStatic.CELLWIDTH * 13 * Preferences.scale);
    }

    public void keyPressed() {
        switch (keyCode) {
            case UP, 87 -> pacman.up();
            case RIGHT, 68 -> pacman.right();
            case DOWN, 83 -> pacman.down();
            case LEFT, 65 -> pacman.left();
        }
    }

    public void setup() {
        noStroke();
        maze_blue = loadImage("maze_blue.png");
        image(maze_blue, 0, 0);
        //pacman.freeze();
        currentMazeImg = maze_blue;
        ghosts = new ShowableGhost[]{new ShowableGhost(GhostType.BLINKY), new ShowableGhost(GhostType.INKY), new ShowableGhost(GhostType.PINKY)};
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
        textAlign(CENTER, CENTER);
        textFont(createFont("minecraft-seven-classic/minecraft-seven-classic.ttf", 8, false));
        pellets[77] = new Fruit(7, 1);
        imageMode(CENTER);
        rectMode(CENTER);
        maze_white = loadImage("maze_white.png");
    }

    private void giveExtraLife() {
        if (score >= minScoreExtraLife) {
            minScoreExtraLife *= 2;
            lives++;
            SoundManager.play(Sound.EXTRA_LIFE);
        }
    }

    private void startGame() {
        new DelayedConcurrentExecutor("Delayed game start handler", 4500) {
            @Override
            void task() {
                for (Ghost g : ghosts) {
                    g.start();
                }
            }
        };
        if (!first) return;
        new ConcurrentRepeatingExecutor("Asynchronous recorder of high score", 20000, 15000) {
            void task() {
                if (scoreIncreased) {
                    scoreIncreased = false;
                    try (PrintWriter writer = new PrintWriter(PacStatic.PATH + "/highscore.txt")) {
                        writer.println(PacStatic.highScore);
                        LOGGER.info("Saved high score");
                    } catch (FileNotFoundException e) {
                        LOGGER.warning("Error while saving high score\n" + e);
                    }

                }
            }
        };
        new ConcurrentRepeatingExecutor("Asynchronous ghost sprite toggler", 4500, 320) {
            void task() {
                if (spriteNum == 0) {
                    spriteNum = 1;
                    return;
                }
                spriteNum = 0;
            }
        };
        lives = 2;
        level = 0;
        pelletsEaten = 0;
        score = 0;
        awaitingStart = false;
        SoundManager.playStartSound();
    }

    public void draw() {
        if (first) {
            startGame();
            first = false;
            LOGGER.info("Game started!");
        }
        if (lives < 0) {
            background(0);
            text("GAME OVER\nScore: " + score, 13 * PacStatic.HALF_CELLWIDTH, 13 * PacStatic.HALF_CELLWIDTH);
            return;
        }
        image(currentMazeImg, PacStatic.CANVAS_CENTRE, PacStatic.CANVAS_CENTRE);
        showScores();
        showLives();
        drawPellets();
        drawGhosts();
        pacman.move();
        showPacman();
    }

    public void mousePressed() {
        if (lives < 0) {
            pacman.reset();
            for (Pickup p : pellets) {
                p.reset();
            }
            startGame();
        }
    }

    private void drawPellets() {
        fill(250, 185, 176);
        for (Pickup p : pellets) {
            p.checkIfBeingEaten(pacman.x, pacman.y);
            p.show();
        }
    }

    private void showPacman() {
        fill(255, 255, 128 + 32);
        translate(pacman.x, pacman.y);
        switch (pacman.dir) {
            case Dir.UP -> rotate(HALF_PI);
            case Dir.DOWN -> rotate(PI + HALF_PI);
            case Dir.RIGHT -> rotate(PI);
        }
        arc(0, 0, PacStatic.CELLWIDTH - 2, PacStatic.CELLWIDTH - 2, -pacman.mouthOpenAngle, pacman.mouthOpenAngle);
    }

    private void showScores() {
        fill(255);
        text("Score: " + score + "\nHIGH SCORE " + (PacStatic.highScore = Math.max(PacStatic.highScore, score)), 13 * PacStatic.HALF_CELLWIDTH, PacStatic.HALF_CELLWIDTH);
    }

    private void showLives() {
        fill(255, 255, 128 + 32);
        for (int i = 0; i < lives; i++) {
            arc((i + 1) * 20, height - PacStatic.HALF_CELLWIDTH, 16, 16, PI / 8, PI * 15 / 8);
        }
    }

    private void drawGhosts() {
        for (ShowableGhost g : ghosts) {
            if (pacman.isNotDying()) {
                if (g.isTouching(pacman.x, pacman.y) && !awaitingStart) {
                    awaitingStart = true;
                    pacman.beginDeathAnimation();
                    LOGGER.info("You died!");
                    SoundManager.play(Sound.DEATH);
                    //pacman.freeze();
                    for (Ghost ghost : ghosts) {
                        ghost.reset();
                    }
                    //pacman.freeze();
                    new DelayedConcurrentExecutor("Delayed post-death reset handler", 2000) {
                        void task() {
                            lives--;
                            if (lives < 0) return;
                            for (Ghost ghost1 : ghosts) {
                                ghost1.start();
                            }
                            //pacman.reset();
                            awaitingStart = false;
                        }
                    };
                    return;
                }
            }
            g.move();
            switch (g.dir) {
                case Dir.UP -> image(g.sprites.up[spriteNum], g.x, g.y);
                case Dir.RIGHT -> image(g.sprites.right[spriteNum], g.x, g.y);
                case Dir.DOWN -> image(g.sprites.down[spriteNum], g.x, g.y);
                case Dir.LEFT -> image(g.sprites.left[spriteNum], g.x, g.y);
            }
        }
    }

    private final class ShowableGhost extends Ghost {
        private final GameWindow.GhostSpriteContainer sprites;

        ShowableGhost(GhostType gT) {
            super(gT);
            sprites = new GameWindow.GhostSpriteContainer(gT);
        }
    }

    private final class Pellet extends Pickup {
        private Pellet(int cellCol, int cellRow) {
            super(cellCol, cellRow);
        }

        void increaseScore() {
            scoreIncreased = true;
            SoundManager.waka();
            score += 10;
            if (pelletsEaten > 75) {
                //pacman.freeze();
                pelletsEaten = 0;
                level++;
                currentMazeImg = maze_white;
                Timer t = new Timer("Pellet update and maze flash handler");
                //pacman.freeze();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_blue;
                    }
                }, 250);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_white;
                    }
                }, 500);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_blue;
                    }
                }, 750);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_white;
                    }
                }, 1000);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_blue;
                    }
                }, 1250);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_white;
                    }
                }, 1500);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //pacman.freeze();
                        currentMazeImg = maze_blue;
                        for (Pickup p : pellets) {
                            p.reset();
                        }
                        for (Ghost g : ghosts) {
                            g.start();
                        }
                        pacman.reset();
                        //pacman.freeze();
                    }
                }, 1750);
                return;
            }
            pelletsEaten++;
            giveExtraLife();
        }

        void handleDraw() {
            ellipse(x * Preferences.scale, y * Preferences.scale, 14 * Preferences.scale, 14 * Preferences.scale);
        }
    }

    private final class Fruit extends Pickup {
        private static int typeID = 0;
        private final FruitSpriteContainer sprites;

        private Fruit(int cellCol, int cellRow) {
            super(cellCol, cellRow);
            sprites = new FruitSpriteContainer();
        }

        void increaseScore() {
            SoundManager.play(Sound.FRUIT);
            score += PacStatic.getFruitValue(typeID);
            giveExtraLife();
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

    final class GhostSpriteContainer extends Thread {
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