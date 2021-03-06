package Pack_Simu;

public class Model {
	
	private int carLength;
	private int streetLength;
	private double carSpeedMean;
	private double clientSpeedSum;
	private double clientSpeedMean;
	private double clientRealSpeedMean;
	
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
