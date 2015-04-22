
public class Town {
	int ID;
	String name;
	double lat;
	double lon;
	public Town(int ID, String name, double lat, double lon){
		this.ID = ID;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}
	public double distance(Town point2){
		double lat2 = point2.lat;
		double lat1 = this.lat;
		
		double lng2 = point2.lon;
		double lng1 = this.lon;
		
		double r = 6371;	//km
		
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = r * c;

	    return (dist);	    
	}
}