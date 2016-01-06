package evo;

public class Food {
	
	public double energy;
	public int x;
	public int y;
	
	public Food(double energy)	{
		this.energy = energy;
	}
	
	public Food(double energy, int x, int y)	{
		this.energy = energy;
		this.x = x;
		this.y = y;
	}
	
	public double getEnergy() { return energy; }
	public void setEnergy(double energy) { this.energy = energy; }
	public void setPosition(int x, int y) { this.x = x; this.y = y; }
	public int getXPosition() { return x; }
	public int getYPosition() { return y; }
	
}
