
/**
 * Androids class where the Androids game is started.
 * Here are the indications for playing the game:
 * The player moves with the following keys:
 * - q for moving diagonally left and up
 * - w for moving straight up
 * - e for moving diagonally right and up
 * - a for moving straight left
 * - d for moving straight rights
 * - z for moving diagonally left and down
 * - x for moving straight down
 * - c for moving diagonally right and down
 * Here are other commands for the player:
 * - n to pass a move and remains still
 * - b to use the blast
 * - t to perform a safe jump. If no safe jumps are possible, nothing happens and the state of the game does not change. If the player has zero safe jumps, a random jump is performed.
 * - s to pass until the level ends
 * In this version of the game, if the player moves into a heap, the player will die.
 * When the app is closed and the player is not dead, the game will resume from that state upon reopening.
 * @author simoncalo
 */

public class Androids {

    public static void main (String[] args){

        Game game = new Game();
        GUI gui = new GUI(game);
        gui.init();

    }

}
