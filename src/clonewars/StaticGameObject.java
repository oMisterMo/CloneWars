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
import common.Vector2D;
//import java.awt.Rectangle;
import common.Rectangle;

/**
 * 05-Feb-2017, 18:54:47.
 *
 * @author Mohammed Ibrahim
 */
public class StaticGameObject extends GameObject {

    protected Vector2D position;
//    protected Vector2D center;
//    protected Rectangle.Float bounds;
    protected Rectangle bounds;

    /**
     * Should initialise here and not in subclass
     *
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     */
    public StaticGameObject(float x, float y, float width, float height) {
        this.position = new Vector2D(x, y);
        this.bounds = new Rectangle(x, y, width, height);
//        this.bounds = new Rectangle.Float(x, y, width, height);
//        this.center = new Vector2D(position.x + width / 2, position.y + height / 2);
//        System.out.println("WIDTH: "+width);
    }

//    public void updateCenter() {
////        System.out.println("updating center");
//        center.x = position.x + bounds.width / 2f;
//        center.y = position.y + bounds.height / 2f;
//    }

    @Override
    public void gameUpdate(float deltaTime) {
    }

    @Override
    public void gameRender(Graphics2D g) {
    }

}
