import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	static Dimension SCENE_VIEWER = new Dimension(992, 509);
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
		
		String[] startingLocation = {"Start"};
        JComboBox<String> startDropDown = new JComboBox<>(startingLocation);
        frame.add(startDropDown);
        
//	        String[] endingLocation = {"End"};
//	        JComboBox<String> endDropDown = new JComboBox<>(endingLocation);
//	        frame.add(endDropDown);
        
        
        JRadioButton timeButton = new JRadioButton("Time");
		JRadioButton distButton = new JRadioButton("Distance");
		
//        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
//	    radioPanel.add(timeButton);
//	    radioPanel.add(distButton);
//	    frame.add(radioPanel, BorderLayout.LINE_START);
		
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);

	}
}


