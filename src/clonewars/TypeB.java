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

import common.Helper;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The TypeB enemy sets its initial velocity to the location of the player. It
 * carries on moving slowly in that direction and wraps around when it reaches
 * the bounds of the viewport.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class TypeB extends Enemy {

    public static final float TYPE_B_WIDTH = 25f;
    public static final float TYPE_B_HEIGHT = 25f;

    private float speed;

    /**
     * Constructs a new TypeB enemy at the position given. The initial velocity
     * is set in the direction of the player.
     *
     * @param x the x position
     * @param y the y position
     * @param player reference to the player
     */
    public TypeB(float x, float y, Player player) {
        super(x, y, TYPE_B_WIDTH, TYPE_B_HEIGHT, player);
        stateTime = 0;
        speed = 50;

        //Follows players initial position
        float xx = player.position.x - position.x;
        float yy = player.position.y - position.y;
        velocity.set(xx, yy);
        velocity.normalize();
        velocity.mult(speed);
    }

    @Override
    public void advance() {
    }

    /**
     * This method is called when the enemy is hit.
     */
    @Override
    public void die() {
        state = ENEMY_DEAD;
        stateTime = 0;
        velocity.x = 0;
    }

    /**
     * Gets the current state of the enemy.
     *
     * @return enemy state
     */
    @Override
    public float getState() {
        return state;
    }

    /**
     * Gets the time the enemy has been in a certain state in seconds.
     *
     * @return state time
     */
    @Override
    public float getStateTime() {
        return stateTime;
    }

    /**
     * Gets the type of the enemy.
     *
     * @return enemy type
     */
    @Override
    public int getType() {
        return Enemy.TYPEB;
    }

    /**
     * Checks to see if the enemy is off screen.
     *
     * @return true if the enemy is out of bounds.
     */
    public boolean enemyOutOfBounds() {
        return (position.x < 0
                || position.x + TYPE_B_WIDTH > World.WORLD_WIDTH
                || position.y < 0
                || position.y + TYPE_B_HEIGHT > World.WORLD_HEIGHT);
    }

    private void wrapTypeB() {
//        System.out.println(player.position);
        //Wrap from left
        if (position.x + TYPE_B_WIDTH / 2 < 0 + World.xShift) {
            position.x = World.WORLD_WIDTH - TYPE_B_WIDTH / 2 + World.xShift;
//            bounds.center.x = World.WORLD_WIDTH + World.xShift;
            bounds.center.x = position.x + TYPE_B_WIDTH / 2;
        }
        //Wrap from right
        if (position.x + TYPE_B_WIDTH / 2 > World.WORLD_WIDTH + World.xShift) {
            position.x = (-TYPE_B_WIDTH / 2) + World.xShift;
//            bounds.center.x = bounds.radius + World.xShift;
            bounds.center.x = position.x + TYPE_B_WIDTH / 2;
        }
        //Wrap from top
        if (position.y + TYPE_B_HEIGHT / 2 < 0 + World.yShift) {
            position.y = World.WORLD_HEIGHT - TYPE_B_WIDTH / 2 + World.yShift;
//            bounds.center.y = World.WORLD_HEIGHT + World.yShift;
            bounds.center.y = position.y + TYPE_B_HEIGHT / 2;
        }
        //Wrap from bottom
        if (position.y + TYPE_B_HEIGHT / 2 > World.WORLD_HEIGHT + World.yShift) {
            position.y = (-TYPE_B_HEIGHT / 2) + World.yShift;
//            bounds.center.y = bounds.radius + World.yShift;
            bounds.center.y = position.y + TYPE_B_HEIGHT / 2;
        }
    }

    @Override
    public void gameUpdate(float deltaTime) {
        //Update enemies position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.center.set(position).add(bounds.radius, bounds.radius);

        switch (state) {
            case ENEMY_OFFSCREEN:
                //If enemy is in bounds of world
                if (!enemyOutOfBounds()) {
                    state = ENEMY_CHASE;
                }
                break;
            case ENEMY_CHASE:
                //Update enemies position
                wrapTypeB();
                break;
            case ENEMY_IDLE:
                break;

            case ENEMY_DEAD:
                break;
        }
        stateTime += deltaTime;
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.drawImage(Assets.skull3, (int) position.x, (int) position.y,
                (int) TYPE_B_WIDTH, (int) TYPE_B_HEIGHT, null);

//        //draw bounds
//        g.setColor(Color.YELLOW);
////        g.drawRect((int) position.x, (int) position.y,      //rect
////                (int) TYPE_B_WIDTH, (int) TYPE_B_HEIGHT);
//        g.drawRect((int) bounds.center.x,
//                (int) bounds.center.y, 1, 1);               //dot
//        g.drawOval((int) (bounds.center.x - bounds.radius), //circle
//                (int) (bounds.center.y - bounds.radius),
//                (int) TYPE_B_WIDTH, (int) TYPE_B_HEIGHT);
    }
}
