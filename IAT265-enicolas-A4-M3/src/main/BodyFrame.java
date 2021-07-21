package main;

import javax.swing.JFrame;

//JFrame which adds the BodyPanel for display

public class BodyFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("IAT265 A4 - Immune System Against Virus");	//create new JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BodyPanel bPanel = new BodyPanel();
        frame.add(bPanel);
        frame.pack();
        frame.setVisible(true);
	}

}
