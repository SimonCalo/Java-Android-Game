import java.io.*;

/**
 * Game class.
 * This class implements one State object and holds an int level which is the level of the game.
 * @author simoncalo
 *
 */

class Game {

    private int level;
    private State state;

    /**
     * Constructor with no input parameters.
     * This constructor checks if it is possible to load the state from a text file and if not, starts a new game.
     *
     */

    Game(){

        if (!loadFromFile("test0.txt")){
            System.out.println("Generating new grid");
            start();
        }
        else {
            System.out.println("Loading grid from existing file");
        }
    }

    /**
     * Load from file method used to check if a game should be loaded from an existing file.
     * If so, the proper state constructor is called and true is returned.
     * If an X is found in the text file, then this method returns false
     * @param fileName: the name of the text file from which the state should be loaded.
     * @return a boolean that indicates whether it is possible to load from file or not.
     */

    private boolean loadFromFile(final String fileName){

        int level = 1;
        int rows = 0;
        int cols;
        int counter = 0;
        int score = 0;
        int safe_teleports = 0;
        boolean blast = false;
        Item[][] char_grid = new Item[0][0];
        RC loc;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = in.readLine();

            while (line != null)
            {

                if (counter == 0){
                    level = Integer.parseInt(line);
                    this.level = level;
                    rows = level  + 4;
                    cols = (rows*3 + 1)/2;
                    char_grid = new Item[rows][cols];
                }

                if (counter > 2 && counter <= rows+2){
                    String no_tabs = line.replaceAll("\t", "");
                    for (int i = 0; i < no_tabs.length(); i++){
                        loc = new RC(counter-3, i);

                        switch (no_tabs.charAt(i)){

                            case 'X':
                                return false;

                            case '@':
                                Player player = new Player('@', loc);
                                char_grid[counter-3][i] = player;
                                break;

                            case '1':
                                Robot robot = new Robot('1', loc, 1);
                                char_grid[counter-3][i] = robot;
                                break;

                            case '2':
                                Robot robot_2 = new Robot('2', loc, 2);
                                char_grid[counter-3][i] = robot_2;
                                break;

                            case '#':
                                Heap heap = new Heap('#', loc);
                                char_grid[counter-3][i] = heap;
                                break;

                            default:
                                break;
                        }
                    }

                }

                if (counter == rows+3 && line.equals("[BLAST]")){
                    blast = true;
                }

                if (counter == rows + 4){
                    safe_teleports = Integer.parseInt(line.replace("safe teleports: ", ""));
                }
                if (counter == rows + 5){
                    score = Integer.parseInt(line.replace("score: ", ""));
                }

                line = in.readLine();
                counter++;
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.state = new State(level, score, safe_teleports, blast, char_grid);
        return true;
    }

    /**
     * save to file method used to save a certain game to a text file.
     * The game is saved in the form of the toString() method of the State class
     * @param fileName: this is the file name.
     */

    void saveToFile(final String fileName){

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
            PrintWriter print_out = new PrintWriter(out);
            print_out.print(state.toString());
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void start(){
        this.level = 1;
        this.state = new State(level, 0, 5);
    }

    /**
     * Method used to create a state object corresponding to the next level.
     */

    void nextLevel(){
        level++;
        int score  = state.getScore();
        int safe_teleports = state.getSafe_teleports();
        this.state = new State(level, score, safe_teleports + 1);
    }

    State getState(){
        return state;
    }

    int getLevel() {return  level;}




}


