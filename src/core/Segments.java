package core;

public class Segments {

	private float deltaLat;
	private float deltaLong;
	
	public Segments(float lat, float lon)
	{
		this.setDeltaLat(lat);
		this.setDeltaLong(lon);
	}

	public float getDeltaLat() {
		return deltaLat;
	}

	public void setDeltaLat(float deltaLat) {
		this.deltaLat = deltaLat;
	}

	public float getDeltaLong() {
		return deltaLong;
	}

	public void setDeltaLong(float deltaLong) {
		this.deltaLong = deltaLong;
	}
	
	
}
