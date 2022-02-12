import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.*;





/**
 * Graphics view class.
 * This class is used to control the graphics aspects of the game.
 * This class also manages reading the commands from the keyboard through a KeyListener implementation.
 */

class Graphics_view extends JPanel implements KeyListener{

    private Game game;

    Graphics_view(Game game){
        this.game = game;
        // Make a grid layout
        final GridLayout layout = new GridLayout(game.getState().getRows(), game.getState().getColumns(), 0, 0);
        setLayout(layout);
        addKeyListener(this);
        setFocusable(true);
    }

    /**
     * Paint method used to paint the pictures in the grid.
     * Depending on the symbol found in the grid.
     * The picture is chosen based on the image associated with the object in the Item class
     * @param g: this is the graphics object needed by the paint method
     */

    public void paint(Graphics g) {
        final int sx = (getWidth() - 5 ) / (game.getState().getColumns());
        final int sy = (getHeight() - 20) / (game.getState().getRows()+1);
        final int s = Math.min(sx, sy);
        g.setColor(Color.lightGray);
        for (int row = 0; row < game.getState().getRows(); row++)
            for (int col = 0; col < game.getState().getColumns(); col++) {
                final int x = (col * s) + 10;
                final int y = (row * s) + 20;
                g.drawRect(x,y,s,s);
                if (game.getState().getState()[row][col] != null) {
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File(game.getState().getState()[row][col].getImageName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    g.drawImage(img, x, y, x + s, y + s, 0, 0, img.getWidth(), img.getHeight(), null);
                }
            }
        displayStrings(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Key pressed method which according to the status of the game performs the right action.
     * If the status is active, then the state is changed through the change_Grid_v3 method.
     * @param e: this is the key event
     */

    @Override
    public void keyPressed(KeyEvent e) {

        if( game.getState().getStatus().equals("Loss")){
            this.game.start();
            this.repaint();
            return;
        }
        if (game.getState().getStatus().equals("Win")){
            this.game.nextLevel();
            this.repaint();
            return;
        }
        if (e.getKeyChar() == 's'){
            while (game.getState().getStatus().equals("Active")){
                game.getState().change_Grid_v3('n');
            }
        }
        else {
            game.getState().change_Grid_v3(e.getKeyChar());
        }
        this.repaint();
        e.consume();
    }
    

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Method used to display the correct text at the bottom of the JFrame.
     * The text displayed contains the level, the score, the number of safe jumps, whether the blast is available.
     * Once a level is completed, a message is displayed.
     * @param g: graphics object needed for the method
     */

    private void displayStrings(Graphics g) {
        int ty = this.getHeight() - 16;
        g.setColor(Color.BLUE);
        Font font = new Font("Serif", 0, 14);
        g.setFont(font);
        String text = "Level: " + game.getLevel();
        g.drawString(text, 14, ty);
        text = "Score: " + game.getState().getScore();
        g.drawString(text, 105, ty);
        text = (game.getState().getBlast() ? "[BLAST]" :"[NO BLAST]");
        g.drawString(text, this.getWidth() - 265, ty);
        text = "Safe Jumps: " + game.getState().getNSafe_teleports();
        g.drawString(text, this.getWidth() - 180, ty);
        if (game.getState().getStatus().equals("Win")){
            font = new Font("Serif", 1, 16);
            g.setFont(font);
            text = "YOU WON! PRESS ANY KEY TO CONTINUE";
            g.drawString(text, this.getWidth()/2 - 210, ty);
        }
        else if (game.getState().getStatus().equals("Loss")){
            font = new Font("Serif", 1, 16);
            g.setFont(font);
            text = "YOU LOST! PRESS ANY KEY TO START A NEW GAME";
            g.drawString(text, this.getWidth()/2 - 230, ty);
        }
    }

}

/**
 * GUI class which maintains the Graphics_view object.
 */


class GUI {

    private JFrame frame;
    private Graphics_view graphics;
    private Game game;

    GUI(Game game)
    {
        this.game = game;
    }

    /**
     * Initialise the GUI.
     * This creates the JFrame object and manages its activity upon closing.
     * Also, the Graphics_view object is added to the frame and the frame is set visible.
     */
    void init()
    {
        frame = new JFrame("Androids game");
        int height = (5*100) + 20;
        int width = (8*100) + 10;
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
          Component added to the frame to perform an action after the JFrame is closed.
          Based on the code found here: https://stackoverflow.com/questions/32051657/how-to-perform-action-after-jframe-is-closed.
          This method is used to save the current game to a text file, when the frame is closed.
         */
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                game.saveToFile("test0.txt");
            }
        });

        graphics = new Graphics_view(game);
        graphics.setSize(width, height);

        frame.add(graphics);

        frame.pack();
        frame.setVisible(true);
    }

}
