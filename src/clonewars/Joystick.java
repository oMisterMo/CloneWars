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

import common.OverlapTester;
import common.Rectangle;
import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Joystick extends GameObject {

    private Vector2D analogPosition;    //main position     (center)
    private float width;
    private float height;

    private Vector2D offset;            //anolog position
    private float offsetWidth;
    private float offsetHeight;

    private Vector2D center;
    public Rectangle bounds;

    private Vector2D touchPos;
    private Vector2D inputVec;

    public Joystick(int x, int y) {
        BufferedImage lc = Assets.largeCircle;
        this.width = lc.getWidth();
        this.height = lc.getHeight();
        this.center = new Vector2D(x + width / 2, y + height / 2);
        this.analogPosition = new Vector2D(x, y);
        System.out.println("anlog pos: " + analogPosition);
        System.out.println("width: " + width);
        System.out.println("height: " + height);
        System.out.println("center: " + center);

        BufferedImage sc = Assets.smallCircle;
        offsetWidth = sc.getWidth();
        offsetHeight = sc.getHeight();
        float tempX, tempY;
//        tempX = (x + width) / 2f - offsetWidth / 2f;
//        tempY = (y + height) / 2f - offsetHeight / 2f;
        tempX = center.x - offsetWidth / 2;
        tempY = center.y - offsetHeight / 2;
        this.offset = new Vector2D(tempX, tempY);
        System.out.println("offset pos: " + offset);
        float moWidth, moHeight;
        moWidth = 100;
        moHeight = 100;
        bounds = new Rectangle(analogPosition.x - moWidth / 2,
                analogPosition.y - moHeight / 2, width + moWidth,
                height + moHeight);
        touchPos = new Vector2D();
        inputVec = new Vector2D();
    }

    /**
     * Constructs a new Joystick with a custom width and height.
     *
     * The small button stays as the default size.
     *
     * @param x the x position
     * @param y the y position
     * @param width
     * @param height
     */
    public Joystick(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        this.center = new Vector2D(x + width / 2, y + height / 2);
        this.analogPosition = new Vector2D(x, y);
        System.out.println("width = " + width);
        System.out.println("height = " + height);
        System.out.println("center: " + center);

        BufferedImage sc = Assets.smallCircle;
        offsetWidth = sc.getWidth();
        offsetHeight = sc.getHeight();
        float tempX, tempY;
        tempX = center.x - offsetWidth / 2f;
        tempY = center.y - offsetHeight / 2f;
        this.offset = new Vector2D(tempX, tempY);

        float moWidth, moHeight;
        moWidth = 100;
        moHeight = 100;
        bounds = new Rectangle(analogPosition.x - moWidth / 2,
                analogPosition.y - moHeight / 2, width + moWidth,
                height + moHeight);
        touchPos = new Vector2D();
        inputVec = new Vector2D();
    }

    public void handleMousePressed(MouseEvent e) {
        handleMouseDragged(e);
    }

    public void handleMouseDragged(MouseEvent e) {
        touchPos.x = e.getX();
        touchPos.y = e.getY();
//        System.out.println(touchPos);
        if (OverlapTester.pointInRectangle(bounds, touchPos)) {
//            System.out.println("Inbounds");
            //normalize touch pos between 0 - 1
            touchPos.x = (touchPos.x - analogPosition.x) / width;
            touchPos.y = (touchPos.y - analogPosition.y) / height;
            //mult value between -1 and 1
            inputVec.set(touchPos.x * 2 - 1, touchPos.y * 2 - 1);
            inputVec = (inputVec.length() > 1) ? inputVec.normalize() : inputVec;
            System.out.println("inputVec: " + inputVec);
            float size = width / 2f;  //radius
            offset.set(center.x + (inputVec.x * size),
                    center.y + (inputVec.y * size)).sub(offsetWidth / 2, offsetHeight / 2);
        }
    }

    public void handleMouseReleased(MouseEvent e) {
        //Analog let go, reset values
        inputVec.set(0, 0);
        offset.set(center).sub(offsetWidth / 2, offsetHeight / 2);
    }

    public Vector2D getInputVec() {
        return inputVec;
    }

    @Override
    void gameUpdate(float deltaTime) {

    }

    @Override
    void gameRender(Graphics2D g) {
        //Draw bounds
        g.setColor(Color.WHITE);
        g.drawRect((int) bounds.topLeft.x, (int) bounds.topLeft.y,
                (int) bounds.width, (int) bounds.height);
        //Draw stick
        g.drawImage(Assets.largeCircle, (int) analogPosition.x, (int) analogPosition.y,
                (int) width, (int) height, null);
        g.drawImage(Assets.smallCircle, (int) offset.x, (int) offset.y,
                (int) offsetWidth, (int) offsetHeight, null);
    }

}
