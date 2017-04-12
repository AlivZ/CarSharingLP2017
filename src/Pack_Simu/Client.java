package Pack_Simu;

import java.awt.Point;


/** 
 * La classe client représente un voyageur
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 *
 */
public class Client
{
	private int idClient;
	int appearanceMoment;
	/**
	 * @see Point
	 */
	private Point[] posClient;
	/**
	 * Etat du client. <br>
	 * <ol> <li> Sur le trottoire </li>
	 * <li> Dans la voiture </li>
	 * </ol>
	 */
	int stateClient;
	private Car carClient;
	private boolean isUsingCarSharing;
	private static int idcptClient=0; 

	/**
	 * Constructeur du client.
	 * @param time
	 * @param coordX
	 * @param coordY
	 */
	Client(int time, int coordX, int coordY){
		this.idClient = idcptClient;
		this.appearanceMoment = time;
		this.posClient = new Point[2];
		this.posClient[0] = new Point(coordX,coordY);
		this.stateClient = 0;
		this.isUsingCarSharing = true;
		idcptClient++;
	}
	
	/**
	 * To String
	 * @return l'id sous forme de string
	 */
	public String toString() {
		String test = Integer.toString(this.idClient);
		return test;
	}
	
	public int getStateClient () {
		return this.stateClient;
	}
	
	public void setStateClient(int newStateClient) {
		this.stateClient = newStateClient;
	}

	public void setIsUsingCarSharing(boolean newIsUsingCarSharing) {
		this.isUsingCarSharing = newIsUsingCarSharing;
	}
	
	public boolean getIsUsingCarSharing () {
		return this.isUsingCarSharing;
	}
	
	public Point[] getPosClient () {
		return this.posClient;
	}
	
	public Car getCarClient () {
		return this.carClient;
	}
	
	public void setCarClient (Car newCar) {
		this.carClient = newCar;
	}

	public int getIdClient() {
		return this.idClient;
	}
}
