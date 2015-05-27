package core;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Queue;

public class Dijkstra {
	
	
	public int max_heap = 0;
	public int num_explored = 0;
	public long algo_time = 0;
	public long init_time = 0;
	public int num_marked = 0;
	public HashMap<Node, Label> label = new HashMap<Node, Label>();
	public ArrayList<Label> marked = new ArrayList<Label>();
	
	Dijkstra(ArrayList<Node> nodelist){
		//Populate the array list with the labels
		//breadthFirstTraversal(nodelist, source);
		applyLabel(nodelist);
		this.max_heap = 0;
		this.num_explored = 0;
	}
	
	//Simple way to visit all of the nodes
	private void applyLabel(ArrayList<Node> nodelist){
		long start_time = System.currentTimeMillis();
		for (Node n : nodelist){
			Label l = new Label(false, Double.MAX_VALUE, null, n, 0);
			label.put(n, l);
			
		}
		long end_time = System.currentTimeMillis();
		this.init_time = end_time - start_time;
	}
	
	//0 = car, 1 = pieton
	public void computeRoutes(Node source, Node fin, boolean Astar, double vitesse_max, boolean use_distance, int vehicle){
		//Begin TIMING
		long start_time = System.currentTimeMillis();
		
		//Update the cost for the current node to be not infinite
		Label l = label.get(source);
		l.setCost(0.0);
		label.put(source, l);
		double estimation = 0;
		double prev_cost = 0;
		double costSoFar = 0;
		double cost = 0;
		num_explored++;
		
		BinaryHeap<Label> nodequeue = new BinaryHeap<Label>();
		l.setMark(true);
		marked.add(l);
		nodequeue.insert(label.get(source));
		
		while(!nodequeue.isEmpty()){
			//Pull the current node to check edges of that node
			Label current = nodequeue.deleteMin();
			
			if(current.getCurrent().getIdentifier() == fin.getIdentifier() ){
				break;
			}

			//Check adjacencies
			for(Edges e : current.getCurrent().successors){
				
				Node target = e.end;
				double distance = label.get(target).getCurrent().airPath(fin);
				double vitesse=((double)(e.desc.vitesseMax()));
				//Time it takes to traverse place
				if(!use_distance){	
					cost =(60.0*((double)(e.getLength()))/(1000.0*vitesse));
					//Add the current distance weight to the previous one to get the weight of the current path.
					costSoFar = cost + current.getCost();
					//If the distance so far is less than the cost of the path, it is a shorter path
					prev_cost = label.get(target).getCost();
				
					//If Astar is wanted, set the estimation value for the label
					if(Astar){
						estimation = (60.0 * distance)/(vitesse_max);  
					}
				}
				//If want to use distance
				else{
					if((vehicle == 1) && (vitesse==130.0||vitesse==110.0)){
						costSoFar = Double.POSITIVE_INFINITY;
					}
					else{
						cost =((double)(e.getLength()))/(1000.0);
						//Add the current distance weight to the previous one to get the weight of the current path.
						costSoFar = cost + current.getCost();
						//If the distance so far is less than the cost of the path, it is a shorter path
						prev_cost = label.get(target).getCost();
					}
					//If Astar is wanted, set the estimation value for the label
					if(Astar){
						estimation = distance;	
					}
				}
				/*System.out.println("From Node: " + current.getCurrent().getIdentifier() + " 1st Cost: "+
									current.getCost() + 
									"To Node: "+ target.getIdentifier() + " Cost: " + costSoFar + 
									" Prev Cost: " + prev_cost);
				*/
				//System.out.println("Estimation from target to end: " + label.get(target).getEstimation());
				if(costSoFar < prev_cost){
					label.get(target).setEstimation(estimation);
					
					//Update the values and set parent node
					label.get(target).setCost(costSoFar);
					label.get(target).setParent(current.getCurrent());
					
					
					//Update the node in the tas, if it isn't there, insert it;
					if(marked.contains(label.get(target))){
						nodequeue.update(label.get(target));
					}
					else{
						label.get(target).setMark(true);
						marked.add(label.get(target));
						num_explored++;
						nodequeue.insert(label.get(target));
					}
				}	
				
				if(nodequeue.size() > max_heap){
					max_heap = nodequeue.size();
				}	
			}

		}
			
		long end_time = System.currentTimeMillis();
		algo_time = end_time - start_time;

		
	}
	
	//After finding the shortest routes from the, you can reverse the path using the parent nodes.
	public List<Label> reversePath(Node end){
		List<Label> route = new ArrayList<Label>();
		for(Label current_node = label.get(end); current_node!=null; current_node = label.get(current_node.getParent())){
			route.add(current_node);
		}
		
		//Reverses the direction of the list so that it starts with the source node
		Collections.reverse(route);
		return route;
	}
	
}
