import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;
import java.lang.Math.*;

public class Particle{
	int mass;
	int diameter;
	int radius;

	int[] direction = {-1,1};

	float px;
	float py;

	float cx;
	float cy;

	int maxSpeedComp = 29; // maxSpeed = 41.006
	float vx;
	float vy;
	Color color;
	Random rand = new Random();

	Particle lastCollision;

	public Particle(int m,int d,boolean mrs,int speed,int x,int y){
		mass = m;
		if(mrs == true){
			diameter = (int) Math.floor(Math.pow(mass,0.5)); //100 mass = 10 diameter
		}
		else{
			diameter = d;
			
		}
		radius = diameter/2;

		vx = (float)(speed*direction[rand.nextInt(direction.length)]/1.414); //pixels per second
		vy = (float)(speed*direction[rand.nextInt(direction.length)]/1.414);
		calcColor();
		px = x;
		py = y;
		cx = px + (radius);
		cy = py + (radius);
	}

	public double calcSpeed(){
		return(Math.floor(Math.pow((vx*vx)+(vy*vy),0.5))); //41 max
	}

	public void calcColor(){
		int[] rgb = {0,0,0};
		double value = calcSpeed()/(maxSpeedComp*1.414);
		if(value>=1){
			value = 1;
		}
		if(value > 0.5){
	        value -= 0.5;
	        rgb[0] = (int)(2*value*255);
	        rgb[1] = (int)((1-2*value)*255);
	        rgb[2] = 0;
   		}
	    else if(value <= 0.5){
	        rgb[0] = 0;
	        rgb[1] = (int)(2*value*255);
	        rgb[2] = (int)((1-2*value)*255);
	    }
	    color = new Color(rgb[0],rgb[1],rgb[2]);
	}

	public double calcDist(float x, float y){
		return(Math.pow(Math.pow(x-cx,2)+Math.pow(y-cy,2),0.5));
	}
}