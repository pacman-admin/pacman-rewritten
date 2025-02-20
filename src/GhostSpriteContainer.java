import processing.core.PImage;

public class GhostSpriteContainer {
        final PImage[] up = new PImage[2];
        final PImage[] right = new PImage[2];
        final PImage[] down = new PImage[2];
        final PImage[] left = new PImage[2];
        GhostSpriteContainer(PImage up_0, PImage up_1, PImage right_0, PImage right_1, PImage down_0, PImage down_1, PImage left_0, PImage left_1){
            up[0] = up_0;
            up[1] = up_1;
            right[0] = right_0;
            right[1] = right_1;
            down[0] = down_0;
            down[1] = down_1;
            left[0] = left_0;
            left[1] = left_1;
    }
}
