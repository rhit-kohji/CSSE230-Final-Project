import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MyViewer {
	static Dimension SCENE_VIEWER = new Dimension(750, 600);
	public static void main(String[] args) {
			JFrame frame = new JFrame();

			frame.setSize(SCENE_VIEWER);
			frame.setTitle("230 Final Project");

			frame.add(new MyComponent());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
			String[] startingLocation = {"Start"};
	        JComboBox<String> startDropDown = new JComboBox<>(startingLocation);
	        frame.add(startDropDown);
	        
//	        String[] endingLocation = {"End"};
//	        JComboBox<String> endDropDown = new JComboBox<>(endingLocation);
//	        frame.add(endDropDown);
	        
	        
	        JRadioButton timeButton = new JRadioButton("Time");
			JRadioButton distButton = new JRadioButton("Distance");
			
	        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		    radioPanel.add(timeButton);
		    radioPanel.add(distButton);
		    frame.add(radioPanel, BorderLayout.LINE_START);
			
	 
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);

	}
}


