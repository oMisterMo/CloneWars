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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Shield extends Droppable {

    public static final float SHEILD_WIDTH = 25;
    public static final float SHEILD_HEIGHT = 25;

    public Shield(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public int getType() {
        return Droppable.SHEILD;
    }

    @Override
    public void gameUpdate(float deltaTime) {
    }

    @Override
    public void gameRender(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillOval((int) position.x, (int) position.y,
                (int) Shield.SHEILD_WIDTH, (int) Shield.SHEILD_HEIGHT);
    }

}
