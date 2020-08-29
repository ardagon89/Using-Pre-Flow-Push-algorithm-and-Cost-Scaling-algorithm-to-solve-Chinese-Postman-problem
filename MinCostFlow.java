package sxa190016;

import sxa190016.Graph.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sxa190016
 * @author axs190140
 * @author epm180002
 * @author nxs190026
 * @version 1.0 Cost Scaling: Implement Cost-scaling min-cost flow algorithm  to find the Max-Flow
 * 						  		with minimum cost through a directed graph.
 */
public class MinCostFlow extends Flow {
	/**
	 * Contains the cost per flow for each edge
	 */
	private HashMap<Edge, Integer> cost;
	
	/**
	 * The optimality approximation factor
	 */
	private float e;
	
	/**
	 * Stores the value of p for each vertex
	 */
	private float [] p;
	
	/**
	 * Stores the initial supply and demand for each vertex
	 */
	private int [] supply;
	
	/**
	 * Set to 1 to print all the intermediate steps
	 */
	public int VERBOSE;

	/**
	 * Constructor to initialize member variables
	 * 
	 * @param g			The input Graph 
	 * @param s			The source vertex
	 * @param t			The sink vertex
	 * @param capacity	The capacity of each edge
	 * @param cost		The cost per flow of each edge
	 */
	public MinCostFlow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity, HashMap<Edge, Integer> cost) {
		super(g, s, t, capacity);
		this.cost = cost;
		this.p = new float[g.size()];
		this.supply = new int[g.size()];
		this.VERBOSE = 0;
	}

	/**
	 * Find the maximum cost per flow among all edges
	 * 
	 * @return	Maximum cost per flow among all edges
	 */
	private int maxCost()
	{
		int result = 0;
		for(Map.Entry<Edge, Integer> entry : cost.entrySet())
		{
			if(entry.getValue().compareTo(result)>0)
			{
				result = entry.getValue();
			}
		}
		if(this.VERBOSE>0)
		{
			System.out.println("Max cost:"+result);
		}
		return result;
	}

	/**
	 * Find the minimum cost of max flow through the graph
	 * 
	 * @return	The cost of max flow found by cost scaling algorithm
	 */
	public int costScalingMinCostFlow() {
		this.e = this.maxCost();
		int v = new Flow(g, s, t, capacity).preflowPush();
		if(this.VERBOSE>0)
		{
			System.out.println("MaxFlow:"+v);
		}
		this.supply[this.s.getIndex()] = v;
		this.supply[this.t.getIndex()] = -v;
		long oldCost = Long.MAX_VALUE;
		long newCost = Long.MAX_VALUE;
		while(true)
		{
			if(this.VERBOSE>0)
			{
				System.out.println("e:"+this.e);
			}
			this.refine();
			newCost = this.calcMinCost();
			if(newCost==oldCost)
			{
				break;
			}
			else
			{
				this.e /= 2;
				oldCost=newCost;
			}
		}
		return (int)newCost;
	}

	/**
	 * Find the minimum cost of max flow through the graph for CPP problem
	 * 
	 * @param supply	The initial supply for each vertex
	 * @return			The minimum cost of max flow through the graph for CPP problem
	 */
	public long MCFforCPP(int [] supply) {
		this.e = this.maxCost();
		this.supply = supply;
		long oldCost = Long.MAX_VALUE;
		long newCost = Long.MAX_VALUE;
		while(true)
		{
			if(this.VERBOSE>0)
			{
				System.out.println("e:"+this.e);
			}
			this.refine();
			newCost = this.calcMinCost();
			if(newCost==oldCost)
			{
				break;
			}
			else
			{
				this.e /= 2;
				oldCost=newCost;
			}
		}
		return newCost;
	}

	/**
	 * Find the min cost for flow going through each edge
	 * 
	 * @return	The total sum of cost of flow going through each edge
	 */
	public long calcMinCost()
	{
		long result = 0;
		for(Edge e: this.g.getEdgeArray())
		{	
			result += (this.flow(e)*this.cost(e));
		}
		return result;
	}

	/**
	 * Refine the cost to make it e/2 optimal
	 */
	public void refine()
	{
		for(Edge e : this.g.getEdgeArray())
		{
			if(RC(e,1)<0)
			{
				this.flow.put(e, this.capacity(e));
			}
			else
			{
				this.flow.put(e, 0);
			}
		}
		if(this.VERBOSE>0)
		{
			System.out.println("Flow:");
			this.printFlow(); 
		}		   	
		for(Vertex u : this.g.getVertexArray())
		{
			this.excess[u.getIndex()] = this.supply[u.getIndex()];
			for(Edge e : this.g.inEdges(u))
			{
				this.excess[u.getIndex()] += this.flow(e);
			}
			for(Edge e : this.g.outEdges(u))
			{
				this.excess[u.getIndex()] -= this.flow(e);
			}
			if(this.excess[u.getIndex()]>0)
			{
				this.q.add(u);    			
			}
		}
		if(this.VERBOSE>0)
		{
			System.out.print("Excess:");
			this.printArray(this.excess);
			System.out.print("p:");
			this.printArray(this.p);
		}

		while(!q.isEmpty())
		{
			this.discharge(q.poll());
			if(this.VERBOSE>0)
			{
				System.out.print("Excess:");
				this.printArray(this.excess);
				System.out.print("p:");
				this.printArray(this.p);
				this.printFlow();
			}
		}
	}

	/**
	 * Print the flow for each edge in a readable fashion
	 */
	public void printFlow()
	{
		for(Edge e : this.g.getEdgeArray())
		{
			System.out.println(e+":"+this.flow(e));
		}
	}

	/**
	 * Discharge the flow through a vertex which has an excess
	 */
	public void discharge(Vertex u)
	{
		//Do while there is an excess
		while(this.excess[u.getIndex()]>0)
		{
			for(Edge e : this.g.outEdges(u))
			{
				if(this.flow(e)<this.capacity(e))
				{
					if(this.RC(e, 1)<0)
					{
						this.push(e, 1);
					}
				}
			}
			for(Edge e : this.g.inEdges(u))
			{
				if(this.flow(e)>0)
				{
					if(this.RC(e, -1)<0)
					{
						this.push(e, -1);
					}
				}
			}
			
			//If it still has an excess then relabel the vertex
			if(this.excess[u.getIndex()]>0)
			{
				this.relabel(u);
			}
		}
	}

	/**
	 * Push flow through an edge
	 * 
	 * @param e		The edge through which flow has to be pushed
	 * @param sign	+1 means in the direction of the edge and -1 means in the reverse direction
	 */
	public void push(Edge e, int sign)
	{
		if(sign>0)
		{
			int min = Math.min(this.excess[e.fromVertex().getIndex()], this.capacity(e)-this.flow(e));
			this.flow.replace(e, this.flow(e)+min);
			this.excess[e.fromVertex().getIndex()] -= min;
			this.excess[e.toVertex().getIndex()] += min;
			if(this.excess[e.toVertex().getIndex()]<=min)
			{
				this.q.add(e.toVertex());
			}
		}
		else
		{
			int min = Math.min(this.excess[e.toVertex().getIndex()], this.flow(e));
			this.flow.replace(e, this.flow(e)-min);
			this.excess[e.toVertex().getIndex()] -= min;
			this.excess[e.fromVertex().getIndex()] += min;
			if(this.excess[e.fromVertex().getIndex()]<=min)
			{
				this.q.add(e.fromVertex());
			}
		}
	}

	/**
	 * Relabel a vertex which has an excess
	 * 
	 * @param u		The vertex which is to be relabeled
	 */
	public void relabel(Vertex u)
	{
		this.p[u.getIndex()] -= this.e/2;
		if(this.VERBOSE>0)
		{
			for(Edge e : this.g.outEdges(u))
			{
				System.out.print(e+"["+(this.capacity(e)-this.flow(e))+","+this.RC(e, 1)+"] ");
			}
			for(Edge e : this.g.inEdges(u))
			{
				System.out.print(e+"["+this.flow(e)+","+this.RC(e, -1)+"] ");
			}
			System.out.println();
		}
	}

	/**
	 * Find the RC value for an edge
	 * 
	 * @param e		The edge for which RC is to be calculated
	 * @param sign	+1 means flow is in the direction of the edge and -1 means it is in reverse direction
	 * @return		The RC value for the edge
	 */
	public float RC(Edge e, int sign)
	{
		float result = sign * (this.cost(e)+this.p[e.fromVertex().getIndex()]-this.p[e.toVertex().getIndex()]);
		if(this.VERBOSE>0)
		{
			System.out.println("RC("+e+", "+sign+"):"+result);
		}
		return result;
	}

	/**
	 * flow going through edge e
	 * 
	 * @param e 	The edge
	 * @return		Flow of the edge
	 */
	public int flow(Edge e) {
		return super.flow(e);
	}

	/**
	 * capacity of edge e
	 * 
	 * @param e		The edge
	 * @return		Capacity of the edge
	 */
	public int capacity(Edge e) {
		return super.capacity(e);
	}

	/**
	 * cost of edge e
	 * 
	 * @param e		The edge
	 * @return		Cost per flow of the edge e
	 */
	public int cost(Edge e) {
		return this.cost.get(e);
	}

	/**
	 * Print the array in a readable format
	 * 
	 * @param arr	The array to be printed
	 */
	protected void printArray(float [] arr)
	{
		for(int i=0; i<arr.length; i++)
		{
			System.out.print(arr[i]+" ");
		}
		System.out.println();
	}
}