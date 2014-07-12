package fourLayerGrid;

import java.awt.*;
import java.awt.color.ColorSpace;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class AlifeGrid {

	// instantiate the simulation
	// n*m grid of biome objects
	// 4 layers in each biome

	// array of colors will contain the entire simulation state
	public int n, m;
	public int turnLimit;
	Color[][] ecoGridState;
	int burnRate;
	int evapRate;
	int lightningChance;
	int globalBurnThreshold;
	int rainTriggerValue;
	int rainFireExtinguishRate;
	int rainWaterValue;
	int rainWaterContribution;
	int neighborRainWaterGain;
	int waterForVegetationLocal;
	int evapWaterAddedToCloudLocal;
	int evapWaterAddedToCloudNeighbor;
	int neighborVegetationLevelForFireSpread;
	int neighborWaterForVegetation;
	// int fireIntensity;
	Boolean isRaining = false;

	public AlifeGrid(int gridLengthN, int gridLengthM, int turnLimit,
			int initialWaterLevel, int evaporationRate,
			double lightningProbability, int burnRateLocal,
			int burnRateNeighborContribution, int rainFireSuppressionRate,
			double rainThreshold, double localCloudWaterAbsorption,
			double neighborCloudWaterAbsorption, int rainIntensity,
			int neighborWaterContribution, int localWaterContribution,
			int burnThresholdLightning, int fireSpreadThreshold) {
		JFrame frame = new JFrame(); // creates frame
		JButton[][] grid; // names the grid of buttons
		this.burnRate = burnRateLocal;
		this.evapRate = evaporationRate;
		this.lightningChance = (int) (100 * lightningProbability);
		this.rainTriggerValue = (int) (255 * rainThreshold);
		this.rainFireExtinguishRate = rainFireSuppressionRate;
		this.rainWaterValue = rainIntensity;
		//Rain for current sector recoups 20% of rainwater
		this.rainWaterContribution = (int) (0.2 * rainWaterValue);
		this.waterForVegetationLocal = (int) (localWaterContribution);
		this.globalBurnThreshold = burnThresholdLightning;
		this.evapWaterAddedToCloudLocal = (int) (evaporationRate * localCloudWaterAbsorption);
		this.evapWaterAddedToCloudNeighbor = (int) (evaporationRate * neighborCloudWaterAbsorption);
		this.neighborVegetationLevelForFireSpread = fireSpreadThreshold;
		this.neighborWaterForVegetation = neighborWaterContribution;
		//each of up to 8 neighbors gets 10% of the rainwater from a sector with rain
		this.neighborRainWaterGain = (int)(rainIntensity*0.1);
		System.out.println("n axis: " + gridLengthN);
		System.out.println("m axis: " + gridLengthM);
		this.ecoGridState = new Color[gridLengthN][gridLengthM];
		// Shape sq = new Rectangle(10,10,10,10);
		// create the grid of n*m biomes
		// for each of the n*m grid locations initialize the levels from
		// parameters
		for (int n = 0; n < gridLengthN; n++) {

			for (int m = 0; m < gridLengthM; m++) {
				// for each n,m sector in the grid
				ecoGridState[n][m] = new Color(0, initialWaterLevel,
						initialWaterLevel, initialWaterLevel);
				// create four layer biome:
				// create water
				// create clouds
				// create vegetation
				// create fire

			}

		}

		// create the JFrame to display color values
		//frame.setLayout(new GridLayout(gridLengthN, gridLengthM)); // set layout
		//grid = new JButton[gridLengthN][gridLengthM]; // allocate the size of
		// grid
		// the inner loop creates the jFrame and applies the actual colors

		for (int turnNumber = 0; turnNumber < turnLimit; turnNumber++) {

			// if(turnNumber>0){
			updateWorld(gridLengthN, gridLengthM);
			// }
			System.out.println("Step #" + turnNumber);
			for (int n = 0; n < gridLengthN; n++) {

				System.out.print("row #" + n + ": ");

				for (int m = 0; m < gridLengthM; m++) {

					System.out.print("[ " + " fire: "
							+ ecoGridState[n][m].getRed() + " vegetation: "
							+ ecoGridState[n][m].getGreen() + " water: "
							+ ecoGridState[n][m].getBlue() + " clouds: "
							+ ecoGridState[n][m].getAlpha() + " ]");

					if(ecoGridState[n][m].getAlpha() >= rainTriggerValue)
					{
						System.out.print(" rain in sector " + n + "," + m + "!");
					}

					if(ecoGridState[n][m].getRed() >= globalBurnThreshold)
					{
						System.out.print("fire in sector " + n + "," + m + "!");	
					}

				}
				// currentColor = new
				// Color(ecoGridState[n][m].getColorSpace(), m);
				//if (turnNumber == turnLimit - 1) {
				//	grid[n][m] = new JButton("[" + n + "," + m + "]"); // creates
					// new
					// button
				//	grid[n][m].setBackground(ecoGridState[n][m]);
				//	grid[n][m].setForeground(ecoGridState[n][m]);
				//	grid[n][m].setOpaque(true);
					// ecoGridState[n][m].getRGBComponents()

					//frame.add(grid[n][m]); // adds button to grid
				//}
				System.out.println("\n");

			}

			// frame.add(grid[n][m]);

		}
	}
	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.pack(); // sets appropriate size for frame
	//	frame.setVisible(true); // makes frame visible

	//}

//}

public void updateWorld(int n, int m) {
	Color newWorldState[][] = new Color[n][m];
	// for the first section, the previous world state provides initial
	// values that are then written to the newWorldState
	Color previousWorldState[][] = ecoGridState.clone();
	// from top-left to bottom-right of the grid:
	// part 1: every sector undergoes internal changes
	// part 2: after that, the neighbors affect each other.
	// Requires two passthroughs per turn.

	// part 1 of new turn: update internal values based on the parameters

	int localColorR;
	int localColorG;
	int localColorB;
	int localColorA;
	//Color neighborColor;
	this.isRaining=false;
	// System.out.println("TEST");

	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			this.isRaining=false;
			localColorR = previousWorldState[i][j].getRed();
			localColorG = previousWorldState[i][j].getGreen();
			localColorB = previousWorldState[i][j].getBlue();
			localColorA = previousWorldState[i][j].getAlpha();

			if (localColorA >= rainTriggerValue) {
				this.isRaining = true;
			}

			// R value greater than 0 means there is an active fire
			if (localColorR > 0) {
				// rain reduces the fire toward 0
				if (localColorA>=rainTriggerValue) {
					// rain slows the burn rate and helps extinguish the
					// fire
					localColorG -= (burnRate - rainFireExtinguishRate);
					localColorR -= burnRate;
				} else {
					localColorG -= burnRate;
					localColorR -= burnRate;
				}
			}

			// if local vegetation (G) is 0 after a burn is applied, stop
			// the fire
			// if the fire has burned out the environment, extinguish the
			// fire
			if (localColorG <= 0) {
				localColorG = 0;
				localColorR = 0;
			}

			if (localColorR < 0) {
				localColorR = 0;
			}

			// vegetation section
			// no growth during a fire
			if (localColorR < 1) {
				localColorG += waterForVegetationLocal;
			}
			
			if(localColorR > 0)
			{
				localColorG-=burnRate;
			}

			if (localColorG > 255) {
				localColorG = 255;
			}

			if(localColorB<localColorG)
			{
				localColorG-=10;	
			}

			// water section

			if (localColorB > 0 && localColorB < evapRate) {
				localColorA += (evapRate - localColorB);
				localColorB = 0;
			} else if (localColorB > 0 && localColorB > evapRate) {
				localColorA += evapRate;
				localColorB -= evapRate;
			}

			// cloud section
			
			if(localColorA > 255)
			{
			localColorA=255;	
			}
			
			if (localColorA>=rainTriggerValue) {
				//evaporation rate is negated for rainfall
				localColorB += rainWaterContribution + evapRate;
				localColorA -= rainWaterValue;
			}

			// if the burn threshold is met, trigger lightning possibly?

			if (localColorG <= globalBurnThreshold) {
				Random rand = new Random();
				int nextInt = rand.nextInt(100);
				if (nextInt < lightningChance) {
					localColorR = localColorG;
					System.out.println("Fire started in grid sector " + i
							+ "," + j);
				}

			}

			// end internal changes
			// write to the newGridState
			
			if(localColorG>255)
			{
				
				localColorG=255;
			}
			
			if(localColorG<0)
			{
				
				localColorG=0;
			}
			if(localColorA>255)
			{
				localColorA=255;
			}
			
			if(localColorB>255)
			{
				localColorB=255;
			}
			
			if(localColorB<0)
			{
				localColorB=0;
			}
			
			if(localColorA<0)
			{
				localColorA=0;
			}
			
			if(localColorR>255)
			{
				localColorR=255;
			}
			
			if(localColorR<0)
			{
				localColorR=0;
			}
			
			newWorldState[i][j] = new Color(localColorR, localColorG,
					localColorB, localColorA);

			// System.out.println("intermediate state: ");
			// System.out.println("[ " + "water: "
			// + newWorldState[i][j].getBlue() + " vegetation: "
			// + newWorldState[i][j].getGreen() + " clouds: "
			// + newWorldState[i][j].getAlpha() + " fire: "
			// + newWorldState[i][j].getRed() + " ]");

		}

	}
	// begin second section with references to neighbors

	// referencing all of the neighbors from their previous state, add their
	// contributions to the current state
	// in 2x2 grid all neighbors reference each other
	// generally speaking, one square references each neighbor that is valid

	//
	int neighborTotalWaterContribution;
	int currentNeighborColorR;
	int currentNeighborColorG;
	int currentNeighborColorB;
	int currentNeighborColorA;
	int newLocalColorR;
	int newLocalColorG;
	int newLocalColorB;
	int newLocalColorA;


	for(int i=0;i<n;i++)
	{

		for(int j=0;j<n;j++)
		{
			neighborTotalWaterContribution=0;
			newLocalColorR = newWorldState[i][j].getRed();
			newLocalColorG = newWorldState[i][j].getGreen();
			newLocalColorB = newWorldState[i][j].getBlue();
			newLocalColorA = newWorldState[i][j].getAlpha();

			//check for up to 8 neighbors to the current sector and contribute their interactions to each other from previous state
			//below format is clockwise from upper-left diagonal

			//if the sector [i-1][j-1] is a valid sector
			//diagonal upper-left neighbor
			if((i-1)>=0 && (j-1)>=0)
			{
				currentNeighborColorR = previousWorldState[i-1][j-1].getRed();
				currentNeighborColorG = previousWorldState[i-1][j-1].getGreen();
				currentNeighborColorB = previousWorldState[i-1][j-1].getBlue();
				currentNeighborColorA = previousWorldState[i-1][j-1].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i-1) + "," + (j-1) + " to sector " + i + "," + j);
				}

				//if it is raining in the neighbor, provide some water to this sector
				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				//add the neighbor's evaporated water level to the local sector

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector above the current exists
			if((i-1)>=0 && j>=0)
			{
				currentNeighborColorR = previousWorldState[i-1][j].getRed();
				currentNeighborColorG = previousWorldState[i-1][j].getGreen();
				currentNeighborColorB = previousWorldState[i-1][j].getBlue();
				currentNeighborColorA = previousWorldState[i-1][j].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i-1) + "," + j + " to sector " + i + "," + j);
				}

				if(currentNeighborColorA<rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector diagonal to upper-right of current exists
			if((i-1)>=0 && (j+1)<m)
			{
				currentNeighborColorR = previousWorldState[i-1][j+1].getRed();
				currentNeighborColorG = previousWorldState[i-1][j+1].getGreen();
				currentNeighborColorB = previousWorldState[i-1][j+1].getBlue();
				currentNeighborColorA = previousWorldState[i-1][j+1].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i-1) + "," + (j+1) + " to sector " + i + "," + j);
				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector to right of current exists
			if(i>=0 && (j+1)<m)
			{
				currentNeighborColorR = previousWorldState[i][j+1].getRed();
				currentNeighborColorG = previousWorldState[i][j+1].getGreen();
				currentNeighborColorB = previousWorldState[i][j+1].getBlue();
				currentNeighborColorA = previousWorldState[i][j+1].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + i + "," + (j+1) + " to sector " + i + "," + j);
				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector diagonal lower-right neighbor exists
			if((i+1)<n && (j+1)<m)
			{
				currentNeighborColorR = previousWorldState[i+1][j+1].getRed();
				currentNeighborColorG = previousWorldState[i+1][j+1].getGreen();
				currentNeighborColorB = previousWorldState[i+1][j+1].getBlue();
				currentNeighborColorA = previousWorldState[i+1][j+1].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i+1) + "," + (j+1) + " to sector " + i + "," + j);

				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}
				
				if(currentNeighborColorA>rainTriggerValue)
				{
					newLocalColorB+=neighborRainWaterGain;
				}

			}

			//if sector below the current exists
			if((i+1)<n && j<m)
			{
				currentNeighborColorR = previousWorldState[i+1][j].getRed();
				currentNeighborColorG = previousWorldState[i+1][j].getGreen();
				currentNeighborColorB = previousWorldState[i+1][j].getBlue();
				currentNeighborColorA = previousWorldState[i+1][j].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i+1) + "," + j + " to sector " + i + "," + j);

				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector diagonal lower-right neighbor exists
			if((i-1)>=0 && (j-1)>=0)
			{
				currentNeighborColorR = previousWorldState[i-1][j-1].getRed();
				currentNeighborColorG = previousWorldState[i-1][j-1].getGreen();
				currentNeighborColorB = previousWorldState[i-1][j-1].getBlue();
				currentNeighborColorA = previousWorldState[i-1][j-1].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i-1) + "," + (j-1) + " to sector " + i + "," + j);

				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}

			}

			//if sector to left of the neighbor exists
			if((i-1)>=0 && j>=0)
			{
				currentNeighborColorR = previousWorldState[i-1][j].getRed();
				currentNeighborColorG = previousWorldState[i-1][j].getGreen();
				currentNeighborColorB = previousWorldState[i-1][j].getBlue();
				currentNeighborColorA = previousWorldState[i-1][j].getAlpha();

				//if the neighbor cell has a fire and the current one does not, check if the fire should spread if it's not raining
				if(currentNeighborColorR>0 && newLocalColorR<1 && newLocalColorG<=neighborVegetationLevelForFireSpread && newLocalColorA<=rainTriggerValue)
				{
					newLocalColorR=255;
					System.out.println("fire spreads from sector " + (i-1) + "," + (j-1) + " to sector " + i + "," + j);

				}

				if(currentNeighborColorA<=rainTriggerValue && currentNeighborColorB>0 && newLocalColorR<1)
				{
					newLocalColorG+=neighborWaterForVegetation;
				}

				if(currentNeighborColorA>0)
				{
					newLocalColorA+=evapWaterAddedToCloudNeighbor;
				}
				
				if(newLocalColorA>255)
				{
					newLocalColorA=255;
				}

			}
			
			if(newLocalColorG>255)
			{
				newLocalColorG=255;
			}
			
			if(newLocalColorB>255)
			{
				newLocalColorB=255;
			}
			
			if(newLocalColorR>0 && newLocalColorG<1)
			{
				newLocalColorR=0;
			}
			//end of updates to local sector from the changes in turn
			ecoGridState[i][j] = new Color(newLocalColorR,newLocalColorG,newLocalColorB,newLocalColorA);
		}
	}

}

}


// no controller class
// load parameters from file
// in each biome instantiate the water, fire, vegetation, and clouds

