package evo;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

public class Plotter {

	Ecosystem ecosystem;
	DefaultXYDataset dataset = new DefaultXYDataset();
	JFreeChart chart;
	ChartFrame frame;
	int height;
	int width;
	
	public Plotter(int width, int height) {
		 
		this.width = width;
		this.height = height;
		
		chart = ChartFactory.createScatterPlot(
		null, // title
		null, // x_axis title
		null, // y_axis title
		dataset,
		PlotOrientation.VERTICAL, // orientation?
		true, // legend?
		false, // tooltips?
		false // URLs?
		);
		
		// Set axis range
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setRange(0, width);
		plot.getRangeAxis().setRange(0, height);
		
		frame = new ChartFrame("eVo", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setEcosystem(Ecosystem eco) { this.ecosystem = eco; }
	public void addDataSet(String label, double [][] data) { dataset.addSeries(label, data); }
	
	public void plot() {
		plotNames();
//		plotCreatures();
		plotFood();
	}
	
	public void plotNames() {
		LinkedHashSet<String> usedNames = new LinkedHashSet<String>();
		
		for ( Iterator<Creature> it = ecosystem.ecoSpace.createAllCreaturesSet().iterator(); it.hasNext(); ) {
			Creature creature = it.next();
			String currentName = creature.getName();
			
			if ( usedNames.contains(currentName) )
				continue;
			
			usedNames.add(currentName);
			plotName(currentName);
		}
	}
	
	protected void plotName(String name) {
		
		LinkedHashSet<Creature> sameNameCreatures = new LinkedHashSet<Creature>();
		
		for ( Iterator<Creature> it = ecosystem.ecoSpace.createAllCreaturesSet().iterator(); it.hasNext(); ) {
			Creature creature = it.next();
			if ( creature.getName().equals(name) )
				sameNameCreatures.add(creature);
		}
		
		double[][] creaturePositions = new double[2][sameNameCreatures.size()];
		int i=0;
		
		for ( Iterator<Creature> it = sameNameCreatures.iterator(); it.hasNext(); i++ ) {
			Creature creature = it.next();
			creaturePositions[0][i] = creature.getXPosition();
			creaturePositions[1][i] = creature.getYPosition();
		}
		
		
		addDataSet( name, creaturePositions );
	}

	public void plotFood() {
		addDataSet( "Food", ecosystem.getFoodPositions() );
	}

	public void plotCreatures() {
		addDataSet( "Creatures", ecosystem.getCreaturePositions() );
	}
	
}

