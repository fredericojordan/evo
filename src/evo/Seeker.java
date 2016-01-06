package evo;

public class Seeker extends Creature {

	public Seeker(String name) {
		super(name);
		sightRange *= BONUSMULTIPLIER;
	}

}
