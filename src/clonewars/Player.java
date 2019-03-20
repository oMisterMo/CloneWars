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

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * The Player handles all interactions the user has with the games world. The
 * Player can move in eight directions (up, down, left, right and diagonals).
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH = 32;
    public static final float PLAYER_HEIGHT = 32;

    public static final int NO_OF_LIVES = 3;
    public int lives = NO_OF_LIVES;
    public static final int X_VEL = 200;
    public static final int Y_VEL = 200;

    //Player states
    public static final int PLAYER_ALIVE = 0;
    public static final int PLAYER_HURT = 1;
    public static final int PLAYER_DEAD = 2;
    public int state = PLAYER_ALIVE;
    public float stateTime = 0;

    public ArrayList<Bullet> bullets;

    private float rotation;
    private AffineTransform trans;

    /**
     * Creates a new player at the position provided with its default size.
     *
     * @param x the x position
     * @param y the y position
     */
    public Player(float x, float y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);

        //bullets = new NARRA
        bullets = new ArrayList<>(100);
        trans = new AffineTransform();
        rotation = 90;
        System.out.println("Player created...");
    }

    private void upDownLeftRight() {
        //Up pressed
        if (Input.isKeyPressed(KeyEvent.VK_W)) {
//            System.out.println("W");
            velocity.y = -Y_VEL;
            rotation = 180;
        } else if (Input.isKeyReleased(KeyEvent.VK_W)) {
            velocity.y = 0;
        }
        //Right pressed
        if (Input.isKeyPressed(KeyEvent.VK_D)) {
//            System.out.println("D");
            velocity.x = X_VEL;
            rotation = 270;
        } else if (Input.isKeyReleased(KeyEvent.VK_D)) {
            velocity.x = 0;
        }
        //Down pressed
        if (Input.isKeyPressed(KeyEvent.VK_S)) {
//            System.out.println("S");
            velocity.y = Y_VEL;
            rotation = 0;
        } else if (Input.isKeyReleased(KeyEvent.VK_S)) {
            velocity.y = 0;
        }
        //Left pressed
        if (Input.isKeyPressed(KeyEvent.VK_A)) {
//            System.out.println("A");
            velocity.x = -X_VEL;
            rotation = 90;
        } else if (Input.isKeyReleased(KeyEvent.VK_A)) {
            velocity.x = 0;
        }
    }

    private void diagonals() {
        //Up-Right
        if (Input.isKeyPressed(KeyEvent.VK_W)
                && Input.isKeyPressed(KeyEvent.VK_D)) {
            rotation = 225;
        }
        //Right-Down
        if (Input.isKeyPressed(KeyEvent.VK_D)
                && Input.isKeyPressed(KeyEvent.VK_S)) {
            rotation = 315;
        }
        //Down-Left
        if (Input.isKeyPressed(KeyEvent.VK_S)
                && Input.isKeyPressed(KeyEvent.VK_A)) {
            rotation = 45;
        }
        //Left-Up
        if (Input.isKeyPressed(KeyEvent.VK_A)
                && Input.isKeyPressed(KeyEvent.VK_W)) {
            rotation = 135;
        }
    }

    /**
     * Handles all movement input from the player.
     */
    public void handleInput() {
//        if(state == PLAYER_HURT) return;
        upDownLeftRight();
        diagonals();
    }

    /**
     * Called when the player is hit.
     */
    public void hurt() {
        state = PLAYER_HURT;
    }

    /**
     * Called when the player loses all of its lives.
     */
    public void die() {
//        velocity.set(0,0);
//        state = STATE_DEAD;
    }

    /**
     * NOT USED CURRENTLY!!!!
     *
     * @return true if player is out of bounds
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
            bounds.topLeft.x = World.WORLD_WIDTH - Player.PLAYER_WIDTH + World.xShift;
        }
        //Wrap from right
        if (position.x > World.WORLD_WIDTH - Player.PLAYER_WIDTH + World.xShift) {
            position.x = 0 + World.xShift;
            bounds.topLeft.x = 0 + World.xShift;
        }
        //Wrap from top
        if (position.y < 0 + World.yShift) {
            position.y = World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift;
            bounds.topLeft.y = World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift;
        }
        //Wrap from bottom
        if (position.y > World.WORLD_HEIGHT - Player.PLAYER_HEIGHT + World.yShift) {
            position.y = 0 + World.yShift;
            bounds.topLeft.y = 0 + World.yShift;
        }
    }

    /**
     * Creates a new bullet object and sets the velocity based on the position
     * given. The bullet has a default magnitude.
     *
     * @param x
     * @param y
     */
    public void fire(float x, float y) {
        Bullet bullet = new Bullet(position.x + PLAYER_WIDTH / 2 - Bullet.BULLET_WIDTH / 2,
                position.y + PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2);

        bullet.velocity.set(x - bullet.position.x, y - bullet.position.y);
        bullet.velocity.normalize();
        bullet.velocity.mult(Bullet.BULLET_SPEED);

        bullets.add(bullet);
    }

    private void updateBullets(float deltaTime) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.gameUpdate(deltaTime);
            if (b.bulletOutOfBounds()) {
//                System.out.println("bullet out of bound -> removing");
                bullets.remove(b);
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

    /* ********************* UPDATE & RENDER ************************* */
    @Override
    public void gameUpdate(float deltaTime) {
//        System.out.println(state);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.topLeft.set(position);
        //Update bullets
        updateBullets(deltaTime);

        //Handle collision AFTER we commit our movement
        wrapPlayer();
    }

    @Override
    public void gameRender(Graphics2D g) {
        //Draw player
        drawPlayer(g);
        //Draw fireballs
        drawBullets(g);

//        g.setColor(Color.WHITE);
//        g.drawString("vel: " + velocity, 10, 50);
    }

    private void drawPlayer(Graphics2D g) {
        /*Draw rotated Sprite*/
        AffineTransform old = g.getTransform();
        trans.setToIdentity();  //AffineTransform trans = new AffineTransform();
        //Get the center of the player
        float centerX = (position.x + PLAYER_WIDTH / 2);
        float centerY = (position.y + PLAYER_HEIGHT / 2);
        trans.translate(centerX, centerY);
        trans.rotate(Math.toRadians(rotation));
        trans.translate(-centerX, -centerY);
        g.setTransform(trans);

        g.drawImage(Assets.player, (int) position.x, (int) position.y,
                (int) PLAYER_WIDTH, (int) PLAYER_HEIGHT, null);

        g.setTransform(old);

//        //Draw bounds
//        g.setColor(Color.BLUE);
////        g.draw(bounds);
//        g.drawRect((int) bounds.topLeft.x, (int) bounds.topLeft.y,
//                (int) bounds.width, (int) bounds.height);
//        g.setColor(Color.WHITE);
//        g.drawRect((int) position.x, (int) position.y,
//                (int) PLAYER_WIDTH, (int) PLAYER_HEIGHT);
    }

}
