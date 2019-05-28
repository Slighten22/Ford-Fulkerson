import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
	ArrayList<Vertex> Vertexes;
	ArrayList<Edge> Edges;
	String start;
	String stop;

	public Graph(ArrayList<Vertex> V, ArrayList<Edge> E, String sta, String sto){
		Vertexes = V;
		Edges = E;
		start = sta;
		stop = sto;
	}
	
	public static void printGraph(ArrayList<Vertex> Vertexes, ArrayList<Edge> Edges) {
		for (Vertex v : Vertexes)
			System.out.print(v.name + ' ');
		System.out.println("");
		for (Edge e : Edges)
			System.out.print(e.vertex1.name + e.vertex2.name + ' ' + e.weight + ", ");
		System.out.println("");
	}

	static Graph readInput(String[] args) {
		if (args.length < 1) {
			System.out.println("Error: No graph file provided!");
			System.exit(0);
		}
		File file = new File(args[0]);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found!");
			System.exit(0);
		}
		int vertexNo = scanner.nextInt();
		int edgesNo = scanner.nextInt();
		ArrayList<Vertex> Vertexes = new ArrayList<Vertex>();
		ArrayList<Edge> Edges = new ArrayList<Edge>();
		scanner.nextLine();
		for (int i = 0; i < vertexNo; i++)
			Vertexes.add(new Vertex(scanner.nextLine(), i));
		for (int j = 0; j < edgesNo; j++) 
			Edges.add(new Edge(scanner.nextLine(), Vertexes)); 
		String start = scanner.next();
		String stop = scanner.next();
		scanner.close();
		//System.out.println("========== Input graph =======");
		//Graph.printGraph(Vertexes, Edges);
		return new Graph(Vertexes, Edges, start, stop);
	}

	public static ResidualNetwork transformGraph(Graph G) {
		ArrayList<Vertex> newVertexes = new ArrayList<>();
		ArrayList<Edge> newEdges = new ArrayList<>();
		Vertex newStart = new Vertex(G.start);
		Vertex newStop = new Vertex(G.stop + "'");
		for (Vertex v : G.Vertexes) {
			if (v.name.equals(G.start))
				newStart.order = 2*v.order;
			if (v.name.equals(G.stop))
				newStop.order = 2*v.order + 1;
		}
		int[][] adjMatrix = new int[2*G.Vertexes.size()][2*G.Vertexes.size()];
		
		//G.Vertexes.sort(null);
		int i = 0;
		for (Vertex v : G.Vertexes) {
			Vertex temp = new Vertex(v.name, v.cost, i);
			Vertex temp2 = new Vertex(v.name + "'", v.cost, i+1);
			newVertexes.add(temp);
			newVertexes.add(temp2);
			newEdges.add(new Edge(temp, temp2, v.cost));
			adjMatrix[i][i+1] = v.cost;
			if (!temp.name.equals(newStart) && !temp2.name.equals(newStop)) {
				newEdges.add(new Edge(temp2, temp, v.cost));
				adjMatrix[i+1][i] = v.cost;
			}
			i += 2;
		}
		
		for (Edge e : G.Edges) {
			Vertex temp = new Vertex(e.vertex1.name + "'", e.vertex1.cost, 2*e.vertex1.order+1);
			Vertex temp2 = new Vertex(e.vertex2.name, e.vertex2.cost, 2*e.vertex2.order);
			newEdges.add(new Edge(temp, temp2, e.weight));
			adjMatrix[temp.order][temp2.order] = e.weight;
			newEdges.add(new Edge(temp2, temp, e.weight));
			adjMatrix[temp2.order][temp.order] = e.weight;			
		}
		
		/*
		System.out.println("===== Transformed graph =====");
		printGraph(newVertexes, newEdges);   
		System.out.print('\n' + "Residual network: " + "\n   ");
		for (int in = 0; in < 2*G.Vertexes.size(); in++ ) {
			System.out.print(newVertexes.get(in).name);
			if (in % 2 == 0) System.out.print(' ');
		}
		
		System.out.println("");
		for (int in = 0; in < 2*G.Vertexes.size(); in++) {
			if (in%2 == 0) System.out.print(newVertexes.get(in).name + "  ");
			else System.out.print(newVertexes.get(in).name + ' ');
			for (int j = 0; j < 2*G.Vertexes.size(); j++)
				System.out.print(adjMatrix[in][j] + " ");
			System.out.println("");
		}
		*/

		return new ResidualNetwork(adjMatrix, newStart, newStop, newVertexes);
	}

}
