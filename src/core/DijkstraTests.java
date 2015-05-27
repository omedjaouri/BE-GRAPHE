package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import base.Readarg;

public class DijkstraTests extends Algo{
	
	final int MAX_TESTS = 5;
	protected int zoneOrigine ;
	protected int origine ;

	protected int zoneDestination ;
	protected int destination ;

	private int trials;
	private boolean use_star;
	private boolean use_distance;
	public ArrayList<Results> dtest_results;
	public ArrayList<Results> atest_results;

	public DijkstraTests(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);
		
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		
		this.trials = readarg.lireInt ("Nombre d'essais: ");
		
		int distance = readarg.lireInt("Utilise distance ou pas (1/0): ");
		if(distance == 1)
			use_distance = true;
		else
			use_distance = false;
		dtest_results = new ArrayList<Results>();
		atest_results = new ArrayList<Results>();
	}


	@Override
	public void run() {
		Results dcurrent = new Results();
		Results acurrent = new Results();
		Results dvalid = new Results();
		Results avalid = new Results();

		for(int k = 0; k<this.trials;k++){
		//Pick random origine and destination nodes
			int orig = pickNode((this.graphe.NodeList.size() - 1));
			int dest = pickNode((this.graphe.NodeList.size() - 1));
			
			//Dijkstra Tests
			
			//Dijkstra Tests: Validity
			//Find the cost between point A and point C	
			Dijkstra dtest_valid = new Dijkstra(this.graphe.NodeList);
			dtest_valid.computeRoutes(this.graphe.NodeList.get(orig), 
					this.graphe.NodeList.get(dest), 
						false, this.graphe.getVitMax(),false,0);
			//Find route between A and C
			dvalid.setIdealRoute(dtest_valid.reversePath(this.graphe.NodeList.get(dest)));
			//Find the cost for A->C
			double a_ccost = dvalid.getIdealRoute().get(dvalid.getIdealRoute().size() - 1).getCost();
			//Pick a point B somewhere on the fastest path
			
			if(dvalid.getIdealRoute().size() > 1 ){
					int point_b = pickNode(dvalid.getIdealRoute().size());
					point_b = dvalid.getIdealRoute().get(point_b).getCurrent().getIdentifier();
					if(point_b == 0 || point_b == (dvalid.getIdealRoute().size() - 1)){
						point_b = 1;
					}
					//Reinitialize Dijkstra to avoid bad pathing. Find shortest distance A -> B
					dtest_valid = new Dijkstra(this.graphe.NodeList);
					dtest_valid.computeRoutes(this.graphe.NodeList.get(orig), 
										this.graphe.NodeList.get(point_b), 
										false, this.graphe.getVitMax(),false,0);
					dvalid.setIdealRoute(dtest_valid.reversePath(this.graphe.NodeList.get(point_b)));
					double a_bcost = dvalid.getIdealRoute().get(dvalid.getIdealRoute().size() - 1).getCost();
					//Reinitialize Dijkstra to avoid bad pathing. Find shortest distance B -> C
					dtest_valid = new Dijkstra(this.graphe.NodeList);
					dtest_valid.computeRoutes(this.graphe.NodeList.get(point_b), 
										this.graphe.NodeList.get(dest), 
										false, this.graphe.getVitMax(),false,0);
					dvalid.setIdealRoute(dtest_valid.reversePath(this.graphe.NodeList.get(dest)));
					double b_ccost = dvalid.getIdealRoute().get(dvalid.getIdealRoute().size() - 1).getCost();
					printToValidityFile(orig, dest, point_b, a_ccost, a_bcost, b_ccost, false);
					
			}
			//Dijkstra Tests: Analytics
			//Assuring optimization using multiple trials
			for(int i = 0; i<MAX_TESTS; i++){
				Dijkstra dtest = new Dijkstra(this.graphe.NodeList);
				dtest.computeRoutes(this.graphe.NodeList.get(orig), 
						this.graphe.NodeList.get(dest), 
							false, this.graphe.getVitMax(),false,0);
				//Set test results
				dcurrent.setIdealRoute(dtest.reversePath(this.graphe.NodeList.get(dest)));
				double dcost = dcurrent.getIdealRoute().get(dcurrent.getIdealRoute().size() - 1).getCost();
				dcurrent.setCost(dcost);
				dcurrent.setMaxHeap(dtest.max_heap);
				dcurrent.setNumExplored(dtest.num_explored);
				dcurrent.setNumMarked(dtest.num_marked);
				dcurrent.setInitTime(dtest.init_time);
				dcurrent.setAlgoTime(dtest.algo_time);
				if(i == (MAX_TESTS - 1)){
					dcurrent.printToFile(false, orig, dest, i+1);
					dtest_results.add(dcurrent);
				}
			}
				
			//A* Tests
			//A* Tests: Validity
		
			//Find the cost between point A and point C	
			Dijkstra atest_valid = new Dijkstra(this.graphe.NodeList);
			atest_valid.computeRoutes(this.graphe.NodeList.get(orig), 
					this.graphe.NodeList.get(dest), 
						true, this.graphe.getVitMax(),false, 0);
			//Find route between A and C
			avalid.setIdealRoute(atest_valid.reversePath(this.graphe.NodeList.get(dest)));
			//Find the cost for A->C
			double a_ccost2 = avalid.getIdealRoute().get(avalid.getIdealRoute().size() - 1).getCost();
			//Pick a point B somewhere on the fastest path
			
			if(avalid.getIdealRoute().size() > 1 ){
					int point_b2 = pickNode(avalid.getIdealRoute().size());
					point_b2 = avalid.getIdealRoute().get(point_b2).getCurrent().getIdentifier();
					if(point_b2 == 0 || point_b2 == (avalid.getIdealRoute().size() - 1)){
						point_b2 = 1;
					}
					//Reinitialize Dijkstra to avoid bad pathing. Find shortest distance A -> B
					atest_valid = new Dijkstra(this.graphe.NodeList);
					atest_valid.computeRoutes(this.graphe.NodeList.get(orig), 
										this.graphe.NodeList.get(point_b2), 
										true, this.graphe.getVitMax(),false, 0);
					avalid.setIdealRoute(atest_valid.reversePath(this.graphe.NodeList.get(point_b2)));
					double a_bcost2 = avalid.getIdealRoute().get(avalid.getIdealRoute().size() - 1).getCost();
					//Reinitialize Dijkstra to avoid bad pathing. Find shortest distance B -> C
					atest_valid = new Dijkstra(this.graphe.NodeList);
					atest_valid.computeRoutes(this.graphe.NodeList.get(point_b2), 
										this.graphe.NodeList.get(dest), 
										true, this.graphe.getVitMax(),false, 0);
					avalid.setIdealRoute(atest_valid.reversePath(this.graphe.NodeList.get(dest)));
					double b_ccost2 = avalid.getIdealRoute().get(avalid.getIdealRoute().size() - 1).getCost();
					printToValidityFile(orig, dest, point_b2, a_ccost2, a_bcost2, b_ccost2, true);
			}
			
			//A* Tests: Analytics
			for(int j = 0; j<MAX_TESTS; j++){
				Dijkstra atest = new Dijkstra(this.graphe.NodeList);
				atest.computeRoutes(this.graphe.NodeList.get(orig), 
						this.graphe.NodeList.get(dest), 
							true, this.graphe.getVitMax(), false, 0);
				//Set test results
				acurrent.setIdealRoute(atest.reversePath(this.graphe.NodeList.get(dest)));
				double acost = acurrent.getIdealRoute().get(acurrent.getIdealRoute().size() - 1).getCost();
				acurrent.setCost(acost);
				acurrent.setMaxHeap(atest.max_heap);
				acurrent.setNumExplored(atest.num_explored);
				acurrent.setNumMarked(atest.num_marked);
				acurrent.setInitTime(atest.init_time);
				acurrent.setAlgoTime(atest.algo_time);
				if(j == (MAX_TESTS - 1)){
					acurrent.printToFile(true, this.origine, this.destination, j+1);
					atest_results.add(acurrent);
				}
			}
			printToCompareFile(orig, dest, dcurrent, acurrent);
		}
	}

	public void printToCompareFile(int orig, int dest, Results d, Results a){
		PrintWriter out = null;
		try {
		    out = new PrintWriter(new BufferedWriter(new FileWriter("comparison_frac.txt", true)));
		    	out.println();
			    out.println("Path from Node " + orig + " to Node " + dest + "     D                          A*");
				out.println("Total time (minutes):              " + 
							d.getCost() + 
							"     " + 
							a.getCost());
				out.println("Number of explored nodes:          " + 
							d.getNumExplored() + 
							"                      " + 
							a.getNumExplored());
				out.println("Max number of nodes in heap (tas): " + 
							d.getMaxHeap() +
							"                      " + 
							a.getMaxHeap());
				out.println("Nodes marked:                      " + 
							d.getNumMarked() + 
							"                      " + 
							a.getNumMarked());
				out.println("Time to initialize labels (ms)   : " + 
							d.getInitTime() + 
							"                      " + 
							a.getInitTime());
				out.println("Time for algorithm (ms):           " + 
							d.getAlgoTime() + 
							"                      " + 
							a.getAlgoTime());
				out.println();
		    	
		}catch (IOException e) {
			System.err.println(e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	public void printToValidityFile(int orig, int dest, int b, double a_ccost, double a_bcost, double b_ccost, boolean astar){
		PrintWriter out = null;
		try {
			if(!astar){
				out = new PrintWriter(new BufferedWriter(new FileWriter("validitydijstra_frac.txt", true)));
			}
			else{
				out = new PrintWriter(new BufferedWriter(new FileWriter("validityastar_frac.txt", true)));
			}
		    	out.println();
			    out.println("Path from Node " + orig + " to Node " + dest + " using Node " + b);
			    out.println("Cost from Node " + orig + " to Node " + dest + ": " + a_ccost);
			    out.println("Cost from Node " + orig + " to Node " + b + ": " + a_bcost);
			    out.println("Cost from Node " + orig + " to Node " + dest + ": " + b_ccost);
				out.println();
		    	
		}catch (IOException e) {
			System.err.println(e);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	public int pickNode(int max_nodes){
		Random rand = new Random();
		return rand.nextInt(max_nodes);
	}
	
	public boolean getUseDistance() {
		return use_distance;
	}
	public void setUseDistance(boolean use_distance) {
		this.use_distance = use_distance;
	}
	
	
	
	
}
