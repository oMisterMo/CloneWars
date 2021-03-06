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

import clonewars.Assets;
import java.awt.Graphics2D;

/**
 * This simple class represents a splat left behind when an enemy dies.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Splat extends Droppable {

    public static final float SPLAT_WIDTH = 128;
    public static final float SPLAT_HEIGHT = 128;

    public static final int MAX_NUM_SPLATS = 10;

    /**
     * Constructs a new splat where the x,y coordinates represent the top-left
     * corner of the object.
     *
     * @param x the x position
     * @param y the y position
     */
    public Splat(float x, float y) {
        super(x, y, SPLAT_WIDTH, SPLAT_HEIGHT);
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.drawImage(Assets.splat, (int) position.x, (int) position.y,
                (int) SPLAT_WIDTH, (int) SPLAT_HEIGHT, null);
    }
}
