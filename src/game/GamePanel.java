package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GamePanel extends JPanel implements Runnable{

	//Dimenzije igre
	static final int GAME_WIDTH = 1000;  
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
        static final int OBSTACLE_WIDTH = 10;
        static final int OBSTACLE_HEIGHT = 400;
        
        boolean signal = true;
        boolean intersectedPaddle2;
        boolean intersectedPaddle1;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
        Obstacle obstacle;
        InputStream music;
        GameFrame gameFrame;
	
	
	public GamePanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		newPaddle();
		newBall();
                newObstacle();
		score = new Score(GAME_WIDTH,GAME_HEIGHT);//Kreira se novi Score
		this.setFocusable(true);
		this.addKeyListener(new ActionListener());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);//Kreira se nova nit koja kontrolise igru
		gameThread.start();//Pokrece se kreirana nit
                
                
	}

    public Thread getGameThread() {
        return gameThread;
    }
        
        
	
	public void newBall() {
		random = new Random();//Kreiranje objekta klase Random
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);//Kreiranje loptice
		
	}
	
	public void newPaddle() {
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT,1);//Kreiranje prvog reketa
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT,2);//Kreiranje drugog reketa
	}
        
        private void newObstacle() {
        Random random = new Random();
        obstacle = new Obstacle(250+random.nextInt(500-OBSTACLE_WIDTH), -OBSTACLE_HEIGHT, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
    }
	
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());//Kreiranje slike
		graphics = image.getGraphics();//Sliku prebacujemo u graficki element
		draw(graphics);//Pozivanje metode za crtanje nad grafickim elementom
		g.drawImage(image, 0, 0, this);
	}
	
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
                obstacle.draw(g);
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
                obstacle.move();
	}
	
	public void checkCollision() {
		
		//odbijanje loptice od ivica ekrana
		if(ball.y <= 0) {
			ball.setYDirection(-ball.yVelocity);//Kada loptica udari u donju ivicu odbija se u suprotnom smeru
                        
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);//Kada loptica udari u gornju ivicu odbija se u suprotnom smeru
                        
		}
		
		//odbijanje loptice od reketa
		if(ball.intersects(paddle1)) {
                    intersectedPaddle2 = false;
			ball.xVelocity = Math.abs(ball.xVelocity);//X koordinata ce uvek biti pozitivna
			ball.xVelocity++; //da bude teze, povećava se brzina loptice
			if(ball.yVelocity>0) {
				ball.yVelocity++;
			}else {
				ball.yVelocity--;
			}
			ball.setXDirection(ball.xVelocity);//Postavlja se vrednost koordinate ya koju ce se loptica pomeriti po horizontalnoj osi
			ball.setYDirection(ball.yVelocity);//Postavlja se vrednost koordinate ya koju ce se loptica pomeriti po vertikalnoj osi
                        
                        intersectedPaddle1 = true;
		}
                
      
		
		if(ball.intersects(paddle2)) {
                    intersectedPaddle1 = false;
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //da bude teze, povećava se brzina loptice
			if(ball.yVelocity>0) {
				ball.yVelocity++;
			}else {
				ball.yVelocity--;
			}
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
                        
                        intersectedPaddle2 = true;
		}

                if(intersectedPaddle1 == true && ball.intersects(obstacle)){
                    ball.xVelocity = Math.abs(ball.xVelocity);
                    ball.setXDirection(-ball.xVelocity);
                    ball.setYDirection(ball.yVelocity);
                    
                }
                
                if(intersectedPaddle2 == true && ball.intersects(obstacle)){
                    ball.xVelocity = Math.abs(ball.xVelocity);
                    ball.setXDirection(ball.xVelocity);
                    ball.setYDirection(ball.yVelocity);
                   
                }
		
		//zaustavlja rekete na ivicama ekrana
		if(paddle1.y<=0) {
			paddle1.y=0;
		}
		if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT)) {
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		}
		
		if(paddle2.y<=0) {
			paddle2.y=0;
		}
		if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT)) {
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		}
		
		//dodaje igracu poen i kreira nove rekete i lopticu
		if(ball.x <= 0) {
			score.player2++;
			newPaddle();
			newBall();
                        newObstacle();
			System.out.println("Igrac 2: "+score.player2);
                        
                        if(score.player2==2){
                            
                            new WinnerForm("Player2", gameFrame).setVisible(true);
                            score.player1=0;
                            score.player2=0;
                             signal = false;
                                
                        }
		}
		
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			score.player1++;
			newPaddle();
			newBall();
                        newObstacle();
			System.out.println("Igrac 1: "+score.player1);
                        
                        if(score.player1==2){
                            
                            new WinnerForm("Player1", gameFrame).setVisible(true);
                            
	                            
                                signal = false;
                                
                        }
                        
                        }
                
                
                 if(obstacle.y>=GAME_HEIGHT){
                    newObstacle();
                }
                
		}
                
               
                
                
                
		

	
	public void run() {
	//Game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		while (signal) {
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();//Poziva se metoda za pomeranje grafickih komponenti(Reketi i loptica)
				checkCollision();//Proverava se sudar loptice sa ivicama i reketima
				repaint();
				delta--;
				
			}
		}
	}

    
	
	public class ActionListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}
	
}
