import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


public class Graph<String>{
	Map<String, Integer> keyToIndex;
	List<String> indexToKey;
	double[][] matrix; //Stores costs for each connection between nodes
	int numEdges;
	
	//These are our conversion factors for the values in our text file:  
	double distanceConversionFactor = 5; //1cm = 5km
	double travelSpeed = 6.44; //walking speed of 6.44km/hr
	
	public Graph(Set<String> keys) {
		numEdges = 0;
		int size = keys.size();
		this.keyToIndex = new HashMap<String, Integer>();
		this.indexToKey = new ArrayList<String>(size);
		this.matrix = new double[size][size];
		
		int i = 0;
		for (String key : keys) {
			this.keyToIndex.put(key, i);
			this.indexToKey.add(key);
			i++;
		}
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
	
	public boolean hasVertex(String key) {
		return this.keyToIndex.containsKey(key);
	}
	
	public boolean hasEdge(String from, String to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(to) || !this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);
		
		return this.matrix[fromIndex][toIndex] > 0;
	}
	
	private class Vertex { //Used to locate nodes we want
		private String name;
		private ArrayList<String> neighbours;
		private int posX;
		private int posY;
		
		public Vertex(String name, int posX, int posY) {
			this.name = name;
			this.neighbours = this.createNeighbourList();
			this.posX = posX;
			this.posY = posY;
		}
		
		private ArrayList<String> createNeighbourList() {
			ArrayList<String> list = new ArrayList<>();
			for(int i=0; i<indexToKey.size(); i++) {
				if( hasEdge(this.name, indexToKey.get(i)) ) {
					this.neighbours.add(indexToKey.get(i));
				}
			}
			return list;
		}

		public String getName() {
			return this.name;
		}
		
		public ArrayList<String> getNeighbours() {
			return this.neighbours;
		}
		
		public int getPosX() { 
			return this.posX;
		}
		
		public int getPosY() {
			return this.posY;
		}
	}
	
	/*
	 * Helper methods for A* algorithm and Graph
	 */
	public double distanceToTimeConverter(double distance) {
		return (distance / travelSpeed) * 60; //in minutes
	}
	
	public double kmConverter(double cmDistance) {
		return cmDistance * distanceConversionFactor;
	}
	
	/*
	 * TODO: This is where we put the A* algorithm
	 */
}
