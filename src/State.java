import java.util.*;

/**
 * State class.
 * This class is the core of the whole program which controls and modifies the state of the game at all times.
 * @author simoncalo
 *
 *
 * ADD SOMETHING CALLED MAXSTEPSROBOT WHICH RETURNS AN INT THAT TELLS YOU THE MAX NUMBER OF STEPS THAT A ROBOT CAN TAKE IN THIS LEVEL
 */

public class State {


    private int rows, columns, n_robots, n_2S_robots, tot_robots, safe_teleports, total_cells, score;
    private boolean blast;
    private String status;
    final private Item[][] state;
    final private List<Robot> robot_list = new ArrayList<>();
    private Player player;
    final private List<RC> longList = new ArrayList<>();

    /**
     * Default constructor for the State object
     * @param level: The level at which the game starts.
     * @param score: The score that the player has.
     * @param safe_teleports: The number of safe teleports available to the player.
     *
     */

    State(int level, int score, int safe_teleports){

        this.rows = level  + 4;
        this.columns = (rows*3 + 1)/2;
        this.total_cells = rows*columns;
        this.tot_robots = (total_cells)/4;
        this.n_2S_robots = (tot_robots/10) + 1;
        this.n_robots = tot_robots - n_2S_robots;
        this.score = score;
        this.safe_teleports = safe_teleports;
        this.status = "Active";
        this.state = new Item[rows][columns];
        this.blast = true;
        generateGrid_v2();

    }

    /**
     * Alternative constructor for the State class used when the State has to be built from a given grid.
     * This method is used when the game is initialised starting from a text file
     * @param level: The level at which the game starts.
     * @param score: The score at which the game starts.
     * @param safe_teleports: The number of safe teleports that the player has.
     * @param blast: Boolean corresponding to the blast.
     * @param state: This is the grid that the state will be initialised to.
     *
     */
    State(int level, int score, int safe_teleports, boolean blast, Item[][] state){
        this.rows = level  + 4;
        this.columns = (rows*3 + 1)/2;
        this.total_cells = rows*columns;
        this.tot_robots = 0;
        this.n_2S_robots = 0;
        this.n_robots = 0;
        this.score = score;
        this.safe_teleports = safe_teleports;
        this.status = "Active";
        this.state = state;
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (state[i][j]== null){
                    continue;
                }
                else if ( state[i][j].getSymbol() == '1') {
                    robot_list.add((Robot) state[i][j]);
                    n_robots++;
                    tot_robots++;
                }
                else if ( state[i][j].getSymbol() == '2'){
                    robot_list.add((Robot) state[i][j]);
                    n_2S_robots++;
                    tot_robots++;
                }
                else if ( state[i][j].getSymbol() == '@'){
                    this.player = (Player) state[i][j];
                }
            }
        }
        this.blast = blast;
        if (robot_list.size() == 0 || tot_robots == 0){ status = "Win";}
    }

    int getRows(){return rows;}

    int getColumns(){return columns;}

    String getStatus(){
        return status;
    }

    int getScore(){ return score;}

    Item[][] getState() {return  state;}

    /**
     * This method is used by the Game object to decide how many extra teleports need to be given to the next level
     * @return: the number of extra safe teleports for the next level
     */

    int getSafe_teleports(){
        int extra = blast ? 1 : 0;
        return (safe_teleports + extra);
    }

    /**
     * This method is the actual getter for the number of safe teleports.
     * @return: the number of safe teleports.
     */

    int getNSafe_teleports(){ return safe_teleports;}

    boolean getBlast(){ return  blast;}


    /**
     * Method used to generate the initial grid once a new level starts.
     * This is done by creating a list of all the locations in the grid, shuffling this list and then assigning the locations to the Items that need to be in the grid.
     * Empty cells are left null.
     */
    private void generateGrid_v2(){
        for (int i = 0; i<rows; i++){
            for (int j=0; j<columns; j++){
                RC loc = new RC(i, j);
                longList.add(loc);
            }
        }
        Collections.shuffle(longList);
        for (int i = 0; i < n_robots; i++){
            Robot robot = new Robot('1', longList.get(i), 1);
            robot_list.add(robot);
            state [longList.get(i).getRow()][longList.get(i).getCol()] = robot;
        }
        for (int i = n_robots; i < (n_robots + n_2S_robots); i++){
            Robot robot = new Robot('2', longList.get(i), 2);
            robot_list.add(robot);
            state [longList.get(i).getRow()][longList.get(i).getCol()] = robot;
        }
        this.player = new Player('@', longList.get(n_robots + n_2S_robots));
        state [longList.get(n_robots + n_2S_robots).getRow()][longList.get(n_robots + n_2S_robots).getCol()] = player;

    }



    /**
     * This method sets the right content at the given location in the given grid.
     * This content depend on what is the content that we want to move there and what the grid already contains in that location.
     * This method also takes care of decreasing the number of robots and increasing the score consequently
     * @param location: This is the RC object relative to the location where we want something to move to
     * @param content: This is the content that we want to move into the location given by the location parameter
     * @param grid: This is the grid in which we want a certain item to move around
     */

    private void setRightContent(final RC location, final Item content, Item[][] grid){
        if (content == null){
            setLocationContent(location, null, grid);
            return;
        }

        if ((content.getSymbol() == 'X') || (content.getSymbol() == '#')){
            return;
        }
        /*
          If the state grid contains a heap at a certain location, a heap will be created at that the same location in the grid.
         */
        if (state[location.getRow()][location.getCol()] != null && state[location.getRow()][location.getCol()].getSymbol() == '#'){
            Heap heap = new Heap('#', location);
            setLocationContent(location, heap, grid);
        }

        if (grid[location.getRow()][location.getCol()] == null) {
            setLocationContent(location, content, grid);
            //return;
        }
        /*
          If the player moves into a heap, it will die.
         */
        else if ((content.getSymbol() == '@') && (state[location.getRow()][location.getCol()] != null) && (state[location.getRow()][location.getCol()].getSymbol() == '#')){
            Dead dead = new Dead('X', location);
            setLocationContent(location, dead, state);
            status = "Loss";
        }
        /*
          If a robot moves where another robot is, it will die.
          A heap will be created there.
         */
        else if ((grid[location.getRow()][location.getCol()].getSymbol() == '1' || grid[location.getRow()][location.getCol()].getSymbol() == '2') && (content.getSymbol() == '1' || content.getSymbol() == '2')){
            Heap heap = new Heap('#', location);
            grid[location.getRow()][location.getCol()].die();
            content.die();
            setLocationContent(location, heap, grid);
            tot_robots-=2;
            score+=2;
        }
        /*
          If a robot moves into the player's location, the player will die.
         */
        else if ((grid[location.getRow()][location.getCol()].getSymbol() == '@' && (content.getSymbol() == '1' || content.getSymbol() == '2'))){
            Dead dead = new Dead('X', location);
            setLocationContent(location, dead, state);
            status = "Loss";
        }
        /*
          If a robot moves where a heap is, it will die.
          A heap will be created there.
         */
        else if ((grid[location.getRow()][location.getCol()].getSymbol() == '#' || state[location.getRow()][location.getCol()].getSymbol() == '#') && (content.getSymbol() == '1' || content.getSymbol() == '2')){
            Heap heap = new Heap('#', location);
            content.die();
            setLocationContent(location, heap, grid);
            tot_robots--;
            score++;
        }


    }

    /**
     *
     * @param location: the location where we want to set a content
     * @param content: the content we want to set at such location
     * @param grid: the grid where this has to be done
     * This is a simple method used to set a specific content in a specific location of a given grid
     */

    private void setLocationContent(final RC location, final Item content, Item[][] grid){
        grid[location.getRow()][location.getCol()] = content;
    }

    /**
     * safe jump method used to perform a safe jump.
     * Create a temporary list of RC locations (called safespots), loop over it and if a location is safe, then add it to the list.
     * Shuffle the list and then make the player jump to the first location.
     * This method returns true if there are safe jumps possible; false otherwise.
     */

    private boolean safeJump(){
        final List<RC> safespots = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (state[i][j]==null){
                    RC loc = new RC(i,j);
                    if (!isAdjacentRobot(loc)){
                        safespots.add(loc);
                    }
                    else{continue;}
                }
                else {continue;}
            }
        }
        if (safespots.size() > 0) {
            Collections.shuffle(safespots);
            player.jump(safespots.get(0));
            return true;
        }
        else
            return false;
    }

    /**
     * Random jump method used to perform a random jump.
     * Create a temporary list of RC locations (called nullspots), loop over it and if a location is null, then add it to the list.
     * Shuffle the list and then make the player jump to the first location.
     *
     */

    private void randomJump(){
        final List<RC> nullspots = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (state[i][j] == null) {
                    RC loc = new RC(i,j);
                    nullspots.add(loc);
                }
            }
        }
        Collections.shuffle(nullspots);
        player.jump(nullspots.get(0));
    }

    /**
     * useBlast method for using the blast.
     * This method checks all the locations around the player and if a robot is found, it is substituted with a heap.
     * The number of robots (tot_robots) and the score are changed consequently.
     * If the blast is used, its value is then set to false.
     */

    private void useBlast(){
        RC location = player.getLocation();
        List<RC> area = getAdjacentLocations_v2(location, 1);
        for (RC loc: area){
            if (state[loc.getRow()][loc.getCol()] != null && (state[loc.getRow()][loc.getCol()].getSymbol() == '1' || state[loc.getRow()][loc.getCol()].getSymbol() == '2')){
                state[loc.getRow()][loc.getCol()].die();
                Heap heap = new Heap('#', location);
                score++;
                tot_robots--;
                setLocationContent(loc, heap, state);
            }
        }

        blast = false;
    }

    /**
     * Method to get a list of all the locations adjacent to a specific cell.
     * A list of RC is returned.
     * @param location: This is the cell of which we want to get the adjacent locations
     * @param steps: This is the number of steps around that locations that we want to consider
     * The code for this method was heavily based on the code by Mark Byers found here: https://stackoverflow.com/questions/2035522/get-adjacent-elements-in-a-two-dimensional-array
     */
    private List<RC> getAdjacentLocations_v2(RC location, int steps){
        List<RC> adjacent_locs = new ArrayList<>();
        RC adj;
        for (int dx = (location.getCol() > (steps-1) ? -steps : 0); dx <= (location.getCol() < (columns-steps) ? steps : 0); ++dx){
            for (int dy = (location.getRow() > (steps-1) ? -steps : 0); dy <= (location.getRow() < (rows-steps) ? steps : 0); ++dy){
                if (dx !=0 || dy !=0){
                    adj  = new RC(location.getRow() + dy, location.getCol() + dx);
                    adjacent_locs.add(adj);
                }
            }
        }

        return adjacent_locs;

    }

    /**
     * Method to check if there is a robot adjacent to a given location.
     * By adjacent is meant whether there is any robot in a 1 cell radius or any 2 steps robot in a 2 cells radius
     * @param location: This is the location that we want to check for ajdacent robots
     * @return: The method return false if there are no adjacent robots, true otherwise.
     */

    private boolean isAdjacentRobot(RC location){
        for (RC loc: getAdjacentLocations_v2(location, 1)){
            if (state[loc.getRow()][loc.getCol()] == null){continue;}
            if (state[loc.getRow()][loc.getCol()].getSymbol() == '1' || state[loc.getRow()][loc.getCol()].getSymbol() == '2'){return true;}
            //if (state[loc.getRow()][loc.getCol()].isRobot() && (((Robot) state[loc.getRow()][loc.getCol()]).getSteps() >= steps)){return true;}

        }
        for (RC loc: getAdjacentLocations_v2(location, 2)){
            if (state[loc.getRow()][loc.getCol()] == null){continue;}
            //System.out.println("location is: row " + loc.getRow() + "column " + loc.getCol());
            if (state[loc.getRow()][loc.getCol()].getSymbol() == '2'){return true;}
        }
        return false;
    }

    /**
     * Method to check if a given RC location is out of bound for the state grid
     */

    private boolean isLocationOutOfBound(RC location){
        return ((location.getRow() >= rows) || (location.getRow() < 0) || (location.getCol() >= columns) || (location.getCol() < 0));
    }


    /**
     * This method is used to change the grid according to the player's move.
     * This is done by creating a temporary empty grid (called temp_state) in which the player moves and then all the robots move.
     * Then, after all robots have moved, the content of temp_state is passed to state through a loop.
     * The content of each cell in this temporary grid is governed by the setRightContent method.
     * This method is also used to check if the player has won the level (i.e. tot_robots == 0).
     * @param action: The character corresponding to the action that will be performed by the player.
     *
     */


    void change_Grid_v3(final char action){
        final Item[][] temp_state = new Item[rows][columns];
        final int old_p_row = player.getLocation().getRow();
        final int old_p_col = player.getLocation().getCol();
        RC rc = new RC(old_p_row, old_p_col);
        if (!(action == 'q' || action == 'w' || action == 'e' || action == 'a' || action == 'd' || action == 'z' || action == 'x' || action == 'c' || action == 'n' || action == 't' || action == 'b' || action == 's')){
            return;
        }
        if (action == 'b'){
            if (blast) {
                useBlast();
                if (robot_list.size() == 0 || tot_robots == 0){ status = "Win";}
            }
            else
                return;
        }
        if (action == 't'){
            if (safe_teleports>0){
                boolean safe = safeJump();
                /*
                  If there are no safe jumps available, no action is performed.
                 */
                if (!safe){
                    return;
                }
                safe_teleports--;
            }
            else { randomJump(); }
        }

        player.move(action);

        if (isLocationOutOfBound(player.getLocation())){
            player.jump(rc);
            return;
        }
        else {
            setRightContent(player.getLocation(), player, temp_state);
        }
/*
  If the status is loss, the old location player is set to null and no more action is performed.
 */
        if(status.equals("Loss")){
            setRightContent(rc, null, state);
            return;
        }
/*
  This part of the method loops over the robot_list and makes each robot respond according to the number of steps that they should take.
  If a robot is dead, it is removed from the list and the counter is not increased.
  If a robot is alive, the robot responds and the counter is increased.
 */
        int counter = 0;
        while (counter < robot_list.size()){
            Robot r = robot_list.get(counter);
            if (r.getState().equals("Alive")) {
                for (int i = 0; i < r.getSteps(); i++){
                    if (i == 1 && r.getState().equals("Alive")){
                        setLocationContent(r.getLocation(), null, temp_state);
                    }
                    r.respond(player.getLocation());
                    setRightContent(r.getLocation(), r, temp_state);
                    if (r.getState().equals("Dead")){
                        robot_list.remove(r);
                        counter--;
                        break;
                    }
                }
                counter++;
            }
            else if (r.getState().equals("Dead")) {
                robot_list.remove(r);
            }

        }
        /*
          In the cells where in the state grid there are heaps or dead, nothing is changed.
          In the other cells, the content is set to be the same as the one in the temp_state grid.
         */
        for (int i = 0; i<rows; i++){
            for (int j = 0; j<columns; j++) {
                if (state[i][j] != null && (state[i][j].getSymbol() == '#' || state[i][j].getSymbol() == 'X')){ continue; }
                else {state[i][j] = temp_state[i][j];}
            }
        }
        /*
          If there are no more robots, the player wins the level.
         */
        if (robot_list.size() == 0 || tot_robots == 0){ status = "Win";}
    }

    /**
     * toString method which returns the grid and the content of each cell in the form of a character.
     * The level, the number or rows and columns, the blast, safe teleports and score are also returned.
     * @return: The correct string is returned.
     */
    @Override
    public String toString(){

        String str =  "" + (rows-4) + "\n" + rows + "\n" + columns + "\n";
        for (int i = 0; i<rows; i++){
            for (int j = 0; j<columns; j++){
              str += ((state[i][j]==null) ? "." : state[i][j].getSymbol()) + "\t";
        }
              str += "\n";
        }
        str += (blast ? "[BLAST]" :"[NO BLAST]") + "\n";
        str += "safe teleports: " + safe_teleports + "\n";
        str += "score: " + score;
        return str;
    }
}
