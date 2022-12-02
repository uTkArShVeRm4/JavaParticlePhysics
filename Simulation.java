import javax.swing.*;
import java.util.Random;

public class Simulation{

	public static void main(String[] args){
		Random rand = new Random();
		int w = 800;
		int h = 600;
		int bw = 640;
		int bh = 480;
		int m = 100;
		int r = 20;
		int particleNumber = 5;
		JFrame f = new JFrame();

		Particle[] Particles = new Particle[particleNumber];

		for(int i=0;i<particleNumber;i++){
			Particles[i] = new Particle(m,r);
		}
	
		DrawingCanvas dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber);
		f.setSize(w,h);
		f.setTitle("Java GUI testing");
		f.add(dc);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		dc.startAnimationThread();
	}
}