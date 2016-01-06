package evo;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Ecosystem eden = new Ecosystem(200,200);
		
		eden.addCreatureRandom(new Bully("Adam"));
		eden.addCreatureRandom(new Speedy("Eve"));
		eden.addCreatureRandom(new Seeker("John"));
		
		for ( int i = 0; i < 50; i++ ) {
			eden.createFoodRandom(10);
		}
		
		for ( int i = 0; i < 365; i++ ) {
			eden.runDay();
			eden.createFoodRandom(10);
			eden.plot();
			
			sleep(40);
			
			if ( eden.isDead() )
				break;
		}

		eden.printStats();
	}
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.printf("ERRO: Na função sleep()\n");
			e.printStackTrace();
		}
	}
}
