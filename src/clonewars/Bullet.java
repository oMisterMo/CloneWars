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

import common.Circle;
import common.Helper;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 21-Feb-2018, 20:21:51.
 *
 * @author Mohammed Ibrahim
 */
public class Bullet extends DynamicGameObject {

    public static final float BULLET_WIDTH = 7;
    public static final float BULLET_HEIGHT = 7;

    public static final float BULLET_SPEED = 800f;
    public Circle bounds;
    
    //test variables
    public static final float MAX_SPEED = 1000f;
    private float deacceleration = 0;

    public Bullet(float x, float y) {
        super(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        float radius = BULLET_WIDTH / 2;   //had radius = width = 2 (:|)
        bounds = new Circle(x + radius, y + radius, radius);
//        //Direction and angle
//        this.velocity.set(vel);
    }

    private void setSpeed(float speed) {
        //if length == 0, then assume motion angle is 0 deg
        if (velocity.length() == 0) {
            velocity.set(speed, 0);
        } else {
            velocity.setLength(speed);
        }
    }

    /**
     * Test me out sometime
     *
     * @param dt
     */
    public void applyPhysics(float dt) {
        //Apply acceleration
        velocity.add(acceleration.x * dt, acceleration.y * dt);
        float speed = velocity.length();
        //Decrease speed when not accelerating
        if (acceleration.length() == 0) {
            speed -= deacceleration * dt;
        }
        //Kepp speed in bounds
        speed = Helper.Clamp(speed, 0, MAX_SPEED);
        //Update velocity
        setSpeed(speed);
        //Apply velocity
        position.add(velocity.x * dt, velocity.y * dt);
        //Reset acceleration
        acceleration.set(0, 0);

    }

    public boolean isDead() {
        return false;
    }

    public boolean bulletOutOfBounds() {
        return (position.x < 0
                || position.x + Bullet.BULLET_WIDTH > World.WORLD_WIDTH
                || position.y < 0
                || position.y + Bullet.BULLET_HEIGHT > World.WORLD_HEIGHT);
    }

    @Override
    public void gameUpdate(float deltaTime) {
        //Updates bullets position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.center.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) this.position.x, (int) this.position.y,
                (int) BULLET_WIDTH, (int) BULLET_HEIGHT);
//        g.drawRect((int) position.x, (int) position.y,      //rect
//                (int) BULLET_WIDTH, (int) BULLET_HEIGHT);
//        g.drawRect((int)(bounds.center.x-bounds.radius),  //circle
//                (int)(bounds.center.y - bounds.radius), 
//                (int)bounds.radius*2, (int)bounds.radius*2);
    }
}
