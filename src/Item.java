import java.util.HashMap;

/**
 * Item abstract class used for all items.
 * @author simoncalo
 */

abstract class Item {

    private final char symbol;
    private final String imageName;
    private String state;
    final RC location;

    /**
     * Hashmap that contains the name of the picture (value) depending on the symbol (key) of the Item
     */
    private HashMap<Character, String> hmap = new HashMap<>();
    { hmap.put('@', "pictures/player.png"); //player.png
    hmap.put('1', "pictures/robot-green.png"); //robot-green.png
    hmap.put('2', "pictures/robot-red.png"); //robot-red.png
    hmap.put('#', "pictures/heap.png");
    hmap.put('X', "pictures/dead.png");
         }

    /**
     * Item constructor
      * @param symbol: character of the item
     * @param location: RC location of the item
     */

    Item(final char symbol, final RC location){
        this.symbol = symbol;
        this.imageName = hmap.get(symbol);
        this.location = location;
        this.state = "Alive";
    }

    char getSymbol(){
        return symbol;
    }

    String getState(){
        return state;
    }

    /**
     * This Method is used only for robots, when they have to be eliminated from the robot list in the state class.
     * The robots that are dead, are removed.
     */
    void die(){
        this.state = "Dead";
    }

    RC getLocation(){
        return location;
    }

    String getImageName() {
        return imageName;
    }

    boolean isRobot(){
        return (imageName.contains("robot"));
    }

}
