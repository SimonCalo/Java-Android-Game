/**
 * Player class used to control the player object and implements its movement.
 * @author simoncalo
 */

public class Player extends Item implements Moveable {
    Player(char symbol, RC location) {
        super(symbol, location);
    }

    /**
     * Method used to make the player move.
     * @param action character of the move that the player needs to perform.
     * In this version, the player passes the turn with n.
     */

    @Override
    public void move(final char action){
        int row_change, col_change;
        switch (action) {
            case 'q':
                row_change = -1; col_change = -1;
                break;

            case 'w':
                row_change = -1; col_change = 0;
                break;

            case 'e':
                row_change = -1; col_change = 1;
                break;

            case 'a':
                row_change = 0; col_change = -1;
                break;

            case 'd':
                row_change = 0; col_change = 1;
                break;

            case 'z':
                row_change = 1; col_change = -1;
                break;

            case 'x':
                row_change = 1; col_change = 0;
                break;

            case 'c':
                row_change = 1; col_change = 1;
                break;

            case 'n':
                row_change = 0; col_change = 0;
                break;

            default:
                return;
        }
        location.changeLoc(row_change, col_change);
    }

    /**
     * Method used to make the player go to a target location (which might be more than one step away).
     * @param target: RC object corresponding to the desired location.
     */

    void jump(RC target){
        int row_change = target.getRow() - location.getRow();
        int col_change = target.getCol() - location.getCol();
        location.changeLoc(row_change, col_change);
    }

    @Override
    public void respond(RC target) {}
}
