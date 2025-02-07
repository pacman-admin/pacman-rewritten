class PacStatic {
    final static float VERSION = 14.0f;
    final static String PATH = System.getProperty("user.home");
    final static int CELLWIDTH = 32;
    final static int HALF_CELLWIDTH = 16;
    final static int CANVAS_WIDTH = (CELLWIDTH * 13);
    final static int CANVAS_HEIGHT = (CELLWIDTH * 13);
    final static Fruit[] FRUIT_POINTS = {Fruit.CHERRY, Fruit.STRAWBERRY, Fruit.ORANGE, Fruit.ORANGE, Fruit.APPLE, Fruit.APPLE, Fruit.MELON, Fruit.MELON, Fruit.GALAXIAN, Fruit.GALAXIAN, Fruit.BELL, Fruit.BELL, Fruit.KEY, Fruit.KEY};
    final static boolean[][] MAP_DESIGN = {{true, false, true, true, true, true, true, true, true, true, true}, {true, true, true, false, false, false, false, false, false, false, true}, {true, false, true, true, true, true, true, true, true, false, true}, {true, false, true, false, false, false, false, false, true, false, true}, {true, true, true, true, true, true, true, true, true, true, true}, {true, false, true, false, true, false, false, true, false, true, true}, {true, false, true, false, true, false, true, true, false, true, true}, {true, false, true, true, true, true, false, true, false, false, true}, {true, false, false, false, true, false, false, true, false, true, true}, {true, true, true, true, true, true, false, true, false, true, true}, {true, true, false, false, false, true, true, true, false, true, true}};
    static int prevHighScore;
}