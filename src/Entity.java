abstract class Entity {
    int x, y;

    /*static double fastDist(double x1, double y1, double x2, double y2) {
        return Math.hypot((x1 - x2), (y1 - y2));
    }*/
    abstract void reset();
}