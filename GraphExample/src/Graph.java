import java.util.ArrayList;
import java.util.Hashtable;

public class Graph<T> {
	private Hashtable<T, Node> nodes;

	public Graph(){
		nodes = new Hashtable<T, Node>();
	}
	
	private class Node {
		private T element;
		private ArrayList<Edge> neighbors;
		
		public Node(T e){
			element = e;
			neighbors = new ArrayList<Edge>();
		}
		
		public void addEdge(T e, int cost) {
			Node otherNode = nodes.get(e);
			neighbors.add(new Edge(otherNode, cost));
		}
		
	}
	
	private class Edge {
		private Node otherNode;
		private int cost;
		
		public Edge(Node n, int c){
			otherNode = n;
			cost = c;
		}
	}

	public boolean addNode(T e) {
		nodes.put(e, new Node(e));
		return true;
	}
	
	public boolean addEdge(T e1, T e2, int cost) {
		if (!nodes.containsKey(e1) && !nodes.containsKey(e2)) return false;
		nodes.get(e1).addEdge(e2, cost);
	    return true;
	}


}
