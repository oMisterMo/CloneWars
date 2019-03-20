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
 * The Bullet class represents a single bullet instance which is fired across
 * the screen. The velocity is set manually after creation.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Bullet extends DynamicGameObject {

    public static final float BULLET_WIDTH = 7;
    public static final float BULLET_HEIGHT = 7;
    public static final float BULLET_SPEED = 800f;
    public Circle bounds;
    public static final float MAX_SPEED = 1000f;
    private float deacceleration = 0;

    /**
     * Constructs a new bullet at x,y which represents the top-left corner of
     * the object.
     *
     * @param x the x position
     * @param y the y position
     */
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
     * Checks if the bullet object is outside of the viewport.
     * 
     * @return true if bullet is out of bounds
     */
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
    }
}
