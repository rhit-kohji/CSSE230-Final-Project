
public class GraphProject {
	
	public static void main(String[] args){
		
		Graph<Integer> g = new Graph<Integer>();
		for (int i = 0; i < 8; i++) {
		   g.addNode(i);
		}
	   g.addEdge(1,2,1);
	   g.addEdge(1,3,1);
	   g.addEdge(2,1,1);
	   g.addEdge(2,4,1);
	   g.addEdge(3,1,1);
	   g.addEdge(3,4,1);
	   g.addEdge(3,6,1);
	   g.addEdge(4,2,1);
	   g.addEdge(4,3,1);
	   g.addEdge(4,5,1);
	   g.addEdge(4,7,1);
	   g.addEdge(5,4,1);
	   g.addEdge(6,3,1);
	   g.addEdge(6,7,1);
	   g.addEdge(7,4,1);	   
	   g.addEdge(7,6,1);		   
		   

	}

}
