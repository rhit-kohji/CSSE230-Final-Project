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


public class TempGraph<String>{
	ArrayList<Vertex> vertices;
	Map<String, Integer> keyToIndex;
	List<String> indexToKey;
	double[][] matrix; //Stores costs for each connection between nodes
	int numEdges;
	
	//These are our conversion factors for the values in our text file:  
	double distanceConversionFactor = 5; //1cm = 5km
	double travelSpeed = 6.44; //walking speed of 6.44km/hr
	
	public TempGraph(ArrayList<String> keys) {
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
		Queue<State> openSet = new PriorityQueue<>(); //vertices to be evaluated
		Map<String, State> closedSet = new HashMap<>(); //vertices that have been evaluated
		State startState = new State(); //TODO: need to set gCost to 0, hCost to 0, parent to null
		
		start.hCost = this.computeHCost(from, to); //TODO: build hCost and gCost into state constructor
		start.gCost = 0; //combine 57, 59, 60 add vertex associated with from
		openSet.add(start); //TODO: iffy part right here 
		
		while( !openSet.isEmpty() ) {
			State current = openSet.poll();
			if (current.name.equals(to)) { //TODO: iffy part right here
				ArrayList<Vertex> finalPath; 
				finalPath = current.backTrace();
				return finalPath;
			}
			
			//TODO: this is where we do our calculations for gCost and hCost for the neighbours, and add neighbours to the priority queue
			//in our compareTo function, we take sum of gCost and hCost of our different paths to our neighbours and take the one that is less
			
			for(Edge neighbour : current.current.getNeighbours()) { //TODO: iffy
				if( closedSet.containsKey(neighbour.name) ) { //vertex already evaluated
					continue; //skip to next neighbour
				}
				State newState = new State();
				if(current.getParent() != null) { //TODO: should be parent.gCost + graph lookup (edge between parent and current node)
					newState.gCost = current.gCost + this.computeDistanceCost(current.name, neighbour.name); //set parent					
				}
				//double newMovementCostToNeighbour = current.gCost + this.computeDistanceCost(current.name, neighbour.name); //TODO: this could be what our pq is doing
				//neighbour.gCost = this.computeDistanceCost(current.name, neighbour.name);
				//if(newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
				//	neighbour.gCost = newMovementCostToNeighbour;
					newState.hCost = this.computeDistanceCost(neighbour.name, to);
					newState.parent = current;
					
					if(!openSet.contains(neighbour)) {
						openSet.add(neighbour);
					}
				}
			}
		}
		return null;
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
	
	public class State { //TODO: should help to navigate the graph 
		private Vertex parent;
		private Vertex current;
		private double hCost;
		private double gCost;
		
		public State(Vertex current) {
			this(current, null, 0, 0);
		}
		
		public State(Vertex current, Vertex parent, double gCost, double hCost) {
			this.current = current;
			this.parent = parent;
			this.gCost = gCost;
			this.hCost = hCost;
		}	
		
		public double fCost() { //returns the total path cost
			return this.gCost + this.hCost;
		}
	}
	
	public class Vertex implements Comparable<Vertex> { //Used to locate nodes we want, TODO: change vertex class into edge class
		private String name;
		private ArrayList<Vertex> neighbours;
		private int posX;
		private int posY;
		
		public Vertex(String name, int posX, int posY) {
			this.name = name;
			this.neighbours = new ArrayList<>();
			this.posX = posX;
			this.posY = posY;
		}
		
		//TODO: we need a separate class for edges
		//Edge itself will have at least 2 fields, one is vertex (one neighbour) and one is a cost 
		
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
		
		public java.lang.String toString() {
			return (java.lang.String) this.name;
		}

		@Override
		public int compareTo(TempGraph<String>.Vertex v) { //TODO: finish this
			//have variable called heuristic, compares heuristics when going to a new node?
			//do calculations for which ones are more optimal
			//this is how it sorts the pq
			//v.gCost + v.hCost
			//return less than or equal to, etc.
			//compare to current one
			return 0;
		}
	}
	
}
