package evo;


/****************************
 *			Creature		*
 *							*
 * Desirable traits:		*
 * - longevity				*
 * - fecundity				*
 * - copying-fidelity		*
 * 							*
 ****************************/

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Creature {
	
	protected String name;
	
	protected boolean living = true;
	
	enum Intention { FLEEING, FEEDING, FIGHTING, MATING, NONE }
	Intention intention = Intention.NONE;
	
	protected static double BONUSMULTIPLIER = 3;
	
	protected Ecosystem ecosystem;
	public int x;
	public int y;
	public int intended_x = -1;
	public int intended_y = -1;
	protected double direction = 2*Math.PI*Math.random();
	
	public double speed = 1 + 2*Math.random();
	public double sightRange = 5 + 10*Math.random();
	protected double power = 1 + 5*Math.random();
	protected double storedEnergy = 15 + 10*Math.random();
	protected double dailyEnergyConsumption = 0.1 + 0.5*Math.random();
	
	protected double energyToDuplicate = 20;
	protected double corpseEnergy = 5;

	protected int foodEaten = 0;
	protected int wonFights = 0;
	protected int duplicatesMade = 0;
	
	public Creature(String name) {
		this.name = name;
//		System.out.printf("Creature %s created!\n", name);
//		printStats();
	}
	
	public void setEcosystem(Ecosystem ecosystem) { this.ecosystem = ecosystem; }
	public void setPosition(int x, int y) { this.x = x; this.y = y; }
	public void setDestination(int x, int y) { this.intended_x = x; this.intended_y = y; }
	public int getXPosition() { return x; }
	public int getYPosition() { return y; }
	public double getPower() { return power; }
	public void logWonFight() { wonFights++; }
	public int wonFights() { return wonFights; }
	public boolean alive() { return living; }
	public String getName() { return name; }
	protected void setSpeed(double speed) { this.speed = speed; }
	protected void setSightRange(double sightRange) { this.sightRange = sightRange; }
	protected void setPower(double power) { this.power = power; }
	protected void setStoredEnergy(double storedEnergy) { this.storedEnergy = storedEnergy; }
	protected void setDailyEnergyConsumption(double dailyEnergyConsumption) { this.dailyEnergyConsumption = dailyEnergyConsumption; }
	
	public void addPower(double add) { this.power += add; }
	public void addEnergyConsumption(double add) { this.dailyEnergyConsumption += add; }
	
	public void runDay() {
		if (living)
		{
			fightLocals();
			if ( !living ) return;
			
			eatLocals();
			tryToDuplicate();
			walk();
			consumeDailyEnergy();
		}
	}
	
	protected void eatLocals() {
		Set<Food> foodHere = ecosystem.ecoSpace.space[x][y].getFoodSet();
		
		if ( !foodHere.isEmpty() )
			eatAll(foodHere);
	}
	
	protected void fightLocals() {
		Set<Creature> creaturesHere = ecosystem.ecoSpace.space[x][y].getCreatureSet();
		
		creaturesHere.remove(this);
		
		if ( !creaturesHere.isEmpty() )
			fightDifferentName(creaturesHere);
	}

	protected void tryToDuplicate() {
		if ( storedEnergy >= 2*energyToDuplicate )
		{
			consumeEnergy(energyToDuplicate);
			ecosystem.addCreature(this.duplicate(), x, y);
		}
	}
	
	protected Creature duplicate() {
		duplicatesMade++;
		
		Creature dupe = new Creature(name);
		dupe.setSpeed(speed);
		dupe.setSightRange(sightRange);
		dupe.setPower(power);
		dupe.setStoredEnergy(energyToDuplicate);
		dupe.setDailyEnergyConsumption(dailyEnergyConsumption);
		return dupe;
	}
	
	protected void consumeDailyEnergy() { consumeEnergy(dailyEnergyConsumption); }

	protected void eatAll(Set<Food> foodSet) {
		if ( !foodSet.isEmpty() )
			for (Iterator<Food> it = foodSet.iterator(); it.hasNext(); ) {
				Food food = it.next();
				eat(food);
				it.remove();
			}
	}
	
	protected void eatAllFoodInRadius(double radius) {
		Set<Food> seenFood = lookForFood(radius);
		
		if ( !seenFood.isEmpty() )
			eatAll(seenFood);
	}
	
	public void consumeEnergy(double energy) {
		storedEnergy -= energy;

		if ( storedEnergy <= 0 )
			die();
	}
	
	protected void eat(Food food) {
		storedEnergy += food.getEnergy();
		foodEaten++;
	}
	
	protected void walk() {
//		teleportRandom();
//		walkOneStepStraight();
//		walkSmooth();
		decideAndWalk();
	}
	
	protected void decideAndWalk() {
		decideDestination();
		walkToDestination();
	}
	
	protected void walkToDestination() {
		
		int delta_x = intended_x-x;
		int delta_y = intended_y-y;
		
		double distance = Math.sqrt(delta_x*delta_x + delta_y*delta_y);

		if ( distance > speed ) {
			double distanceFactor = speed/distance;

			int new_x = this.x + (int) Math.round(distanceFactor*delta_x);
			int new_y = this.y + (int) Math.round(distanceFactor*delta_y);

			new_x = limitX(new_x);
			new_y = limitY(new_y);
			ecosystem.ecoSpace.moveCreature(this, new_x, new_y);
		}
		else
			ecosystem.ecoSpace.moveCreature(this, intended_x, intended_y);

	}
	
	protected boolean onDestination() { return ( x == intended_x && y == intended_y ); }
	protected boolean hasDestination() { return ( intended_x >= 0 && intended_y >= 0 ); }
	
	protected void decideDestination() {
		Set<Food> seenFood = lookForFood(sightRange);
		Set<Creature> seenCreatures = lookForCreatures(sightRange);
		
		if ( !hasDestination() || onDestination() ) {
			intended_x = -1;
			intended_y = -1;
			intention = Intention.NONE;
		}
		
		if ( !seenCreatures.isEmpty() )
			decideDestinationBasedOnCreatures(seenCreatures);
		
		if ( !seenFood.isEmpty() && checkValidIntention(Intention.FEEDING) )
			defineDestinationBestFood(seenFood);
		
		if ( intention == Intention.NONE )
			decideRandomDestination();

	}
	
	protected void decideDestinationBasedOnCreatures(Set<Creature> creatures) {
		removeSameName(creatures);
		
		Creature weakest = this;
		Creature strongest = this;
		
		for (Iterator<Creature> it = creatures.iterator(); it.hasNext(); ) {
			Creature creature = it.next();
			
			if ( creature.getPower() > strongest.getPower() )
				strongest = creature;
			if ( creature.getPower() < weakest.getPower() )
				weakest = creature;
		}
		
		if ( strongest != this && strongest.getPower() > 2*power && checkValidIntention(Intention.FLEEING) )
			runAwayFrom( strongest.getXPosition(), strongest.getYPosition() );
		else if ( weakest != this && weakest.getPower()*2.5 < power && checkValidIntention(Intention.FIGHTING) ) {
			intention = Intention.FIGHTING; 
			setDestination(weakest.getXPosition(), weakest.getYPosition());
		}
	}
	
	protected void runAwayFrom( int x_away, int y_away ) {
		int new_x, new_y;
		
		new_x = limitX(2*x - x_away);
		new_y = limitY(2*y - y_away);
		
		intention = Intention.FLEEING;
		setDestination(new_x, new_y);
	}
	
	protected boolean checkValidIntention(Intention newIntention) {
		
		boolean decision = false;
		
		switch(intention) {

		case FEEDING:
			if (newIntention == Intention.FLEEING || newIntention == Intention.MATING )
				decision = true;
			break;

		case FLEEING:
			if (newIntention == Intention.FLEEING)
				decision = true;
			break;

		case FIGHTING:
			if (newIntention == Intention.FLEEING || newIntention == Intention.FEEDING || newIntention == Intention.MATING )
				decision = true;
			break;

		case MATING:
			if (newIntention == Intention.FLEEING )
				decision = true;
			break;
			
		case NONE:
			decision = true;
			break;
			
		default:
			break;
		}
		
		return decision;
	}
	
	protected void defineDestinationBestFood(Set<Food> seenFood) {
		double maxEnergy = 0;
		Food bestFood = null;
		
		if ( !seenFood.isEmpty() )
			for (Iterator<Food> it = seenFood.iterator(); it.hasNext(); ) {
				Food food = it.next();
				if ( food.getEnergy() > maxEnergy ||
						(food.getEnergy() == maxEnergy && Misc.calculateDistance(x, y, food.x, food.y) < Misc.calculateDistance(x, y, bestFood.x, bestFood.y)) )
				{
					bestFood = food;
					maxEnergy = food.getEnergy();
				}
			}
		
		if ( bestFood != null ) {
			intention = Intention.FEEDING;
			setDestination(bestFood.getXPosition(), bestFood.getYPosition());
		}
	}
	
	protected void decideRandomDestination() {
		Random rng = new Random();
		double newDestinationDirection = Math.floor(2*Math.PI*Math.random());
		double newDestinationDistance = 1/rng.nextGaussian();
		
		int delta_x = (int) Math.round(newDestinationDistance*Math.cos(newDestinationDirection));
		int delta_y = (int) Math.round(newDestinationDistance*Math.sin(newDestinationDirection));
		
		
		int new_x = this.x + delta_x;
		int new_y = this.y + delta_y;
		
		intention = Intention.NONE;
		setDestination(limitX(new_x), limitY(new_y));
	}
	
	protected void teleportRandom() {
		
		Random rng = new Random();
		double direction = Math.floor(2*Math.PI*Math.random());
		double step_size = 1/rng.nextGaussian();
		
		int delta_x = (int) Math.round(step_size*Math.cos(direction));
		int delta_y = (int) Math.round(step_size*Math.sin(direction));
		
		
		int new_x = this.x + delta_x;
		int new_y = this.y + delta_y;

		new_x = limitX(new_x);
		new_y = limitY(new_y);
		
		ecosystem.ecoSpace.moveCreature(this, new_x, new_y);
	}
	
	protected void walkOneStepStraight() {
		double direction = Math.floor(4*Math.random());
		int new_x = this.x;
		int new_y = this.y;

		if ( direction == 0 )
			new_x = (int) Math.round(x+speed);
		else if ( direction == 1 )
			new_y = (int) Math.round(y+speed);
		else if ( direction == 2 )
			new_x = (int) Math.round(x-speed);
		else if ( direction == 3 )
			new_y = (int) Math.round(y-speed);

		new_x = limitX(new_x);
		new_y = limitY(new_y);
		
		ecosystem.ecoSpace.moveCreature(this, new_x, new_y);
	}
	
	protected void walkSmooth() {
		
		Random rng = new Random();
		double arc_length = Math.PI/8.0; // 22.5 degrees
		double delta_direction = arc_length*(rng.nextGaussian());
		
		direction += delta_direction;
		
		if ( direction < 0 )
			direction += 2*Math.PI;
		if ( direction > 2*Math.PI )
			direction -= 2*Math.PI;
		
		int delta_x = (int) Math.round(speed*Math.cos(direction));
		int delta_y = (int) Math.round(speed*Math.sin(direction));
		
		
		int new_x = this.x + delta_x;
		int new_y = this.y + delta_y;

		new_x = limitX(new_x);
		new_y = limitY(new_y);
		
		ecosystem.ecoSpace.moveCreature(this, new_x, new_y);
	}
	
	protected int limitX(int x) { return Math.max(Math.min(x, ecosystem.ecoSpace.width-1), 0); }
	protected int limitY(int y) { return Math.max(Math.min(y, ecosystem.ecoSpace.height-1), 0); }
	
	protected Set<Creature> lookForCreatures(double radius) {
		Set<Creature> seen = ecosystem.ecoSpace.createCreatureCircle(x,y,radius);
		seen.remove(this);
		return seen;
	}
	
	protected Set<Food> lookForFood(double radius) {
		return ecosystem.ecoSpace.createFoodCircle(x,y,radius);
	}
	
	protected void fightDifferentName(Set<Creature> creatures) {
		removeSameName(creatures);
		fightAll(creatures);
	}
	
	protected void removeSameName(Set<Creature> creatures) {
		for (Iterator<Creature> it = creatures.iterator(); it.hasNext(); ) {
			Creature creature = it.next();
			
			if ( creature.getName().equals(name) )
				it.remove();
		}
	}
	
	protected void fightAll(Set<Creature> creatures) {
		for (Iterator<Creature> it = creatures.iterator(); it.hasNext(); ) {
			Creature creature = it.next();

			Fight fight = new Fight(this, creature);
			fight.simulate();

			if ( !living )
				break;
		}
	}

	public void die() {
		if ( living ) {
//			System.out.printf("%s has died!\n", name);
			living = false;
			leaveCorpse();
		}
	}
	
	protected void leaveCorpse() {
		ecosystem.ecoSpace.addFood(new Food(corpseEnergy, x, y), x, y);
	}
	
	public void printStats() {
		System.out.printf("----------------------\n");
		System.out.printf("Name = %s\n", name);
		System.out.printf("Estimated remaining life = %.1f\n", storedEnergy/dailyEnergyConsumption );
		System.out.printf("Power = %.2f\n", power);
		System.out.printf("Stored Energy = %.2f\n", storedEnergy);
		System.out.printf("Energy Consumption = %.2f\n", dailyEnergyConsumption);
		System.out.printf("Sight range = %.2f\n", sightRange);
		System.out.printf("Speed = %.2f\n", speed);
		System.out.printf("Won Fights = %d\n", wonFights);
		System.out.printf("Food Eaten = %d\n", foodEaten);
		System.out.printf("Duplicates Made = %d\n", duplicatesMade);
		System.out.printf("----------------------\n");
	}
}
