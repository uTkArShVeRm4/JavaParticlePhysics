import java.lang.Math.*;

public class collisionResolver{

	private Particle rotateVelocity(Particle p, double angle){
		p.vx = p.vx * (float)Math.cos(angle) - p.vy * (float)Math.sin(angle);//-
		p.vy = p.vx * (float)Math.sin(angle) + p.vy * (float)Math.cos(angle);//+

		return p;
	}

	public void resolveCollision(Particle p1, Particle p2){
		float xVelocityDiff = p1.vx - p2.vx;
        float yVelocityDiff = p1.vy - p2.vy;

    	float xDist = p2.cx - p1.cx;
    	float yDist = p2.cy - p1.cy;

    // Prevent accidental overlap of particles
	    if (xVelocityDiff * xDist + yVelocityDiff * yDist >= 0) {

	        // Grab angle between the two colliding particles
	        double angle = -Math.atan2(p2.cy - p1.cy, p2.cx - p1.cx);

	        // Store mass in var for better readability in collision equation
	        int m1 = p1.mass;
	        int m2 = p2.mass;

	        // Velocity before equation
	        float u1x = rotateVelocity(p1, angle).vx;
	        float u1y = rotateVelocity(p1, angle).vy;
	        float u2x = rotateVelocity(p2, angle).vx;
	        float u2y = rotateVelocity(p2, angle).vy;

	        // Velocity after 1d collision equation
	        float v1x = u1x * (m1 - m2) / (m1 + m2) + u2x * 2 * m2 / (m1 + m2);
	        float v1y = u1y;
	        float v2x = u2x * (m2 - m1) / (m1 + m2) + u1x * 2 * m1 / (m1 + m2);
	        float v2y = u2y;

	        p1.vx = v1x;
	        p1.vy = v1x;
	        p2.vx = v2x;
	        p2.vy = v2y;
	        // Final velocity after rotating axis back to original location
	        float vFinal1x = rotateVelocity(p1, -angle).vx;
	        float vFinal1y = rotateVelocity(p1, -angle).vy;
	        float vFinal2x = rotateVelocity(p2, -angle).vx;
	        float vFinal2y = rotateVelocity(p2, -angle).vy;

	        // Swap particle velocities for realistic bounce effect
	        p1.vx = vFinal1x;
	        p1.vy = vFinal1y;

	        p2.vx = vFinal2x;
	        p2.vy = vFinal2y;
	    }

	}
}