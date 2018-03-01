package clonewars;

import common.Animation;
import common.SpriteSheet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import common.Vector2D;
import java.util.ArrayList;

/**
 * Represents a player within our game world
 *
 * 32 pixels = 1 meter
 *
 * 07-Sep-2016, 01:56:02.
 *
 * @author Mo
 */
public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH = 28;    //32
    public static final float PLAYER_HEIGHT = 28;   //32
    
    public static final int NO_OF_LIVES = 3;
    private static final int X_VEL = 200;
    private static final int Y_VEL = 200;

    //Player states
    public static final int PLAYER_ALIVE = 0;
    public static final int PLAYER_HURT = 1;
    public static final int PLAYER_DEAD = 2;
    public int state = PLAYER_ALIVE;
    public float stateTime = 0;

    public ArrayList<Bullet> bullets;
    public int lives = NO_OF_LIVES;

    //For debugging****************************
    private Font f;
    private Vector2D fontPos;
    private Stroke stroke;
    private String state_debug = "FALLING";
    private int numOfCollision = 0;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);

        initFont();
        //bullets = new NARRA
        bullets = new ArrayList<>(100);
        System.out.println("Player created...");
    }

    private void initFont() {
//        f = new Font("Comic Sans MS", Font.PLAIN, 25);
//        f = new Font("Times new roman", Font.PLAIN, 25);
        f = new Font("Courier new", Font.PLAIN, 25);
        fontPos = new Vector2D(5, 60);
        stroke = new BasicStroke(1);
    }

    public void handleInput() {
        //Up pressed
        if (Input.isKeyPressed(KeyEvent.VK_W)) {
//            System.out.println("W");
            velocity.y = -Y_VEL;
        } else if (Input.isKeyReleased(KeyEvent.VK_W)) {
            velocity.y = 0;
        }
        //Right pressed
        if (Input.isKeyPressed(KeyEvent.VK_D)) {
//            System.out.println("D");
            velocity.x = X_VEL;
        } else if (Input.isKeyReleased(KeyEvent.VK_D)) {
            velocity.x = 0;
        }
        //Down pressed
        if (Input.isKeyPressed(KeyEvent.VK_S)) {
//            System.out.println("S");
            velocity.y = Y_VEL;
        } else if (Input.isKeyReleased(KeyEvent.VK_S)) {
            velocity.y = 0;
        }
        //Left pressed
        if (Input.isKeyPressed(KeyEvent.VK_A)) {
//            System.out.println("A");
            velocity.x = -X_VEL;
        } else if (Input.isKeyReleased(KeyEvent.VK_A)) {
            velocity.x = 0;
        }
    }

    public void hurt(){
        state = PLAYER_HURT;
    }
    public void die() {
//        velocity.set(0,0);
//        state = STATE_DEAD;
    }

    /**
     * NOT USED CURRENTLY!!!!
     *
     * @return
     */
    public boolean playerOutOfBounds() {
        return (position.x < 0
                || position.x + PLAYER_WIDTH > World.WORLD_WIDTH
                || position.y < 0
                || position.y + PLAYER_HEIGHT > World.WORLD_HEIGHT);
    }

    private void wrapPlayer() {
//        System.out.println(player.position);
        //Wrap from left
        if (position.x < 0 + World.xShift) {
            position.x = World.WORLD_WIDTH - Player.PLAYER_WIDTH + World.xShift;
            bounds.lowerLeft.x = World.WORLD_WIDTH - Player.PLAYER_WIDTH + World.xShift;
        }
        //Wrap from right
        if (position.x > World.WORLD_WIDTH - Player.PLAYER_WIDTH + World.xShift) {
            position.x = 0 + World.xShift;
            bounds.lowerLeft.x = 0 + World.xShift;
        }
        //Wrap from top
        if (position.y < 0 + World.yShift) {
            position.y = World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift;
            bounds.lowerLeft.y = World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift;
        }
        //Wrap from bottom
        if (position.y > World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift) {
            position.y = 0 + World.yShift;
            bounds.lowerLeft.y = 0 + World.yShift;
        }
    }

    public void fire(int x, int y) {
        Bullet bullet = new Bullet(position.x + PLAYER_WIDTH / 2 - Bullet.BULLET_WIDTH / 2,
                position.y + PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2,
                Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT);
        bullet.velocity.set(x - bullet.position.x, y - bullet.position.y);

        bullet.velocity.normalize();
        bullet.velocity.mult(Bullet.BULLET_SPEED);
//        bullet.velocity.setLength(300);
//        float angle = bullet.velocity.angle();
//        System.out.println("angle: " + angle);
//        System.out.println("len: " + bullet.velocity.length());

        //a new bullet to the arraylist
        bullets.add(bullet);
//        System.out.println("bullets.size() = " + bullets.size());
    }

    private void updateBullets(float deltaTime) {
        int size = bullets.size();
        for (int i = size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.gameUpdate(deltaTime);
            if (b.bulletOutOfBounds()) {
//                System.out.println("bullet out of bound -> removing");
                bullets.remove(b);
//                size = bullets.size();
            }
        }
    }

    private void drawBullets(Graphics2D g) {
        int size = bullets.size();
        for (int i = size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.gameRender(g);
        }
    }

    /**
     * ************UPDATE & RENDER
     *
     **************
     * @param deltaTime
     */
    @Override
    void gameUpdate(float deltaTime) {
//        System.out.println(state);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
//        bounds.lowerLeft.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position);
        //Update bullets
        updateBullets(deltaTime);

        //Handle collision AFTER we commit our movement
        wrapPlayer();
    }

    @Override
    void gameRender(Graphics2D g) {
        //Draw player
        drawHitbox(g);
        //Draw fireballs
        drawBullets(g);
    }

    private void drawHitbox(Graphics2D g) {
        //Draw bounds
        g.setColor(Color.BLUE);
//        g.draw(bounds);
        g.drawRect((int) bounds.lowerLeft.x, (int) bounds.lowerLeft.y,
                (int) bounds.width, (int) bounds.height);
//        g.setColor(Color.WHITE);
//        g.drawRect((int) position.x, (int) position.y,
//                (int) PLAYER_WIDTH, (int) PLAYER_HEIGHT);
    }

    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(f);
//        g.drawString("Draw REAL POS", fontPos.x, fontPos.y);
//        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Pos: " + "x: " + (int) bounds.lowerLeft.x
                + ", y: " + (int) bounds.lowerLeft.y, fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity),
                fontPos.x, fontPos.y + 35);
//        g.drawString("Acc: " + String.valueOf(Level.gravity), fontPos.x, fontPos.y + 65);
        g.drawString(state_debug, fontPos.x, fontPos.y + 95);
        g.drawString("Col: " + String.valueOf(numOfCollision), fontPos.x, fontPos.y + 125);
    }

}
