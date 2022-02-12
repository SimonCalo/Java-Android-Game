public class BadFileFormatException {

    private String problem;
    private int row, column;

    public BadFileFormatException(String problem, int row, int column){

        this.problem = problem;
        this.row = row;
        this.column = column;
    }

    public String toString(){

        return "Exception BadFileFormatException detected at row: " + row + " and column: " + column;

    }
}
