class PacStatic {
    final static float VERSION = 14.0f;
    final static String PATH = System.getProperty("user.home");
    final static int CELLWIDTH = 32;
    final static int HALF_CELLWIDTH = 16;
    final static int CANVAS_CENTRE = (int) (CELLWIDTH * 6.5);
    final static boolean[][] MAP_DESIGN = {{false, false, false, false, false, false, false, false, false, false, false, false, false}, {false, true, false, true, true, true, true, true, true, true, true, true, false}, {false, true, true, true, false, false, false, false, false, false, false, true, false}, {false, true, false, true, true, true, true, true, true, true, false, true, false}, {false, true, false, true, false, false, false, false, false, true, false, true, false}, {false, true, true, true, true, true, true, true, true, true, true, true, false}, {false, true, false, true, false, true, false, false, true, false, true, true, false}, {false, true, false, true, false, true, false, true, true, false, true, true, false}, {false, true, false, true, true, true, true, false, true, false, false, true, false}, {false, true, false, false, false, true, false, false, true, false, true, true, false}, {false, true, true, true, true, true, true, false, true, false, true, true, false}, {false, true, true, false, false, false, true, true, true, false, true, true, false}, {false, false, false, false, false, false, false, false, false, false, false, false, false}};
    static int prevHighScore;
    static boolean scaleWasChanged = false;

    static int getFruitID(int level) {
        switch (level) {
            case 3 -> {
                return 2;
            }
            case 4, 5 -> {
                return 3;
            }
            case 6, 7 -> {
                return 4;
            }
            case 8, 9 -> {
                return 5;
            }
            case 10, 11 -> {
                return 6;
            }
        }
        if (level > 11) {
            return 7;
        }
        return level;
    }

    static int getFruitValue(int ID) {
        switch (ID) {
            case 0 -> {
                return 100;
            }
            case 1 -> {
                return 300;
            }
            case 2 -> {
                return 500;
            }
            case 3 -> {
                return 700;
            }
            case 4 -> {
                return 1000;
            }
            case 5 -> {
                return 2000;
            }
            case 6 -> {
                return 3000;
            }
        }
        return 5000;
    }
}