import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;
import java.lang.Math.*;

public class DrawingCanvas extends JComponent implements Runnable{

	private int height;
	private int width;
	float FPS = 60;
	int x = 100;
	int y = 100;
	Particle[] particles;
	int particleNumber;
	int boxW;
	int boxH;
	int boxX = 10;
	int boxY = 10;
	float deltaTime = 1/FPS;
	Thread animationThread;
	Random rand = new Random();
	boolean running;

	//collisionResolver cr = new collisionResolver();

	public DrawingCanvas(int w, int h, int bw,int bh, Particle[] p,int pn){
		width = w;
		height = h;
		boxW = bw;
		boxH = bh;
		particles = p;
		particleNumber = pn;
	}

	public void paintComponent(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		
		Rectangle2D.Double r = new Rectangle2D.Double(boxX,boxY,boxW,boxH);
		g2D.setColor(Color.black);
		g2D.fill(r);

		for(int i=0;i<particleNumber;i++){

			Ellipse2D.Double circle = new Ellipse2D.Double(particles[i].px,particles[i].py,particles[i].diameter,particles[i].diameter);
			//Rectangle2D.Double rect = new Rectangle2D.Double(particles[i].px,particles[i].py,particles[i].diameter,particles[i].diameter);
			g2D.setColor(particles[i].color);
			g2D.fill(circle);

			Ellipse2D.Double pointc = new Ellipse2D.Double(particles[i].cx,particles[i].cy,1,1);
			Ellipse2D.Double pointp = new Ellipse2D.Double(particles[i].px,particles[i].py,1,1);
			// Rectangle2D.Double rect = new Rectangle2D.Double(particles[i].px,particles[i].py,particles[i].diameter,particles[i].diameter);
			g2D.setColor(Color.red);
			g2D.fill(pointc);
			g2D.fill(pointp);
			// g2D.fill(rect);
			
		}
		g2D.dispose();
	}

	public void startAnimationThread(){

		animationThread = new Thread(this);
		animationThread.start();
	}

	public void stop(){
		running = false;
	}

	public void update(){

		//checks for each particle per frame
		for(int i=0;i<particleNumber;i++){

			checkWallCollision(particles[i]);

			//checking and resolving collision with rest of particles
			for(int j=i+1;j<particleNumber;j++){
				checkParticleCollision(particles[i],particles[j]);


			//updating positions from velocities
			particles[i].px += particles[i].vx*deltaTime;
			particles[i].py += particles[i].vy*deltaTime;
			particles[i].cx = particles[i].px + particles[i].radius;
			particles[i].cy = particles[i].py + particles[i].radius;

		}
		particles[particleNumber-1].px += particles[particleNumber-1].vx*deltaTime;
		particles[particleNumber-1].py += particles[particleNumber-1].vy*deltaTime;
		particles[i].cx = particles[i].px + particles[i].radius;
		particles[i].cy = particles[i].py + particles[i].radius;
	}
}


	public void run(){
		running = true;
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while(running){

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;

			if(delta>=1){
				update();
				repaint();
				delta--;
			}

		}
	}

	public void checkWallCollision(Particle p){

		p.lastCollision = p;

		if(p.px<=boxX){
			p.px = boxX;
			p.vx *= -1;
		}

		if(p.px+(p.diameter)>boxX + boxW){
			p.px = boxX + boxW - p.diameter;
			p.vx *= -1;
		}

		if(p.py<=boxY){
			p.py = boxY;
			p.vy *= -1;
		}
		if(p.py+(p.diameter)>boxY + boxH){
			p.py = boxY + boxH - p.diameter;
			p.vy *= -1;
		}

		p.calcColor();
	}


	public boolean checkParticleCollision(Particle p1, Particle p2){

		//root((x1-x2)^2 + (y1-y2)^2)
		double c1c2 = Math.pow(Math.pow((p1.cx-p2.cx),2) + Math.pow((p1.cy-p2.cy),2),0.5);
		double r1r2 = (double) (p1.radius+p2.radius);

		//if distance between centres of particles is less than sum of radii then collision happens	
		if(c1c2<=r1r2){ 
			// cr.resolveCollision(p1,p2);
			particleCollisionResolution(p1,p2);
			// p1.lastCollision = p2;
			// p2.lastCollision = p1;
			return true;
		}
		else{
			return false;
		}
	}

	public void particleCollisionResolution(Particle p1, Particle p2){

		// if((p1.lastCollision != p2) && (p2.lastCollision != p1)){
			float u1x = p1.vx;
			float u1y = p1.vy;
			float u2x = p2.vx;
			float u2y = p2.vy;

			float dist = (float)(p1.calcDist(p2.cx,p2.cy));
			float overlap = (dist-p1.radius-p2.radius)/2;

			p1.px -= overlap*(p1.cx-p2.cx)/dist;
			p1.py -= overlap*(p1.cy-p2.cy)/dist;

			p2.px += overlap*(p1.cx-p2.cx)/dist;
			p2.py += overlap*(p1.cy-p2.cy)/dist;

			float xVelocityDiff = p1.vx - p2.vx;
        	float yVelocityDiff = p1.vy - p2.vy;

    		float xDist = p2.cx - p1.cx;
    		float yDist = p2.cy - p1.cy;

    		if (xVelocityDiff * xDist + yVelocityDiff * yDist >= 0){ 
				p1.vx = (u1x*(p1.mass-p2.mass)+2*p2.mass*u2x)/(p1.mass+p2.mass);
				p1.vy = (u1y*(p1.mass-p2.mass)+2*p2.mass*u2y)/(p1.mass+p2.mass);

				p2.vx = (u2x*(p2.mass-p1.mass)+2*p1.mass*u1x)/(p1.mass+p2.mass);
				p2.vy = (u2y*(p2.mass-p1.mass)+2*p1.mass*u1y)/(p1.mass+p2.mass);
			}

			capSpeed(p1);
			capSpeed(p2);
		//}
	}

	public void capSpeed(Particle p){
		if(p.vx>(p.maxSpeedComp)){
			p.vx = (p.maxSpeedComp);
		}
		if(p.vy>(p.maxSpeedComp)){
			p.vy = (p.maxSpeedComp);
		}
		p.calcColor();
	}

	public void cool(){
		for(int i=0; i<particleNumber;i++){
			if (particles[i].vx *0.95 > 5){
				particles[i].vx *= 0.95;
			}
				
			if (particles[i].vy *0.95 > 5){
				particles[i].vy *= 0.95;
			}

		}
	}
	public void heat(){
		for(int i=0; i<particleNumber;i++){
			if (particles[i].vx *1.05 < 29){
				particles[i].vx *= 1.05;
			}
				
			if (particles[i].vy *1.05 < 29){
				particles[i].vy *= 1.05;
			}
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