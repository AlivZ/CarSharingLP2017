package Pack_Simu;

/**
 * Classe Model
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 *
 */
public class Model {
	
	private int carLength;
	private int streetLength;
	private double carSpeedMean;
	private double clientSpeedSum;
	private double clientSpeedMean;
	private double clientRealSpeedMean;
	
	public Model(int cLength, int sLength) {
		this.carLength = cLength;
		this.streetLength = sLength;
		this.carSpeedMean = 0 ;
		this.clientSpeedSum = 0 ;
		this.clientSpeedMean = 0 ;
		this.clientRealSpeedMean = 0 ;
	}

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
