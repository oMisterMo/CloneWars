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

import clonewars.StaticGameObject;
import java.awt.Graphics2D;

/**
 * 29-Apr-2018, 18:00:53.
 *
 * @author Mohammed Ibrahim
 */
public abstract class Droppable extends StaticGameObject {

    public static final int LIFE = 0;
    public static final int SHEILD = 1;
    public static int type = -1;

    public Droppable(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public int getType() {
        //Overriden, doesn't get called
        System.out.println("Should not get called");
        return -1;
    }

    @Override
    public void gameUpdate(float deltaTime) {
    }

    @Override
    public void gameRender(Graphics2D g) {
    }
}
