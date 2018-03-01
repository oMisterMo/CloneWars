/*
 * Copyright (C) 2018 Mo
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
import java.awt.geom.AffineTransform;

/**
 * 28-Feb-2018, 21:33:04.
 *
 * @author Mo
 */
public class TypeC extends Enemy {

    public static final float TYPEC_WIDTH = 25f;
    public static final float TYPEC_HEIGHT = 25f;
    public static final float ROTATE_SPEED = 100f;

    private float speed;
    private float rotate;
    AffineTransform trans;

    public TypeC(float x, float y, float width, float height, Player player) {
        super(x, y, width, height, player);
        stateTime = 0;
        speed = 100;
        rotate = 45;

        //Follows players initial position
        float xx = player.position.x - position.x;
        float yy = player.position.y - position.y;
        velocity.set(xx, yy);
        velocity.normalize();
        velocity.mult(speed);

        trans = new AffineTransform();
    }

    @Override
    public void advance() {
//        float x = player.position.x - position.x;
//        float y = player.position.y - position.y;
//        if (elapsedTime >= TIME_TO_JUMP) {
//            float ran = Helper.Random();
//            if (ran < 0.25f) {
//                //jump up
//                velocity.set(0, -JUMP_SPEED);
//            } else if (ran < 0.5f) {
//                //jump down
//                velocity.set(0, JUMP_SPEED);
//            } else if (ran < 0.75f) {
//                //jump right
//                velocity.set(JUMP_SPEED, 0);
//            } else {
//                //jump left
//                velocity.set(-JUMP_SPEED, 0);
//            }
//            elapsedTime = 0;
//        }
//        float x = player.position.x - position.x;
//        float y = player.position.y - position.y;
//        velocity.set(x, y);
//        velocity.normalize();
//        velocity.mult(speed);
    }

    @Override
    public void die() {
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

    public boolean enemyOutOfBounds() {
        return (position.x < 0
                || position.x + TYPEC_WIDTH > World.WORLD_WIDTH
                || position.y < 0
                || position.y + TYPEC_HEIGHT > World.WORLD_HEIGHT);
    }

    private void wrapTYPECold() {
//        System.out.println(player.position);
        //Wrap from left
        if (position.x < 0 + World.xShift) {
            position.x = World.WORLD_WIDTH - TYPEC_WIDTH + World.xShift;
//            bounds.center.x = World.WORLD_WIDTH - bounds.radius + World.xShift;
            bounds.center.x = position.x;
        }
        //Wrap from right
        if (position.x > World.WORLD_WIDTH - TYPEC_WIDTH + World.xShift) {
            position.x = 0 + World.xShift;
//            bounds.center.x = bounds.radius + World.xShift;
            bounds.center.x = position.x;
        }
        //Wrap from top
        if (position.y < 0 + World.yShift) {
            position.y = World.WORLD_HEIGHT - TYPEC_HEIGHT + World.yShift;
//            bounds.center.y = World.WORLD_HEIGHT - bounds.radius + World.yShift;
            bounds.center.y = position.y;
        }
        //Wrap from bottom
        if (position.y > World.WORLD_HEIGHT - TYPEC_HEIGHT + World.yShift) {
            position.y = 0 + World.yShift;
//            bounds.center.y = bounds.radius + World.yShift;
            bounds.center.y = position.y;
        }
    }

    private void wrapTypeB() {
//        System.out.println(player.position);
        //Wrap from left
        if (position.x - bounds.radius < 0 + World.xShift) {
            position.x = World.WORLD_WIDTH - bounds.radius + World.xShift;
            bounds.center.x = World.WORLD_WIDTH + World.xShift;
        }
        //Wrap from right
        if (position.x + bounds.radius > World.WORLD_WIDTH + World.xShift) {
            position.x = bounds.radius + World.xShift;
            bounds.center.x = bounds.radius + World.xShift;
        }
        //Wrap from top
        if (position.y - bounds.radius < 0 + World.yShift) {
            position.y = World.WORLD_HEIGHT - bounds.radius + World.yShift;
            bounds.center.y = World.WORLD_HEIGHT + World.yShift;
        }
        //Wrap from bottom
        if (position.y + bounds.radius > World.WORLD_HEIGHT + World.yShift) {
            position.y = bounds.radius + World.yShift;
            bounds.center.y = bounds.radius + World.yShift;
        }
    }

    @Override
    void gameUpdate(float deltaTime) {
//        super.gameUpdate(deltaTime);    //moves enemy
        //Update enemies position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
//        bounds.center.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.center.set(position).add(bounds.radius, bounds.radius);
//        rotate += stateTime;
        rotate += ROTATE_SPEED * deltaTime;
        
        switch (state) {
            case ENEMY_OFFSCREEN:
                //If enemy is in bounds of world
                if (!enemyOutOfBounds()) {
                    state = ENEMY_CHASE;
                }
                break;
            case ENEMY_CHASE:
                //Update enemies position
//                position.add(velocity.x * deltaTime, velocity.y * deltaTime);
//                bounds.center.add(velocity.x * deltaTime, velocity.y * deltaTime);
                wrapTYPECold();
                break;
            case ENEMY_IDLE:
                break;

            case ENEMY_DEAD:
                break;
        }
        stateTime += deltaTime;
    }

    @Override
    void gameRender(Graphics2D g) {
        AffineTransform old = g.getTransform();
        trans.setToIdentity();  //AffineTransform trans = new AffineTransform();
        trans.translate(bounds.center.x, bounds.center.y);
        trans.rotate(Math.toRadians(rotate));
        trans.translate(-bounds.center.x, -bounds.center.y);
        g.setTransform(trans);

        g.setColor(Color.PINK);
        g.fillRect((int) position.x, (int) position.y, //rect
                (int) TYPEC_WIDTH, (int) TYPEC_HEIGHT);

//        g.setColor(Color.WHITE);
//        g.drawRect((int) bounds.center.x, 
//                (int) bounds.center.y, 1, 1);                   //dot
//        g.drawOval((int) (bounds.center.x - bounds.radius),     //circle
//                (int) (bounds.center.y - bounds.radius),
//                (int) TYPEC_WIDTH, (int) TYPEC_HEIGHT);
        g.setTransform(old);

    }
}