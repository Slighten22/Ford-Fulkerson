
/*
 * Ford-Fulkerson algorithm for solving the Min Cut problem in a weighted graph
 * Author: Przemyslaw Kacperski
 * Student ID number: 283670
 * Project: AAL_BT.13
 * Topic: Cheapest repair cost in a computer network
 * Compilation in terminal: javac Main.java
 * Execution in terminal: java Main filename.txt
 */

public class Main {

	public static void main(String[] args) {
		System.out.println("*** Ford-Fulkerson algorithm for solving the MinCut problem in a weighted graph ***");
		
		// Read the data from the input file
		Graph G = Graph.readInput(args);
		// Transform the graph and build an adjacency matrix to be able to use Ford-Fulkerson algorithm
		ResidualNetwork R = Graph.transformGraph(G);
		// Perform the algorithm and return the value&shape of the min cut (equal to the calculated value of the max flow)
		MinCut MinC = ResidualNetwork.calculateMinCut(R);
		System.out.println(" - Min cut value: " + MinC.getMinCutValue());
		System.out.println(" - First set of vertices: V' = " + MinC.getFirstSet());
		System.out.println(" - Second set of vertices: V\\V' = " + MinC.getSecondSet());
		System.out.println("\nWhich parts of the network should be damaged to break the communication?" + MinC.whatToDamage());
	}
	
}
