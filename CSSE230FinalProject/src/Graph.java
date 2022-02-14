import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


public class Graph<String>{
	ArrayList<Vertex> vertices;
	Map<String, Integer> keyToIndex;
	List<String> indexToKey;
	double[][] matrix; //Stores costs for each connection between nodes
	int numEdges;
	
	//These are our conversion factors for the values in our text file:  
	double distanceConversionFactor = 5; //1cm = 5km
	double travelSpeed = 6.44; //walking speed of 6.44km/hr
	
	public Graph(ArrayList<String> keys) {
		numEdges = 0;
		int size = keys.size();
		this.keyToIndex = new HashMap<String, Integer>();
		this.indexToKey = new ArrayList<String>(size);
		this.matrix = new double[size][size];
		this.vertices = new ArrayList<>();
		
		int i = 0;
		for (String key : keys) {
			this.keyToIndex.put(key, i);
			this.indexToKey.add(key);
			i++;
		}
	}
	
	/*
	 * Helper methods for A* algorithm and Graph
	 */
	public double distanceToTimeConverter(double distance) {
		return (distance / travelSpeed) * 60; //in minutes
	}
	
	/*
	 * TODO: This is where we put the A* algorithm
	 */
	public ArrayList<Vertex> findRoute(String from, String to) {
		if (!this.keyToIndex.containsKey(to) || !this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		Queue<Vertex> openSet = new PriorityQueue<>(); //vertices to be evaluated
		Map<String, Vertex> closedSet = new HashMap<>(); //vertices that have been evaluated
		
		ArrayList<Vertex> finalPath = new ArrayList<>();
		Vertex start = this.getVertex(from);
		Vertex target = this.getVertex(to);
		start.hCost = this.computeHCost(from, to);
		start.gCost = 0;
		openSet.add(start); //TODO: iffy part right here
		closedSet.put(from, start); 
		
		while( !openSet.isEmpty() ) {
			Vertex current = openSet.poll();
			if (current.name.equals(to)) { //TODO: iffy part right here
				finalPath = this.backTrace(start, target);
			}
			
			//TODO: this is where we do our calculations for gCost and hCost for the neighbours, and add neighbours to the priority queue
			//in our compareTo function, we take sum of gCost and hCost of our different paths to our neighbours and take the one that is less
			
			for(Vertex neighbour : current.getNeighbours()) { //TODO: iffy
				if( closedSet.containsKey(neighbour.name) ) { //vertex already evaluated
					continue; //skip to next neighbour
				}
				
				if(current.getParent() != null) {
					current.gCost = this.computeDistanceCost(current.getParent().name, current.name); //set parent					
				}
				double newMovementCostToNeighbour = current.gCost + this.computeDistanceCost(current.name, neighbour.name); //TODO: this could be what our pq is doing
				neighbour.gCost = this.computeDistanceCost(current.name, neighbour.name);
				if(newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
					neighbour.gCost = newMovementCostToNeighbour;
					neighbour.hCost = this.computeDistanceCost(neighbour.name, to);
					neighbour.parent = current;
					
					if(!openSet.contains(neighbour)) {
						openSet.add(neighbour);
					}
				}
			}
		}
		return finalPath;
	}
	
	public ArrayList<Vertex> backTrace(Vertex start, Vertex end) {
		ArrayList<Vertex> path = new ArrayList<>();
		Vertex current = end;
		while(current != start) {
			path.add(current);
			current = current.parent;
		}
		Collections.reverse(path);
		
		return path;
	}
	
	public int size() {
		return this.indexToKey.size();
	}
	
	public int numEdges() {
		return numEdges;
	}
	
	public boolean addEdge(String from, String to, double distance) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(to) || !this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		if (this.matrix[fromIndex][toIndex] != 0) {
			return false;
		}
		
		this.matrix[fromIndex][toIndex] = distance;

		this.numEdges++;
		return true;
	}
	
	public Vertex getVertex(String name) {
		int index = this.keyToIndex.get(name);
		return this.vertices.get(index);
//		return this.vertices.get(this.vertices.indexOf(name));
		
	}
	
	public boolean hasVertex(String key) {
		return this.keyToIndex.containsKey(key);
	}
	
	public void addVertex(String name, int x, int y) {
		Vertex temp = new Vertex(name, x, y);
		this.vertices.add(temp);
	}
	
	public boolean hasEdge(String from, String to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(to) || !this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		return this.matrix[fromIndex][toIndex] > 0;
	}
	
	public double computeTimeCost(String from, String to) throws NoSuchElementException {
		if(!hasEdge(from, to)) throw new NoSuchElementException();
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		return distanceToTimeConverter(this.matrix[fromIndex][toIndex]);
	}
	
	public double computeDistanceCost(String from, String to) throws NoSuchElementException {
		if(!hasEdge(from, to)) throw new NoSuchElementException();
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		return this.matrix[fromIndex][toIndex] * distanceConversionFactor;
	} 
	
	public double computeHCost(String from, String to) {
		Vertex fromVertex = this.getVertex(from);
		Vertex toVertex = this.getVertex(to);
		double hCost = Math.sqrt(Math.pow((toVertex.posX - fromVertex.posX), 2) + Math.pow((toVertex.posY - fromVertex.posY), 2));
		return hCost;
	}
	
	public class Vertex implements Comparable<Vertex> { //Used to locate nodes we want
		private String name;
		private Vertex parent;
		private ArrayList<Vertex> neighbours;
		private int posX;
		private int posY;
		private double hCost; //TODO: check this later
		private double gCost;
		
		public Vertex(String name, int posX, int posY) {
			this.name = name;
			this.parent = null;
			this.neighbours = new ArrayList<>();
			this.posX = posX;
			this.posY = posY;
			this.hCost = 0;
			this.gCost = 0;
		}
		
		public void createNeighbourList() {
			ArrayList<Vertex> list = new ArrayList<>();
			for(int i=0; i<indexToKey.size(); i++) {
				if( hasEdge(this.name, indexToKey.get(i)) ) {
//					System.out.println(this.name + " " + vertices.get(i).getName());
					this.neighbours.add(vertices.get(i));
				}
			}
		}
		
		public Vertex getParent() {
			return this.parent;
		}
		
		public void setParent(Vertex parent) {
			this.parent = parent;
		}

		public String getName() {
			return this.name;
		}
		
		public ArrayList<Vertex> getNeighbours() {
			return this.neighbours;
		}
		
		public int getPosX() { 
			return this.posX;
		}
		
		public int getPosY() {
			return this.posY;
		}
		
		public double fCost() { //returns the total path cost
			return this.gCost + this.hCost;
		}
		
		public java.lang.String toString() {
			return (java.lang.String) this.name;
		}

		@Override
		public int compareTo(Graph<String>.Vertex vertex) { //TODO: finish this
			//have variable called heuristic, compares heuristics when going to a new node?
			//do calculations for which ones are more optimal
			return 0;
		}
	}
	
}
