package evo;

import java.util.Random;

public class Fight {
	
	private Creature creature1;
	private Creature creature2;
	private Creature winner;
	private Creature loser;

	public Fight(Creature creature1, Creature creature2) {
		this.creature1 = creature1;
		this.creature2 = creature2;
		
//		System.out.printf("\n(%s vs %s @ %d:%d)\n", creature1.getName(), creature2.getName(), creature1.getXPosition(), creature1.getYPosition());
		
	}
	
	protected void decideWinner() {
		Random rng = new Random();
		double randSeed1 = Math.abs(rng.nextGaussian());
		double randSeed2 = Math.abs(rng.nextGaussian());
		
		double result = creature1.getPower()*randSeed1 - creature2.getPower()*randSeed2;
		
		if ( result > 0 )
		{
			winner = creature1;
			loser = creature2;
		}
		else
		{
			winner = creature2;
			loser = creature1;
		}
	}
	
	public Creature simulate() {
		decideWinner();
		winner.logWonFight();
		winner.addPower(1);
		winner.addEnergyConsumption(0.1);
		winner.consumeEnergy(0.5);
		loser.die();
		
		return winner;
	}

}