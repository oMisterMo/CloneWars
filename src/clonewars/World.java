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

import common.Helper;
import common.OverlapTester;
import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 20-Feb-2018, 22:16:38.
 *
 * @author Mo
 */
public class World extends GameObject {

    public static final float WORLD_WIDTH = 600;
    public static final float WORLD_HEIGHT = 600;
    private Color backgroundColor = Color.BLACK;
//    private Color backgroundColor = Color.DARK_GRAY;

    public static final float TIME_TO_SPAWN = 0.4f;       //spawn enemy
    public static final float DIST_AWAY = 100f;         //distance from screen
    private float elapsedTime = 0;

    private SpatialHashGrid grid;
    private Player player;
    private ArrayList<Enemy> enemies;

    private ParticleSystem death;
    private boolean cFire = false;          //Mouse held down?
    private final Point touchPos = new Point();

//    public static final int xShift = (int) (GamePanel.GAME_WIDTH / 2 - WORLD_WIDTH / 2);
//    public static final int yShift = (int) (GamePanel.GAME_HEIGHT / 2 - WORLD_HEIGHT / 2);
    public static final int xShift = 0;
    public static final int yShift = 0;
    private float scaleTime = 1f;

    public World() {
        init();
        spawnEnemy();
        spawnEnemy();
        System.out.println("background Color: " + backgroundColor);
        System.out.println("World loaded...");
    }

    private void init() {
        player = new Player(
                WORLD_WIDTH / 2 - Player.PLAYER_WIDTH / 2 + xShift,
                WORLD_HEIGHT / 2 - Player.PLAYER_HEIGHT / 2 + yShift,
                Player.PLAYER_WIDTH,
                Player.PLAYER_HEIGHT);
        enemies = new ArrayList<>(50);
//        Enemy typeA = new TypeA(350, 300, TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT, player);
//        typeA.velocity.set(Helper.Random(-100, 100), Helper.Random(-100, 100));
//        enemies.add(typeA);
//        enemies.add(
//                new TypeA(350, 300, TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT,
//                        player,
//                        Helper.Random(-100, 100),
//                        Helper.Random(-100, 100))
//        );
//        enemies.add(
//                new TypeA(350, 300, TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT,
//                        player,
//                        Helper.Random(-100, 100),
//                        Helper.Random(-100, 100))
//        );
//        enemies.add(
//                new TypeA(350, 300, TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT,
//                        player,
//                        Helper.Random(-100, 100),
//                        Helper.Random(-100, 100))
//        );

        death = new ParticleSystem();
        initGrid();
        //-----------------------------END my random test
//        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
//        backgroundColor = new Color(135,206,235);    //Represents colour of background
    }

    private void initGrid() {
        //Experiment with cellsize -> bigger size = more collision checks
        grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 100f);
        //Add all static objects
        //Add all dynamic objects

        grid.printInfo();
        System.out.println("SpatialHashGrid created...");
    }

    public void handleKeyEvents() {
        //Extra inputs
        if (Input.isKeyPressed(KeyEvent.VK_N)) {
            scaleTime = 0.3f;
        } else if (Input.isKeyReleased(KeyEvent.VK_N)) {
            scaleTime = 1f;
        }
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
//            backgroundColor = new Color(0, 0, 0);
            setBackgroundColor(0, 0, 0, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
//            backgroundColor = new Color(100, 120, 100);
            setBackgroundColor(100, 120, 100, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_R)) {
            reset();
        }

        //Handle sub class input
        player.handleInput();
    }

    public void handleMousePressed(MouseEvent e) {
//        System.out.println("WORLD Pressed!");
        int x = e.getX();
        int y = e.getY();
        touchPos.x = x;
        touchPos.y = y;
        System.out.println("x " + x + " y " + y);

//        player.fire(x, y);
        cFire = true;
    }

    public void handleMouseReleased(MouseEvent e) {
//        System.out.println("WORLD RELEASED!");
        cFire = false;
    }

    public void handleMouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        touchPos.x = x;
        touchPos.y = y;
//        player.fire(x, y);
//        cFire = true;
    }

    public void handleMouseMoved(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        player.fire(x, y);
//        mouseCache.x = x;
//        mouseCache.y = y;

//        cFire = true;
//        System.out.println("MOVED");
    }

    private void continousFire() {
        if (cFire) {
            //limit fire rate

            //fire
            player.fire(touchPos.x, touchPos.y);
        }
    }

    private void reset() {
        System.out.println("RESET");
    }

    private void bulletCollision() {
        //Loop through all bullets -> check if it hits an enemy (NAIVE)
        int bulLen = player.bullets.size() - 1;
        int enLen = enemies.size() - 1;

        for (int i = enLen; i >= 0; i--) {
            for (int j = bulLen; j >= 0; j--) {
                Bullet b = player.bullets.get(j);
                Enemy e = enemies.get(i);
//                if (b.bounds.intersects(e.bounds)) {
                if (OverlapTester.overlapCircles(e.bounds, b.bounds)) {
                    //kill enemy (if alive)
                    if (e.getState() != Enemy.ENEMY_DEAD) {
//                        System.out.println("hit");
                        death.playExplosion(e.bounds.center);
                        //play sound

                        //set enemy state
                        e.die();
                    }
                    //remove bullet (or keep for more power)
//                    player.bullets.remove(b);
//                    bulLen = player.bullets.size() - 1;
                }
            }
        }
    }

    private void playerCollision() {
        int len = enemies.size() - 1;
        for (int i = len; i >= 0; i--) {
            Enemy e = enemies.get(i);
//            if (player.bounds.intersects(e.bounds)) {
//            if (OverlapTester.overlapCircles(player.bounds, e.bounds)) {
            if (OverlapTester.overlapCircleRectangle(e.bounds, player.bounds)) {
//                System.out.println("PLAYER HURT!");
                player.lives -= 1;
                System.out.println("player.lives = " + player.lives);
                //kill all enemiees
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy dead = enemies.get(j);
                    death.playExplosion(dead.bounds.center);
                    dead.die();
                }
                //respawn player
            }
        }
    }

    private void enemyCollision() {
        int len = enemies.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                Enemy one = enemies.get(i);
                Enemy two = enemies.get(j);
                resolveEnemyCollision(one, two);
            }
        }
    }

    private void resolveEnemyCollision(Enemy one, Enemy two) {
        if (OverlapTester.overlapCircles(one.bounds, two.bounds)) {
//            System.out.println("TWO HIT");
            Vector2D collisionDist = new Vector2D(
                    two.bounds.center.x - one.bounds.center.x,
                    two.bounds.center.y - one.bounds.center.y);
            float overlap = (one.bounds.radius + two.bounds.radius)
                    - collisionDist.length() + 0.2f; //extra 0.2f (random gap)
            //the length of collisionDist is set to half the overlap
            collisionDist.normalize();
            collisionDist.mult(overlap / 2);

            //Move ball 1 out to one directtion
            two.bounds.center.add(collisionDist);
            two.position.set(two.bounds.center).sub(two.bounds.radius, two.bounds.radius);

            //Move ball 2 out to oppositie diection
//            one.position.add(collisionDist.mult(-1));
            one.bounds.center.add(collisionDist.mult(-1));
            one.position.set(one.bounds.center).sub(one.bounds.radius, one.bounds.radius);;

        }
    }

    private void handleCollisions(float deltaTime) {
//        //Wrap to world
//        wrapPlayer();

        /* Enemies collisions */
        //Bullet - Enemey 
        bulletCollision();
        //Player - Enemy
        playerCollision();
        //Enemy - Enemy
        enemyCollision(); //FPS KILLER
    }

    private void checkGameover() {
        if (player.lives < 0) {
            //GameOver

        }
    }

    private void spawnEnemy() {
        System.out.println("SPAWNING");
        //get point offscreen
        float x, y;
        if (Helper.Random() > 0.5f) {
            //Spawn either Up or Down
            float num = Helper.Random();
            x = Helper.Random(0, WORLD_WIDTH);
            y = (num < 0.5f) ? -DIST_AWAY : WORLD_HEIGHT + DIST_AWAY;
        } else {
            //Spawn either Left or Right
            float num = Helper.Random();
            x = (num < 0.5f) ? -DIST_AWAY : WORLD_WIDTH + DIST_AWAY;
            y = Helper.Random(0, WORLD_HEIGHT);
        }
        spawnType(x, y, Helper.Random(0, Enemy.NO_OF_ENEMIES));
//        enemies.add(
////                new TypeA(x, y, TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT,
////                        player)
////                new TypeC(x, y, TypeB.TYPEB_WIDTH, TypeB.TYPEB_HEIGHT,
////                        player)
//                new TypeC(x, y, TypeC.TYPEC_WIDTH, TypeC.TYPEC_HEIGHT,
//                        player)
//        );
        elapsedTime = 0;
    }

    private void spawnType(float x, float y, int type) {
//        System.out.println("type: "+type);
        switch (type) {
            case Enemy.TYPEA:
                enemies.add(new TypeA(x, y,
                        TypeA.TYPEA_WIDTH, TypeA.TYPEA_HEIGHT, player));
                break;
            case Enemy.TYPEB:
                enemies.add(new TypeB(x, y,
                        TypeB.TYPEB_WIDTH, TypeB.TYPEB_HEIGHT, player));
                break;
            case Enemy.TYPEC:
                enemies.add(new TypeC(x, y,
                        TypeC.TYPEC_WIDTH, TypeC.TYPEC_HEIGHT, player));
                break;
        }
    }

    private void updateEnemies(float deltaTime) {
        if (elapsedTime >= TIME_TO_SPAWN) {
            spawnEnemy();
        }
        int len = enemies.size() - 1;
        for (int i = len; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.gameUpdate(deltaTime);
            //as soon as enemy is hit, remove from array
            if (e.getState() == Enemy.ENEMY_DEAD /*&& e.getStateTime() > Enemy.DEATH_TIME*/) {
                //The particle effect if over, remove enemy
                System.out.println("DEATH");
                enemies.remove(e);
            }
        }
    }

    private void drawEnemies(Graphics2D g) {
        int len = enemies.size() - 1;
        for (int i = len; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.gameRender(g);
        }
    }

    private void handleCollisionHashgrid() {
        List<StaticGameObject> colliders = grid.getPotentialColliders(player);
        int len = colliders.size();
//        System.out.println("potential coll: "+len);
        for (int i = 0; i < len; i++) {
            StaticGameObject collider = colliders.get(i);
//            numOfCollision++;
//            System.out.println(numOfCollision);

            //If player bounds intersects any other static object within cell
//            if (player.bounds.intersects(collider.bounds)) {
//
//            }
        }
    }

    @Override
    void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        elapsedTime += deltaTime;
        deltaTime *= scaleTime; //Objects that are slow mo after this line
        continousFire();

        player.gameUpdate(deltaTime);
        updateEnemies(deltaTime);
        death.gameUpdate(deltaTime);

//        //Check for collisions
//        handleCollisionHashgrid();
        handleCollisions(deltaTime);
        checkGameover();
    }

    @Override
    void gameRender(Graphics2D g) {
        //Clear screen
        g.setColor(backgroundColor);
        g.fillRect(0 + xShift, 0 + yShift, (int) WORLD_WIDTH, (int) WORLD_HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //********** Do drawings HERE **********
        player.gameRender(g);
        drawEnemies(g);
        death.gameRender(g);
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
    }

    private void drawHashGrid(Graphics2D g) {
        g.setColor(Color.YELLOW);
        int cell = (int) grid.getCellSize();
//        cell += (int) WORLD_POS.x;
        //draw vertical
        for (int i = cell; i < WORLD_WIDTH; i += cell) {
//            g.drawLine(i, 0, i, (int) WORLD_HEIGHT);
            g.drawLine(i + xShift, 0 + yShift, i + xShift, (int) WORLD_HEIGHT + yShift);
        }
//        //draw horizontal
        for (int i = cell; i < WORLD_HEIGHT; i += cell) {
            g.drawLine(0 + xShift, i + yShift, (int) WORLD_WIDTH + xShift, i + yShift);
        }
    }

    public void setBackgroundColor(int r, int g, int b, int a) {
        backgroundColor = new Color(r, g, b, a);
    }

    /**
     * Only for debugging to get the players information and draw it
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
}
