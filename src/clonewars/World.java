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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import items.Droppable;
import items.Life;
import items.Shield;
import items.Splat;

/**
 * 20-Feb-2018, 22:16:38.
 *
 * @author Mohammed Ibrahim
 */
public class World extends GameObject {

    public static final float WORLD_WIDTH = 600;
    public static final float WORLD_HEIGHT = 600;

    public static final int WORLD_STATE_READY = 0;
    public static final int WORLD_STATE_RUNNING = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public int state;

    public static final float GUN_COOLDOWN = 0.064f;    //seconds
    public static final float TIME_TO_SPAWN = 0.4f;     //spawn enemy
    public static final float DIST_AWAY = 100f;         //distance from screen
    public static final int NO_OF_WAVES = 5;
    public static int currentWave = 0;  //0 means 1...duh
    public static boolean waveComplete = false;
    private static ListToSpawn[] waves;
    private static int[] enemiesToKill;
    private static int[] currentEnemiesAlive;   //might not need
    private static int[] spawnTracker;

    private final WorldListener listener;
    public Player player;
    private ArrayList<Enemy> enemies;
    private ParticleSystem death;

    private ArrayList<Droppable> items;
    private ArrayList<Droppable> splats;

    private final Vector2D touchPos = new Vector2D();
    private boolean cFire = false;          //Mouse held down?

    private Color backgroundColor = Color.BLACK;
    public static final int xShift = 0; //(int) (GamePanel.GAME_WIDTH / 2 - WORLD_WIDTH / 2)
    public static final int yShift = 0;
    private static float elapsedSpawn = 0;
    private static float elapsedGun = 0;
    private static float elapsedLoad = 0;
    private SpatialHashGrid grid;
    private float scaleTime = 1f;

    public World(WorldListener lis) {
        this.listener = lis;
        init();
        state = WORLD_STATE_RUNNING;
//        spawnEnemy();
//        spawnEnemy();
        System.out.println("background Color: " + backgroundColor);
        System.out.println("World loaded...");
    }

    public interface WorldListener {

        void fire();

        void enemySpawn();

        void enemyDie();

        void playerSpawn();

        void playerHurt();

        void playerDie();

        void loadNextWave();

        void sayPraise();
    }

    private void init() {
        initWaves();
        enemiesToKill = new int[Enemy.NO_OF_ENEMIES];
        currentEnemiesAlive = new int[Enemy.NO_OF_ENEMIES];
        spawnTracker = new int[Enemy.NO_OF_ENEMIES];

        player = new Player(
                WORLD_WIDTH / 2 - Player.PLAYER_WIDTH / 2 + xShift,
                WORLD_HEIGHT / 2 - Player.PLAYER_HEIGHT / 2 + yShift);
        enemies = new ArrayList<>(50);
        items = new ArrayList<>(10);
        splats = new ArrayList<>(30);

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
        waves[0].toSpawn[0] = new ToSpawn(Enemy.TYPEB, 15);

        //Wave 2
        waves[1] = new ListToSpawn(1);
        waves[1].toSpawn[0] = new ToSpawn(Enemy.TYPEA, 10);

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
        waves[4].toSpawn[0] = new ToSpawn(Enemy.TYPEC, 200);

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
        player.handleMouseMoved(e);
    }

    private void continousFire() {
        //limit fire rate
        if (elapsedGun >= GUN_COOLDOWN) {
            if (cFire) {
                listener.fire();
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

    private void dropItem(float x, float y) {
        float num = Helper.Random();
//        System.out.println("Random num generated: " + num);
        if (num < 0.005f) {
            items.add(new Life(x - Life.LIFE_WIDTH / 2,
                    y - Life.LIFE_HEIGHT / 2,
                    Life.LIFE_WIDTH, Life.LIFE_HEIGHT));
        } else if (num < 0.05) {
            items.add(new Shield(x - Shield.SHEILD_WIDTH / 2,
                    y - Shield.SHEILD_HEIGHT / 2,
                    Shield.SHEILD_WIDTH, Shield.SHEILD_HEIGHT));
        }
    }

    private void leaveSplat(float x, float y) {
        float num = Helper.Random();
//        System.out.println("Random num generated: " + num);
        if (num < 0.5f) {
            int numSplats = splats.size();
            if (numSplats < Splat.MAX_NUM_SPLATS) {
                splats.add(new Splat(x - Splat.SPLAT_WIDTH / 2, y - Splat.SPLAT_HEIGHT / 2,
                        Splat.SPLAT_WIDTH, Splat.SPLAT_HEIGHT));
            } else {
                System.out.println("Can't create any more splats");
            }
        }
    }

    private void killAllEnemies() {
        System.out.println("Killing all enemies...");
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
                        listener.enemyDie();
                        //set enemy state
                        e.die();
                        //check if level is complete
                        waveComplete = isWaveComplete();
                        //Drop random item
                        dropItem(e.position.x, e.position.y);
                        leaveSplat(e.position.x, e.position.y);
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
                listener.playerHurt();
                player.lives -= 1;
                killAllEnemies();
                break;
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

    private void itemCollision() {
        int len = items.size();
        for (int i = len - 1; i >= 0; i--) {
            Droppable item = items.get(i);
            int type = item.getType();
            //Check if player overlaps any item
            if (OverlapTester.overlapRectangles(item.bounds, player.bounds)) {
                switch (type) {
                    case Droppable.LIFE:
                        System.out.println("GOT A HEART");
                        items.remove(item);
                        player.lives++;
                        break;
                    case Droppable.SHEILD:
                        System.out.println("GOT A SHEILD");
                        items.remove(item);

                        break;
                }
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
        //Bullet - Enemy 
        bulletCollision();
        //Player - Enemy
        playerCollision();
        //Enemy - Enemy
        enemyCollision(); //FPS KILLER
        //Player - Item
        itemCollision();
    }

    private void checkGameover() {
        if (player.lives <= 0) {
            listener.playerDie();
            //GameOver
            GamePanel.state = GamePanel.GAME_GAMEOVER;
//            state = WORLD_STATE_GAME_OVER;
        }
    }

    private boolean isWaveComplete() {
//        int a = enemiesKilled[Enemy.TYPEA];
//        int b = enemiesKilled[Enemy.TYPEB];
//        int c = enemiesKilled[Enemy.TYPEC];
//        boolean b = false;

//        int numEnemies = waves[currentWave].toSpawn.length;
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
        listener.sayPraise();
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
        elapsedSpawn = 0;
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
                enemies.add(new TypeA(x, y, player));
                break;
            case Enemy.TYPEB:
                enemies.add(new TypeB(x, y, player));
                break;
            case Enemy.TYPEC:
                enemies.add(new TypeC(x, y, player));
                break;
        }
    }

    private void spawnType(float x, float y, int type) {
//        System.out.println("type: "+type);
        switch (type) {
            case Enemy.TYPEA:
                enemies.add(new TypeA(x, y, player));
                break;
            case Enemy.TYPEB:
                enemies.add(new TypeB(x, y, player));
                break;
            case Enemy.TYPEC:
                enemies.add(new TypeC(x, y, player));
                break;
        }
    }

    private void handleSpawn() {
        //Spawn [The faster the spawn rate, the faster it gets called]
        if (elapsedSpawn >= TIME_TO_SPAWN) {

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
            elapsedSpawn = 0;
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

    private void updateWave() {
        currentWave++;
        if (currentWave + 1 > NO_OF_WAVES) {
            System.out.println("capping current wave to last wave");
            currentWave = NO_OF_WAVES - 1;
        }
    }

    private void setEnemiesToKill() {
        int noOfDiffEnemies = waves[currentWave].toSpawn.length;
//        System.out.println("Num of different ENENIES: " + noOfDiffEnemies);
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

    private void clearSplats() {
        //attemp 1
//        When you remove an element from an array, it resizes
//        int len = splats.size();
//        System.out.println("len of splats: "+len);
//        for (int i = 0; i < splats.size(); i++) {
////            System.out.println("i: "+i);
//            splats.remove(i);
//        }

//        //attemp 2
//        System.out.println("Clearing splats...");
//        int len = splats.size();
//        for(int i=len -1; i>=0; i--){
//            splats.remove(i);
//        }
        //attemp 3 (clear to let GC do its work)
        splats.clear(); //sets all elements to null

    }

    public void loadNextWave() {
        System.out.println("Loading next wave...");
        player.velocity.set(0, 0);
        waveComplete = false;
        updateWave();
        setEnemiesToKill();
        resetSpawnTracker();
        clearSplats();
        printDebug();
        System.out.println("current wave: " + currentWave);
        elapsedSpawn = 0;    //Time since last enemy spawned
        GamePanel.state = GamePanel.GAME_READY;
//        state = WORLD_STATE_READY;
    }

    private void checkWaveComplete(float deltaTime) {
        if (waveComplete) {
            elapsedLoad += deltaTime;
            //Wait 1 seconds before loading a new wave
            if (elapsedLoad > 1f) {
                //load next wave
                loadNextWave();
                elapsedLoad = 0;
            }
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

    private void drawItems(Graphics2D g) {
        int len = items.size() - 1;
        for (int i = len; i >= 0; i--) {
            Droppable item = items.get(i);
            item.gameRender(g);
        }
    }

    private void drawSplats(Graphics2D g) {
        int len = splats.size() - 1;
        for (int i = len; i >= 0; i--) {
            Droppable splat = splats.get(i);
            splat.gameRender(g);
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
        elapsedSpawn += deltaTime;
        elapsedGun += deltaTime;
        deltaTime *= scaleTime; //Objects that are slow mo after this line

        checkWaveComplete(deltaTime);
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

        //********** Do drawings HERE **********
        drawSplats(g);
        drawItems(g);
        drawEnemies(g);
        death.gameRender(g);
        player.gameRender(g);
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + player.lives, WORLD_WIDTH - 140, 30);
//        drawDeadInfo(g);
//        drawSpawnInfo(g);
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
