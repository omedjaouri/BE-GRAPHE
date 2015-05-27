package core ;

import java.awt.Color;
import java.io.* ;
import java.util.List;

import base.Readarg ;

public class Pcc extends Algo {

	// Numero des sommets origine et destination
	protected int zoneOrigine ;
	protected int origine ;

	protected int zoneDestination ;
	protected int destination ;
	
	private boolean use_star  = false;

	public boolean isStar() {
		return use_star;
	}

	public void setStar(boolean use_star) {
		this.use_star = use_star;
	}

	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;

		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
	}
	
	//Draws the route generated by Dijkstra
	public void drawRoute(List<Label> route, boolean distance){
		for(Label l : route){
			if(l.getParent() != null){
				float lat1 = l.getCurrent().getCoord().getLatitude();
				float lat2 = l.getParent().getCoord().getLatitude();
				float long1 = l.getCurrent().getCoord().getLongitude();
				float long2 = l.getParent().getCoord().getLongitude();
				this.graphe.dessin.setColor(Color.RED);
				this.graphe.dessin.drawPoint(long1, lat1, 10);
				if(distance){
					if(use_star)
						this.graphe.dessin.setColor(Color.DARK_GRAY);
					else
						this.graphe.dessin.setColor(Color.BLUE);
				}
				else{
					if(use_star)
						this.graphe.dessin.setColor(Color.LIGHT_GRAY);
					else
						this.graphe.dessin.setColor(Color.BLACK);
				}
				this.graphe.dessin.setWidth(2);
				this.graphe.dessin.drawLine(long1, lat1, long2, lat2);
			}
		}
	}

	public void run(){
		
		//Create file buffers
		try {
			PrintWriter Dwriter = new PrintWriter("dijkstra_tests.txt", "UTF-8");
			PrintWriter Awriter = new PrintWriter("astar_tests.txt", "UTF-8");
		
			boolean use_distance = false;
		
			if(!use_star){
				Dwriter.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
			}
			if(use_star){
				Awriter.println("Run PCC star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
			}

			//Calculate Dijkstra's with the cost being time
			Dijkstra testtime = new Dijkstra(this.graphe.NodeList);
			//With time -> change the false to true if distance is desired
			testtime.computeRoutes(this.graphe.NodeList.get(this.origine), 
								this.graphe.NodeList.get(this.destination), 
									use_star, graphe.getVitMax(), use_distance, 0);
			List<Label> routetime = testtime.reversePath(this.graphe.NodeList.get(destination));
			
			System.out.println("Total time: " + routetime.get(routetime.size() - 1).getCost());
			System.out.println("Number of explored nodes: " + testtime.num_explored);
			System.out.println("Max number of nodes in heap (tas): " + testtime.max_heap);
			System.out.println("Nodes marked: " + testtime.marked.size());
			System.out.println("Time for algorithm (Dijkstras) in milliseconds: " + testtime.algo_time);		
		
			//Print the Chemin to easily view the route (cost = time)
			drawRoute(routetime, use_distance);
			
			//Calculate Dijkstra's with the cost being distance
			use_distance = true;
			Dijkstra testdist = new Dijkstra(this.graphe.NodeList);
			testdist.computeRoutes(this.graphe.NodeList.get(this.origine), 
								this.graphe.NodeList.get(this.destination), 
									use_star, graphe.getVitMax(), true, 0);
			List<Label> routedist = testdist.reversePath(this.graphe.NodeList.get(destination));
			
			System.out.println("Total Distance: " + routedist.get(routedist.size() - 1).getCost() + "kilometres");
			System.out.println("Number of explored nodes: " + testdist.num_explored);
			System.out.println("Max number of nodes in heap (tas): " + testdist.max_heap);
			System.out.println("Nodes marked: " + testdist.marked.size());
			System.out.println("Time for algorithm (Dijkstras) in milliseconds: " + testdist.algo_time);
			
			//Print the Chemin to easily view the route (cost = distance)
			drawRoute(routedist, use_distance);
			
			if(!use_star){
				Dwriter.println("Total time: " + routetime.get(routetime.size() - 1).getCost());
				Dwriter.println("Number of explored nodes: " + testtime.num_explored);
				Dwriter.println("Max number of nodes in heap (tas): " + testtime.max_heap);
				Dwriter.println("Nodes marked: " + testtime.marked.size());
				Dwriter.println("Time for algorithm (Dijkstras) in milliseconds: " + testtime.algo_time);
				Dwriter.println("");
				
				Dwriter.println("Total Distance: " + routedist.get(routedist.size() - 1).getCost() + "kilometres");
				Dwriter.println("Number of explored nodes: " + testdist.num_explored);
				Dwriter.println("Max number of nodes in heap (tas): " + testdist.max_heap);
				Dwriter.println("Nodes marked: " + testdist.marked.size());
				Dwriter.println("Time for algorithm (Dijkstras) in milliseconds: " + testdist.algo_time);
				Dwriter.println("");
			}
			else{
				Awriter.println("Total time: " + routetime.get(routetime.size() - 1).getCost());
				Awriter.println("Number of explored nodes: " + testtime.num_explored);
				Awriter.println("Max number of nodes in heap (tas): " + testtime.max_heap);
				Awriter.println("Nodes marked: " + testtime.marked.size());
				Awriter.println("Time for algorithm (Dijkstras with Astar) in milliseconds: " + testtime.algo_time);
				Awriter.println("");
				
				Awriter.println("Total Distance: " + routedist.get(routedist.size() - 1).getCost() + "kilometres");
				Awriter.println("Number of explored nodes: " + testdist.num_explored);
				Awriter.println("Max number of nodes in heap (tas): " + testdist.max_heap);
				Awriter.println("Nodes marked: " + testdist.marked.size());
				Awriter.println("Time for algorithm (Dijkstras with Astar) in milliseconds: " + testdist.algo_time);
				Awriter.println("");
			}

			
			Dwriter.close();
			Awriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
	}

}
