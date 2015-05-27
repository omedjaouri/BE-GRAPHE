package core;
import java.util.ArrayList;



/**
 * 
 *+ coordinates: Coord
 *+ successors: ArrayList<Edges>
 *+ Identifier:Integer
 *
 */

public class Node {
	
	
	private Coordinate Coord;
	public ArrayList<Edges> successors;
	private int Identifier;
	
	
	
	public Node(Coordinate coord,int Id)
	{
		this.setCoord(new Coordinate(coord.getLatitude(),coord.getLongitude()));
		this.successors = new ArrayList<Edges>() ;
		this.setIdentifier(Id) ;
	}
	
	public void AddSuccessor(Edges SomeEdge)
	{
		this.successors.add(SomeEdge);		
		
	}

	public Edges Successor(Node last){
		for (Edges e : successors){
			if(e.end.getIdentifier() == last.getIdentifier()){
				System.out.println("Next Node " + last.getIdentifier()+
						"Sucessor " + e.end.getIdentifier());
				return e;
			}
		}

		return null;
	}
	
	public double airPath(Node fin){
		double long2 = fin.getCoord().getLongitude();
		double lat2 = fin.getCoord().getLatitude();
		double long1 = this.getCoord().getLongitude();
		double lat1 = this.getCoord().getLatitude();
		
		return Graphe.distance(long1, lat1, long2, lat2)/1000.0;
	}
	
	
	//Getters and Setters
	public Coordinate getCoord() {
		return Coord;
	}

	public void setCoord(Coordinate coord) {
		Coord = coord;
	}

	public int getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(int identifier) {
		Identifier = identifier;
	}

}
