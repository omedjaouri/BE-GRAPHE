package core;

public class Coordinate {
	
	private float latitude;
	private float longitude;
	
	
	public Coordinate()
	{
		this.latitude = 0;
		this.longitude = 0;
	}
	
	public Coordinate(float lat, float lon)
	{
		this.setLatitude(lat);
		this.setLongitude(lon);
	}


	public float getLongitude() {
		return longitude;
	}


	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}


	public float getLatitude() {
		return latitude;
	}


	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
}
