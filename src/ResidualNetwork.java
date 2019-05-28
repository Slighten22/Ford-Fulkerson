import java.util.ArrayList;
import java.util.LinkedList;

public class ResidualNetwork {
	int[][] adjMatrix;
	Vertex start;
	Vertex stop;
	ArrayList<Vertex> Vertexes;
	
	public ResidualNetwork(int[][] matrix, Vertex sta, Vertex sto, ArrayList<Vertex> V) {
		adjMatrix = matrix;
		start = sta;
		stop = sto;
		Vertexes = V;
	}
	
	public static MinCut calculateMinCut(ResidualNetwork R) {
		int maxFlow = 0; // min cut value is equal to the value of max flow in a given network
		int verCount = R.adjMatrix.length; 
		int[][] adjMatrix = new int[verCount][verCount]; 
		for(int i = 0; i < verCount; i++)
			for(int j = 0; j < verCount; j++) 
				adjMatrix[i][j] = R.adjMatrix[i][j];
		Vertex start = R.start;
		Vertex stop = R.stop;
		int parent[] = new int[R.adjMatrix.length];	//holds parent of a vertex when a path if found (filled by BFS)
		int vertexU, vertexV;	//iterator vertices to loop over the matrix
		int currBottleneck1 = -1, currBottleneck2 = -1;
		ArrayList<Edge> bottleneckEdges = new ArrayList<Edge>();

		while (bfs(adjMatrix, start, stop, parent)) {		//if a path exists from S to T
			String pathString = "";		//Shows the augmented path taken

			//find bottleneck by looping over path from BFS using parent[] array
			int bottleneckFlow = Integer.MAX_VALUE;		//we want the bottleneck (minimum), so initially set it to the largest number possible. Loop updates value if it's smaller
			for (vertexV = stop.order; vertexV != start.order; vertexV = parent[vertexV]) {		//loop backward through the path using parent[] array
				vertexU = parent[vertexV];		//get the previous vertex in the path
				
				if(adjMatrix[vertexU][vertexV] < bottleneckFlow) { //minimum of previous bottleneck & the capacity of the new edge
					bottleneckFlow = adjMatrix[vertexU][vertexV];
					currBottleneck1 = vertexU;
					currBottleneck2 = vertexV;
				}
				pathString = " --> "+ vertexV + pathString;	//prepend vertex to path
			}
			
			Vertex v1 = new Vertex();
			Vertex v2 = new Vertex();
			for(Vertex v : R.Vertexes) {
				if(currBottleneck1 == v.order)
					v1 = v;
				else if (currBottleneck2 == v.order)
					v2 = v;
			}
			bottleneckEdges.add(new Edge(v1, v2, 0));
			pathString= start.name + pathString;		//loop stops before it gets to S, so add S to the beginning

			//Update residual graph capacities & reverse edges along the path
			for (vertexV = stop.order; vertexV != start.order; vertexV = parent[vertexV]) {	//loop backwards over path (same loop as above)
				vertexU = parent[vertexV];
				adjMatrix[vertexU][vertexV] -= bottleneckFlow;		//back edge
				
				//??
				adjMatrix[vertexV][vertexU] += bottleneckFlow;		//forward edge
			}

			maxFlow += bottleneckFlow;		//add the smallest flow found in the augmentation path to the overall flow
		}
		
		ArrayList<Vertex> FirstSet = new ArrayList<Vertex>();
		ArrayList<Vertex> SecondSet = new ArrayList<Vertex>();		
		for (int i = 0; i < R.adjMatrix.length; i++)
			SecondSet.add(R.Vertexes.get(i));
		MinCut temp = minCutBFS(adjMatrix, start, stop, R.adjMatrix);
		ArrayList<Integer> FirstSetOrders = temp.FirstSetOrders;
		ArrayList<Integer> BlockingEdges = temp.BlockingEdges;
		for(Vertex v : R.Vertexes) {
			if (FirstSetOrders.contains(v.order)) {
				FirstSet.add(v);
				SecondSet.remove(SecondSet.indexOf(v));
			}
		}
		
		return new MinCut(maxFlow, FirstSet, SecondSet, BlockingEdges);
	}

	//Returns true if it finds a path from S to T
	//saves the vertices in the path in parent[] array
	public static boolean bfs(int matrix[][], Vertex vertexS, Vertex vertexT, int parent[]) {
		boolean visited[] = new boolean[matrix.length];	//has a vertex been visited when finding a path. Boolean so all values start as false

		LinkedList<Integer> vertexQueue = new LinkedList<Integer>();		//queue of vertices to explore (BFS to FIFO queue)
		vertexQueue.add(vertexS.order);	//add source vertex
		visited[vertexS.order] = true;	//visit it
		parent[vertexS.order] = -1;			//"S" has no parent

		while (!vertexQueue.isEmpty()) {
			int vertexU = vertexQueue.remove();		//get a vertex from the queue

			for (int vertexV = 0; vertexV < matrix.length; vertexV++) {	//Check all edges to vertexV by checking all values in the row of the matrix
				if (visited[vertexV]==false && matrix[vertexU][vertexV] > 0) {	//residualGraph[u][v] > 0 means there actually is an edge
					vertexQueue.add(vertexV);
					parent[vertexV] = vertexU;	//??//used to calculate path later
					visited[vertexV] = true;
				}
			}
		}

		return visited[vertexT.order];	//return true/false if we found a path to T
		
	}
	
	
	public static MinCut minCutBFS(int matrix[][], Vertex vertexS, Vertex vertexT, int origMatrix[][]){
		boolean visited[] = new boolean[matrix.length];	//has a vertex been visited when finding a path. Boolean so all values start as false
		LinkedList<Integer> vertexQueue = new LinkedList<Integer>();		//queue of vertices to explore (BFS to FIFO queue)
		vertexQueue.add(vertexS.order);	//add source vertex
		visited[vertexS.order] = true;	//visit it
		ArrayList<Integer> BlockingEdges = new ArrayList<Integer>();
		
		while (!vertexQueue.isEmpty()) {
			int vertexU = vertexQueue.remove();		//get a vertex from the queue

			for (int vertexV = 0; vertexV < matrix.length; vertexV++) {	//Check all edges to vertexV by checking all values in the row of the matrix
				if (visited[vertexV]==false && matrix[vertexU][vertexV] > 0) {	//residualGraph[u][v] > 0 means there actually is an edge
					vertexQueue.add(vertexV);
					visited[vertexV] = true;
				}
				else if (visited[vertexV]==false && matrix[vertexU][vertexV] == 0 && origMatrix[vertexU][vertexV] > 0) {
					BlockingEdges.add(vertexU);
					BlockingEdges.add(vertexV);
				}
			}
		}
		
		ArrayList<Integer> FirstSetOrders = new ArrayList<Integer>();
		for(int i=0; i<matrix.length; i++) {
			if(visited[i]) 
				FirstSetOrders.add(i);
		}
		return new MinCut(FirstSetOrders, BlockingEdges);
	}
	
}
