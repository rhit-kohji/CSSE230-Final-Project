import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MyViewer {
	static Dimension SCENE_VIEWER = new Dimension(1100, 630);
	public static void main(String[] args) {
	    Graph<String> graph;

	    try {
	    	ArrayList<String> keys = new ArrayList<>();
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
	    
	    try {
	    	File file = new File("../Vertex Positions.txt");

	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            String[] split = line.split(", ");
	            
	            String name = split[0];
	            int posX = Integer.parseInt(split[1]);
	            int posY = Integer.parseInt(split[2]);
	            
	            graph.addVertex(name, posX, posY);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return;
	    }
	    
	    for(int i=0; i<graph.vertices.size(); i++) {
	    	graph.vertices.get(i).createNeighbourList();
	    }
	    
//	    System.out.println(graph.getVertex("Hia Miu").toString());
	    ArrayList<Graph<String>.Vertex> route = graph.findRoute("Hia Miu", "Goma Asaagh");
	    System.out.println(route);
	    
//	    for(int i=0; i<graph.vertices.size(); i++) { //Checks for neighbours
//	    	System.out.println(graph.vertices.get(i).getName());
//	    	System.out.print("neighbours: ");
//	    	for(int j=0; j<graph.vertices.get(i).getNeighbours().size(); j++) {
//	    		System.out.print(graph.vertices.get(i).getNeighbours().get(j).getName() + " ");
//	    	}
//	    	System.out.println();
//	    }
	    
//	    System.out.println();
//	    System.out.println(Arrays.deepToString(graph.matrix)); //Checks state of matrix
	    
//	    System.out.println(graph.hasEdge("Katah Chuki", "Shae Loya")); //Checks hasEdge
//	    System.out.println(graph.hasEdge("Katah Chuki", "Sheem Dagoze"));
	
		JFrame frame = new JFrame();

		frame.setPreferredSize(SCENE_VIEWER);
		frame.setTitle("230 Final Project");

		frame.add(new MyComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
      
        JRadioButton timeButton = new JRadioButton("Distance");
		String[] distLocation = {"Distance in KM","5", "10", "20", "30", "40", "50"};
        JComboBox<String> distDropDown = new JComboBox<>(distLocation);
    
		JRadioButton distButton = new JRadioButton("Time");
		String[] timeLocation = {"Time","1 hour","2 hour","3 hour","4 hour","5 hour",};
        JComboBox<String> timeDropDown = new JComboBox<>(timeLocation);
        
		String[] startingLocation = {"Start","Katah Chuki", "Noya Neha", "Akh Va'quot", "Bareeda Naag", "Sha Warvo", "Tena Ko'sah","Voo Lota", "Maag No'rah", "Mijah Rokee", "Mogg Latan", "Shae Loya", 
				"Sheem Dagoze","Toh Yahsa", "Zalta Wa", "Monya Toma", "Rona Kachta", "Dunba Taag", "Gee Ha'rah","Goma Asaagh", "Hia Miu", "Lanno Kooh", "Maka Rah",
				"Mozo Shenno", "Qaza Toki", "Rin Oyaa", "Rok Uwog", "Sha Gemma", "Shada Naw", "To Quomo"};
        JComboBox<String> startDropDown = new JComboBox<>(startingLocation); 
		
		String[] endingLocation = {"End","Katah Chuki", "Noya Neha", "Akh Va'quot", "Bareeda Naag", "Sha Warvo", "Tena Ko'sah","Voo Lota", "Maag No'rah", "Mijah Rokee", "Mogg Latan", "Shae Loya", 
				"Sheem Dagoze","Toh Yahsa", "Zalta Wa", "Monya Toma", "Rona Kachta", "Dunba Taag", "Gee Ha'rah","Goma Asaagh", "Hia Miu", "Lanno Kooh", "Maka Rah",
				"Mozo Shenno", "Qaza Toki", "Rin Oyaa", "Rok Uwog", "Sha Gemma", "Shada Naw", "To Quomo"};
	    JComboBox<String> endDropDown = new JComboBox<>(endingLocation);
	    
	    JButton enter =new JButton("Enter");
	    
	    frame.add(new MyComponent(),BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		//Set up the panel to use a vertical layout and give it a background color
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.add(timeButton);
		buttonPanel.add(distButton);
		buttonPanel.add(startDropDown);
		buttonPanel.add(endDropDown);
		buttonPanel.add(timeDropDown);
		buttonPanel.add(distDropDown);
		buttonPanel.add(enter);
			
		frame.add(buttonPanel,BorderLayout.EAST);
		
        //Display the window.
        frame.pack();
        frame.setVisible(true);

	}
}


