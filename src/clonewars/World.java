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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 20-Feb-2018, 22:16:38.
 *
 * @author Mo
 */
public class World extends GameObject {

    public static final float WORLD_WIDTH = 600;
    public static final float WORLD_HEIGHT = 600;

    public static final float GUN_COOLDOWN = 0.064f;    //seconds
    private static float elapsedGun = 0;

    public static final float TIME_TO_SPAWN = 0.4f;     //spawn enemy
    public static final float DIST_AWAY = 100f;         //distance from screen
    public static final int NO_OF_WAVES = 5;
    public static int currentWave = 0;  //0 means 1...duh
    public static boolean waveComplete = false;
    private static ListToSpawn[] waves;
    private static int[] enemiesToKill;
    private static int[] currentEnemiesAlive;
    private static int[] spawnTracker;

    private SpatialHashGrid grid;
    private Player player;
    private ArrayList<Enemy> enemies;

    private ParticleSystem death;
    private boolean cFire = false;          //Mouse held down?
    private final Vector2D touchPos = new Vector2D();

    private Color backgroundColor = Color.BLACK;
    public static final int xShift = 0; //(int) (GamePanel.GAME_WIDTH / 2 - WORLD_WIDTH / 2)
    public static final int yShift = 0;
    private float scaleTime = 1f;
    private float elapsedTime = 0;

    public World() {
        init();
//        spawnEnemy();
//        spawnEnemy();
        System.out.println("background Color: " + backgroundColor);
        System.out.println("World loaded...");
    }

    private void init() {
        initWaves();
        enemiesToKill = new int[Enemy.NO_OF_ENEMIES];
        currentEnemiesAlive = new int[Enemy.NO_OF_ENEMIES];
        spawnTracker = new int[Enemy.NO_OF_ENEMIES];

        player = new Player(
                WORLD_WIDTH / 2 - Player.PLAYER_WIDTH / 2 + xShift,
                WORLD_HEIGHT / 2 - Player.PLAYER_HEIGHT / 2 + yShift,
                Player.PLAYER_WIDTH,
                Player.PLAYER_HEIGHT);
        enemies = new ArrayList<>(50);

        setEnemiesToKill();
        printDebug();
        death = new ParticleSystem();
        initGrid();
        //-----------------------------END my random test
//        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
//        backgroundColor = new Color(135,206,235);    //Represents colour of background
    }

    private void initWaves() {
        /*
         Trying to store the data type {id, {int, int][]}
         could possible be done better using a text file
         */
        //Wave 1
        waves = new ListToSpawn[NO_OF_WAVES];
        waves[0] = new ListToSpawn(1);
        waves[0].toSpawn[0] = new ToSpawn(Enemy.TYPEA, 10);

        //Wave 2
        waves[1] = new ListToSpawn(2);
        waves[1].toSpawn[0] = new ToSpawn(Enemy.TYPEA, 3);
        waves[1].toSpawn[1] = new ToSpawn(Enemy.TYPEB, 5);

        //Wave 3
        waves[2] = new ListToSpawn(3);
        waves[2].toSpawn[0] = new ToSpawn(Enemy.TYPEA, 6);
        waves[2].toSpawn[1] = new ToSpawn(Enemy.TYPEB, 30);
        waves[2].toSpawn[2] = new ToSpawn(Enemy.TYPEC, 30);

        //Wave 4
        waves[3] = new ListToSpawn(2);
        waves[3].toSpawn[0] = new ToSpawn(Enemy.TYPEC, 15);
        waves[3].toSpawn[1] = new ToSpawn(Enemy.TYPEB, 5);

        //Wave 5
        waves[4] = new ListToSpawn(1);
        waves[4].toSpawn[0] = new ToSpawn(Enemy.TYPEC, 100);

        System.out.println("Initialised waves...");
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
        if (elapsedGun >= GUN_COOLDOWN) {
            if (cFire) {
            //limit fire rate

                //fire
                player.fire(touchPos.x, touchPos.y);
            }
            elapsedGun = 0;
        }
    }

    private void reset() {
        System.out.println("RESET");
    }

    private void printDebug() {
        for (int i = 0; i < Enemy.NO_OF_ENEMIES; i++) {
            System.out.println("Type " + (i + 1)
                    + " to be killed: " + enemiesToKill[i]);
        }
        for (int i = 0; i < Enemy.NO_OF_ENEMIES; i++) {
            System.out.println("spawned type " + i + ": " + spawnTracker[i]);
        }
    }

    private void killAllEnemies() {
        for (int j = 0; j < enemies.size(); j++) {
            Enemy dead = enemies.get(j);
            death.playExplosion(dead.bounds.center);
            dead.die();

            //Enemy must get repawned so that we can kill it with fire!
            spawnTracker[dead.getType()]--;
        }
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
                        //Increament death counter of current enemy
                        enemiesToKill[e.getType()]--;
//                        System.out.println("hit");
                        death.playExplosion(e.bounds.center);
                        //play sound

                        //set enemy state
                        e.die();
                        //check if level is complete
                        waveComplete = isWaveComplete();

                        /**
                         * *Debugging only**
                         */
//                        printDebug();
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
                killAllEnemies();
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
        //Bullet - Enemey 
        bulletCollision();
        //Player - Enemy
        playerCollision();
        //Enemy - Enemy
        enemyCollision(); //FPS KILLER
    }

    private void checkGameover() {
        if (player.lives <= 0) {
            //GameOver
            GamePanel.state = GamePanel.WORLD_STATE_GAMEOVER;
        }
    }

    /*FIX ME*/
    private boolean isWaveComplete() {
//        int a = enemiesKilled[Enemy.TYPEA];
//        int b = enemiesKilled[Enemy.TYPEB];
//        int c = enemiesKilled[Enemy.TYPEC];
//        boolean b = false;

        /*THIS METHOD WILL FAIL IF ENEMIES ORDER IS SWITCHED*/
        int numEnemies = waves[currentWave].toSpawn.length;
//        for (int i = 0; i < numEnemies; i++) {
        for (int i = 0; i < Enemy.NO_OF_ENEMIES; i++) {
            //No of enemies killed >= amount of enemies per wave
//            System.out.println(waves[currentWave].toSpawn[i].getAmount());
//            if (enemiesKilled[i] <= waves[currentWave].toSpawn[i].getAmount()) {
//                return false;
//            }
            if (enemiesToKill[i] > 0) {
                return false;
            }
        }
        System.out.println("***LEVEL OVER***");
        return true;
    }

    private void spawnEnemy() {
//        System.out.println("SPAWNING");
        //get the amount of enemies on a wave
        ListToSpawn curEnemyList = waves[currentWave];
        int rangeEnemies = curEnemyList.toSpawn.length;

        //spawn a random enemy on a given wave
        ToSpawn typeToSpawn = curEnemyList.toSpawn[Helper.Random(0, rangeEnemies)];
        /* ONLY SPAWN ENEMIES IF THEY ARE NOT PAST THE KILL COUNTER */
        //list.toSpawn[0] == first enemy
//        int[] curEn = new int[rangeEnemies];
//        for(int i=0; i<rangeEnemies; i++){
//            curEn[i] = curEnemyList.toSpawn[i].getType();
//        }
//        if(enemiesToKill[curEn[0]] < 20){
//            
//        }
//        Set<Integer> test = new HashSet<>();
//        ArrayList<Integer> test = new ArrayList<>();
//        for(int i=0; i<enemiesToKill.length; i++){
//            //enemiesToKill[Enemy.TYPEA] > 0
//            if(enemiesToKill[i] > 0){
//                test.add(i);
//            }
//        }

        //Spawn
        spawnType(typeToSpawn.getType());
//        spawnType(test.get(0));
//        spawnType(x, y, Helper.Random(0, Enemy.NO_OF_ENEMIES));
        elapsedTime = 0;
    }

    private void spawnType(int type) {
        //get random point offscreen
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
//        System.out.println("type: "+type);
        switch (type) {
            case Enemy.TYPEA:
                enemies.add(new TypeA(x, y,
                        TypeA.TYPE_A_WIDTH, TypeA.TYPE_A_HEIGHT, player));
                break;
            case Enemy.TYPEB:
                enemies.add(new TypeB(x, y,
                        TypeB.TYPE_B_WIDTH, TypeB.TYPE_B_HEIGHT, player));
                break;
            case Enemy.TYPEC:
                enemies.add(new TypeC(x, y,
                        TypeC.TYPE_C_WIDTH, TypeC.TYPE_C_HEIGHT, player));
                break;
        }
    }

    private void spawnType(float x, float y, int type) {
//        System.out.println("type: "+type);
        switch (type) {
            case Enemy.TYPEA:
                enemies.add(new TypeA(x, y,
                        TypeA.TYPE_A_WIDTH, TypeA.TYPE_A_HEIGHT, player));
                break;
            case Enemy.TYPEB:
                enemies.add(new TypeB(x, y,
                        TypeB.TYPE_B_WIDTH, TypeB.TYPE_B_HEIGHT, player));
                break;
            case Enemy.TYPEC:
                enemies.add(new TypeC(x, y,
                        TypeC.TYPE_C_WIDTH, TypeC.TYPE_C_HEIGHT, player));
                break;
        }
    }

    private void handleSpawn() {
        //Spawn [The faster the spawn rate, the faster it gets called]
        if (elapsedTime >= TIME_TO_SPAWN) {

            int noOfEnemies = waves[currentWave].toSpawn.length;
            ListToSpawn baddies = waves[currentWave];

            /* change cuurentEnemiesAlive -> baddie.getType() */
            for (int j = 0; j < noOfEnemies; j++) {
                ToSpawn baddie = baddies.toSpawn[j];
                currentEnemiesAlive[j] = baddie.getType();
                //If the enemy type has can spawn
                if (enemiesToKill[currentEnemiesAlive[j]] > 0) {
//                    System.out.println("enemiesToKill[" + currentEnemiesAlive[j]
//                            + "]: " + (enemiesToKill[currentEnemiesAlive[j]] > 0));
                    //If there are < enemy.amount on screen
                    if (spawnTracker[currentEnemiesAlive[j]]
                            < baddie.getAmount()/*obj.getAmount*/) { //enemiesToKill[currentEnemiesAlive[j]]*/s
                        spawnType(currentEnemiesAlive[j]);      //spawn enemy
                        spawnTracker[currentEnemiesAlive[j]]++; //update enemies spawned
                    }
                }
            }
//            spawnEnemy();
            elapsedTime = 0;
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

    private void setEnemiesToKill() {
        int noOfDiffEnemies = waves[currentWave].toSpawn.length;
        System.out.println("NO OF ENENIES: " + noOfDiffEnemies);
        for (int i = 0; i < noOfDiffEnemies; i++) {
            ToSpawn enemy = waves[currentWave].toSpawn[i];
//            enemiesToKill[i] = waves[currentWave].toSpawn[i].getAmount();
            enemiesToKill[enemy.getType()] = enemy.getAmount();
        }
    }

    private void resetSpawnTracker() {
        int len = spawnTracker.length;
        for (int i = 0; i < len; i++) {
            spawnTracker[i] = 0;
        }
    }

    public void loadNextWave() {
        System.out.println("Loading next wave...");
        waveComplete = false;
//        int len = enemiesKilled.length;
//        for (int i = 0; i < len; i++) {
//            enemiesKilled[i] = 0;
//        }
        currentWave++;
        setEnemiesToKill();
        resetSpawnTracker();
        printDebug();
        System.out.println("current wave: " + currentWave);
        elapsedTime = 0;    //Time since last enemy spawned
        GamePanel.state = GamePanel.WORLD_STATE_READY;
    }

    private void checkWaveComplete() {
        if (waveComplete) {
            //Wait 2 seconds

            //load next wave
            loadNextWave();
        }
    }

    private void updateEnemies(float deltaTime) {
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

    private void drawDeadInfo(Graphics2D g) {
        int len = enemiesToKill.length;
        for (int i = 0; i < len; i++) {
            g.drawString("Type " + (i + 1) + ": " + enemiesToKill[i],
                    WORLD_WIDTH - 140, 70 + (i * 30));
        }
    }

    private void drawSpawnInfo(Graphics2D g) {
        int len = enemiesToKill.length;
        for (int i = 0; i < len; i++) {
            g.drawString("Type " + (i + 1) + ": " + spawnTracker[i],
                    WORLD_WIDTH - 140, 270 + (i * 30));
        }
    }

    @Override
    void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        elapsedTime += deltaTime;
        elapsedGun += deltaTime;
        deltaTime *= scaleTime; //Objects that are slow mo after this line

        checkWaveComplete();
        continousFire();
        player.gameUpdate(deltaTime);
        handleSpawn();
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

        if (waveComplete) {
            System.out.println("SETTING BIG FONT");
            g.setFont(new Font("Courier new", Font.PLAIN, 85));
            return;
        }
        //********** Do drawings HERE **********
        player.gameRender(g);
        drawEnemies(g);
        death.gameRender(g);
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + player.lives, WORLD_WIDTH - 140, 30);
        drawDeadInfo(g);
        drawSpawnInfo(g);
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
