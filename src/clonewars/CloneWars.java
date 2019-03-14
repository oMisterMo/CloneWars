package clonewars;

import java.awt.Cursor;
import javax.swing.JFrame;

/**
 * Main Class
 *
 * 20-Feb-2018, 21:44:26.
 *
 * @author Mohammed Ibrahim
 */
public class CloneWars {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("Clone Wars");
        GamePanel game = new GamePanel();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        window.add(game);
        window.pack();
//        window.setLocation(70, 50);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);

        window.setAlwaysOnTop(true);
    }

}
