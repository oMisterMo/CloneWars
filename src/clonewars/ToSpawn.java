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

/**
 * The ToSpawn class is an enemy spawn helper. The class stores the type and
 * amount of enemies to spawn on a level.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class ToSpawn {

    private final int type;
    private final int amount;

    /**
     * Initialise the fields to the parameters passed into the constructor.
     *
     * @param type the type of enemy to spawn
     * @param amount the amount of enemies to spawn
     */
    public ToSpawn(int type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Return the type.
     *
     * @return the type of enemy to spawn
     */
    public int getType() {
        return type;
    }

    /**
     * Return the amount.
     *
     * @return the amount of enemies to spawn
     */
    public int getAmount() {
        return amount;
    }
}
