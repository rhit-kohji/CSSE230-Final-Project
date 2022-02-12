import java.util.ArrayList;
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
		Queue<Vertex> unvisitedVertices = new PriorityQueue<>();
		Map<String, Vertex> allVertices = new HashMap<>();
		
		Vertex start = this.getVertex(from);
		start.hCost = this.computeHCost(from, to);
		start.gCost = 0;
		unvisitedVertices.add(start); //TODO: iffy part right here
		allVertices.put(from, start);
		
		while( !unvisitedVertices.isEmpty() ) {
			Vertex next = unvisitedVertices.poll();
			if (next.name.equals(to)) { //TODO: iffy part right here
				List<Vertex> path = new ArrayList<>();
				Vertex current = next;
		       do {
		            path.add(0, current);
		            current = allVertices.get(current.getParent()); //always puts current at start of list, since going backwards
		        } while (current != null);
			}
		} //TODO: stopped here
		
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
		return this.vertices.get(this.vertices.indexOf(name));
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
		
		public ArrayList<Vertex> backTrace(Vertex start) { //TODO: check this later after implementing priority queue
			ArrayList<Vertex> path = new ArrayList<>();
			Vertex current = this;
//			while(current != start) {
//				path.add(current);
//				current = current.getParent();
//			}
			return path;
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
		
		public double getFCost() { //returns the total path cost
			return this.gCost + this.hCost;
		}

		@Override
		public int compareTo(Graph<String>.Vertex vertex) { //TODO: finish this
			//have variable called heuristic, compares heuristics when going to a new node?
			//do calculations for which ones are more optimal
			return 0;
		}
	}
	
}
