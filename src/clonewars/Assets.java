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
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Holds all assets used in CloneWars.
 *
 * @version 0.1.0
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
    public static BufferedImage smallCircle;
    public static BufferedImage largeCircle;

    public Assets() {
        System.out.println("Loading assets...");
        loadImages();
    }

    public void loadImages() {
        try {
            player = ImageIO.read(getClass().getResource("/assets/player.png"));
            skull = ImageIO.read(getClass().getResource("/assets/skull.png"));
            skull2 = ImageIO.read(getClass().getResource("/assets/skull2.png"));
            skull3 = ImageIO.read(getClass().getResource("/assets/skull3.png"));

            heart = ImageIO.read(getClass().getResource("/assets/heart.png"));
            splat = ImageIO.read(getClass().getResource("/assets/splat.png"));

            smallCircle = ImageIO.read(getClass().getResource("/assets/smallCircle.png"));
            largeCircle = ImageIO.read(getClass().getResource("/assets/largeCircle.png"));

        } catch (IOException e) {
            System.out.println("Error loading assets...");
        }
    }
}
