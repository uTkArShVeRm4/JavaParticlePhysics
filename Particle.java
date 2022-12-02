import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;

public class Particle{
	int mass;
	int radius;
	int pX;
	int pY;
	int vX;
	int vY;
	Color color;
	Random rand = new Random();

	public Particle(int m,int r){
		mass = m;
		radius = r;
		pX = rand.nextInt(540);
		pY = rand.nextInt(380);
		vX = rand.nextInt(-2,3);
		vY = rand.nextInt(-2,3);
		color = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
	}
}