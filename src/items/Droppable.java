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
package items;

import clonewars.StaticGameObject;
import java.awt.Graphics2D;

/**
 * The abstract class Droppable represents all droppable items.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public abstract class Droppable extends StaticGameObject {

    public static final int LIFE = 0;
    public static final int SHEILD = 1;
    public static int type = -1;

    /**
     * Initialises a droppable game object.
     *
     * @param x the x position
     * @param y the y position
     * @param width the width of the item
     * @param height the height of the item
     */
    public Droppable(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Gets the specific type of item.
     *
     * @return the unique integer representing the item
     */
    public int getType() {
        //Overriden, doesn't get called
        System.out.println("Should not get called");
        return -1;
    }

    public void gameUpdate(float deltaTime) {
    }

    public void gameRender(Graphics2D g) {
    }
}
