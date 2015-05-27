package core;

//It is the label assigned to nodes when performing Djikstra's Algorithm
public class Label implements Comparable<Label>{

	private boolean mark;
	private double cost;
	private Node parent;
	private Node current;
	private double estimation;
	
	
	Label(boolean mark, double cost, Node parent, Node current, double estimation){
		this.mark = mark;
		this.cost = cost;
		this.parent = parent;
		this.current = current;
		this.estimation = estimation;
	}
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public boolean isMark() {
		return mark;
	}
	public void setMark(boolean mark) {
		this.mark = mark;
	}
	public Node getCurrent() {
		return current;
	}
	public void setCurrent(Node current) {
		this.current = current;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public String fromTo(){
		if(this.parent == null){
			return "Start with: " + this.current.getIdentifier()  ;
		}
		return this.parent.getIdentifier() + " to " + this.current.getIdentifier()  ;
	}
	
	
	//0 = equal
	//1 = less
	//-1 = more
	public int compareTo(Label l) {
		if((this.cost + this.estimation) < (l.getCost() + l.getEstimation())){
			return -1;
		}
		else if((this.cost + this.estimation) == (l.getCost() + l.getEstimation())){
			
			if(this.estimation < l.getEstimation()){
				return 1;
			}
			else if(this.estimation == l.getEstimation()){
				return 0;
			}
			else{
				return -1;
			}
		}
		else{
			return 1;
		}
	}

	public double getEstimation() {
		return estimation;
	}

	public void setEstimation(double estimation) {
		this.estimation = estimation;
	}

	
	
}
