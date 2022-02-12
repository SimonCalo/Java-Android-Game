/**
 * RC class used as a support class.
 * This class is used to store locations in the form of arrays of a row and column.
 * @author simoncalo
 */
public class RC {

    private int row;
    private int col;
    private int[] location;

    /**
     * RC constructor that creates a list with row and column
     * @param row: int corresponding to row
     * @param col: int corresponding to column
     */

    RC(int row, int col){
        this.row = row;
        this.col = col;
        location = new int[] {row, col} ;}

    int getRow(){
        return row;
    }

    int getCol(){
        return col;
    }

    private void setRow(int new_row){
        row = new_row;
    }

    private void setCol(int new_col){
        col = new_col;
    }

    /**
     * Method used to change a location by a specific amount in each direction.
     * @param rowChange: int corresponding to vertical change
     * @param colChange: into corresponding to horizontal change
     */

    void changeLoc(int rowChange, int colChange){
        setRow(row + rowChange);
        setCol(col + colChange);
    }

    @Override
    public String toString(){
        return("" + row + ", " + col);
    }
}

