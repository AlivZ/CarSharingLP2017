package Pack_Genetique;
/**
 * Passager de voiture.
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Passager {
	
	/**
	 * Id unique de Passager
	 */
	private int id;
	/**
	 * Point de départ et point d'arrivée.
	 */
	private Point[] points = new Point[2] ;
	/**
	 * Nombre de passager totaux (inutile si Base de donnée : SELECT COUNT(*)
	 */
	private static int nbPassagers = 0;

	/**
	 * Constructeur de Passager
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public Passager(){
		Passager.nbPassagers ++;
		this.id = Passager.nbPassagers;
		points[0] = new Point();
		points[1] = new Point();

	}
 
    /* Getters and setters */
	public Point getArrivee() {
		return this.points[0];
	}

	public Point getDepart() {
		return this.points[1];
	}
	
	public int getId() {
		return this.id;
	}
	public void setId(int i) {
		this.id = i;
	}
 
    @Override
    public String toString() {
        String passagerString = "";
        passagerString += "Passager n°"+this.id+" position de départ: "+this.getDepart()+", destination : "+this.getArrivee(); 
        return passagerString;
    }

}