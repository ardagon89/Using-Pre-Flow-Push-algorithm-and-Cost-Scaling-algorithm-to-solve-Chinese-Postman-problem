package sxa190016;

import sxa190016.Graph.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// Find a minimum weight postman tour that goes through every edge of g at least once
/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Chinese Postman algorithm: Implement Chinese Postman algorithm  to find the 
 * 											minimum cost route to cover all the edges of a graph.
 */
public class Postman {
	/**
	 * The original graph
	 */
	protected Graph g;
	
	/**
	 * The start vertex
	 */
	protected Vertex startVertex;
	
	/**
	 * The supply and demand at each vertex
	 */
	protected int [] supply;
	
	/**
	 * The capacity for each edge
	 */
	protected HashMap<Edge, Integer> capacity; 
	
	/**
	 * The cost for each edge
	 */
	protected HashMap<Edge, Integer> cost;
	
	/**
	 * The list of edges which constitute a tour
	 */
	protected List<Edge> tour;
	
	/**
	 * The minimum cost of the tour
	 */
	protected long tourCost;
	
	/**
	 * The edge given pair of vertices
	 */
	protected HashMap<String, Edge> edgeDict;
	
	/**
	 * Set to 1 to print all the intermediate steps
	 */
	public int VERBOSE;
	
	/**
	 * Constructor to initialize the member variables
	 * 
	 * @param g		The input graph g
	 */
    public Postman(Graph g) {
    	this(g, g.size()>0?g.getVertex(1):null);
    }
 
    /**
     * Constructor to initialize the member variables
     * 
     * @param g		The input graph g
     * @param startVertex	The start vertex of the tour
     */
	public Postman(Graph g, Vertex startVertex) {
		this.VERBOSE = 0;
    	this.g = g;
    	this.startVertex = startVertex;
    	this.supply = new int [this.g.size()+2];
    	this.capacity = new HashMap<Edge, Integer>();
    	this.cost = new HashMap<Edge, Integer>();
    	this.tour = new LinkedList<Edge>();
    	this.tourCost = 0L;
    	this.edgeDict = new HashMap<String, Edge>();
    	
    	//Make the graph Eulerian
    	this.makeEulerian();
    }
    
	/**
	 * Make the original graph Eulerian
	 */
    public void makeEulerian()
    {    	
    	// create an augmented graph instance with extra source and sink
    	Graph aug_g = new Graph(this.g.size()+2, true);
    	for(Edge e : this.g.getEdgeArray())
    	{
    		aug_g.addEdge(aug_g.getVertex(e.fromVertex()), aug_g.getVertex(e.toVertex()), e.getWeight(), e.getName());
    		this.edgeDict.put(e.fromVertex().getName()+"-"+e.toVertex().getName(), e);
    	}
    	if(this.VERBOSE>0)
    	{
        	aug_g.printGraph(true);
    	}
    	
    	int maxCapacity = 0;
    	for(Vertex v: this.g.getVertexArray())
    	{
    		int supply = v.inDegree()-v.outDegree();
    		this.supply[v.getIndex()] = supply;
    		if(supply>0)
    		{
    			maxCapacity += supply;
    		}
    	}
    	
    	for(Edge e : aug_g.getEdgeArray())
    	{
    		this.cost.put(e, e.getWeight());
    		this.capacity.put(e, maxCapacity);
    	}
    	
    	if(this.VERBOSE>0)
    	{
        	System.out.println(this.cost);
        	System.out.println(this.capacity);
    	}
    	
    	//Add extra edges from/to source and sink
    	for(Vertex v: this.g.getVertexArray())
    	{
    		int supply = this.supply[v.getIndex()];
    		if(supply>0)
    		{
    			Edge e = aug_g.addEdge(aug_g.getVertex(aug_g.size()-1), v, 0, aug_g.edgeSize()+1);
    			this.cost.put(e, 0);
    			this.capacity.put(e, supply);
    		}
    		else if(supply<0)
    		{
    			Edge e = aug_g.addEdge(v, aug_g.getVertex(aug_g.size()), 0, aug_g.edgeSize()+1);
    			this.cost.put(e, 0);
    			this.capacity.put(e, -supply);
    		}
    	}
    	
    	if(this.VERBOSE>0)
    	{
        	this.printArray(this.supply);
        	aug_g.printGraph(true);
        	System.out.println(this.cost);
        	System.out.println(this.capacity);
        	
        	for(Edge e: aug_g.getEdgeArray())
        	{
        		System.out.print(e.getName()+""+e+"["+e.getWeight()+"]");
        	}
        	System.out.println();
    	}
    	
    	//Initialize the MinCostFlow class with augmented graph
    	MinCostFlow mcf = new MinCostFlow(aug_g, aug_g.getVertex(aug_g.size()-1), aug_g.getVertex(aug_g.size()), this.capacity, this.cost);

    	//Find minimum cost flow
    	long result = mcf.MCFforCPP(this.supply);
    	
    	if(this.VERBOSE>0)
    	{
        	System.out.println(result);
    	}
    	
    	//Add extra edges wherever there is positive flow
    	for(Edge e : aug_g.getEdgeArray())
    	{
    		for(int i=0; i<mcf.flow(e); i++)
    		{
    			this.g.addEdge(this.g.getVertex(e.fromVertex().getName()), this.g.getVertex(e.toVertex().getName()), mcf.cost(e), this.g.edgeSize()+1);
    		}
    	}
    	if(this.VERBOSE>0)
    	{
        	this.g.printGraph(true);
    	}
    }
    
    /**
     * Get a postman tour
     * 
     * @return	The minimum cost tour
     */
    public List<Edge> getTour() {
    	if(this.tour.isEmpty())
    	{
    		/**
    		 * Create an object of Euler class
    		 */
    		Euler euler = new Euler(this.g, this.startVertex);

    		/**
    		 * Create a variable to store the tour vertices
    		 */
    		List<Vertex> ls = euler.findEulerTour();
    		for(int i=1; i<ls.size(); i++)
    		{
    			Edge e = this.edgeDict.get(ls.get(i-1).getName()+"-"+ls.get(i).getName());
    			this.tour.add(e);
    			this.tourCost += e.getWeight();
    		}
    	}
    	return this.tour;
    }

    /**
     * Find minimum cost of the postman tour
     * 
     * @return		The minimum cost of the postman tour
     */
    public long postmanTour() {
    	if(this.tour.isEmpty())
    	{
    		this.getTour();
    	}
    	return this.tourCost;
    }
    
    /**
     * Print the array in a readable format
     * 
     * @param arr		The array to be printed
     */
    protected void printArray(int [] arr)
    {
    	for(int i=0; i<arr.length; i++)
    	{
    		System.out.print(arr[i]+" ");
    	}
    	System.out.println();
    }
}
