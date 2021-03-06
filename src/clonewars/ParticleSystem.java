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

import common.Helper;
import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * A ParticleSystem holds a set of Particles that, when combined create a
 * particular effect.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class ParticleSystem extends GameObject {

    private final static int MAX_SIZE = 15;

    private ArrayList<Particle> particles;
    private Vector2D center;

    /**
     * Creates a new system with a single effect type
     */
    public ParticleSystem() {
        particles = new ArrayList<>(MAX_SIZE);
        center = new Vector2D(GamePanel.GAME_WIDTH / 2f,
                GamePanel.GAME_HEIGHT / 2f);
    }

    private void explosion() {
        Particle p = new Particle(
                center.x,
                center.y,
                8f, 8f, //w, h
                0.6f, 1f, //age, damp
                0f, 0f, 1f, //rotation
                1f, 0f, 0f, 1f, //scale
                Color.BLUE, new Color(0, 255, 0, 0), 0.5f //init/final col
        );
        p.velocity.set(Helper.Random(-100, 100), Helper.Random(-100, 100));
        p.velocity.normalize();
        p.velocity.mult(110);
//        p.acceleration.set(0, 0);
        particles.add(p);
    }

    public void playExplosion(float x, float y) {
        center.x = x;
        center.y = y;
        for (int i = 0; i < MAX_SIZE; i++) {
            explosion();
        }
    }

    public void playExplosion(Vector2D pos) {
        center.set(pos);
        for (int i = 0; i < MAX_SIZE; i++) {
            explosion();
        }
    }

    /**
     * Sets the new origin of the particle system to the current touch position.
     *
     * @param e key event
     */
    public void mousePressed(MouseEvent e) {
        System.out.println("Pressed");

        center.x = e.getX();
        center.y = e.getY();

        for (int i = 0; i < MAX_SIZE; i++) {
//            addWaterfallParticle();
//            addFireParticle();
//            testEffect();
            explosion();
        }
    }

    private void resetParticle(Particle p) {
//        Particle particle = p;
//        int wh = Helper.Random(2, 25);
//
//        particle.age = Helper.Random(0, (1 * 1000) + 1500);
//        particle.width = wh;
//        particle.height = wh;
//        particle.position.x = center.x;
//        particle.position.y = center.y;
//        particle.velocity.x = Helper.Random(-100, 100);
//        particle.velocity.y = Helper.Random(-100, 100);
//        particle.acceleration.y = 30f;
//        particle.setState(false);

    }

    /**
     * Loop through list backwards to avoid skipping elements!
     *
     * @param deltaTime
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If time is up change position of center
        int len = particles.size() - 1;
        for (int i = len; i >= 0; i--) {
            Particle p = particles.get(i);
            p.gameUpdate(deltaTime);

            if (p.isDead()) {
                particles.remove(p);
//                len = particles.size() - 1;
            }
            /* Memory efficient, resets position, doesn't create new particle */
//            if (p.isDead()) {
//                resetParticle(p);
//            }
        }
    }

    @Override
    void gameRender(Graphics2D g) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.gameRender(g);
        }
    }
}
