package clonewars;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Holds all (currently only spikeBlocks)
 *
 * 1.Store each SpriteSheet once 2.Store each Animation 3.Store each Image
 *
 * 11-Jan-2017, 12:25:37.
 *
 * @author Mohammed Ibrahim
 */
public class Assets {

    public static BufferedImage player;
    public static BufferedImage skull;      //TYPE C
    public static BufferedImage skull2;     //TYPE A
    public static BufferedImage skull3;     //TYPE B
    
    public static BufferedImage heart;
    public static BufferedImage splat;
    
    //Joystick
    public static BufferedImage largeCircle;
    public static BufferedImage smallCircle;

    public Assets() {
        //Loading all tiles (NOT CALLED)
//        System.out.println("Loading tiles...");
//        loadImages();
    }

    public static void loadImages() {
        try {
            player = ImageIO.read(new File("assets\\player.png"));
            skull = ImageIO.read(new File("assets\\skull.png"));
            skull2 = ImageIO.read(new File("assets\\skull2.png"));
            skull3 = ImageIO.read(new File("assets\\skull3.png"));
            
            heart = ImageIO.read(new File("assets\\heart.png"));
            splat = ImageIO.read(new File("assets\\splat.png"));
            
            largeCircle = ImageIO.read(new File("assets\\largeCircle.png"));
            smallCircle = ImageIO.read(new File("assets\\smallCircle.png"));

        } catch (IOException e) {
            System.out.println("Error loading assets (images)...");
        }
    }
}
