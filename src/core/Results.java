package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Results {

	private List<Label> ideal_route;
	private double cost;
	private int max_heap;
	private int num_explored;
	private long algo_time;
	private int num_marked;
	private long init_time;
	
	
	//Used to print results to predetermined files. Simple boolean to decide whether to print to
	//A* file or Dijkstra file.
	//Throws IOException
	public void printToFile(boolean astar, int start, int end, int trial){
		if(astar){
			//Open a file and print the results to the A* tests file. If there is an exception,
			//close the file.
			PrintWriter out = null;
			try {
			    out = new PrintWriter(new BufferedWriter(new FileWriter("astar_tests.txt", true)));
			    out.println("");
			    out.println("Path from Node " + start + " to Node " + end + ": Trial " + trial);
				out.println("Total time (minutes): " + this.cost);
				out.println("Number of explored nodes: " + this.num_explored);
				out.println("Max number of nodes in heap (tas): " + this.max_heap);
				out.println("Nodes marked: " + this.num_marked);
				out.println("Time to initialize labels in milliseconds: " + this.init_time);
				out.println("Time for algorithm (Dijkstras + A*) in milliseconds: " + this.algo_time);
			    out.println("");
			}catch (IOException e) {
			    System.err.println(e);
			}finally{
			    if(out != null){
			        out.close();
			    }
			}
		}
		else{
			//Open a file and print the results to the dijkstra tests file. If there is an exception,
			//close the file.
			PrintWriter out = null;
			try {
			    out = new PrintWriter(new BufferedWriter(new FileWriter("dijkstra_tests.txt", true)));
			    out.println("");
			    out.println("Path from Node " + start + " to Node " + end + ": Trial " + trial);
				out.println("Total time (minutes): " + this.cost);
				out.println("Number of explored nodes: " + this.num_explored);
				out.println("Max number of nodes in heap (tas): " + this.max_heap);
				out.println("Nodes marked: " + this.num_marked);
				out.println("Time to initialize labels in milliseconds: " + this.init_time);
				out.println("Time for algorithm (Dijkstras) in milliseconds: " + this.algo_time);
			    out.println("");
			}catch (IOException e) {
			    System.err.println(e);
			}finally{
			    if(out != null){
			        out.close();
			    }
			} 
		}
		
	}
	
	
	//GETTERS AND SETTERS
	public List<Label> getIdealRoute() {
		return ideal_route;
	}
	public void setIdealRoute(List<Label> ideal_route) {
		this.ideal_route = ideal_route;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getMaxHeap() {
		return max_heap;
	}
	public void setMaxHeap(int max_heap) {
		this.max_heap = max_heap;
	}
	public int getNumExplored() {
		return num_explored;
	}
	public void setNumExplored(int num_explored) {
		this.num_explored = num_explored;
	}
	public long getAlgoTime() {
		return algo_time;
	}
	public void setAlgoTime(long algo_time) {
		this.algo_time = algo_time;
	}
	public int getNumMarked() {
		return num_marked;
	}
	public void setNumMarked(int num_marked) {
		this.num_marked = num_marked;
	}


	public long getInitTime() {
		return init_time;
	}


	public void setInitTime(long init_time) {
		this.init_time = init_time;
	}
	
	
	
}
