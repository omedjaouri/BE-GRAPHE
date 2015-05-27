package core;

import java.util.ArrayList;
import base.Descripteur;



public class Edges {
	
	public Node end;
	public ArrayList<Path> paths = new ArrayList<Path>();
	public Descripteur desc;
	private double length;
	
	public Edges(Node ending, Descripteur desc, double Long, int NbSeg)
	{
		this.end = ending;
		this.desc = desc;
		this.length = Long;
	}
	
	public Edges(Node ending, Descripteur desc, double Long, int NbSeg, Path path)
	{
		this.end = ending;
		this.desc = desc;
		this.length = Long;
		this.paths.add(path);
	}
	
	public void addPath(Path path){
		this.paths.add(path);
	}
	
	public Boolean checkSuccessor(Node last)
	{
		int end_node = last.getIdentifier();
		int current = this.end.getIdentifier();
		if(end_node == current){
			return true;
		}
		else return false;
	}
	
	
	public double getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}


	
}
