package core;

import java.util.ArrayList;


import base.Dessin;



public class Path {

	public Node source ;
	public ArrayList<Segments> path ;
	public double longeur;
	private int nb_seg;

	// Rayon de la terre en metres
	private static final double rayon_terre = 6378137.0;

	public Path(Node sourceuh, double lon)
	{
		this.path = new ArrayList<Segments>();
		this.source = sourceuh;
		this.longeur = lon;

	}
//Use length instead in Edges
	public double verifyPath(double vitesse){

		double distance = this.longeur;
		double time = (distance/1000)/vitesse;
		System.out.println("Vitesse: " + vitesse + " Distance: " + distance + " Time: " + time);
		return time;
	}

	
	

	public static double distance(double long1, double lat1, double long2, double lat2) {
		double sinLat = Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2));
		double cosLat = Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));
		double cosLong = Math.cos(Math.toRadians(long2-long1));
		return rayon_terre*Math.acos(sinLat+cosLat*cosLong);
	}

	public void DrawPath(Dessin dessin)
	{
		if(nb_seg == 0){
			return;
		}
		float current_long = path.get(0).getDeltaLong();
		float current_lat = path.get(0).getDeltaLat();

		for (int i = 1 ; i <= nb_seg ; i++) 
		{
			dessin.drawLine(current_long, current_lat, (current_long + path.get(i).getDeltaLong()), (current_lat + path.get(i).getDeltaLat())) ;
			current_long += path.get(i).getDeltaLong() ;
			current_lat  += path.get(i).getDeltaLat() ;
		}
	}




}
