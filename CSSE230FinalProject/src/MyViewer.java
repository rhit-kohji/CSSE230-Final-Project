
import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
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

// tiny change
public class MyViewer {
	static Dimension SCENE_VIEWER = new Dimension(1100, 630);

	public static void main(String[] args) {
		Graph<String> graph;
		MyComponent component = new MyComponent();

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
		} catch (FileNotFoundException e) {
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
		} catch (FileNotFoundException e) {
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < graph.vertices.size(); i++) { // DO NOT DELETE this is where we create our neighbour list
															// for each node
			graph.vertices.get(i).createNeighbourList();
		}

		/*
		 * Test neighbours
		 */
//	    	    for(int i=0; i<graph.vertices.size(); i++) { //Checks for neighbours
		// System.out.println(graph.vertices.get(i).getName());
		// System.out.print("neighbours: ");
		// for(int j=0; j<graph.vertices.get(i).getNeighbours().size(); j++) {
		// System.out.print(graph.vertices.get(i).getNeighbours().get(j).cost + " ");
		// }
		// System.out.println();
//	        	}

		/*
		 * Test route cases (both findRoute and findRouteWithMaxCost)
		 */
//	    	    ArrayList<Graph<String>.Vertex> route = graph.findRoute("Hia Miu", "To Quomo");
//	    	    ArrayList<Graph<String>.Vertex> route = graph.findRoute("Hia Miu", "Shada Naw");
//	    	    ArrayList<Graph<String>.State> maxRoutes = graph.findRouteWithMaxCost("Dunba Taag", 5.0, true);
//	    	    for(int i=0; i<maxRoutes.size(); i++) {
//	    	    	System.out.println(maxRoutes.get(i).getPath());
//	    	    }

		JFrame frame = new JFrame();

		frame.setPreferredSize(SCENE_VIEWER);
		frame.setTitle("230 Final Project");

		frame.add(new MyComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		String[] distLocation = { "Distance in KM", "None", "5", "10", "20", "30", "40", "50", "100", "150", "200" };
		JComboBox<String> distDropDown = new JComboBox<>(distLocation);
		distDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Distance Added");
				component.dist = true;
				component.distOnlyStr = distDropDown.getSelectedItem().toString();
			}
		});

		String[] timeLocation = { "Time in Hours", "None", "1", "2", "3", "4", "5", };
		JComboBox<String> timeDropDown = new JComboBox<>(timeLocation);
		timeDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time Added");
				component.time = true;
				component.timeOnlyStr = timeDropDown.getSelectedItem().toString();
			}
		});

		String[] startingLocation = { "Start", "Akh Va'quot", "Bareeda Naag", "Dunba Taag", "Gee Ha'rah", "Goma Asaagh",
				"Hia Miu", "Kah Okeo", "Katah Chuki", "Lanno Kooh", "Maag No'rah", "Maka Rah", "Mijah Rokee",
				"Mogg Latan", "Monya Toma", "Mozo Shenno", "Noya Neha", "Qaza Toki", "Rin Oyaa", "Rok Uwog",
				"Rona Kachta", "Sha Gemma", "Sha Warvo", "Shada Naw", "Shae Loya", "Sheem Dagoze", "Tena Ko'sah",
				"To Quomo", "Toh Yahsa", "Voo Lota", "Zalta Wa" };
		JComboBox<String> startDropDown = new JComboBox<>(startingLocation);
		startDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("First Location");
				component.start = true;
				component.startStr = startDropDown.getSelectedItem().toString();
			}
		});

		String[] endingLocation = { "End", "Akh Va'quot", "Bareeda Naag", "Dunba Taag", "Gee Ha'rah", "Goma Asaagh",
				"Hia Miu", "Kah Okeo", "Katah Chuki", "Lanno Kooh", "Maag No'rah", "Maka Rah", "Mijah Rokee",
				"Mogg Latan", "Monya Toma", "Mozo Shenno", "Noya Neha", "Qaza Toki", "Rin Oyaa", "Rok Uwog",
				"Rona Kachta", "Sha Gemma", "Sha Warvo", "Shada Naw", "Shae Loya", "Sheem Dagoze", "Tena Ko'sah",
				"To Quomo", "Toh Yahsa", "Voo Lota", "Zalta Wa" };
		JComboBox<String> endDropDown = new JComboBox<>(endingLocation);
		endDropDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("End Added");
				component.end = true;
				component.endStr = endDropDown.getSelectedItem().toString();
			}
		});

		String[] startingLocationTimeOrDist = { "Start for Dist/Time", "Akh Va'quot", "Bareeda Naag", "Dunba Taag",
				"Gee Ha'rah", "Goma Asaagh", "Hia Miu", "Kah Okeo", "Katah Chuki", "Lanno Kooh", "Maag No'rah",
				"Maka Rah", "Mijah Rokee", "Mogg Latan", "Monya Toma", "Mozo Shenno", "Noya Neha", "Qaza Toki",
				"Rin Oyaa", "Rok Uwog", "Rona Kachta", "Sha Gemma", "Sha Warvo", "Shada Naw", "Shae Loya",
				"Sheem Dagoze", "Tena Ko'sah", "To Quomo", "Toh Yahsa", "Voo Lota", "Zalta Wa" };
		JComboBox<String> startDropDownTimeOrDist = new JComboBox<>(startingLocationTimeOrDist);
		startDropDownTimeOrDist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start Location for Time or Dist");
				component.startStrTimeOrDist = startDropDownTimeOrDist.getSelectedItem().toString();
				component.startDistOrTime = true;
			}
		});

		JButton enter = new JButton("Enter");

		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Entered");
				Double maxCost;
				boolean isTime;
				if (component.end && component.start) {
					ArrayList<Graph<String>.Vertex> all = graph.findRoute(component.startStr, component.endStr);
					if (!all.isEmpty()) {
						component.addPathLines(all);
						double cost = graph.totalRouteCost(all);
						component.distStr = graph.totalRouteDCost(cost);
						component.timeStr = graph.totalRouteTCost(cost);
						component.repaint();
					}
				} else if (component.time && component.dist && component.startDistOrTime) {
					if (component.timeOnlyStr != "None" && component.distOnlyStr == "None") {
						isTime = true;
						maxCost = Double.parseDouble(component.timeOnlyStr);
					} else {
						isTime = false;
						maxCost = Double.parseDouble(component.distOnlyStr);
					}
					ArrayList<Graph<String>.State> al = graph.findRouteWithMaxCost(component.startStrTimeOrDist,
							maxCost, isTime);
					for (int i = 0; i < al.size() - 1; i++) {
						System.out.println(i + "a");
						component.addPathLines(al.get(i).getPath());
					}
					component.repaint();
				} else {
					System.out.print("error message for more than one component being selected");
				}

			}
		});

		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				component.start = false;
				component.end = false;
				component.dist = false;
				component.time = false;
				component.clear();
				component.repaint();
				// reset frame

			}
		});
		frame.add(component, BorderLayout.CENTER);

		frame.getContentPane().addMouseListener(new mouseClickListener());

		JPanel buttonPanel = new JPanel();
		// Set up the panel to use a vertical layout and give it a background color
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.add(startDropDown);
		buttonPanel.add(endDropDown);
		buttonPanel.add(startDropDownTimeOrDist);
		buttonPanel.add(timeDropDown);
		buttonPanel.add(distDropDown);
		buttonPanel.add(enter);
		buttonPanel.add(restart);

		frame.add(buttonPanel, BorderLayout.EAST);

		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}
}
