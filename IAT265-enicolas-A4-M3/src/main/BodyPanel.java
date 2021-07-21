package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

import background.BodyBackground;
import intruder.*;
import bodycells.*;
import immunesystem.*;
import util.*;

/*
 * 
 * This class is in charge of initializing the whole application, creating objects setting the JButtons, updating and drawing all of the objects and controlling collisions.
 * 
 *  
 */

public class BodyPanel extends JPanel implements ActionListener{
	
	private Timer timer;
	
	public final static int SCREEN_WIDTH = 1200;
	public final static int SCREEN_HEIGHT= 800;
	
	public static ArrayList<BaseCell> intruderList = new ArrayList<BaseCell>();
	public static ArrayList<BaseImmuneCell> immuneCellList = new ArrayList<BaseImmuneCell>();
	public static ArrayList<BaseCell> bodyCellList = new ArrayList<BaseCell>();
	public static ArrayList<BaseCell> infectedCellList = new ArrayList<BaseCell>();
	public static ArrayList<OrganCellWall> organCellWallList = new ArrayList<OrganCellWall>();
	
	private static BodyBackground background = new BodyBackground();
	
	//The start screen is just an image
	private BufferedImage startScreen;
	
	//sets the initial amount that will spawn for these objects
	private int initialMacrophage = 5;
	private int initialBodyCell = 20;
	
	private static int energy;
	private int respawnCellsTimer = 100;
	private int respawnBodyCellsTimer = 100;
	private int respawnOrganCellsTimer = 1000;
	
	private final static int START_SCREEN = 0;
	private final static int RUNNING_PROGRAM = 1;
	private final static int INTRUDER_WIN = 2;
	private final static int BODY_WIN = 3;
	
	private int programState = 0;
	
	JButton startButton = new JButton("Start");
	
	JButton resetButton = new JButton("Reset");
	
	JButton addBacteriaButton = new JButton("Add Bacteria");
	JButton addVirusButton = new JButton("Add Virus");
	JButton addMacrophageButton = new JButton("Add Macrophage");
	JButton addTCellButton = new JButton("Add TCell");
	JButton addAntibodyButton = new JButton("Add Antibody");
	JButton addBodyCellButton = new JButton("Add Body Cell");
	
	BodyPanel(){
		timer = new Timer(30,this);
		setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		
		//shows the start screen
		startScreen = Util.loadImage("assets/startScreen.png");
		startButton.setBounds(525,650,150,30);
		startButton.setFont(new Font("Arial", Font.PLAIN, 25));
		this.setLayout(null);
		this.add(startButton);
		
		startButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializeProgram();
			}
		}
		));
		
		initializeButtons();
		
		timer.start();
	}
	
	
	private void initializeProgram() {
		programState = RUNNING_PROGRAM;
		this.remove(startButton);
		this.remove(resetButton);
		
		this.add(addBacteriaButton);
		this.add(addVirusButton);
		this.add(addMacrophageButton);
		this.add(addTCellButton);
		this.add(addAntibodyButton);
		
		respawnCellsTimer = 100;
		respawnBodyCellsTimer = 100;
		respawnOrganCellsTimer = 1000;
		
		for(int i=0;i<initialMacrophage;i++) {
			immuneCellList.add(new Macrophage(Util.random(100,1000), Util.random(50,750), Util.random(-5,5), Util.random(-5,5),50,50,1));
		}
		
		for(int i=0;i<initialBodyCell;i++) {
			bodyCellList.add(new BodyCell(Util.random(100,800), Util.random(50,750), Util.random(-5,5), Util.random(-5,5),80,80,1));
		}
		
		
		//these objects use for loops to spawn because it would be very tedious to do it individually
		for(int i=1090;i<=1170;i+=80) {
			for(int j=80;j<800;j+=80) {
				bodyCellList.add(new OrganCell(i, j, 0, 0,70,70,1));
			}
		}
		
		for(int i=0;i<900;i+=80) {
			organCellWallList.add(new OrganCellWall(980,i, 0, 0,80,80,1));
		}
		
		energy = bodyCellList.size() * 100;
		
		//A 50% chance if the program starts off with a virus or a bacteria
		if(Util.random(0,1) > 0.5) {
			int initialBacteria = 15;
			for(int i=0;i<initialBacteria;i++) {
				intruderList.add(new Bacteria(Util.random(100,600), Util.random(50,750), Util.random(-3,3), Util.random(-3,3),20,15,1));
			}
		} else {
			bodyCellIterator();
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(programState==RUNNING_PROGRAM) {
			try {
				for(int i=0; i<intruderList.size();i++) {
					BaseCell in = intruderList.get(i);
					in.move();
				}
				
				for(int i=0; i<immuneCellList.size();i++) {
					BaseImmuneCell ic = immuneCellList.get(i);
					ic.move();
					
					//Separates objects into their own roles
					
					//TCells follow macrophages and increase their energy
					if(ic instanceof TCell) {
						for(int o=0;o<immuneCellList.size();o++) {
							if(immuneCellList.get(o) instanceof Macrophage) {
								BaseCell macro = immuneCellList.get(o);
								ic.goTowards(macro);
								if(ic.collideWith(macro)) {
									Macrophage m = (Macrophage) macro;
									m.increaseEnergy(5);
								}
							}
						}
						
					//Marcophages chase down any intruders. Everytime it eats something, its energy decreases
					} else if(ic instanceof Macrophage) {
						ic.goTowards(intruderList);
						for(int b=0;b<intruderList.size();b++) {
							BaseCell t = intruderList.get(b);
							if(ic.collideWith(t)){
								ic.decreaseEnergy(20);
								intruderList.remove(t);
							}
						}
						
					//Antibodies drift around on the screen and stop any intruders it touches
					} else{
						for(int b=0;b<intruderList.size();b++) {
							BaseCell t = intruderList.get(b);
							if(ic.collideWith(t)){
								t.setVel(t.getVel().x/2,t.getVel().y/2);
								ic.setVel(t.getVel().x,t.getVel().y);
							}
						}
					}
				}
				
				for(int i=0; i<bodyCellList.size();i++) {
					BaseBodyCell bc = (BaseBodyCell) bodyCellList.get(i);
					bc.move();
					
					if(bc instanceof BodyCell) {
						BodyCell b = (BodyCell) bc;
						if(b.getInfected() && !b.hasInfectedCell()) {
							
							//If a virus infects the body cell, this changes the decorator shown on top of the cell.
							if(b.getInfectedTimer() >= 200){
								BaseBodyCell bci = new BodyCellSlightInfected(bc);
								b.setInfectedCell(bci);
								infectedCellList.add(bci);
							} else if(b.getInfectedTimer() > 100) {
								BaseBodyCell bci = new BodyCellInfected(bc);
								b.setInfectedCell(bci);
								infectedCellList.add(bci);
							}					
						}
						
						//If a bacteria touches the body cell, this changes the decorator
						if(b.getInfectedLevel()!=0 && !b.hasInfectedCell()) {
							if(b.getInfectedLevel()==1) {
								BaseBodyCell bci = new BodyCellInfected(bc);
								b.setInfectedCell(bci);
								infectedCellList.add(bci);
							}
						}
						
					}
					
					
					//Change the the cell's fields according to what kind of intruder it is infected with
					for(int v=0;v<intruderList.size();v++) {
						BaseCell in = intruderList.get(v);
						if(in instanceof Virus) {
							if(bc.collideWith(in)) {
								bc.setInfected(true);
								intruderList.remove(in);
							}
						}
						
						if(in instanceof Bacteria) {
							if(bc.collideWith(in)) {
								bc.raiseInfectedLevel();
								intruderList.remove(in);
							}
						}
					}
					
				}
				
				for(int i=0; i<organCellWallList.size();i++) {
					OrganCellWall ocw = organCellWallList.get(i);
					
					if(!ocw.getPassable()) {
						for(int j=0;j<bodyCellList.size();j++) {
								if(bodyCellList.get(j) instanceof BodyCell) {
								BaseCell bc = bodyCellList.get(j);
								if(bc.collideWith(ocw)) {
									bc.setVel(Util.random(-5,-1),bc.getVel().y);
								}
							}
						}
					
					
						for(int j=0; j<intruderList.size();j++) {
							BaseCell in = intruderList.get(j);
							if(in.collideWith(ocw)) {
								intruderList.remove(in);
								ocw.setScale(ocw.getScale() - 0.25);
							}
						}
					}
				}
				manageEnergy();
			}catch (Exception ex) {
				System.out.println("Error in BodyPanel ActionPerformed method");
			}
		}
		
		
		manageStates();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(programState==RUNNING_PROGRAM) {
			background.drawBackground(g2);
			
			for(int i=0; i<intruderList.size();i++) {
				BaseCell in = intruderList.get(i);
				in.drawCell(g2);
			}
			
			for(int i=0; i<immuneCellList.size();i++) {
				BaseCell ic = immuneCellList.get(i);
				ic.drawCell(g2);
			}
			
			for(int i=0; i<bodyCellList.size();i++) {
				BaseCell bc = bodyCellList.get(i);
				bc.drawCell(g2);
			}
			
			for(int i=0;i<organCellWallList.size();i++) {
				BaseCell ocw = organCellWallList.get(i);
				ocw.drawCell(g2);
			}
			
			if(infectedCellList.size()!=0) {
	
				for(int i=0;i<infectedCellList.size();i++) {
					BaseCell ibc = infectedCellList.get(i);
					ibc.drawCell(g2);
				}
			}
			
			g2.setColor(new Color(67,219,49));
			g2.drawString("Body Energy: " + Integer.toString(energy), 10, 15);
		}
		
		else if(programState== START_SCREEN){
			g2.drawImage(startScreen,0,0,null);
			
		}
		
		else if(programState == INTRUDER_WIN) {
			g2.setColor(Color.red);
			g2.setFont(new Font("Arial", Font.PLAIN, 60));
			g2.drawString("HOST IS IN CRITICAL CONDITION", 150,300);
			g2.setFont(new Font("Arial", Font.PLAIN, 30));
			g2.drawString("The body can do nothing anymore",400,380);
		}
		
		else if(programState == BODY_WIN) {
			background.drawBackground(g2);
			g2.setColor(Color.white);
			g2.setFont(new Font("Arial", Font.PLAIN, 60));
			g2.drawString("THE INTRUDERS HAVE BEEN", 200,300);
			g2.drawString("ELIMINATED",450,380);
		}
		
	}
		
	private void manageEnergy() {
		energy = bodyCellList.size() * 100;
		
		if(energy>4000) {
			energy = 4000;
		}
		
		if(energy<=0) {
			programState = INTRUDER_WIN;
		}
		
		if(energy>=4000 && infectedCellList.size()==0 && intruderList.size()==0) {
			programState = BODY_WIN;
		}
		
		
		respawnCellsTimer--;
		respawnBodyCellsTimer--;
		respawnOrganCellsTimer--;
		
		//respawns organ cells and regrows organ cell walls
		if(respawnOrganCellsTimer == 0) {
			removeOldOrganCells();
			for(int j=80;j<800;j+=80) {
				bodyCellList.add(new OrganCell(1170, j, 0, 0,70,70,1));
			}
			
			for(int i=0; i<organCellWallList.size();i++) {
				OrganCellWall ocw = organCellWallList.get(i);
				ocw.setScale(ocw.getScale() + 0.25);
			}
		}
		
		if(respawnOrganCellsTimer == -500) {
			removeOldOrganCells();
			for(int j=80;j<800;j+=80) {
				bodyCellList.add(new OrganCell(1090, j, 0, 0,70,70,1));
			}
			respawnOrganCellsTimer = 2000;
		}
		
		
		//respawns body cells
		if(respawnBodyCellsTimer<0 && energy<4000) {
				addBodyCellAtPosition(Util.random(100,1000), Util.random(50,750));
			respawnBodyCellsTimer = 150;
		}
		
		//Spawns immune cells depending on the energy level
		if(intruderList.size()!=0) {
			
			if(energy < 1000 && respawnCellsTimer<0) {
				addTCellAtPosition(Util.random(100,1000), Util.random(50,750));
				
				for(int i=0;i<10;i++) {
					addAntibodyAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<2;i++) {
					addMacrophageAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				respawnCellsTimer = 100;
				energy-=50;
				organCellWallIterator();
				
			} else if(energy < 2500 && respawnCellsTimer<0){
				for(int i=0;i<2;i++) {
					addTCellAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<30;i++) {
					addAntibodyAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<3;i++) {
					addMacrophageAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				respawnCellsTimer = 75;
				energy-=150;
				organCellWallIterator();
				
				
			} else if(energy < 3100 && respawnCellsTimer<0){
				for(int i=0;i<1;i++) {
					addTCellAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<10;i++) {
					addAntibodyAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<2;i++) {
					addMacrophageAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				respawnCellsTimer = 80;
				energy-=100;
				organCellWallIterator();
				
			} else if(energy < 3700 && respawnCellsTimer<0) {
				for(int i=0;i<5;i++) {
					addAntibodyAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				for(int i=0;i<2;i++) {
					addMacrophageAtPosition(Util.random(100,1000), Util.random(50,750));
				}
				respawnCellsTimer = 70;
				energy-=100;
				organCellWallIterator();
			}
			
		}
	}
	
	private void manageStates() {
		if(programState==INTRUDER_WIN) {
			energy--;
			
			if(energy==-1) {	//-1 because i only want this happening once
				resetButton.setBounds(525,650,150,30);
				this.setLayout(null);
				
				this.remove(addBacteriaButton);
				this.remove(addVirusButton);
				this.remove(addMacrophageButton);
				this.remove(addTCellButton);
				this.remove(addAntibodyButton);
				
				this.add(resetButton);
				resetButton.addActionListener((new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						intruderList.clear();
						immuneCellList.clear();
						bodyCellList.clear();
						infectedCellList.clear();
						organCellWallList.clear();
						energy = 0;
						
						initializeProgram();
					}
				}
				));
			}
		}
		
		if(programState==BODY_WIN) {
			energy++;
			if(energy==4001) {	//4001 because i only want this happening once
				resetButton.setBounds(525,650,150,30);
				this.setLayout(null);
				
				this.remove(addBacteriaButton);
				this.remove(addVirusButton);
				this.remove(addMacrophageButton);
				this.remove(addTCellButton);
				this.remove(addAntibodyButton);
				
				this.add(resetButton);
				resetButton.addActionListener((new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						intruderList.clear();
						immuneCellList.clear();
						bodyCellList.clear();
						infectedCellList.clear();
						organCellWallList.clear();
						energy = 0;
						
						initializeProgram();
					}
				}
				));
			}
		}
		
	}
	
	
	//removes all of the old organ cells
	private void removeOldOrganCells() {
		for(int i=0;i<bodyCellList.size();i++) {
			if(bodyCellList.get(i) instanceof OrganCell) {
				BaseCell oc = bodyCellList.get(i);
				if(oc.getPos().x == 1170 && respawnOrganCellsTimer == 0 ) {
					bodyCellList.remove(oc);
				}
				
				if(oc.getPos().x == 1090 && respawnOrganCellsTimer == -500 ) {
					bodyCellList.remove(oc);
				}
			}
		}
	}
	
	//sets infected cells
	private void bodyCellIterator() {
		try {
			ListIterator litr = bodyCellList.listIterator();
			
			int amountInfected = 0;
			int index = -1;
			while(litr.hasNext()) {
				index++;
				if(litr.next() instanceof BodyCell) {
					BodyCell bc =  (BodyCell) bodyCellList.get(index);
					if(Util.random(0,100) < 10) {
						bc.setInfected(true);
						amountInfected++;
					}
				}
			}
			
			//if there arent any infected cells, just make the first body cell infected
			if(amountInfected==0) {
				BodyCell bc = (BodyCell) bodyCellList.get(0);
				bc.setInfected(true);
			}
		}catch(Exception e) {
			System.out.println("Error in BodyCellIterator");
		}
	}
	
	private void organCellWallIterator() {
		ListIterator litr = organCellWallList.listIterator();
		
		int index = -1;
		while(litr.hasNext()) {
			OrganCellWall ocw = (OrganCellWall) litr.next();
			if(ocw.getScale() <= 0.50) ocw.setPassable(true);
			else ocw.setPassable(false);
		}
	}
		
	
	public static int getEnergy() {
		return energy;
	}
	
	public static void addVirusAtPosition(float x, float y) {
		intruderList.add(new Virus(x, y, Util.random(-5,5), Util.random(-5,5),5,5,1));
	}
	
	public static void addBacteriaAtPosition(float x, float y) {
		intruderList.add(new Bacteria(x, y, Util.random(-3,3), Util.random(-3,3),5,5,1));
	}
	
	public static void addMacrophageAtPosition(float x, float y) {
		immuneCellList.add(new Macrophage(x, y, Util.random(-5,5), Util.random(-5,5),50,50,1));
	}

	public static void addTCellAtPosition(float x, float y) {
		immuneCellList.add(new TCell(x, y, Util.random(-5,5), Util.random(-5,5),20,20,1));
	}
	
	public static void addAntibodyAtPosition(float x, float y) {
		immuneCellList.add(new Antibody(x, y, Util.random(-5,5), Util.random(-5,5),20,20,1));
	}
	
	public static void addBodyCellAtPosition(float x, float y) {
		bodyCellList.add(new BodyCell(x, y, Util.random(-5,5), Util.random(-5,5),80,80,1));
	}


	private void initializeButtons() {
		addBacteriaButton.setBounds(120,750,150,20);
		addVirusButton.setBounds(290,750,150,20);
		addMacrophageButton.setBounds(460,750,150,20);
		addTCellButton.setBounds(630,750,150,20);
		addAntibodyButton.setBounds(800,750,150,20);
		
		addBacteriaButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBacteriaAtPosition(Util.random(100,1000), Util.random(50,750));
			}
		}
		));
		
		addVirusButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVirusAtPosition(Util.random(100,1000), Util.random(50,750));
			}
		}
		));
		
		addMacrophageButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMacrophageAtPosition(Util.random(100,1000), Util.random(50,750));
			}
		}
		));
		
		addTCellButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTCellAtPosition(Util.random(100,1000), Util.random(50,750));
			}
		}
		));
		
		addAntibodyButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAntibodyAtPosition(Util.random(100,1000), Util.random(50,750));
			}
		}
		));
		
		resetButton.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				intruderList.clear();
				immuneCellList.clear();
				bodyCellList.clear();
				infectedCellList.clear();
				organCellWallList.clear();
				energy = 0;
				
				initializeProgram();
			}
		}
		));
		
		
	}

}
