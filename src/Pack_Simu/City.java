package Pack_Simu;

/**
 * Classe de la ville (qui est en fait un tableau)
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 *
 */
public class City {

	private int cityWidth;
	private int cityHeight;
	private int[][][] streetArray;
	
	public City(int Width, int Height, Model mod) {

		this.cityWidth = Width/(mod.getStreetLength()+1);
		this.cityHeight = Height/(mod.getStreetLength()+1);
		this.streetArray = new int[this.cityWidth][this.cityHeight][2];
	}
	
	public int getCityHeight() {
		return cityHeight;
	}
	
	public int getCityWidth() {
		return cityWidth;
	}

	public void setCityWidth(int cityWidth) {
		this.cityWidth = cityWidth;
	}
	
	public void setCityHeight(int cityHeight) {
		this.cityHeight = cityHeight;
	}

	public int[][][] getStreetArray() {
		return streetArray;
	}

	public void setStreetArray(int[][][] streetArray) {
		this.streetArray = streetArray;
	}
}
