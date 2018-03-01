/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonewars;

import common.Animation;
import common.SpriteSheet;
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
 * @author Mo
 */
public class Assets {

    //Mario liker payer sheet
    public static BufferedImage testLevel;  //level
    public static BufferedImage level0;  //level

    public static BufferedImage playerSheet;    //main character spritesheet
    public static BufferedImage playerMo;         //player (mo - not used)

    //Mario world sprites
    public static BufferedImage marioTL;    //top left
    public static BufferedImage marioTM;    //top mid
    public static BufferedImage marioTR;    //top right
    public static BufferedImage marioML;    //left
    public static BufferedImage marioMM;    //mid
    public static BufferedImage marioMR;    //right
    public static BufferedImage marioBlock;    //wall 1
    public static BufferedImage marioBlock2;    //wall 2
    public static BufferedImage puzzlebuddyWall;    //my wall

    //1001 Spikes tiles
    private static SpriteSheet spikeSheet;
    public static BufferedImage spikeBlock;
    public static BufferedImage spikeTL;
    public static BufferedImage spikeTM;
    public static BufferedImage spikeTR;
    public static BufferedImage spikeML;
    public static BufferedImage spikeMM;
    public static BufferedImage spikeMR;
    public static SpriteSheet spikeWaterSheet;
    public static Animation spikeWater;
    public static SpriteSheet spikeLavaSheet;
    public static Animation spikeLava;

    public static BufferedImage spikeFireball;

    //Background
    public static BufferedImage cloud;

    //Enemiers
//    public static Animation thwomp;
    public static SpriteSheet enemies;
    public static BufferedImage[] thwomp;
    public static Animation mole;

    public Assets() {
        //Loading all tiles (NOT CALLED)
//        System.out.println("Loading tiles...");
//        loadImages();
    }

    public static void loadImages() {
        try {
            testLevel = ImageIO.read(new File("assets\\testFile.png"));
            level0 = ImageIO.read(new File("assets\\level0.png"));
            playerSheet = ImageIO.read(new File("assets\\playerSheet.png"));
            playerMo = ImageIO.read(new File("assets\\player.png"));

            marioTL = ImageIO.read(new File("assets\\t1.png"));
            marioTM = ImageIO.read(new File("assets\\t2.png"));
            marioTR = ImageIO.read(new File("assets\\t3.png"));
            marioML = ImageIO.read(new File("assets\\t4.png"));
            marioMM = ImageIO.read(new File("assets\\t5.png"));
            marioMR = ImageIO.read(new File("assets\\t6.png"));
            puzzlebuddyWall = ImageIO.read(new File("assets\\wall.png"));
            marioBlock = ImageIO.read(new File("assets\\t10.png"));
            marioBlock2 = ImageIO.read(new File("assets\\t11.png"));

            spikeSheet = new SpriteSheet("assets\\area4.png");
            spikeBlock = spikeSheet.getTile(3, 4, 32, 32);
            //(13,9), (14,9), (15,9)
            spikeTL = spikeSheet.getTile(13, 9, 32, 32);
            spikeTM = spikeSheet.getTile(14, 9, 32, 32);
            spikeTR = spikeSheet.getTile(15, 9, 32, 32);
            spikeML = spikeSheet.getTile(13, 10, 32, 32);
            spikeMM = spikeSheet.getTile(14, 10, 32, 32);
            spikeMR = spikeSheet.getTile(15, 10, 32, 32);
            spikeWaterSheet = new SpriteSheet("assets\\water.png");
            int numOfFrames = 3;
            BufferedImage[] testImage = new BufferedImage[numOfFrames];    //can delete
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = spikeWaterSheet.getTile(i + 1, 1, 32, 32);
            }
            spikeWater = new Animation();
            spikeWater.setFrames(testImage);
            spikeWater.setDelay(300);

            spikeLavaSheet = new SpriteSheet("assets\\lava.png");
            numOfFrames = 4;
            testImage = new BufferedImage[numOfFrames];    //can delete
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = spikeLavaSheet.getTile(i + 1, 1, 32, 32);
            }
            spikeLava = new Animation();
            spikeLava.setFrames(testImage);
            spikeLava.setDelay(200);

            spikeFireball = spikeSheet.getTile(15, 6, 32, 32);
            enemies = new SpriteSheet("assets\\enemies2.png");
            numOfFrames = 3;
            thwomp = new BufferedImage[numOfFrames];
            for (int i = 0; i < numOfFrames; i++) {
                thwomp[i] = ImageIO.read(new File("assets\\thwomp" + (i + 1) + ".png"));
            }
            numOfFrames = 2;
            testImage = new BufferedImage[numOfFrames];
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = enemies.getPosition(((i) * 36) , 0, 36, 32);
            }
            mole = new Animation();
            mole.setFrames(testImage);
            mole.setDelay(300);

            cloud = ImageIO.read(new File("assets\\cloud1.png"));

        } catch (IOException e) {
            System.out.println("Error loading assets (images)...");
        }
    }
}
