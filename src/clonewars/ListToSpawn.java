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
 * The ListToSpawn class stores the type and amount of enemies to spawn for each
 * wave.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class ListToSpawn {

    public ToSpawn[] toSpawn;

    /**
     * The default constructor initialises the array with the amount of unique
     * enemies there are on a given level.
     *
     * @param rangeOfEnemies number of unique enemies from 1 - n (number of
     * enemies)
     */
    public ListToSpawn(int rangeOfEnemies) {
        toSpawn = new ToSpawn[rangeOfEnemies];
    }
}
