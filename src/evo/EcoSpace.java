package evo;

import java.util.LinkedHashSet;
import java.util.Set;

public class EcoSpace {

	public int width;
	public int height;
	public EcoPoint[][] space;
	
	public EcoSpace(int width, int height) {
		this.height = height;
		this.width = width;
		this.space = new EcoPoint[width][height];
		
		createSpace();
	}
	
	public void createSpace() {
		System.out.printf("Creating space... ");
		
		for ( int x=0; x<width; x++ )
			for ( int y=0; y<height; y++ )
				space[x][y] = new EcoPoint(x, y);
		
		System.out.printf("done!\n");
	}
	
	public void addCreature(Creature creature, int x, int y) { space[x][y].addCreature(creature); }
	public void removeCreature(Creature creature) { space[creature.x][creature.y].removeCreature(creature); }
	public void addFood(Food food, int x, int y) { space[x][y].addFood(food); }
	public void removeFood(Food food) { space[food.x][food.y].removeFood(food);	}
	public int getMidX() { return (int) Math.floor(width/2); }
	public int getMidY() { return (int) Math.floor(height/2); }
	public int getRandomX() { return (int) Math.floor(Math.random()*width); }
	public int getRandomY() { return (int) Math.floor(Math.random()*height); }
	
	public Set<Creature> createCreatureCircle(int x_center, int y_center, double radius) {
		Set<Creature> creatures = new LinkedHashSet<Creature>();
		
		for ( int x=0; x<width; x++ )
			for ( int y=0; y<height; y++ )
				if ( Math.sqrt( (x-x_center)*(x-x_center) + (y-y_center)*(y-y_center) ) < radius )
					creatures.addAll(space[x][y].getCreatureSet());
		
		return creatures;
					
	}
	
	public void moveCreature(Creature creature, int x, int y) {
		space[creature.x][creature.y].removeCreature(creature);
		space[x][y].addCreature(creature);
		creature.setPosition(x, y);
	}
	
	public Set<Creature> createAllCreaturesSet() {
		Set<Creature> creatures = new LinkedHashSet<Creature>();
		
		for ( int x=0; x<width; x++ )
			for ( int y=0; y<height; y++ )
				creatures.addAll(space[x][y].getCreatureSet());
		
		return creatures;
					
	}
	
	public Set<Food> getAllFood() {
		Set<Food> food = new LinkedHashSet<Food>();
		
		for ( int x=0; x<width; x++ )
			for ( int y=0; y<height; y++ )
				food.addAll(space[x][y].getFoodSet());
		
		return food;
	}

	public Set<Food> createFoodCircle(int x_center, int y_center, double radius) {
		Set<Food> food = new LinkedHashSet<Food>();
		
		for ( int x=0; x<width; x++ )
			for ( int y=0; y<height; y++ )
				if ( Math.sqrt( (x-x_center)*(x-x_center) + (y-y_center)*(y-y_center) ) < radius )
					food.addAll(space[x][y].getFoodSet());
		
		return food;
	}

}
