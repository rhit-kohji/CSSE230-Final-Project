import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<String> {
	ArrayList<Vertex> vertices;
	Map<String, Integer> keyToIndex;
	List<String> indexToKey;
	double[][] matrix; // Stores costs for each connection between nodes
	int numEdges;

	// These are our conversion factors for the values in our text file:
	double distanceConversionFactor = 5; // 1cm = 5km
	double travelSpeed = 22; // average horse cantor speed of 22km/hr

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
	public void reset() {
		for (int i = 0; i < this.vertices.size(); i++) {
			this.vertices.get(i).gCost = Double.MAX_VALUE;
			this.vertices.get(i).hCost = Double.MAX_VALUE;
			this.vertices.get(i).parent = null;
		}
	}

	/*
	 * This is where we put the A* algorithm
	 */
	public ArrayList<Vertex> findRoute(String from, String to) {
		if (!this.keyToIndex.containsKey(to) || !this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		this.reset();
		Queue<Vertex> openSet = new PriorityQueue<>(); // vertices to be evaluated, it needs to store edges
		Set<Vertex> closedSet = new HashSet<>(); // vertices that have been evaluated

		Vertex start = this.getVertex(from);
		Vertex target = this.getVertex(to);
		start.gCost = 0;
		start.hCost = this.computeHCost(from, to);
		openSet.add(start);

		while (!openSet.isEmpty()) {

			Vertex current = openSet.poll();
			if (closedSet.contains(current)) {
				continue;
			}

			closedSet.add(current);
			if (current.name.equals(to)) {
				return this.backTrace(start, target);
			}

			// this is where we do our calculations for gCost and hCost for the neighbours,
			// and add neighbours to the priority queue
			// in our compareTo function, we take sum of gCost and hCost of our different
			// paths to our neighbours and take the one that is less
			for (Edge neighbour : current.getNeighbours()) {
				if (closedSet.contains(neighbour.otherVertex)) { // vertex already evaluated
					continue; // skip to next neighbour
				}

				double newMovementCostToNeighbour = current.gCost + neighbour.getCost();

				if (newMovementCostToNeighbour < neighbour.otherVertex.gCost
						|| !openSet.contains(neighbour.otherVertex)) { // if new path to neighbour is shorter than prev.
																		// best path
					neighbour.otherVertex.gCost = newMovementCostToNeighbour; // assigning current best path
					neighbour.otherVertex.hCost = this.computeHCost(neighbour.otherVertex.name, to);
					neighbour.otherVertex.parent = current;

					openSet.add(neighbour.otherVertex);
				}
			}
		}
		return null;
	}

	/*
	 * This is wrapper class for the modified A* algorithm to find route(s) by cost.
	 * Each state holds a path that can be travelled under the max cost.
	 */
	public class State {
		private Vertex current;
		private ArrayList<Vertex> path;
		private double maxCost;

		public State(State current, Vertex newVertex, double cost) {
			this.current = newVertex;
			this.path = (ArrayList<Graph<String>.Vertex>) current.path.clone();
			this.path.add(newVertex);
			this.maxCost = current.maxCost + cost;
		}

		public State(Vertex current) {
			this.current = current;
			this.path = new ArrayList<Vertex>();
			this.maxCost = 0.0;
		}

		public ArrayList<Vertex> getPath() {
			return this.path;
		}
	}

	/*
	 * This is where we put the modified A* algorithm to find route(s) by cost
	 */
	public ArrayList<State> findRouteWithMaxCost(String from, double maxCost, boolean isTime) { // passing in maxCost as
																								// km or hours
		if (!this.keyToIndex.containsKey(from)) {
			throw new NoSuchElementException();
		}
		this.reset();

		if (isTime) {
			maxCost = (maxCost * travelSpeed) / distanceConversionFactor;
		} else {
			maxCost = (maxCost / distanceConversionFactor);
		}
		maxCost = maxCost / 2; // halved because we want to travel to and fro

		ArrayList<State> paths = new ArrayList<>(); // return this, pick out paths of each state in MyViewer

		Vertex start = this.getVertex(from);
		start.gCost = 0;

		paths.add(new State(start)); // start of arraylist

		int index = 0;

		while (index < paths.size()) {

			State current = paths.get(index++);

			for (Edge neighbour : current.current.getNeighbours()) {
				// make sure neighbour is not already in the current path
				// if it is not and if cost to get to the new neighbor is less than the
				// specified cost, create a new state and add it to paths
				// current + cost to get neighbor
				// eventually, return the arraylist of paths
				if (current.path.contains(neighbour.otherVertex)) { // make sure that the neighbor is not already in the
																	// current path
					continue; // skip to next neighbour
				}

				double newMovementCostToNeighbour = current.current.gCost + neighbour.getCost();
				if (newMovementCostToNeighbour <= maxCost && !neighbour.otherVertex.equals(start)) { // neighbour should
																										// not travel
																										// back to start
					State newState = new State(current, neighbour.otherVertex, neighbour.getCost());
					paths.add(newState);
					neighbour.otherVertex.gCost = newMovementCostToNeighbour; // assigning current best path
				}
			}
		}
		return paths;
	}

	public ArrayList<Vertex> backTrace(Vertex start, Vertex end) {
		ArrayList<Vertex> path = new ArrayList<>();
		Vertex current = end;
		while (current != null) {
			System.out.println(current.name);
			path.add(current);
			current = current.parent;
		}
		Collections.reverse(path);

		return path;
	}

	public double totalRouteCost(ArrayList<Vertex> route) {
		double total = 0;
		for (int i = 0; i < route.size() - 1; i++) {
			String from = route.get(i).name;
			String to = route.get(i + 1).name;
			double edgeCost = this.computePlainCost(from, to);
			total += edgeCost;
		}
		return total;
	}

	public String totalRouteDCost(double totalRouteCost) {
		double totalRouteDCost = totalRouteCost * distanceConversionFactor;
		totalRouteDCost = Math.floor(totalRouteDCost * 100) / 100;

		return (String) (totalRouteDCost + " km");
	}

	public String totalRouteTCost(double totalRouteCost) {
		double hours = ((totalRouteCost * distanceConversionFactor) / travelSpeed);
		int finalHours = (int) hours;
		int minutes = (int) (hours * 60) % 60;
		return (String) ("Hours: " + finalHours + " Minutes: " + minutes);
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

	public double computePlainCost(String from, String to) throws NoSuchElementException {
		if (!hasEdge(from, to))
			throw new NoSuchElementException();
		int fromIndex = this.keyToIndex.get(from);
		int toIndex = this.keyToIndex.get(to);

		return this.matrix[fromIndex][toIndex];
	}

	public double computeHCost(String from, String to) {
		Vertex fromVertex = this.getVertex(from);
		Vertex toVertex = this.getVertex(to);
		double hCost = Math
				.sqrt(Math.pow((toVertex.posX - fromVertex.posX), 2) + Math.pow((toVertex.posY - fromVertex.posY), 2));
		return hCost;
	}

	/*
	 * Edge stores the generic cost, computing time and distance will be done after
	 * route is found
	 */
	public class Edge implements Serializable {
		private Vertex otherVertex;
		private double cost;

		public Edge(Vertex otherVertex, double cost) {
			this.otherVertex = otherVertex;
			this.cost = cost;
		}

		public void setEdgeCost(double cost) {
			this.cost = cost;
		}

		public void setOtherVertex(Vertex otherVertex) {
			this.otherVertex = otherVertex;
		}

		public double getCost() {
			return this.cost;
		}
	}

	public class Vertex implements Comparable<Vertex> { // Used to locate nodes we want
		private String name;
		private Vertex parent;
		private ArrayList<Edge> neighbours;
		private int posX;
		private int posY;
		private double hCost; // straight line estimate cost to destination
		private double gCost; // total cost traveled from start node

		public Vertex(String name, int posX, int posY) {
			this.name = name;
			this.parent = null;
			this.neighbours = new ArrayList<>();
			this.posX = posX;
			this.posY = posY;
			this.hCost = Double.MAX_VALUE;
			this.gCost = Double.MAX_VALUE;
		}

		public void createNeighbourList() {
			ArrayList<Edge> list = new ArrayList<>();
			for (int i = 0; i < indexToKey.size(); i++) {
				if (hasEdge(this.name, indexToKey.get(i))) {
//					System.out.println(this.name + " " + vertices.get(i).getName());
					int fromIndex = Graph.this.keyToIndex.get(this.name);
					int toIndex = Graph.this.keyToIndex.get(indexToKey.get(i));
					double cost = matrix[fromIndex][toIndex];
					Edge newEdge = new Edge(Graph.this.getVertex(indexToKey.get(i)), cost);
					this.neighbours.add(newEdge);
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

		public ArrayList<Edge> getNeighbours() {
			return this.neighbours;
		}

		public int getPosX() {
			return this.posX;
		}

		public int getPosY() {
			return this.posY;
		}

		public double fCost() { // returns the total path cost
			return this.gCost + this.hCost;
		}

		public java.lang.String toString() {
			return (java.lang.String) this.name;
		}

		@Override
		public int compareTo(Vertex otherVertex) { // using fCost instead each cost separately
			// -1 higher priority
			// 0 just equal
			// 1 lower priority
			return (int) (otherVertex.fCost() - this.fCost());
		}

	}

}