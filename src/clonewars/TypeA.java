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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class TypeA extends Enemy {

    public static final float TYPE_A_WIDTH = 25;
    public static final float TYPE_A_HEIGHT = 25;
    private float speed;

    public TypeA(float x, float y, Player player) {
        super(x, y, TYPE_A_WIDTH, TYPE_A_HEIGHT, player);
        stateTime = 0;
        speed = 100;

        //initial vel = towards player
        float xx = player.position.x - position.x;
        float yy = player.position.y - position.y;
        velocity.set(xx, yy);
        velocity.normalize();
        velocity.mult(speed);
    }

//    public TypeA(float x, float y, float width, float height, Player player, float velX, float velY) {
//        super(x, y, width, height, player);
//        this.velocity.set(velX, velY);
//        stateTime = 0;
//    }
    /**
     * Enemy specific behaviour
     */
    @Override
    public void advance() {
//        Vector2D pos = player.position.sub(position);
        //Attempt2
//        float x = player.bounds.lowerLeft.x - bounds.center.x;
//        float y = player.bounds.lowerLeft.y - bounds.center.y;

        float x = player.position.x - position.x;
        float y = player.position.y - position.y;
        velocity.set(x, y);
        velocity.normalize();
        velocity.mult(speed);
    }

    @Override
    public void die() {
//        System.out.println("STATE = dead");
        state = ENEMY_DEAD;
        stateTime = 0;
        velocity.x = 0;
    }

    @Override
    public float getState() {
        return state;
    }

    @Override
    public float getStateTime() {
        return stateTime;
    }

    @Override
    public int getType() {
        return Enemy.TYPEA;
    }

    public boolean enemyOutOfBounds() {
        return (position.x < 0
                || position.x + TYPE_A_WIDTH > World.WORLD_WIDTH
                || position.y < 0
                || position.y + TYPE_A_HEIGHT > World.WORLD_HEIGHT);
    }

    @Override
    public void gameUpdate(float deltaTime) {
//        super.gameUpdate(deltaTime);    //moves enemy
        if (state == ENEMY_OFFSCREEN) {
            //Update enemies position
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            bounds.center.add(velocity.x * deltaTime, velocity.y * deltaTime);
            if (!enemyOutOfBounds()) {
                state = ENEMY_CHASE;
            }
        } else if (state == ENEMY_CHASE) {
            //Update enemies position
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            bounds.center.add(velocity.x * deltaTime, velocity.y * deltaTime);
            //get new velocity
            advance();
        }
        stateTime += deltaTime;
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.drawImage(Assets.skull2, (int) position.x, (int) position.y,
                (int) TYPE_A_WIDTH, (int) TYPE_A_HEIGHT, null);

        //draw position
//        g.setColor(Color.RED);
//        g.drawRect((int) position.x, (int) position.y,      //rect
//                (int) TYPE_A_WIDTH, (int) TYPE_A_HEIGHT);
//        g.drawRect((int) bounds.center.x, (int) bounds.center.y, 1, 1);   //dot
//        g.drawOval((int) (bounds.center.x - bounds.radius),
//                (int) (bounds.center.y - bounds.radius),
//                (int) TYPE_A_WIDTH, (int) TYPE_A_HEIGHT);
        //draw bounds (test)
//        g.setColor(Color.WHITE);
//        g.drawOval((int) (position.x), //circle
//                (int) (position.y),
//                (int) TYPE_A_WIDTH, (int) TYPE_A_HEIGHT);
    }

}
