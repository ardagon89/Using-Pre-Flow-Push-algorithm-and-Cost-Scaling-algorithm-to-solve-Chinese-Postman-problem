// Driver code for max flow
package sxa190016;

import sxa190016.Graph;
import sxa190016.Graph.Edge;
import sxa190016.Graph.Vertex;

import java.util.HashMap;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Max Flow: Implement Pre-Flow Push algorithm to find the Max-Flow through a directed graph.
 */
public class maxFlowDriver {
	/**
	 * integer to denote the verbosity of output
	 */
	static int VERBOSE = 0;

	/**
	 * The main method of the class
	 * 
	 * @param args			String array of arguments passed through the command line
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			VERBOSE = Integer.parseInt(args[0]);
		}
		
		//Read from console
		java.util.Scanner in = new java.util.Scanner(System.in);
		
		//Make the directed graph
		Graph g = Graph.readDirectedGraph(in);
		
		//Set the timer
		Timer timer = new Timer();
		
		//Select source vertex
		int s = in.nextInt();
		
		//Select sink vertex
		int t = in.nextInt();
		
		//Initialize capacity f all the edges
		HashMap<Edge, Integer> capacity = new HashMap<>();
		int[] arr = new int[1 + g.edgeSize()];
		for (int i = 1; i <= g.edgeSize(); i++) {
			arr[i] = 1;   // default capacity
		}
		
		//Read capacity values from input and store in an array
		while (in.hasNextInt()) {
			int i = in.nextInt();
			int cap = in.nextInt();
			arr[i] = cap;
		}
		
		//Update capacities of all the edges
		for (Vertex u : g) {
			for (Edge e : g.outEdges(u)) {
				capacity.put(e, arr[e.getName()]);
			}
		}

		//Initialize flow object
		Flow f = new Flow(g, g.getVertex(s), g.getVertex(t), capacity);
		
		//Set verbosity level of output
		//f.setVerbose(VERBOSE);
		
		//Find max-flow value
		int value = f.preflowPush();

		//Print max-flow value
		System.out.println(value);
		
		//If verbosity is greater than 0 print the final flow through all edges and the min-cut sets S and T
		if (VERBOSE > 0) {
			for (Vertex u : g) {
				System.out.print(u + " : ");
				for(Edge e: g.outEdges(u)) {
					System.out.print(e + ":" + f.flow(e) + "/" + f.capacity(e) + " | ");
				}
				System.out.println();
			}
			System.out.println("Min cut: S = " + f.minCutS());
			System.out.println("Min cut: T = " + f.minCutT());
		}

		//Print the time taken for execution
		System.out.println(timer.end());
	}
}

