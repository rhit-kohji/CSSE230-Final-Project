import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<T extends GraphNode>{
	
	private Set<T> nodes;
	private Map<String, Set<String>> edges;
	
	public Graph(Set<T> nodes, Map<String, Set<String>> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}

	public T getNode(String id) {
		return nodes.stream()
				.filter(node -> node.getID().equals(id))
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
	}
	
	public Set<T> getEdges(T node) {
		return edges.get(node.getID()).stream()
	            .map(this::getNode)
	            .collect(Collectors.toSet());
	}
}
