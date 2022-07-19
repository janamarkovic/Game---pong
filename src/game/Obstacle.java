/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author aleks
 */
public class Obstacle extends Rectangle{
    int speed;
    //int yVelocity;

    public Obstacle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    public void move() {
		y = y+10;
	}
    
    public void draw(Graphics g) {
		g.setColor(Color.decode("#7FFF00"));//postavlja se boja loptice
		g.fillRect(x, y, width, height); // oblik loptice se definise
	}
    
}
