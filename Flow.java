package sxa190016;

import sxa190016.Graph.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Max Flow: Implement Pre-Flow Push algorithm to find the Max-Flow through a directed graph.
 */
public class Flow {
	/**
	 * The directed graph g
	 */
	protected Graph g;
	
	/**
	 * The source vertex of the pre-flow push algo
	 */
	protected Vertex s;
	
	/**
	 * The sink vertex of the pre-flow push algo
	 */
	protected Vertex t;
	
	/**
	 * Contains the capacity of all the edges
	 */
	protected HashMap<Edge, Integer> capacity;
	
	/**
	 * Stores the flow through all the edges
	 */
	protected HashMap<Edge, Integer> flow;
	
	/**
	 * Queue to store all the active nodes
	 */
	protected Queue<Vertex> q;
	
	/**
	 * Stores the height at each vertex
	 */
	private int [] height;
	
	/**
	 * Stores the excess at each vertex
	 */
	protected int [] excess;
	
	/**
	 * Flag to control the verbosity of the output
	 */
	private boolean VERBOSE;
	
	/**
	 * Set to store the vertices of set S of the min cut.
	 */
	private Set<Vertex> S;
	
	/**
	 * Constructor to initialize the member variables of the flow class
	 * 
	 * @param g			The directed graph
	 * @param s			The start vertex
	 * @param t			The sink vertex
	 * @param capacity	hashmap containing capacity of each edge
	 */
    public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
    	this.g = g;
    	this.s = s;
    	this.t = t;
    	this.capacity = capacity;
    	this.flow = new HashMap<Edge, Integer>();
    	this.q = new LinkedList<Vertex>();
    	this.height = new int[g.size()];
    	this.excess = new int[g.size()];
    	this.VERBOSE = false;
    	this.S = new HashSet<Vertex>();
    }
    
    /**
     * Set the verbosity of the output if VERBOSE>0
     * 
     * @param VERBOSE		Input containing the integer verbosity level
     */
    public void setVerbose(int VERBOSE)
    {
    	if(VERBOSE>0)
    	{
    		this.VERBOSE = true;
    	}
    }

    /**
     * Find the max-flow using FIFO queue of active vertices
     * 
     * @return			Return max flow found.
     */
    public int preflowPush() {
    	initialize();
    	
    	if(this.VERBOSE)
    	{
	    	System.out.println("After initializing: ");
	    	System.out.println(this.q);
	    	this.printArray(this.height);
	    	this.printArray(this.excess);
    	}
    	
    	int i = 1;
    	Vertex u;
    	
    	//While there exist exist active vertices in the queue
    	while(this.q.size()!=0)
    	{
    		//pop an active vertex from the queue
    		u = q.poll();
    		
    		//discharge the excess at the vertex
    		discharge(u);
    		
    		//If it still has an excess relabel
    		if(this.excess[u.getIndex()]>0)
    		{
    			relabel(u);
    		}
    		
    		if(this.VERBOSE)
    		{
        		System.out.println("After "+i+++"th iteration: ");
            	System.out.println(this.q);
            	this.printArray(this.height);
            	this.printArray(this.excess);
    		}
    	}
    	
	//Return the excess at sink
	return this.excess[this.t.getIndex()];
    }
    
    /**
     * Initialize flow and excess to 0, push flow from source and label the height of each vertex
     */
    private void initialize()
    {
    	//Initialize flow to 0
    	for(Edge e: this.g.getEdgeArray())
    	{
    		this.flow.put(e, 0);
    	}
    	
    	//Initialize excess to 0
    	for(int i=0; i<this.excess.length; i++)
    	{
    		this.excess[i] = 0;
    	}
    	
    	//Label the height of each vertex
    	initHeight();
    	
    	//Label source as the highest vertex
    	this.height[this.s.getIndex()] = this.g.size();
    	
    	//Push the flow along all the outgoing edges of g
    	for(Edge e: this.g.outEdges(this.s))
    	{
    		this.flow.replace(e, this.capacity.get(e));
    		this.excess[s.getIndex()] -= this.capacity.get(e);
    		Vertex v = e.toVertex();
    		this.excess[v.getIndex()] += this.capacity.get(e);
    		if(this.capacity.get(e)>0 && !v.equals(this.s) && !v.equals(this.t))
    		{
        		this.q.add(e.otherEnd(this.s));
    		}
    	}
    }
    
    /**
     * Calculate and initialize the height of each vertex from sink
     */
    private void initHeight()
    {
    	//Reverse the graph
    	this.g.reverseGraph();
    	
    	//Do BFS
    	Queue<Vertex> queue = new LinkedList<Vertex>();
    	queue.add(this.t);
    	while(!queue.isEmpty())
    	{
    		for(int k=0; k<queue.size(); k++)
    		{
    			Vertex u = queue.poll();
    			for(Edge e: this.g.outEdges(u))
    			{
    				Vertex v = e.otherEnd(u);
    				if(!v.equals(this.t) && this.height[v.getIndex()]==0)
    				{
    					this.height[v.getIndex()] = this.height[u.getIndex()] + 1;
    					queue.add(v);
    				}
    			}
    		}
    	}
    	
    	//Reverse the graph again
    	this.g.reverseGraph();
    }
    
    //Print the array in a readable format
    protected void printArray(int [] arr)
    {
    	for(int i=0; i<arr.length; i++)
    	{
    		System.out.print(arr[i]+" ");
    	}
    	System.out.println();
    }
    
    /**
     * Discharge the excess flow at vertex u
     * 
     * @param u			The vertex from which excess flow has to be discharged
     */
    protected void discharge(Vertex u)
    {
    	//Discharge along each outgoing edges till there is excess
    	for(Edge e: this.g.outEdges(u))
    	{
    		Vertex v = e.toVertex();
    		
    		//If height of u is height of v+1
    		if(this.height[u.getIndex()] == this.height[v.getIndex()]+1)
    		{
    			int delta = 0;
    			
    			//delta is the min of excess and residual capacity of the edges
				delta = Math.min(this.excess[u.getIndex()], this.capacity(e)-this.flow(e));
				this.flow.replace(e, this.flow(e)+delta);
    			if(delta>0)
    			{
    				this.excess[u.getIndex()] -= delta;
        			this.excess[v.getIndex()] += delta;
        			
        			//Add to active list if it has excess
        			if(this.excess[v.getIndex()] == delta && !v.equals(this.s) && !v.equals(this.t))	//Change this to check without searching the list
        			{
        				this.q.add(v);
        			}
        			if(this.excess[u.getIndex()]==0)
        			{
        				return;
        			}
    			}
    		}
    	}
    	
    	//Same of incoming edges
    	for(Edge e: this.g.inEdges(u))
    	{
    		Vertex v = e.fromVertex();
    		if(this.height[u.getIndex()] == this.height[v.getIndex()]+1)
    		{
    			int delta = 0;
    			delta = Math.min(this.excess[u.getIndex()], this.flow(e));
    			this.flow.replace(e, this.flow(e)-delta);
    			if(delta>0)
    			{
    				this.excess[u.getIndex()] -= delta;
        			this.excess[v.getIndex()] += delta;
        			if(this.excess[v.getIndex()] == delta && !v.equals(this.s) && !v.equals(this.t))				//Change this to check without searching the list
        			{
        				this.q.add(v);
        			}
        			if(this.excess[u.getIndex()]==0)
        			{
        				return;
        			}
    			}
    		}
    	}
    }
    
    /**
     * Relabel vertex if it still has excess after discharge
     * 
     * @param u			The vertex to be relabeled
     */
    protected void relabel(Vertex u)
    {
    	int minHeight = Integer.MAX_VALUE;
    	
    	//Find the min height among all outgoing neighbors
    	for(Edge e: this.g.outEdges(u))
    	{
    		if(this.capacity(e)>this.flow(e))
			{
				minHeight = Math.min(minHeight, this.height[e.otherEnd(u).getIndex()]);
			}
    	}
    	
    	//Find the min height among all incoming neighbors
    	for(Edge e:this.g.inEdges(u))
    	{
    		if(this.flow(e)>0)
			{
				minHeight = Math.min(minHeight, this.height[e.otherEnd(u).getIndex()]);
			}
    	}
    	
    	//Set vertex height to minHeight + 1
    	this.height[u.getIndex()]=minHeight+1;
    	this.q.add(u);
    }

    /**
     * Flow going through edge e
     * @param e			The edge  in question
     * @return			The flow going through it
     */
    public int flow(Edge e) {
	return this.flow.get(e);
    }

    /**
     * Capacity of edge e
     * 
     * @param e			The edge in question
     * @return			It's capacity
     */
    public int capacity(Edge e) {
	return this.capacity.get(e);
    }

    /**
     * After maxflow has been computed, this method can be called to get 
     * the "S"-side of the min-cut found by the algorithm
     * 
     * @return			The set of vertices reachable from source
     */
    public Set<Vertex> minCutS() {
    	if(this.S.isEmpty())
    	{
    		//Do BFS
    		Queue<Vertex> queue = new LinkedList<Vertex>();
        	this.S.add(this.s);
        	queue.add(this.s);
        	while(!queue.isEmpty())
        	{
        		for(int k=0; k<queue.size(); k++)
        		{
        			Vertex u = queue.poll();
        			for(Edge e: this.g.outEdges(u))
        			{
        				if(this.capacity(e)-this.flow(e)>0)
        				{
        					Vertex v = e.otherEnd(u);
        					if(!this.S.contains(v))
        					{
        						this.S.add(v);
        				    	queue.add(v);
        					}
        				}
        			}
        		}
        	}
    	}    	
	return this.S;
    }

    /**
     * After maxflow has been computed, this method can be called to get 
     * the "T"-side of the min-cut found by the algorithm
     * 
     * @return			The set of vertices which are not reachable from source
     */
    public Set<Vertex> minCutT() {
    	//Make a set of all the vertices
    	Set<Vertex> T = new HashSet<Vertex>(Arrays.asList(this.g.getVertexArray()));
    	
    	//Remove the vertices reachable from source
    	T.removeAll(this.minCutS());
	return T;
    }
}
