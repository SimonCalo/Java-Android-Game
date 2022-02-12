
/**
 * Robot class used to control the robot object and to implement its movement.
 * @author simoncalo
 */

public class Robot extends Item implements Moveable{
    private final int steps;
    Robot(char symbol, RC location, int steps) {
        super(symbol, location);
        this.steps = steps; //number of steps that the robot takes each turn
    }

    @Override
    public void move(char action) {}

    /**
     * Respond method used to make a robot object respond, by moving towards a target location by 1 step.
     * The change in row and column are calculated by taking the difference between the target location and the current location and dividing it by its length.
     * This results in a unitary displacement.
     * @param target: this is the RC location that the robot is moving to.
     */
    @Override
    public void respond(RC target) {

        int target_row = target.getRow();
        int target_col = target.getCol();
        int row_change;
        int col_change;
        if (location.getRow() == target_row) {
            row_change = 0;
        } else {
            row_change = (target_row - location.getRow()) / (Math.abs(target_row - location.getRow()));
        }
        if (location.getCol() == target_col) {
            col_change = 0;
        } else {
            col_change = (target_col - location.getCol()) / (Math.abs(target_col - location.getCol()));
        }
        location.changeLoc(row_change, col_change);

    }

    int getSteps(){
        return steps;
    }
}
