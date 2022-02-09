import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MyViewer {
	static Dimension SCENE_VIEWER = new Dimension(750, 600);
	public static void main(String[] args) {
	    Graph<String> graph;

	    try {
	    	Set<String> keys = new HashSet<String>();
	    	File file = new File("../Nodes CSSE230 final.txt");
	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            
	            keys.add(line);
	        }
	        sc.close();
	        
	        graph = new Graph<String>(keys);
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return;
	    }
	    
	    try {
	    	File file = new File("../Distance measurements CSSE230 final.txt");

	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            String[] split = line.split(", ");
	            
	            String from = split[0];
	            String to = split[1];
	            double distance = Double.parseDouble(split[2]);
	            
	            graph.addEdge(from, to, distance);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return;
	    }
	    
	    System.out.println(Arrays.deepToString(graph.matrix));
	    
	    System.out.println(graph.hasEdge("Katah Chuki", "Sheem"));
	
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


