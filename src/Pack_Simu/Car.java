package Pack_Simu;

import java.util.ArrayList;
import java.awt.Point;

/**
 * La classe des voitures
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 *
 */
public class Car
{
	private int idCar;
	private Point posCar;
	/**
	 * parcoursList contient les étapes du parcours
	 */
	private ArrayList<ParcoursStep> parcoursList;
	/**
	 * occupantList contient les clients dans la voiture
	 */
	private ArrayList<Client> occupantList;
	/**
	 * indique le numéro de la rue dans laquelle la voiture se trouve
	 */
	int streetId;
	protected boolean isDoingCarSharing;
	private static int cptIdCar = 0;

	/**
	 * Constructeur d'une voiture avec des coordonnées de départ.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param coordX
	 * @param coordY
	 */
	public Car(int coordX, int coordY){
		this.idCar = cptIdCar;
		this.posCar = new Point(coordX,coordY);
		this.parcoursList = new ArrayList<ParcoursStep>();
		this.occupantList = new ArrayList<Client>();
		this.streetId = -1;
		this.isDoingCarSharing = true;
		cptIdCar++;
	}

	public boolean getIsDoingCarSharing() {
		return this.isDoingCarSharing;
	}
	
	public void setIsDoingCarSharing(boolean newIsDoingCarSharing) {
		this.isDoingCarSharing = newIsDoingCarSharing;
	}

	public Point getPosCar() {
		return this.posCar;
	}

	public int getIdCar() {
		return this.idCar;
	}

	public ArrayList<Client> getOccupantListCar() {
		return this.occupantList;
	}
	
	public ArrayList<ParcoursStep> getParcoursListCar() {
		return this.parcoursList;
	}
	
	public void setParcoursList(ArrayList<ParcoursStep> newParcoursList) {
		this.parcoursList = newParcoursList;
	}
}
