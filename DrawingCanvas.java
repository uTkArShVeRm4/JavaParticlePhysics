import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;

public class DrawingCanvas extends JComponent implements Runnable{

	private int height;
	private int width;
	int FPS = 60;
	int x = 100;
	int y = 100;
	Particle[] particles;
	int particleNumber;
	Thread animationThread;
	Random rand = new Random();

	public DrawingCanvas(int w, int h, Particle[] p,int pn){
		width = w;
		height = h;
		particles = p;
		particleNumber = pn;
	}

	public void paintComponent(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		

		for(int i=0;i<particleNumber;i++){

			Ellipse2D.Double circle = new Ellipse2D.Double(particles[i].pX,particles[i].pY,particles[i].radius,particles[i].radius);
			//Rectangle2D.Double r = new Rectangle2D.Double(particles[i].pX,particles[i].pY,particles[i].radius,particles[i].radius);
			g2D.setColor(particles[i].color);
			g2D.fill(circle);
			
		}
		g2D.dispose();
	}

	public void startAnimationThread(){

		animationThread = new Thread(this);
		animationThread.start();
	}

	public void update(Particle[] particles){

		for(int i=0;i<particleNumber;i++){
			checkWallCollision(particles[i]);
			particles[i].pX += particles[i].vX;
			particles[i].pY += particles[i].vY;
		}
	
	}


	public void run(){

		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while(animationThread!=null){

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;

			if(delta>=1){
				update(particles);
				repaint();
				delta--;
			}

		}
	}

	public void checkWallCollision(Particle p){

		if((p.pX<=0) || (p.pX+(p.radius)>=width)){
			p.vX *= -1;
		}
		if((p.pY<=0) || (p.pY+(2*p.radius)>=height)){
			p.vY *= -1;
		}
	}

	// public void checkParticleCollision(Particle[] particles){

	// 	for(int i=0;i<particleNumber;i++){
	// 		for(int j=i+1;j<particleNumber;j++){

	// 		}
	// 	}

	// }

	// @Override
	// public void run(){

	// 	double drawInterval = 1000000000/FPS;
	// 	double nextDrawTime = System.nanoTime() + drawInterval;
		

	// 	while(animationThread != null){
	// 		update();
	// 		repaint();
	// 	}

	// 	try{
	// 		double remainingTime = nextDrawTime - System.nanoTime();
	// 		remainingTime = remainingTime / 1000000;

	// 		if(remainingTime<0){
	// 			remainingTime = 0;
	// 		}

	// 		Thread.sleep((long) remainingTime);
	// 		nextDrawTime += drawInterval;
	// 	}
	// 	catch(InterruptedException e){
	// 		e.printStackTrace();
	// 	}

	// }
}