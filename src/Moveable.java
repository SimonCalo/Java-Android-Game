/**
 * Moveable interface that both the Player class and the Robot class extend.
 * @author simoncalo
 */

public interface Moveable{
    void move(final char action);
    void respond(final RC target);
}
