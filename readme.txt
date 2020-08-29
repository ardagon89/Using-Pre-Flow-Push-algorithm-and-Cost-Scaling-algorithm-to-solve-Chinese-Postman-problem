README:
======

Long Project # 05.a: Finding max-flow by implementing Pre-Flow Push algorithm using FIFO queue.
		  b: Finding the minimum cost of the flow by implementing the Cost-Scaling algorithm.
		  c: Find the minimum cost tour of the Chinese Postman Problem by using Cost-Scaling algorithm to make the graph Eulerian and then finding the Euler tour of the augmented graph.


Authors :
------
1) Shariq Ali SXA190016
2) Abhigyan axs190140
3) Enakshi epm180002
4) Navanil nxs190026


How to compile and run the code:
-------------------------------
The files PostmanDriver.java, Postman.java, MinCostFlowDriver.java, MinCostFlow.java, Euler.java, DFS.java, Flow.java, maxFlowDriver.java, Graph.java & Timer.java should be placed inside the folder named as 'sxa190016' which is the package name.
Run the below commands sequentially to execute the program

1) The command prompt path should be in "sxa190016" directory
2) javac Timer.java
3) javac Graph.java
4) javac Flow.java
5) javac maxFlowDriver.java
6) javac DFS.java
7) javac Euler.java
8) javac MinCostFlow.java
9) javac MinCostFlowDriver.java
10) javac Postman.java
11) javac PostmanDriver.java
12) java maxFlowDriver
13) java MinCostFlowDriver
14) java PostmanDriver


Methods in Flow.java:
---------------------
The main methods written for Flow class are:

Flow		- Constructor to initialize the member variables of the flow class

setVerbose	- Set the verbosity of the output if VERBOSE>0

preflowPush	- Find the max-flow using FIFO queue of active vertices

initialize	- Initialize flow and excess to 0, push flow from source and label the height of each vertex

initHeight	- Calculate and initialize the height of each vertex from sink

printArray	- Print the array in a readable format

discharge	- Discharge the excess flow at vertex u

relabel		- Relabel vertex if it still has excess after discharge

flow		- Flow going through edge e

capacity	- Capacity of edge e

minCutS		- After maxflow has been computed, this method can be called to get the "S"-side of the min-cut found by the algorithm

minCutT		- After maxflow has been computed, this method can be called to get the "T"-side of the min-cut found by the algorithm


The main function in maxFlowDriver.java follows the below steps:
----------------------------------------------------------------
When you run the main function, it will
1. Read from console
2. Make the directed graph
3. Set the timer
4. Select source vertex
5. Select sink vertex
6. Initialize capacity f all the edges
7. Read capacity values from input and store in an array
8. Update capacities of all the edges
9. Initialize flow object
10. Set verbosity level of output
11. Find max-flow value
12. Print max-flow value
13. If verbosity is greater than 0 print the final flow through all edges and the min-cut sets S and T
14. Print the time taken for execution


Methods in MinCostFlow.java:
----------------------------
The main methods written for MinCostFlow class are:

MinCostFlow		- Constructor to initialize member variables

maxCost			- Find the maximum cost per flow among all edges

costScalingMinCostFlow	- Find the minimum cost of max flow through the graph

MCFforCPP		- Find the minimum cost of max flow through the graph for CPP problem

calcMinCost		- Find the min cost for flow going through each edge

refine			- Refine the cost to make it e/2 optimal

printFlow		- Print the flow for each edge in a readable fashion

discharge		- Discharge the flow through a vertex which has an excess

push			- Push flow through an edge

relabel			- Relabel a vertex which has an excess

RC			- Find the RC value for an edge

flow			- flow going through edge e

capacity		- capacity of edge e

cost			- cost of edge e

printArray		- Print the array in a readable format


The main function in MinCostFlowDriver.java follows the below steps:
--------------------------------------------------------------------
When you run the main function, it will
0. Set integer to denote the verbosity of output
1. Read from console
2. Make the directed graph
3. Read source vertex 
4. Read sink vertex
5. Initialize capacities of all edges
6. Initialize cost of all the edges
7. Initialize and populate the array
8. Set Source vertex
9. Set Sink Vertex
10. Fill capacities and cost of each edge
11. Set the timer
12. Initialize the min-cost flow object
13. Find the minimum cost of flow through the graph
14. Print min-cost value
15. If verbosity is greater than 0 print the final flow through all edges
16. Print the time taken for execution


Methods in Postman.java:
----------------------------
The main methods written for MinCostFlow class are:

Postman		- Constructor to initialize member variables

makeEulerian	- Make the original graph Eulerian

getTour		- Get a postman tour

postmanTour	- Find minimum cost of the postman tour

printArray	- Print the array in a readable format


The main function in PostmanDriver.java follows the below steps:
--------------------------------------------------------------------
When you run the main function, it will
0. Set integer to denote the verbosity of output
1. Read from console
2. Make the directed graph
3. Set the timer
4. Create an object of the Postman class
5. Find the min-cost of the tour which covers all edges
6. Print the result
7. Print the time taken for execution
8. If verbosity is greater than 0 print the tour


#Note : Set the VERBOSE = 1 if you want to see the intermediate steps in the algorithm.