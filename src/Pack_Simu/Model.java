package Pack_Simu;

/**
 * Classe Model
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 *
 */
public class Model {
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/      
	
	
	private int carLength;
	private int streetLength;
	
	/**
	 * STAT - Moyenne des vitesses instantanées :
	 */
	private double carSpeedMean;
	/**
	 * ?
	 */
	private double clientSpeedSum;
	/**
	 * STAT - Moyenne des vitesses des trajets terminés
	 */
	private double clientSpeedMean;
	/**
	 * STAT - Moyenne des vitesses des trajets en cours
	 */
	private double clientRealSpeedMean;
	
	 /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	  */
	
	public Model(int cLength, int sLength) {
		this.carLength = cLength;
		this.streetLength = sLength;
		this.carSpeedMean = 0 ;
		this.clientSpeedSum = 0 ;
		this.clientSpeedMean = 0 ;
		this.clientRealSpeedMean = 0 ;
	}
	
	/*
	  __  __      _   _               _           
	 |  \/  |    | | | |             | |          
	 | \  / | ___| |_| |__   ___   __| | ___  ___ 
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` |/ _ \/ __|
	 | |  | |  __/ |_| | | | (_) | (_| |  __/\__ \
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|\___||___/
	                                              
	 */	
	
	
	public void addClientSpeedSum(double val) {
		this.clientSpeedSum += val;
	}
	
	/*
	   _____      _               _____      _   
	  / ____|    | |     ___     / ____|    | |  
	 | |  __  ___| |_   ( _ )   | (___   ___| |_ 
	 | | |_ |/ _ \ __|  / _ \/\  \___ \ / _ \ __|
	 | |__| |  __/ |_  | (_>  <  ____) |  __/ |_ 
	  \_____|\___|\__|  \___/\/ |_____/ \___|\__|
	                                             
	  */

	public double getClientRealSpeedMean() {
		return clientRealSpeedMean;
	}

	public void setClientRealSpeedMean(double clientRealSpeedMean) {
		this.clientRealSpeedMean = clientRealSpeedMean;
	}

	public double getClientSpeedMean() {
		return clientSpeedMean;
	}

	public void setClientSpeedMean(double clientSpeedMean) {
		this.clientSpeedMean = clientSpeedMean;
	}

	public int getCarLength() {
		return carLength;
	}
	
	public void setCarLength(int carLength) {
		this.carLength = carLength;
	}
	
	public int getStreetLength() {
		return streetLength;
	}
	
	public void setStreetLength(int streetLength) {
		this.streetLength = streetLength;
	}
	
	public double getCarSpeedMean() {
		return carSpeedMean;
	}
	
	public void setCarSpeedMean(double carSpeedMean) {
		this.carSpeedMean = carSpeedMean;
	}
	
	public double getClientSpeedSum() {
		return clientSpeedSum;
	}
	
	public void setClientSpeedSum(double clientSpeedSum) {
		this.clientSpeedSum = clientSpeedSum;
	}
	
}
