package sxa190016;

import sxa190016.Graph.*;

import java.util.List;
import java.util.Scanner;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Max Flow: Implement Chinese Postman algorithm to find the min-cost route 
 * 						which covers all the edges through a directed graph.
 */
public class PostmanDriver {

	/**
	 * Set integer to denote the verbosity of output
	 */
	static int VERBOSE = 1;

	/**
	 * The main method of the class
	 * 
	 * @param args			String array of arguments passed through the command line
	 */
	public static void main(String[] args) throws Exception {

		//Read from console
		Scanner in = new Scanner(System.in);
		
		//Make the directed graph
		Graph g = Graph.readDirectedGraph(in);
		
		if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }

		//Set the timer
		Timer timer = new Timer();
		
		//Create an object of the Postman class
		Postman p = new Postman(g);
		
		//Find the min-cost of the tour which covers all edges
		long result = p.postmanTour();
		
		//Print the result
		System.out.println(result);
		
		//Print the time taken for execution
		System.out.println(timer.end());
		
		//If verbosity is greater than 0 print the tour
		if(VERBOSE > 0) {
			List<Edge> tour = p.getTour();
			if(tour != null) {
				for(Edge e: tour) { System.out.print(e); }
				System.out.println();
			}
		}
	}
}
