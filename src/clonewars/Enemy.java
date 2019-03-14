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

/**
 * 22-Feb-2018, 14:37:55.
 *
 * @author Mohammed Ibrahim
 */
abstract class Enemy extends DynamicGameObject {

    public static final int NO_OF_ENEMIES = 3;
    public static final int TYPEA = 0;
    public static final int TYPEB = 1;
    public static final int TYPEC = 2;
    private int type = -1;      //curent type of this enemy

    public static final int ENEMY_CHASE = 0;
    public static final int ENEMY_DEAD = 1;
    public static final int ENEMY_IDLE = 2;
    public static final int ENEMY_OFFSCREEN = 3;
    public int state = ENEMY_OFFSCREEN;

    public static final float DEATH_TIME = 2;   //in seconds
    public float stateTime; //how long enemy has been in a state

    //Player to chase
    protected Player player;    //reference to the player
    protected Circle bounds;    //field hides gameObjects rectangles

    public Enemy(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        float radius = width / 2;
        bounds = new Circle(x + radius, y + radius, radius);
    }

    public void advance() {
        System.out.println("NOT CALLED");
    }

    public void die() {
        System.out.println("NOT CALLED");
    }

    public float getState() {
        System.out.println("NOT CALLED");
        return 0;
    }

    public float getStateTime() {
        System.out.println("NOT CALLED");
        return 0;
    }
    
    public int getType(){
        //Method should be overridden
        System.out.println("NOT CALLED");
        return -1;  //no enemy of type -1
    }

//    @Override
//    void gameUpdate(float deltaTime) {
////        super.gameUpdate(deltaTime); 
//        System.out.println("NOO");
//    }
//
//    @Override
//    void gameRender(Graphics2D g) {
////        super.gameRender(g);
//    }
}
