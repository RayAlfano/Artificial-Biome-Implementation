package fourLayerGrid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EcoAlifeSim {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
			Properties prop = new Properties();
			
			//get properties file
			try
			{
				prop.load(new FileInputStream("../Alife/src/fourLayerGrid/localsettings.properties"));	
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
			//end properties file load
			
			//assign properties
			//grid size
			System.out.print("length of grid axis, parameter N: ");
			System.out.println(prop.getProperty("N_VALUE"));
			System.out.print("length of grid axis, parameter M: ");
			System.out.println(prop.getProperty("M_VALUE"));
			System.out.print("turn limit: ");
			System.out.println(prop.getProperty("TURN_LIMIT"));
			int gridLengthN = Integer.parseInt(prop.getProperty("N_VALUE"));
			int gridLengthM = Integer.parseInt(prop.getProperty("M_VALUE"));
			int turnLimit = Integer.parseInt(prop.getProperty("TURN_LIMIT"));
			
			//water properties
			
			System.out.print("water level initially: ");
			System.out.println(prop.getProperty("WATER_LEVEL"));
			System.out.print("water evaporation rate: ");
			System.out.println(prop.getProperty("EVAPORATION_RATE"));
			int initialWaterLevel = Integer.parseInt(prop.getProperty("WATER_LEVEL"));
			int evaporationRate = Integer.parseInt(prop.getProperty("EVAPORATION_RATE"));
			
			//fire (and ignition) properties
			
			System.out.print("lightning probability: ");
			System.out.println(prop.getProperty("LIGHTNING_PROBABILITY"));
			System.out.print("burn rate during a fire in current sector: ");
			System.out.println(prop.getProperty("BURN_RATE_LOCAL"));
			System.out.print("contribution to burn rate during a fire in neighboring sector: ");
			System.out.println(prop.getProperty("BURN_RATE_NEIGHBOR_CONTRIBUTION"));
			System.out.print("rain suppression effect during rain: ");
			System.out.println(prop.getProperty("RAIN_FIRE_SUPPRESSION"));
			double lightningProbability = Double.parseDouble(prop.getProperty("LIGHTNING_PROBABILITY"));
			int burnRateLocal = Integer.parseInt(prop.getProperty("BURN_RATE_LOCAL"));
			int burnRateNeighborContribution = Integer.parseInt(prop.getProperty("BURN_RATE_NEIGHBOR_CONTRIBUTION"));
			int rainFireSuppressionRate = Integer.parseInt(prop.getProperty("RAIN_FIRE_SUPPRESSION"));
			
			//cloud properties
			
			System.out.println("rain will start when clouds reach a constant water concentration");
			System.out.print("cloud rain threshold: ");
			System.out.println(prop.getProperty("CLOUD_RAIN_THRESHOLD"));
			System.out.print("percentage of evaporated water absorbed by local clouds: ");
			System.out.println(prop.getProperty("CLOUD_ABSORPTION_LOCAL"));
			System.out.print("percentage of evaporated water absorbed by neighboring clouds: ");
			System.out.println(prop.getProperty("CLOUD_ABSORPTION_NEIGHBOR"));
			System.out.print("rain intensity, the amount of water lost by clouds to environment: ");
			System.out.println(prop.getProperty("RAIN_INTENSITY"));
			double rainThreshold = Double.parseDouble(prop.getProperty("CLOUD_RAIN_THRESHOLD"));
			double localCloudWaterAbsorption = Double.parseDouble(prop.getProperty("CLOUD_ABSORPTION_LOCAL"));
			double neighborCloudWaterAbsorption = Double.parseDouble(prop.getProperty("CLOUD_ABSORPTION_NEIGHBOR"));
			int rainIntensity = Integer.parseInt(prop.getProperty("RAIN_INTENSITY"));
			System.out.print("actual cloud integer value for rainfall to begin: ");
			System.out.println((int)(rainThreshold*255));
			
			//vegetation properties
			
			System.out.print("contribution of neighboring sectors' water: ");
			System.out.println(prop.getProperty("NEIGHBOR_WATER_CONTRIBUTION"));
			System.out.print("local water contribution to vegetation: ");
			System.out.println(prop.getProperty("LOCAL_WATER_CONTRIBUTION"));
			System.out.print("vegetation threshold for local lightning strike to cause a fire: ");
			System.out.println(prop.getProperty("BURN_THRESHOLD_LIGHTNING"));
			System.out.print("vegetation threshold for fires in neighbors to spread into a sector ");
			System.out.println(prop.getProperty("FIRE_SPREAD_THRESHOLD"));
			int neighborWaterContribution = Integer.parseInt(prop.getProperty("NEIGHBOR_WATER_CONTRIBUTION"));
			int localWaterContribution = Integer.parseInt(prop.getProperty("LOCAL_WATER_CONTRIBUTION"));
			int burnThresholdLightning = Integer.parseInt(prop.getProperty("BURN_THRESHOLD_LIGHTNING"));
			int fireSpreadThreshold = Integer.parseInt(prop.getProperty("FIRE_SPREAD_THRESHOLD"));

			//end properties
			
			//Call the grid class to set up the environment
			
			AlifeGrid sim = new AlifeGrid(gridLengthN, gridLengthM, turnLimit, initialWaterLevel, evaporationRate, lightningProbability, burnRateLocal, burnRateNeighborContribution, rainFireSuppressionRate, rainThreshold, localCloudWaterAbsorption, neighborCloudWaterAbsorption, rainIntensity, neighborWaterContribution, localWaterContribution, burnThresholdLightning, fireSpreadThreshold);
		//end main method
		}
	}
