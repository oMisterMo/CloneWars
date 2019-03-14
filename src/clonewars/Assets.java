/* 
 * Copyright (C) 2019 Mohammed Ibrahim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
