import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;

public class Particle{
	int mass;
	int diameter;
	int radius;

	float px;
	float py;

	float cx;
	float cy;

	int speedFac = 10;
	float vx;
	float vy;
	Color color;
	Random rand = new Random();

	public Particle(int m,int d){
		mass = m;
		diameter = d;
		radius = diameter/2;
		px = rand.nextInt(50,300);
		py = rand.nextInt(50,300);
		vx = rand.nextInt(-4,5)*speedFac; //pixels per second
		vy = rand.nextInt(-4,5)*speedFac;
		color = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));

		cx = px + (radius);
		cy = py + (radius);
	}
}