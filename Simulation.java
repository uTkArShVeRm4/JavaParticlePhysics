import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Simulation implements ActionListener{

	Random rand = new Random();
	int w = 900;
	int h = 600;
	int bw = 640;
	int bh = 480;
	int m = 100;
	int d = 20;
	int particleNumber = 3;

	JFrame f = new JFrame();
	JButton b = new JButton("Add Particle");
	JLabel l = new JLabel("Particles: "+particleNumber);

	Particle[] Particles = new Particle[100];
	
	DrawingCanvas dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber);

	
	
	public void simulate(){
		Particles[0] = new Particle(m,d);
		Particles[1] = new Particle(m,d);
		Particles[2] = new Particle(m,d);
		b.addActionListener(this);
		b.setBounds(650, 10, 100, 50);
		f.add(b);
		l.setBounds(750, 10, 100, 50);
		f.add(l);
		
		f.setSize(w,h);
		f.setTitle("Particle Physics Simulation");
		f.add(dc);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		dc.startAnimationThread();
	}

	public void actionPerformed(ActionEvent e){

		if(e.getActionCommand().equals("Add Particle")){
			System.out.println("button");
			Particles[particleNumber] = new Particle(m,d);
			particleNumber++;

			
			f.remove(l);
			l = new JLabel("Particles: "+particleNumber);
			l.setBounds(750, 10, 100, 50);
			f.add(l);
			updateDrawingCanvas();
			
		}
	}

	public void updateDrawingCanvas(){
		dc.stop();
		f.remove(dc);
		dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber);
		f.add(dc);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		dc.startAnimationThread();
	}

	public static void main(String[] args) {
		Simulation s = new Simulation();
		s.simulate();
	}


	
}