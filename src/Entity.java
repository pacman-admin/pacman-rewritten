abstract class Entity {
    int x, y;
    Dir dir;
    int coordsX;
    int coordsY;

    Entity() {
        reset();
    }

    abstract void reset();
}