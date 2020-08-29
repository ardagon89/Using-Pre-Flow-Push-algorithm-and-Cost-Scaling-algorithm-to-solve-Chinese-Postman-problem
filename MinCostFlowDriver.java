package sxa190016;

import sxa190016.Graph;
import sxa190016.Graph.*;


import java.util.HashMap;
import java.util.Scanner;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Max Flow: Implement Cost-Scaling algorithm to find the min-cost Max-Flow through a directed graph.
 */
public class MinCostFlowDriver {

	/**
	 * Set integer to denote the verbosity of output
	 */
	static int VERBOSE = 0;

	/**
	 * The main method of the class
	 * 
	 * @param args			String array of arguments passed through the command line
	 */
	public static void main(String[] args) throws Exception {
		//Read from console
		Scanner in = new Scanner(System.in);

		if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		
		//Make the directed graph
		Graph g = Graph.readDirectedGraph(in);
		
		//Read source vertex
		int s = in.nextInt();
		
		//Read sink vertex
		int t = in.nextInt();
		
		//Initialize capacities of all edges
		HashMap<Edge,Integer> capacity = new HashMap<>();
		
		//Initialize cost of all the edges
		HashMap<Edge,Integer> cost = new HashMap<>();
		
		//Initialize and populate the array
		int[] arr = new int[1 + g.edgeSize()];
		for(int i=1; i<=g.edgeSize(); i++) {
			arr[i] = 1;   // default capacity
		}
		
		while(in.hasNextInt()) {
			int i = in.nextInt();
			int cap = in.nextInt();
			arr[i] = cap;
		}

		//Set Source vertex
		Vertex src = g.getVertex(s);
		
		//Set Sink Vertex
		Vertex target = g.getVertex(t);

		//Fill capacities and cost of each edge
		for(Vertex u: g) {
			for(Edge e: g.outEdges(u)) {
				capacity.put(e, arr[e.getName()]);
				cost.put(e, e.getWeight());
			}
		}

		//Set the timer
		Timer timer = new Timer();
		
		//Initialize the min-cost flow object
		MinCostFlow mcf = new MinCostFlow(g, src, target, capacity, cost);

		//Find the minimum cost of flow through the graph
		int result = mcf.costScalingMinCostFlow();

		//Print min-cost value
		System.out.println(result);

		//If verbosity is greater than 0 print the final flow through all edges
		if(VERBOSE > 0) {
			for(Vertex u: g) {
				System.out.print(u + " : ");
				for(Edge e: g.outEdges(u)) {
					if(mcf.flow(e) != 0) { System.out.print(e + ":" + mcf.flow(e) + "/" + mcf.capacity(e) + "@" + mcf.cost(e) + "| "); }
				}
				System.out.println();
			}
		}

		//Print the time taken for execution
		System.out.println(timer.end());
	}
}
