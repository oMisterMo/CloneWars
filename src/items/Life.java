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
package items;

import clonewars.Assets;
import java.awt.Graphics2D;

/**
 * 29-Apr-2018, 17:46:49.
 *
 * @author Mohammed Ibrahim
 */
public class Life extends Droppable {

    public static final float LIFE_WIDTH = 32;
    public static final float LIFE_HEIGHT = 32;
    public static int stateTime;

    public Life(float x, float y, float width, float height) {
        super(x, y, width, height);
        stateTime = 0;
    }

    @Override
    public int getType() {
        return Droppable.LIFE;
    }
    
    

    @Override
    public void gameUpdate(float deltaTime) {

        stateTime += deltaTime;
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.drawImage(Assets.heart, (int) position.x, (int) position.y, null);
        g.drawRect((int) bounds.topLeft.x, (int) bounds.topLeft.y,
                (int) bounds.width, (int) bounds.height);
    }
}