package Pack_Genetique;

/**
 * Classe des postions départ et arrivée des passagers.
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Point {
	private int pos_x;
	private int pos_y;
	
	/**
	 * Constructeur d'un point de départ ou d'arrivée.
	 */
	public Point(){
		//Here we give him a random position on the 20*20 grid
		this.pos_x = (int)(Math.random() * 20 + 1);
		this.pos_y = (int)(Math.random() * 20 + 1);
	}
	
	public int getPos_y() {
		return pos_y;
	}
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	public int getPos_x() {
		return pos_x;
	}
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	 @Override
	    public String toString() {
		 String pointString = "";
		 pointString = "("+this.getPos_x()+","+this.getPos_y()+")";
		 return pointString;
	 }
}
