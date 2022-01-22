package org.cis120.mushroom;

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * 
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class Circle extends GameObj {
    public static final int SIZE = 20;
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 170;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 3;

    private Color color;

    public Circle(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}