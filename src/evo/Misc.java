package evo;

public class Misc {
	
	static public double calculateDistance(EcoPoint p1, EcoPoint p2) {
		double delta_x = Math.abs(p1.getX()-p2.getX());
		double delta_y = Math.abs(p1.getY()-p2.getY());
		
		double distance = Math.sqrt(delta_x*delta_x + delta_y*delta_y);
		
		return distance;
	}
	
	static public double calculateDistance(int x1, int y1, int x2, int y2) {
		double delta_x = Math.abs(x1-x2);
		double delta_y = Math.abs(y1-y2);
		
		double distance = Math.sqrt(delta_x*delta_x + delta_y*delta_y);
		
		return distance;
	}

}
