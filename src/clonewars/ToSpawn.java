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
package clonewars;

/**
 * 02-Mar-2018, 15:58:56.
 *
 * @author Mohammed Ibrahim
 */
public class ToSpawn {

    private final int type;
    private final int amount;

    public ToSpawn(int type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /* Getters & Setters */
    public int getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
