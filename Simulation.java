import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Simulation implements ActionListener{

	Random rand = new Random();
	int w = 1000;
	int h = 600;
	int bw = 640;
	int bh = 480;
	int m = 100;
	int d = 10;
	int r = d/2;
	int speed = 5;
	int particleNumber = 0;
	int px,py;
	boolean massRadiusScale = false;

	JButton	startSim = new JButton("Start Simulation");
	JButton	reset = new JButton("Reset");

	JButton cool = new JButton("Decrease Temp");
	JButton heat = new JButton("Increase Temp");

	JFrame f = new JFrame();
	JButton pb = new JButton("Add Particle");
	JLabel plabel = new JLabel("Particles: "+particleNumber);

	JButton mPlus = new JButton("Mass +");
	JLabel mlabel = new JLabel("Mass: "+m);
	JButton mMinus = new JButton("Mass -");

	JButton sPlus = new JButton("Speed +");
	JLabel slabel = new JLabel("Speed: "+speed);
	JButton sMinus = new JButton("Speed -");

	// JLabel massRadiusScaleLabel = new JLabel("Have radius proportional to mass");
	JCheckBox massRadiusScaleCheckbox = new JCheckBox("Have radius proportional to mass");

	Particle[] Particles = new Particle[100];
	
	DrawingCanvas dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber);

	public void setup(){

		mMinus.setBounds(650, 75, 75, 50);
		mPlus.addActionListener(this);
		mlabel.setBounds(735,75, 75, 50);
		mPlus.setBounds(810,75, 75, 50);
		mMinus.addActionListener(this);
		f.add(mPlus);
		f.add(mlabel);
		f.add(mMinus);

		cool.setBounds(650, 250, 150,50);
		f.add(cool);
		heat.setBounds(810, 250, 150,50);
		f.add(heat);
		cool.addActionListener(this);
		heat.addActionListener(this);

		sMinus.setBounds(650, 150, 75, 50);
		sPlus.addActionListener(this);
		slabel.setBounds(735,150, 75, 50);
		sPlus.setBounds(810,150, 75, 50);
		sMinus.addActionListener(this);
		f.add(sPlus);
		f.add(slabel);
		f.add(sMinus);

		// massRadiusScaleLabel.setBounds(650, 140, 250, 50);
		massRadiusScaleCheckbox.setBounds(650, 100, 250, 70);//910
		massRadiusScaleCheckbox.addActionListener(this);
		//f.add(massRadiusScaleLabel);
		f.add(massRadiusScaleCheckbox);

		startSim.setBounds(650,300,150,50);
		reset.setBounds(810,300,75,50);
		startSim.addActionListener(this);
		reset.addActionListener(this);
		f.add(startSim);
		f.add(reset);

		pb.addActionListener(this);
		pb.setBounds(650, 10, 100, 50);
		f.add(pb);
		plabel.setBounds(750, 10, 100, 50);
		f.add(plabel);
		f.add(dc);
		f.setVisible(true);
	}
	
	public void simulate(){
		
		f.setSize(w,h);
		f.setTitle("Particle Physics Simulation");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
		
	}

	public void actionPerformed(ActionEvent e){

		if(e.getActionCommand().equals("Add Particle")){
			px = rand.nextInt(r,bw-r);
			py = rand.nextInt(r,bh-r);
			// generating new particle to not intersect with preexisting particles
			for(int i = 0; i<particleNumber; i++){
				if(Particles[i].calcDist(px,py)<=Particles[i].radius+r){
					px = rand.nextInt(r,bw-r);
					py = rand.nextInt(r,bh-r);
				}
			}

			Particles[particleNumber] = new Particle(m,d,massRadiusScale,speed,px,py);
			particleNumber++;
			updateParticleGUI();
			updateDrawingCanvas();
		}
		else if(e.getActionCommand().equals("Mass +")){

			m+=100;
			updateMassGUI();
		}	
		else if(e.getActionCommand().equals("Mass -")){

			m-=100;
			if(m<=100){
				m=100;
			}
			updateMassGUI();
		}
		else if(e.getActionCommand().equals("Have radius proportional to mass")){
			massRadiusScale = !massRadiusScale;
		}	
		else if(e.getActionCommand().equals("Speed +")){

			speed+=5;
			if(speed>=40){
				speed=40;
			}
			updateSpeedGUI();
		}	
		else if(e.getActionCommand().equals("Speed -")){

			speed-=5;
			if(speed<5){
				speed=5;
			}
			updateSpeedGUI();
		}
		else if(e.getActionCommand().equals("Start Simulation")){
			startSimulation();
		}
		else if(e.getActionCommand().equals("Reset")){
			resetSimulation();
		}
		else if(e.getActionCommand().equals("Increase Temp")){
			dc.heat();
		}
		else if(e.getActionCommand().equals("Decrease Temp")){
			dc.cool();
		}

	}

	public void updateMassGUI(){
		f.remove(mlabel);
		mlabel = new JLabel("Mass: "+m);
		mlabel.setBounds(735,75, 75, 50);
		updateDrawingCanvas();
		f.add(mlabel);
	}

	public void updateSpeedGUI(){
		f.remove(slabel);
		slabel = new JLabel("Speed: "+speed);
		slabel.setBounds(735,150, 75, 50);
		updateDrawingCanvas();
		f.add(slabel);
	}

	public void updateDrawingCanvas(){
		dc.stop();
		f.remove(dc);
		dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber);
		f.add(dc);
		f.setVisible(true);
	}

	public void updateParticleGUI(){
		f.remove(plabel);
		plabel = new JLabel("Particles: "+particleNumber);
		plabel.setBounds(750, 10, 100, 50);
		f.add(plabel);
	}

	public void startSimulation(){
		dc.startAnimationThread();
	}

	public void resetSimulation(){
		dc.stop();
		f.remove(dc);
		Particles = new Particle[100];
		particleNumber = 0;
		updateParticleGUI();
		dc = new DrawingCanvas(w,h,bw,bh,Particles,particleNumber); 
		f.add(dc);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		Simulation s = new Simulation();
		s.simulate();
	}

	
}