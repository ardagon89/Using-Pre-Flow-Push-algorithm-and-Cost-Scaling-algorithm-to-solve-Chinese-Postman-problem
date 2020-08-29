package sxa190016;

import sxa190016.Graph.Vertex;
import sxa190016.Graph;
import sxa190016.Graph.Edge;
import sxa190016.Graph.GraphAlgorithm;
import sxa190016.Graph.Factory;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 DFS: Long project 2 Find the number of strongly connected
 *          components in a directed graph.
 */
public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

	private LinkedList<Vertex> topologicalOrderedList;
	public int scc;
	private int topNo;
	public boolean isCyclic;
	List<List<Vertex>> SCC_Vertex;

	public static class DFSVertex implements Factory {

		int cno;
		int tp_rnk;
		public boolean visited;
		public Vertex parent;

		/**
		 * Converting vertex u to DFSVertex
		 * 
		 * @param u Given Vertex
		 */
		public DFSVertex(Vertex u) {
			this.cno = 0;
			this.tp_rnk = 0;
			this.visited = false;
			this.parent = null;
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
	}

	/**
	 * Does Depth First Search on the graph
	 * 
	 * @param g Given graph
	 * @return DFS object with the objects sorted
	 */
	public static DFS depthFirstSearch(Graph g) {
		DFS depthFirstSearch = new DFS(g);
		depthFirstSearch.dfsAll();
		return depthFirstSearch;
	}

	/**
	 * DFS method to run depth first search on the graph with a given order
	 * 
	 * @param iterable Order of vertex to perform DFS if they are not in the same
	 *                 connected components
	 */
	public void dfsAll(Iterable<Vertex> iterable) {
		topNo = g.size();
		for (Vertex u : g) {
			get(u).cno = 0;
			get(u).tp_rnk = 0;
			get(u).visited = false;
			get(u).parent = null;
		}

		scc = 0;
		topologicalOrderedList = new LinkedList<>();
		isCyclic = false;
		SCC_Vertex = new ArrayList<>();

		for (Vertex u : iterable) {
			if (!get(u).visited) {
				get(u).cno = ++scc;
				SCC_Vertex.add(new ArrayList<>());
				dfsVisit(u);
			}
		}
	}

	/**
	 * Initialing Graph g with numberOfConnectedComponents and isCyclic
	 * topologicalOrderedList is a new LinkedList will be used for storing the nodes
	 * in topological order SCC_Vertex is a new ArrayList for storing the vertices
	 * of a SCC.
	 * 
	 * @param g Given graph
	 */
	public DFS(Graph g) {
		super(g, new DFSVertex(null));
		scc = 0;
		topologicalOrderedList = new LinkedList<>();
		isCyclic = false;
		SCC_Vertex = new ArrayList<>();
	}

	/**
	 * Computes topological order of a given graph
	 * 
	 * @param g Given graph
	 * @return Returns null if g is not a DAG else return the topological oder of a
	 *         DAG using DFS.
	 */
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder1();
	}

	/**
	 * DFS method to run depth first search on the graph This method calls the
	 * method dfsALL by passing the Graph. Here we are using function overloading.
	 */
	public void dfsAll() {
		dfsAll(g);
	}

	/**
	 * Visiting each node adjacent to Vertex u
	 * 
	 * @param u Given vertex to visit
	 */
	private void dfsVisit(Vertex u) {
		get(u).visited = true;
		SCC_Vertex.get(scc - 1).add(u);
		for (Edge e : g.outEdges(u)) {
			Vertex v = e.otherEnd(u);
			if (!get(v).visited) {
				get(v).cno = get(u).cno;
				get(v).parent = u;
				dfsVisit(v);
			} else {
				if (get(v).tp_rnk == 0) {
					isCyclic = true;
				}
			}
		}
		get(u).tp_rnk = topNo--;
		topologicalOrderedList.addFirst(u);
	}

	/**
	 * Returns the strongly connected component index of a vertex
	 * 
	 * @param u Given Vertex
	 * @return SCC index of the vertex u
	 */
	public int cno(Vertex u) {
		return get(u).cno;
	}

	/**
	 * @return Returns null if graph is cyclic, otherwise list of vertices in
	 *         topological order
	 */
	public List<Vertex> topologicalOrder1() {
		dfsAll();
		if (isCyclic) {
			return null;
		}
		return topologicalOrderedList;
	}

	/**
	 * Get the number of strongly connected component of the graph
	 * 
	 * @return The number of SCC in the graph
	 */
	public int connectedComponents() {
		dfsAll();
		return scc;
	}

	/**
	 * Computes strongly connected components of a given graph using Kosaraju's
	 * algorithm does DFS then reverses the graph and does DFS from the sink which
	 * is the former source.
	 * 
	 * @param g Given graph
	 * @return Returns DFS object with SCC information
	 */
	public static DFS SCC_Vertex(Graph g) {
		DFS d = new DFS(g);
		d.dfsAll();
		List<Vertex> list = d.topologicalOrderedList;
		g.reverseGraph();
		d.dfsAll(list);
		g.reverseGraph();
		return d;
	}

	/**
	 * main function
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// String string ="7 8 1 2 0 1 3 0 2 4 0 3 4 0 4 5 0 6 5 0 6 7 0 7 5 0";
		Scanner in;

		String string = "16 24   1 2 1   2 6 1   3 8 1   4 3 1   5 6 1   5 9 1   6 7 1   6 12 1   7 1 1   7 3 1   7 11 1   8 4 1   8 12 1   9 14 1   10 11 1   10 13 1   11 8 1   11 12 1   12 15 1   12 16 1   13 9 1   14 10 1   14 15 1   15 11 1";
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		// 6 7 1 3 2 4 5

		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);
		System.out.println(SCC_Vertex(g).scc);
		System.out.println(SCC_Vertex(g).SCC_Vertex);
	}
}