import processing.core.PApplet;
import processing.core.PImage;

import java.util.logging.Logger;

/**
 * @author Langdon Staab
 * Rewriten 2025
 */
public final class GameWindow extends PApplet {
    private static final Logger LOGGER = LoggerFactory.createLogger(GameWindow.class.getName());
    private boolean paused;
    private int startMillis;
    private int time;
    private int pauseUntil;
    private final Ghost[] ghost = {new Ghost(GhostType.BLINKY), new Ghost(GhostType.INKY), new Ghost(GhostType.PINKY)};

    public static void launch() {
        LOGGER.info("Pac-Man started");
        PApplet.main(new String[]{"GameWindow"});
    }
    private void loadGhostSprites(){

    }

    private void calcTime() {
        time = millis() - startMillis;
    }

    public void setup() {
        noStroke();
        ellipseMode(CENTER);
        textAlign(CENTER, CENTER);
        rectMode(CENTER);
        imageMode(CENTER);

        startMillis = millis();
        calcTime();
        startGame();
    }

    private void startGame() {
        pauseUntil = time + 4500;
        SoundManager.play(Sound.GAME_START);
    }

    public void draw() {
        if (paused) {
            return;
        }
        calcTime();
        if (time < pauseUntil) {
            return;
        }
        if (((frameCount/60) % 2) == 0){

        }
    }


}