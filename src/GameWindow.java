import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Langdon Staab
 */
public class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    private final ArrayList<String> messages = new ArrayList<>();
    private final Ghost blinky = new Ghost();
    private final Ghost inky = new Ghost();
    private final Ghost pinky = new Ghost();
    private final Pacman pacman = new Pacman();
    private final Pellet[] pellet = new Pellet[78];
    private final PImage[] blinky_Up = {null, null};
    private final PImage[] blinky_Down = {null, null};
    private final PImage[] blinky_Left = {null, null};
    private final PImage[] blinky_Right = {null, null};
    private final PImage[] pinky_Up = {null, null};
    private final PImage[] pinky_Down = {null, null};
    private final PImage[] pinky_Left = {null, null};
    private final PImage[] pinky_Right = {null, null};
    private final PImage[] inky_Up = {null, null};
    private final PImage[] inky_Down = {null, null};
    private final PImage[] inky_Left = {null, null};
    private final PImage[] inky_Right = {null, null};
    private final boolean[] keys = new boolean[255];
    private PFont pxFont;
    private PImage maze_blue;
    private PImage maze_white;
    private int lives = 3;
    private int chompSpeed = 8;
    private boolean playStartSound = true;
    private boolean finishedDelay;
    private boolean first1 = true;
    private boolean lostLife;
    private boolean paused;
    private boolean pelletFirst;
    private boolean runSetup = true;
    private int startMillis;
    private int chomp = 30;
    private int duration;
    private int durationStart;
    private int fruitWorth;
    private int highScore;
    private int level;
    private int livesClaimed;
    private int pelletsEaten;
    private int score;
    private int startFrames;
    private int coordsX;
    private int coordsY;
    private PImage cherry;
    private PImage strawberry;
    private PImage apple;
    private PImage orange;
    private PImage melon;
    private PImage galaxian;
    private PImage bell;
    private PImage keyI;
    private PImage restartB;
    private PImage settingsB;
    private PImage pauseButtonImg;

    public static void main() {
        PApplet.main(new String[]{"GameWindow"});
    }

    private static boolean hitBoxCollision(int cellX, int cellY, float objectX, float objectY) {
        return objectX > cellX && objectX < cellX + PacStatic.CELLWIDTH && objectY > cellY && objectY < cellY + PacStatic.CELLWIDTH;
    }

    private static double fastDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void settings() {
        size(PacStatic.CANVAS_WIDTH, PacStatic.CANVAS_HEIGHT);
    }

    public void setup() {
        surface.setTitle("Loading...");
        System.out.println("Please wait...");
        background(0);
        noStroke();
        textSize(16);
        textAlign(CENTER, CENTER);
        fill(255);
        text("Loading...\nBy Langdon Staab\n\nSound manager by Tyler Tomas\n\nwww.langdonstaab.ca", Math.round(width / 2f), Math.round(height / 2f));
        frameRate(120);
        //check setup2() for setup
    }

    private void setup2() {
        surface.setResizable(true);
        imageMode(CENTER);
        changeAppIcon();
        System.out.println("Loading Game Assets...");
        cherry = loadImage("cherry.png");
        settingsB = loadImage("Preferences.png");
        restartB = loadImage("restart.png");
        strawberry = loadImage("strawberry.png");
        pxFont = createFont("minecraft-seven-classic/minecraft-seven-classic.ttf", 8, false);
        pauseButtonImg = loadImage("pause_button.png");
        blinky_Down[0] = loadImage("ghost/blinky/down.png");
        blinky_Down[1] = loadImage("ghost/blinky/down2.png");
        blinky_Up[0] = loadImage("ghost/blinky/up.png");
        blinky_Up[1] = loadImage("ghost/blinky/up2.png");
        blinky_Left[0] = loadImage("ghost/blinky/left.png");
        blinky_Left[1] = loadImage("ghost/blinky/left2.png");
        blinky_Right[0] = loadImage("ghost/blinky/right.png");
        blinky_Right[1] = loadImage("ghost/blinky/right2.png");
        inky_Down[0] = loadImage("ghost/inky/down.png");
        inky_Down[1] = loadImage("ghost/inky/down2.png");
        inky_Up[0] = loadImage("ghost/inky/up.png");
        inky_Up[1] = loadImage("ghost/inky/up2.png");
        inky_Left[0] = loadImage("ghost/inky/left.png");
        inky_Left[1] = loadImage("ghost/inky/left2.png");
        inky_Right[0] = loadImage("ghost/inky/right.png");
        inky_Right[1] = loadImage("ghost/inky/right2.png");
        pinky_Down[0] = loadImage("ghost/pinky/down.png");
        pinky_Down[1] = loadImage("ghost/pinky/down2.png");
        pinky_Up[0] = loadImage("ghost/pinky/up.png");
        pinky_Up[1] = loadImage("ghost/pinky/up2.png");
        pinky_Left[0] = loadImage("ghost/pinky/left.png");
        pinky_Left[1] = loadImage("ghost/pinky/left2.png");
        pinky_Right[0] = loadImage("ghost/pinky/right.png");
        pinky_Right[1] = loadImage("ghost/pinky/right2.png");
        maze_blue = loadImage("maze_blue.png");
        maze_white = loadImage("maze_white.png");

        System.out.println("Initializing...");
        pellet[5].isFruit = true;
        surface.setTitle("Pac-Man " + PacStatic.VERSION);

        System.out.println("Loading Complete!");
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MAIN PROGRAM ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void draw() {
        if (!paused) {
            try {
                //updateKeys();

                if (runSetup) {
                    setup2();
                    durationStart = millis();
                    startMillis = millis();
                    System.out.println(millis());
                    runSetup = false;
                    textFont(pxFont);
                    startFrames = frameCount;
                    duration = 4500 + durationStart;
                } else if (millis() < 2000) ;
                    //display loading screen for a minimum of 2 seconds.
                    //wait until 2 seconds have passed
                else if (lives <= 0) {
                    background(0);
                    fill(255, 0, 0);
                    text("GAME OVER", PacStatic.CANVAS_WIDTH / 2f, PacStatic.CANVAS_HEIGHT / 2f);
                    text("Click the screen to play again", PacStatic.CANVAS_WIDTH / 2f, PacStatic.CANVAS_HEIGHT / 2f + 40);
                    text("By Langdon Staab\nwww.langdonstaab.ca", PacStatic.CANVAS_WIDTH / 2f, PacStatic.CANVAS_HEIGHT / 2f + 80);
                    if (mousePressed) {
                        restart();
                    }
                } else {
                    if (frameCount % 2 == 0) {
                        if (millis() < duration) {
                            pacman.stop();
                            pacman.stopped = true;
                            finishedDelay = false;
                            blinky.halt();
                            inky.halt();
                            pinky.halt();
                        } else if (!finishedDelay) {
                            blinky.up();
                            inky.up();
                            pinky.up();
                            finishedDelay = true;
                        }
                        destroyUselessMessages();
                        if (lostLife) {
                            if (chomp < 60) {
                                chomp++;
                            }
                            if (first1) {
                                SoundManager.play(Sound.DEATH);
                                first1 = false;
                                System.gc();
                            }
                            pacman.stop();
                            blinky.halt();
                            inky.halt();
                            pinky.halt();
                            if (true) {
                                chomp += 3;
                            } else {
                                blinky.newGame();
                                inky.newGame();
                                pinky.newGame();
                                pacman.x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                                pacman.y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                                first1 = true;
                                lostLife = false;
                                chomp = 16;
                                duration = 2000 + millis();
                                pacman.stop();
                                pacman.stopped = true;
                                lives--;
                            }
                        }
                        if (!pacman.dir.equals(Dir.STOPPED)) {
                            if (chomp > 96) {
                                chompSpeed = -chompSpeed;
                            }
                            if (chomp < 8) {
                                chompSpeed = -chompSpeed;
                            }
                            chomp += chompSpeed;
                        }
                        if (pelletsEaten < pellet.length - 1 && !lostLife) {
                            pacman.update();
                        }
                        blinky.update();
                        inky.update();
                        pinky.update();
                        updateKeys();
                        image(maze_blue, 208, 208);
                        if (pelletsEaten >= pellet.length - 1) {
                            blinky.halt();
                            inky.halt();
                            pinky.halt();
                            pacman.stop();
                            if ((millis() - durationStart < 250) || (millis() - durationStart < 750 && millis() - durationStart > 500) || (millis() - durationStart < 1250 && millis() - durationStart > 1000) || (millis() - durationStart < 1750 && millis() - durationStart > 1500)) {
                                image(maze_white, 208, 208);
                            }
                            if (millis() - durationStart >= 2000) {
                                pacman.update();
                                blinky.up();
                                inky.up();
                                pinky.up();
                                pacman.stopped = true;
                                pacman.stop();
                                playStartSound = false;
                            }
                        }
                        display();
                        //int useless = 5 / 0;
                    }
                }
            } catch (Exception e) {
                LOGGER.severe("Game Error!\n" + e);
            }
        }
    }

    private void display() throws FileNotFoundException {
        drawButtons();
        showLives();
        fill(255, 128, 0);
        for (Pellet value : pellet) {
            value.isBEaten();
            value.draw();
        }
        drawGhosts();
        fill(255);
        float tempFPSVal = ((millis() - startMillis) / 1000f) > 0 ? ((millis() - startMillis) / 1000f) : 1;
        text(str(Math.round((frameCount - startFrames) / tempFPSVal)), 330, 10);
        text("HIGH SCORE\n" + highScore, width / 2f, 16);
        displayMessages();
        pacman.show(chomp);
    }

    String loadString(String filename) {
        String[] ret;
        String data;
        try {
            ret = loadStrings(filename);
            data = ret[0];
            return data;
        } catch (Exception ignored) {
            return "error";
        }
    }

    public void keyPressed() {
        keys[keyCode] = true;
    }

    public void keyReleased() {
        keys[keyCode] = false;
    }

    private void updateKeys() {
        if (keys[LEFT]) {
            pacman.left();
        }
        if (keys[RIGHT]) {
            pacman.right();
        }
        if (keys[UP]) {
            pacman.up();
        }
        if (keys[DOWN]) {
            pacman.down();
        }
        if (keys[65]) {
            pacman.left();
        }
        if (keys[68]) {
            pacman.right();
        }
        if (keys[87]) {
            pacman.up();
        }
        if (keys[83]) {
            pacman.down();
        }
    }

    private Dir createRDir(int posX, int posY) {
        int tempVar = Math.round(random(3));
        Dir possDir = makeDir(tempVar);
        while (checkGoodDir(possDir, posX, posY)) {
            tempVar = makeDirNum();
            possDir = makeDir(tempVar);
        }
        return possDir;
    }

    private void changeAppIcon() {
        getSurface().setIcon(loadImage("icon.png"));
    }

    private void displayMessages() {
        fill(0, 255, 50);
        for (int i = (messages.size() - 1); i >= 0; i--) {
            text(messages.get(i), 64, 8 + (i * 16));
        }
    }

    private void addLife() {
        lives++;
        livesClaimed++;
        SoundManager.play(Sound.EXTRA_LIFE);
        messages.add("Claimed extra life!");
    }

    private void giveLives() {
        if (score >= 1000 && livesClaimed < 1) {
            addLife();
        } else if (score >= 2000 && livesClaimed < 2) {
            addLife();
        } else if (score >= 5000 && livesClaimed < 3) {
            addLife();
        } else if (score >= 10000 && livesClaimed < 4) {
            addLife();
        } else if (score >= 20000 && livesClaimed < 5) {
            addLife();
        } else if (score >= 50000 && livesClaimed < 6) {
            addLife();
        } else if (score >= 100000 && livesClaimed < 7) {
            addLife();
        } else if (score >= 200000 && livesClaimed < 8) {
            addLife();
        } else if (score >= 500000 && livesClaimed < 9) {
            addLife();
        } else if (score >= 1000000 && livesClaimed < 10) {
            addLife();
        }
    }

    private int createPosition(boolean dirIsX) {
        if (dirIsX) {
            return ((int) (random(3, 12)) * PacStatic.CELLWIDTH) + PacStatic.HALF_CELLWIDTH;
        } else {
            return ((int) (random(1, 12)) * PacStatic.CELLWIDTH) + PacStatic.HALF_CELLWIDTH;
        }
    }

    private boolean checkGoodDir(Dir dir, int posX, int posY) {
        boolean goodDir = true;
        switch (dir) {
            case Dir.UP -> {
                if (posY - 1 >= 0) {
                    goodDir = PacStatic.MAP_DESIGN[posX][posY - 1];
                }
            }
            case Dir.DOWN -> {
                if (posY + 1 <= height / PacStatic.CELLWIDTH) {
                    goodDir = PacStatic.MAP_DESIGN[posX][posY + 1];
                }
            }
            case Dir.RIGHT -> {
                if (posX + 1 <= width / PacStatic.CELLWIDTH) {
                    goodDir = PacStatic.MAP_DESIGN[posX + 1][posY];
                }
            }
            case Dir.LEFT -> {
                if (posX - 1 >= 0) {
                    goodDir = PacStatic.MAP_DESIGN[posX - 1][posY];
                }
            }
        }
        return !goodDir;
    }

    private Dir makeDir(int Var) {
        return switch (Var) {
            case 0 -> Dir.UP;
            case 1 -> Dir.DOWN;
            case 2 -> Dir.RIGHT;
            case 3 -> Dir.LEFT;
            default -> Dir.STOPPED;
        };
    }

    private int makeDirNum() {
        float tempDirNum = random(-0.1f, 4);
        int dirNum = floor(tempDirNum);
        dirNum = constrain(dirNum, 0, 3);
        return dirNum;
    }


    private void destroyUselessMessages() {
        while (messages.size() > 6) {
            messages.removeFirst();
        }
        if (millis() % 25 == 0 && (millis() - startMillis) > 500 && !messages.isEmpty()) {
            messages.removeFirst();
        }
        if (messages.size() > 4) {
            messages.removeFirst();
        }
    }

    private void showLives() {
        int size = 20;
        float sizeT, sizeB;
        sizeT = map(size, 0, 60, 0, 0.52f);
        sizeB = map(size, 0, 60, TWO_PI, 5.76f);
        fill(255, 202, 0);
        for (int x = 1; x < lives; x++) {
            arc(25 * x, height - PacStatic.HALF_CELLWIDTH, 20, 20, sizeT, sizeB);
        }
    }

    private void increaseHighScore() throws FileNotFoundException {
        if (PacStatic.prevHighScore > highScore) {
            highScore = PacStatic.prevHighScore;
        }
        if (score > highScore) {
            highScore = score;
        }
        if (highScore > PacStatic.prevHighScore) {
            PrintWriter out = new PrintWriter(PacStatic.PATH + "/highscore.txt");
            out.println(str(highScore));
            out.close();
        }
    }

    private void restart() {
        messages.clear();
        blinky.newGame();
        inky.newGame();
        pinky.newGame();
        lives = 3;
        lostLife = false;
        level = 0;
        playStartSound = true;
        pelletsEaten = 0;
        pacman.update();
        pacman.x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        pacman.y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
        score = 0;
        determineFruitType();
        for (Pellet value : pellet) {
            value.update();
        }
        durationStart = millis();
        duration = 4500 + millis();
    }

    private void restartButton() {
        int x = PacStatic.CELLWIDTH * 4, y = PacStatic.CELLWIDTH * 12;
        if (hitBoxCollision(x, y, mouseX, mouseY)) {
            restart();
        }
    }

    private void pauseButton() {
        int x = PacStatic.CELLWIDTH * 5, y = PacStatic.CELLWIDTH * 12;
        if (hitBoxCollision(x, y, mouseX, mouseY)) {
            if (paused) {
                SoundManager.stopPauseBeat();
            } else {
                SoundManager.play(Sound.PAUSE);
                if (Preferences.playPauseBeat) {
                    SoundManager.loopPauseBeat();
                }
            }
            paused = !paused;
        }
    }

    private void pauseBeatOffButton() {
        int x = PacStatic.CELLWIDTH * 3, y = PacStatic.CELLWIDTH * 12;
        if (hitBoxCollision(x, y, mouseX, mouseY)) {
            PreferencePane.launch();
        }
    }

    private void drawButtons() {
        image(settingsB, PacStatic.CELLWIDTH * 3.5f, PacStatic.CELLWIDTH * 12.5f);
        image(restartB, PacStatic.CELLWIDTH * 4.5f, PacStatic.CELLWIDTH * 12.5f);
        image(pauseButtonImg, PacStatic.CELLWIDTH * 5.5f, PacStatic.CELLWIDTH * 12.5f/*, PacStatic.CELLWIDTH - 4, PacStatic.CELLWIDTH - 4*/);
    }

    private void determineFruitType() {
        if (level == 1) {
            new LazySpriteLoader();
        }
        if (level < PacStatic.FRUIT_POINTS.length) {
            pellet[5].fruitType = PacStatic.FRUIT_POINTS[level];
        } else {
            pellet[5].fruitType = PacStatic.FRUIT_POINTS[PacStatic.FRUIT_POINTS.length - 1];
        }
    }

    public void mouseClicked() {
        pauseBeatOffButton();
        restartButton();
        pauseButton();
    }

    private void drawGhosts() {
        if (!blinky.dir.equals(Dir.STOPPED)) {
            if ((frameCount - startFrames) % 100 < 45) {
                switch (blinky.dir) {
                    case Dir.UP -> image(blinky_Up[0], blinky.x, blinky.y);
                    case Dir.DOWN -> image(blinky_Down[0], blinky.x, blinky.y);
                    case Dir.RIGHT -> image(blinky_Right[0], blinky.x, blinky.y);
                    case Dir.LEFT -> image(blinky_Left[0], blinky.x, blinky.y);
                }
            } else {
                switch (blinky.dir) {
                    case Dir.UP -> image(blinky_Up[1], blinky.x, blinky.y);
                    case Dir.DOWN -> image(blinky_Down[1], blinky.x, blinky.y);
                    case Dir.RIGHT -> image(blinky_Right[1], blinky.x, blinky.y);
                    case Dir.LEFT -> image(blinky_Left[1], blinky.x, blinky.y);
                }
            }
        }
        if (!inky.dir.equals(Dir.STOPPED)) {
            if ((frameCount - startFrames) % 100 < 45) {
                switch (inky.dir) {
                    case Dir.UP -> image(inky_Up[0], inky.x, inky.y);
                    case Dir.DOWN -> image(inky_Down[0], inky.x, inky.y);
                    case Dir.RIGHT -> image(inky_Right[0], inky.x, inky.y);
                    case Dir.LEFT -> image(inky_Left[0], inky.x, inky.y);
                }
            } else {
                switch (inky.dir) {
                    case Dir.UP -> image(inky_Up[1], inky.x, inky.y);
                    case Dir.DOWN -> image(inky_Down[1], inky.x, inky.y);
                    case Dir.RIGHT -> image(inky_Right[1], inky.x, inky.y);
                    case Dir.LEFT -> image(inky_Left[1], inky.x, inky.y);
                }
            }
        }
        if (!pinky.dir.equals(Dir.STOPPED)) {
            if ((frameCount - startFrames) % 100 < 45) {
                switch (pinky.dir) {
                    case Dir.UP -> image(pinky_Up[0], pinky.x, pinky.y);
                    case Dir.DOWN -> image(pinky_Down[0], pinky.x, pinky.y);
                    case Dir.RIGHT -> image(pinky_Right[0], pinky.x, pinky.y);
                    case Dir.LEFT -> image(pinky_Left[0], pinky.x, pinky.y);
                }
            } else {
                switch (pinky.dir) {
                    case Dir.UP -> image(pinky_Up[1], pinky.x, pinky.y);
                    case Dir.DOWN -> image(pinky_Down[1], pinky.x, pinky.y);
                    case Dir.RIGHT -> image(pinky_Right[1], pinky.x, pinky.y);
                    case Dir.LEFT -> image(pinky_Left[1], pinky.x, pinky.y);
                }
            }
        }
    }

    private final class Ghost {
        private int coordsX;
        private int coordsY;
        private int x;
        private int y;
        private Dir dir;

        private Ghost() {
            x = createPosition(true);
            y = createPosition(false);
            //println("Ghost position: " + x + ", " + y);
            dir = Dir.UP;
        }

        private void updateCoords() {
            final float a = 3, b = 1;
            float offsetY = 0, offsetX = 0;
            switch (dir) {
                case Dir.UP -> offsetY += (PacStatic.CELLWIDTH / a) + b;
                case Dir.DOWN -> offsetY -= (PacStatic.CELLWIDTH / a) + b;
                case Dir.RIGHT -> offsetX -= (PacStatic.CELLWIDTH / a) + b;
                case Dir.LEFT -> offsetX += (PacStatic.CELLWIDTH / a) + b;
            }
            coordsX = Math.round((x + offsetX) / PacStatic.CELLWIDTH + 0.5f) - 1;
            coordsY = Math.round((y + offsetY) / PacStatic.CELLWIDTH + 0.5f) - 1;
        }

        private void update() {
            updateCoords();
            switch (dir) {
                case Dir.UP -> {
                    if (coordsY - 1 >= 0 && PacStatic.MAP_DESIGN[coordsX][coordsY - 1]) {
                        x = coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                        y -= Preferences.ghostSpeed;
                    } else {
                        dir = createRDir(coordsX, coordsY);
                    }
                }
                case Dir.DOWN -> {
                    if (coordsY + 1 <= height / PacStatic.CELLWIDTH && PacStatic.MAP_DESIGN[coordsX][coordsY + 1]) {
                        x = coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                        y += Preferences.ghostSpeed;
                    } else {
                        dir = createRDir(coordsX, coordsY);
                    }
                }
                case Dir.RIGHT -> {
                    if (coordsX + 1 <= width / PacStatic.CELLWIDTH && PacStatic.MAP_DESIGN[coordsX + 1][coordsY]) {
                        y = coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                        x += Preferences.ghostSpeed;
                    } else {
                        dir = createRDir(coordsX, coordsY);
                    }
                }
                case Dir.LEFT -> {
                    if (coordsX - 1 >= 0 && PacStatic.MAP_DESIGN[coordsX - 1][coordsY]) {
                        y = coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                        x -= Preferences.ghostSpeed;
                    } else {
                        dir = createRDir(coordsX, coordsY);
                    }
                }
            }
            if (fastDist(x, y, pacman.x, pacman.y) < PacStatic.HALF_CELLWIDTH) {
                if (x >= PacStatic.CELLWIDTH * 2) {
                    lostLife = true;
                }
            }
        }

        private void up() {
            dir = Dir.UP;
        }        /*private void right() {            dir = Dir.RIGHT;        }*/

        private void halt() {
            dir = Dir.STOPPED;
        }

        private void newGame() {
            x = createPosition(true);
            y = createPosition(false);
            //println("Ghost position: " + x + ", " + y);
            updateCoords();
            while (!PacStatic.MAP_DESIGN[coordsX][coordsY] || (x > PacStatic.CANVAS_WIDTH - PacStatic.CELLWIDTH || x < PacStatic.CELLWIDTH || y > PacStatic.CANVAS_HEIGHT - PacStatic.CELLWIDTH || y < PacStatic.CELLWIDTH)) {
                x = createPosition(true);
                y = createPosition(false);
                //println("Ghost position: " + x + ", " + y);
                messages.add("Adjustment in Progress...");
                updateCoords();
            }
        }
    }

    private final class LazySpriteLoader extends Thread {
        LazySpriteLoader() {
            this.start();
        }

        public void run() {
            messages.add("Loading more fruit sprites...");
            orange = loadImage("orange.png");
            apple = loadImage("apple.png");
            melon = loadImage("melon.png");
            galaxian = loadImage("galaxian.png");
            bell = loadImage("bell.png");
            keyI = loadImage("key.png");
            messages.add("All fruit sprites loaded successfully.");
        }

    }

    final class Pellet {
        final private int x;
        final private int y;
        private boolean eaten = false;
        private boolean isFruit = false;
        private Fruit fruitType = Fruit.CHERRY;

        private Pellet(int x1, int y1) {
            x = x1;
            y = y1;
        }

        void update() {
            eaten = false;
            if (isFruit) {
                fruitWorth = 100;
            }
        }

        private void isBEaten() throws FileNotFoundException {
            if (!eaten && fastDist(x, y, pacman.x, pacman.y) < PacStatic.CELLWIDTH / 8D + Pacman.size / 8D) {
                if (isFruit) {
                    switch (fruitType) {
                        case Fruit.CHERRY -> fruitWorth = 100;
                        case Fruit.STRAWBERRY -> fruitWorth = 300;
                        case Fruit.ORANGE -> fruitWorth = 500;
                        case Fruit.APPLE -> fruitWorth = 700;
                        case Fruit.MELON -> fruitWorth = 1000;
                        case Fruit.GALAXIAN -> fruitWorth = 2000;
                        case Fruit.BELL -> fruitWorth = 3000;
                        case Fruit.KEY -> fruitWorth = 5000;
                    }
                    score += fruitWorth;
                    SoundManager.play(Sound.FRUIT);
                } else {
                    if (!pelletFirst) {
                        SoundManager.play(Sound.WA);
                        pelletFirst = true;
                    } else {
                        SoundManager.play(Sound.KA);
                        pelletFirst = false;
                    }
                    score += 10;
                    pelletsEaten++;
                }
                eaten = true;
                giveLives();
                messages.add("Your score is:" + str(score));
                increaseHighScore();
                if (pelletsEaten >= pellet.length - 1 && !lostLife) {
                    level++;
                    determineFruitType();
                    durationStart = millis();
                    duration = 2000 + millis();
                    System.gc();
                }
            }
        }

        private void draw() {
            if (!eaten) {
                if (isFruit) {
                    switch (fruitType) {
                        case Fruit.STRAWBERRY -> image(strawberry, x, y);
                        case Fruit.ORANGE -> image(orange, x, y);
                        case Fruit.APPLE -> image(apple, x, y);
                        case Fruit.MELON -> image(melon, x, y);
                        case Fruit.GALAXIAN -> image(galaxian, x, y);
                        case Fruit.BELL -> image(bell, x, y);
                        case Fruit.KEY -> image(keyI, x, y);
                        default -> image(cherry, x, y);
                    }
                } else {
                    ellipse(x, y, PacStatic.HALF_CELLWIDTH, PacStatic.HALF_CELLWIDTH);
                }
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Pacman~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    final class Pacman {
        static final float size = PacStatic.CELLWIDTH - 2;
        float x = PacStatic.HALF_CELLWIDTH + PacStatic.CELLWIDTH;
        float y = PacStatic.HALF_CELLWIDTH + PacStatic.CELLWIDTH;
        private boolean stopped = true;
        private Dir nextDir = Dir.STOPPED;
        private Dir dir = Dir.STOPPED;
        private Dir lastDir = Dir.STOPPED;

        private void show(int mouthSize) {
            float mouthOpenTop, mouthOpenBottom;
            if (stopped) {
                mouthSize = 0;
                if (playStartSound) {
                    SoundManager.play(Sound.GAME_START);
                    playStartSound = false;
                }
            }
            mouthOpenTop = map(mouthSize, 0, 60, 0, 0.52f);
            mouthOpenBottom = map(mouthSize, 0, 60, TWO_PI, 5.76f);
            fill(255, 255, 0);
            translate(x, y);
            switch (lastDir) {
                case Dir.UP -> rotate(PI + HALF_PI);
                case Dir.DOWN -> rotate(HALF_PI);
                case Dir.LEFT -> rotate(PI);
            }
            arc(0, 0, size, size, mouthOpenTop, mouthOpenBottom);
        }

        private void update() {
            coordsX = Math.round(((x / PacStatic.CELLWIDTH)) + 0.5f) - 1;
            coordsY = Math.round(((y / PacStatic.CELLWIDTH)) + 0.5f) - 1;
            if (pelletsEaten >= pellet.length - 1) {
                for (Pellet value : pellet) {
                    value.update();
                }
                coordsX = 1;
                coordsY = 1;
                x = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                y = PacStatic.CELLWIDTH + PacStatic.HALF_CELLWIDTH;
                dir = Dir.STOPPED;
                nextDir = Dir.STOPPED;
                pelletsEaten = 0;
            }
            switch (nextDir) {
                case Dir.UP -> {
                    if (PacStatic.MAP_DESIGN[coordsX][coordsY - 1]) {
                        dir = nextDir;
                        x = coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                    }
                }
                case Dir.DOWN -> {
                    if (PacStatic.MAP_DESIGN[coordsX][coordsY + 1]) {
                        dir = nextDir;
                        x = coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                    }
                }
                case Dir.RIGHT -> {
                    if (PacStatic.MAP_DESIGN[coordsX + 1][coordsY]) {
                        dir = nextDir;
                        y = coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                    }
                }
                case Dir.LEFT -> {
                    if (PacStatic.MAP_DESIGN[coordsX - 1][coordsY]) {
                        dir = nextDir;
                        y = coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH);
                    }
                }
            }

            int stopBuffer = 2;
            switch (dir) {
                case Dir.UP -> {
                    if (PacStatic.MAP_DESIGN[coordsX][coordsY - 1]) {
                        y -= Preferences.pacSpeed;
                        stopped = false;
                        if (x < coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            x += Preferences.pacSpeed;
                        }
                        if (x > coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            x -= Preferences.pacSpeed;
                        }
                        lastDir = Dir.UP;
                    } else {
                        if (y <= (coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) + stopBuffer) {
                            dir = nextDir;
                            nextDir = Dir.STOPPED;
                        } else {
                            y -= Preferences.pacSpeed;
                        }
                    }
                }
                case Dir.DOWN -> {
                    if (PacStatic.MAP_DESIGN[coordsX][coordsY + 1]) {
                        y += Preferences.pacSpeed;
                        stopped = false;
                        if (x < coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            x += Preferences.pacSpeed;
                        }
                        if (x > coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            x -= Preferences.pacSpeed;
                        }
                        lastDir = Dir.DOWN;
                    } else {
                        if (y >= (coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) - stopBuffer) {
                            dir = nextDir;
                            nextDir = Dir.STOPPED;
                        } else {
                            y += Preferences.pacSpeed;
                        }
                    }
                }
                case Dir.RIGHT -> {
                    if (PacStatic.MAP_DESIGN[coordsX + 1][coordsY]) {
                        x += Preferences.pacSpeed;
                        stopped = false;
                        if (y < coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            y += Preferences.pacSpeed;
                        }
                        if (y > coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            y -= Preferences.pacSpeed;
                        }
                        lastDir = Dir.RIGHT;
                    } else {
                        if (x >= (coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) - stopBuffer) {
                            dir = nextDir;
                            nextDir = Dir.STOPPED;
                        } else {
                            x += Preferences.pacSpeed;
                        }
                    }
                }
                case Dir.LEFT -> {
                    if (PacStatic.MAP_DESIGN[coordsX - 1][coordsY]) {
                        x -= Preferences.pacSpeed;
                        stopped = false;
                        if (y < coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            y += Preferences.pacSpeed;
                        }
                        if (y > coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                            y -= Preferences.pacSpeed;
                        }
                        lastDir = Dir.LEFT;
                    } else {
                        if (x <= (coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) + stopBuffer) {
                            dir = nextDir;
                            nextDir = Dir.STOPPED;
                        } else {
                            x -= Preferences.pacSpeed;
                        }
                    }
                }
                case Dir.STOPPED -> {
                    if (x < coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                        x += Preferences.pacSpeed;
                    }
                    if (x > coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                        x -= Preferences.pacSpeed;
                    }
                    if (y < coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                        y += Preferences.pacSpeed;
                    }
                    if (y > coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                        y -= Preferences.pacSpeed;
                    }

                }
            }
            if (x > 400) {
                x = PacStatic.CELLWIDTH + size / 2;
            }
            if (y > 400) {
                y = PacStatic.CELLWIDTH + size / 2;
            }
            coordsX = Math.round((x / PacStatic.CELLWIDTH) + 0.5f) - 1;
            coordsY = Math.round((y / PacStatic.CELLWIDTH) + 0.5f) - 1;
        }

        private void up() {
            coordsX = Math.round((x / PacStatic.CELLWIDTH) + 0.5f) - 1;
            coordsY = Math.round((y / PacStatic.CELLWIDTH) + 0.5f) - 1;
            if (PacStatic.MAP_DESIGN[coordsX][coordsY - 1]) {
                dir = Dir.UP;
                nextDir = Dir.UP;
                if (x < coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    x += Preferences.pacSpeed;
                }
                if (x > coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    x -= Preferences.pacSpeed;
                }
            } else {
                nextDir = Dir.UP;
            }
        }

        private void down() {
            coordsX = Math.round((x / PacStatic.CELLWIDTH) + 0.5f) - 1;
            coordsY = Math.round((y / PacStatic.CELLWIDTH) + 0.5f) - 1;
            if (PacStatic.MAP_DESIGN[coordsX][coordsY + 1]) {
                dir = Dir.DOWN;
                nextDir = Dir.DOWN;
                if (x < coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    x += Preferences.pacSpeed;
                }
                if (x > coordsX * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    x -= Preferences.pacSpeed;
                }
            } else {
                nextDir = Dir.DOWN;
            }
        }

        private void right() {
            coordsX = Math.round((x / PacStatic.CELLWIDTH) + 0.5f) - 1;
            coordsY = Math.round((y / PacStatic.CELLWIDTH) + 0.5f) - 1;
            if (PacStatic.MAP_DESIGN[coordsX + 1][coordsY]) {
                dir = Dir.RIGHT;
                nextDir = Dir.RIGHT;
                if (y < coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    y += Preferences.pacSpeed;
                }
                if (y > coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    y -= Preferences.pacSpeed;
                }
            } else {
                nextDir = Dir.RIGHT;
            }
        }

        private void left() {
            coordsX = Math.round((x / PacStatic.CELLWIDTH) + 0.5f) - 1;
            coordsY = Math.round((y / PacStatic.CELLWIDTH) + 0.5f) - 1;
            if (PacStatic.MAP_DESIGN[coordsX - 1][coordsY]) {
                dir = Dir.LEFT;
                nextDir = Dir.LEFT;
                if (y < coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    y += Preferences.pacSpeed;
                }
                if (y > coordsY * PacStatic.CELLWIDTH + (PacStatic.HALF_CELLWIDTH)) {
                    y -= Preferences.pacSpeed;
                }
            } else {
                nextDir = Dir.LEFT;
            }
        }

        private void stop() {
            dir = Dir.STOPPED;
            stopped = false;
            nextDir = Dir.STOPPED;
        }
    }

}
