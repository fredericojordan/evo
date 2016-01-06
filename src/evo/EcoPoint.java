package evo;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class EcoPoint {
	
	private double x=-1;
	private double y=-1;
	private Set<Creature> creaturePool = new LinkedHashSet<Creature>();
	private Set<Food> foodPool = new LinkedHashSet<Food>();
	
	public EcoPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void addCreature(Creature creature) { creaturePool.add(creature); }
	public void addFood(Food food) { foodPool.add(food); }
	public void removeCreature(Creature creature) { creaturePool.remove(creature); }
	public void removeFood(Food food) { foodPool.remove(food); }
	public Iterator<Creature> getCreatureIterator() { return creaturePool.iterator(); }
	public Set<Creature> getCreatureSet() { return creaturePool; }
	public Set<Food> getFoodSet() { return foodPool; }
	public double getX() { return x; }
	public double getY() { return y; }
	
}
