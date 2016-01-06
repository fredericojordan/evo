package evo;

import java.util.Iterator;

public class Ecosystem {

	private Plotter plotter;
	EcoSpace ecoSpace;
	
	private int dayCount = 0;
	
	public Ecosystem(int width, int height) {
		plotter = new Plotter(width, height);
		plotter.setEcosystem(this);
		ecoSpace = new EcoSpace(width, height);
	}
	
	public void plot() { plotter.plot(); }

	public void addCreatureMiddle(Creature creature) {
		int x = ecoSpace.getMidX();
		int y = ecoSpace.getMidY();
		ecoSpace.addCreature(creature, x, y);
		creature.setEcosystem(this);
		creature.setPosition(x,y);
	}
	
	public void addCreatureRandom(Creature creature) {
		int x = ecoSpace.getRandomX();
		int y = ecoSpace.getRandomY();
		ecoSpace.addCreature(creature, x, y);
		creature.setEcosystem(this);
		creature.setPosition(x,y);
	}

	public void addCreature(Creature creature, int x, int y) {
		ecoSpace.addCreature(creature, x, y);
		creature.setEcosystem(this);
		creature.setPosition(x,y);
	}

	public void createFoodRandom(double energy) {
		Food new_food = new Food(energy);
		int x = ecoSpace.getRandomX();
		int y = ecoSpace.getRandomY();
		ecoSpace.addFood(new_food, x, y);
		new_food.setPosition(x, y);
	}
	
	public void runDay() {
		this.dayCount++;
		printDay();
		runCreaturesDay();
	}
	
	public void printDay() {
		System.out.printf(".");
		if ( dayCount % 10 == 0 )
			System.out.printf("\n");
	}
	
	protected void runCreaturesDay() {
		for ( Iterator<Creature> it = ecoSpace.createAllCreaturesSet().iterator(); it.hasNext(); ) {
			Creature creature = it.next();
			creature.runDay();
			if ( !creature.alive() )
				ecoSpace.removeCreature(creature);
		}
	}
	
	public void printStats() {
		System.out.printf("\n");
		printDaysElapsed();
		printRemainingFood();
		printCreatureStats();
	}
	
	public void printCreatureStats() {
		System.out.printf("%d creatures remaining.\n", ecoSpace.createAllCreaturesSet().size() );
		
		for ( Iterator<Creature> it = ecoSpace.createAllCreaturesSet().iterator(); it.hasNext(); ) {
			Creature creature = it.next();
//			creature.printStats();
		}
		
	}
	
	public void printDaysElapsed() { System.out.printf("%d days elapsed.\n", dayCount); }
	
	public void printRemainingFood() { System.out.printf("%d food remaining.\n", ecoSpace.getAllFood().size() );	}
	
	public boolean isDead() { return ecoSpace.createAllCreaturesSet().isEmpty(); }
	
	public double[][] getCreaturePositions() {
		double[][] creaturePositions;
		int numberOfCreatures = ecoSpace.createAllCreaturesSet().size();
		int i=0;
		
		creaturePositions = new double[2][numberOfCreatures];
		
		for ( Iterator<Creature> it = ecoSpace.createAllCreaturesSet().iterator(); it.hasNext(); i++ ) {
			Creature creature = it.next();
			creaturePositions[0][i] = creature.getXPosition();
			creaturePositions[1][i] = creature.getYPosition();
		}
		
		return creaturePositions;
	}
	
	public double[][] getFoodPositions() {
		double[][] foodPositions;
		int numberOfFood = ecoSpace.getAllFood().size();
		int i=0;
		
		foodPositions = new double[2][numberOfFood];
		
		for ( Iterator<Food> it = ecoSpace.getAllFood().iterator(); it.hasNext(); i++ ) {
			Food food = it.next();
			foodPositions[0][i] = food.getXPosition();
			foodPositions[1][i] = food.getYPosition();
		}
		
		return foodPositions;
	}
	
}
