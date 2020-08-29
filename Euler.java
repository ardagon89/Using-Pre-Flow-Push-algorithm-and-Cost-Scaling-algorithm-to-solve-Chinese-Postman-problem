package sxa190016;

import sxa190016.Graph.Vertex;
import sxa190016.Graph;
import sxa190016.Graph.Edge;
import sxa190016.Graph.GraphAlgorithm;
import sxa190016.Graph.Factory;
import sxa190016.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Euler Circuit: Long project 2 Find Euler circuit in a directed
 *          graph or return null if not possible.
 */
public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
	/**
	 * To control the level of verbosity
	 */
	static int VERBOSE = 1;

	/**
	 * The start vertex of the Euler circuit
	 */
	Vertex start;

	/**
	 * The path followed by the Euler circuit
	 */
	List<Vertex> tour;

	/**
	 * Stack to keep track of active vertices
	 */
	Stack<Vertex> stack;

	// You need this if you want to store something at each node
	/**
	 * @author sxa190016
	 * @author axs190140
	 * @author epm180002
	 * @author nxs190026
	 * @version 1.0 Euler Circuit: Long project 2 Needed to save markedEdges for
	 *          each vertex
	 */
	static class EulerVertex implements Factory {
		/**
		 * Number of edges marked for each vertex
		 */
		int markedEdges;

		/**
		 * The constructor for EulerVertex
		 * 
		 * @param u The vertex of Graph.Vertex class
		 */
		EulerVertex(Vertex u) {
			markedEdges = 0;
		}

		/**
		 * Returns the EulerVertex
		 * 
		 * @param u The vertex of Graph.Vertex class
		 */
		public EulerVertex make(Vertex u) {
			return new EulerVertex(u);
		}
	}

	/**
	 * Constructor for Euler class
	 * 
	 * @param g     The graph of idsa.Graph class
	 * @param start The start vertex for the Euler tour
	 */
	public Euler(Graph g, Vertex start) {
		super(g, new EulerVertex(null));
		this.start = start;
		tour = new LinkedList<>();
		stack = new Stack<Vertex>();
	}

	/**
	 * Find unmarked vertices for the current vertex
	 * 
	 * @param v The current vertex
	 * @return True if unmarked vertex exists otherwise false
	 */
	public boolean hasUnmarkedVertices(Vertex v) {
		return this.get(v).markedEdges < v.outDegree();
	}

	/**
	 * Get the next unmarked edge for the current vertex
	 * 
	 * @param v The currect vertex
	 * @return The next unmarked edge
	 */
	public Edge getUnmarkedEdge(Vertex v) {
		int i = 0;
		for (Edge e : this.g.outEdges(v)) {
			if (i == this.get(v).markedEdges) {
				this.get(v).markedEdges++;
				return e;
			}
			i++;
		}
		return null;
	}

	/**
	 * To do: test if the graph is Eulerian. If the graph is not Eulerian, it prints
	 * the message: "Graph is not Eulerian" and one reason why, such as "inDegree =
	 * 5, outDegree = 3 at Vertex 37" or "Graph is not strongly connected"
	 * 
	 * @return True is graph is Eulerian else false
	 */
	public boolean isEulerian() {
		for (Vertex v : this.g.getVertexArray()) {
			// Check for inDegree!=outDegree
			if (v.outDegree() != v.inDegree()) {
				System.out.println("Graph is not Eulerian inDegree = " + v.inDegree() + ", outDegree = " + v.outDegree()
						+ " at Vertex " + v);
				return false;
			}
		}

		// Check for strongly connected components
		if (DFS.SCC_Vertex(g).scc > 1) {
			System.out.println("Graph is not strongly connected");
			return false;
		}
		return true;
	}

	/**
	 * Find the list of vertices which constitute the Euler tour
	 * 
	 * @return The list of vertices which constitute the Euler tour
	 */
	public List<Vertex> findEulerTour() {
		if (!isEulerian()) {
			return null;
		}

		/**
		 * The Euler tour in reverse order
		 */
		List<Vertex> revList = new LinkedList<>();
		this.stack.push(start);
		while (!stack.empty()) {
			Vertex u = stack.peek();
			if (this.hasUnmarkedVertices(u)) {
				Edge e = this.getUnmarkedEdge(u);
				stack.push(e.otherEnd(u));
			} else {
				revList.add(stack.pop());
			}
		}

		for (int i = revList.size() - 1; i >= 0; i--) {
			// Append the elements in reverse order
			tour.add(revList.get(i));
		}

		return tour;
	}

	/**
	 * Main function to test the LP2 class
	 * 
	 * @param args Arguments to be passed to the main function
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * Scanner to read input
		 */
		Scanner in;

		// Read from keyboard
		if (args.length > 1) {
			in = new Scanner(System.in);
		}

		// Read from file
		else if (args.length > 2) {
			File file = new File(
					"C:\\Users\\shari\\eclipse-workspace\\LP2\\src\\sxa190016\\lp2 test cases\\test3-xk-m.txt");
			in = new Scanner(file);
		}

		// Read from text string
		else {
			String input = "9 13\n" + "1 2 1\n" + "2 3 1\n" + "3 1 1\n" + "3 4 1\n" + "4 5 1\n" + "5 6 1\n" + "6 3 1\n"
					+ "4 7 1\n" + "7 8 1\n" + "8 4 1\n" + "5 7 1\n" + "7 9 1\n" + "9 5 1";
			in = new Scanner(input);
		}

		/**
		 * The start vertex
		 */
		int start = 1;
		if (args.length > 1) {
			start = Integer.parseInt(args[1]);
		}
		// output can be suppressed by passing 0 as third argument
		if (args.length > 2) {
			VERBOSE = Integer.parseInt(args[2]);
		}
		Graph g = Graph.readDirectedGraph(in);

		// Print the graph
		g.printGraph(false);

		// Get the start vertex
		Vertex startVertex = g.getVertex(start);

		// Start the timer
		Timer timer = new Timer();

		/**
		 * Create an object of Euler class
		 */
		Euler euler = new Euler(g, startVertex);

		/**
		 * Create a variable to store the tour vertices
		 */
		List<Vertex> tour = euler.findEulerTour();

		/**
		 * End the timer
		 */
		timer.end();
		if (VERBOSE > 0) {
			System.out.println("Output:");
			// print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
			System.out.println(tour);
		}
		System.out.println(timer);
	}

	/**
	 * Set the level of verbosity
	 * 
	 * @param ver The required level of verbosity
	 */
	public void setVerbose(int ver) {
		VERBOSE = ver;
	}
}
