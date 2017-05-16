package v3_algo;

import java.util.ArrayList;

import v3_window.Cell;
/**
 * Passager de voiture.
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Passager {

	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/                                                             
	      
	
	/**
	 * Id unique de Passager
	 */
	private int id;
	/**
	 * PositionPassager de départ et point d'arrivée.
	 */
	private Cell[] positionPassagers = new Cell[2] ;
	/**
	 * Nombre de passager totaux (inutile si Base de donnée : SELECT COUNT(*)
	 */
	private static int nbPassagers = 0;
	/**
	 * Déclare
	 */
	private boolean exist;
	
    /*                                                   
	    _____                _                   _                  
	   / ____|              | |                 | |                 
	  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
	  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
	  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
	   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	
	*/

	/**
	 * Constructeur de passager qui déclare si le passager existe (tableau de passager dans passagerParVoiture) <br>
	 * Permet de gérer moins de passager que de place dispo.
	 * @param b
	 * @version Build III -  v0.5
	 * @since Build III -  v0.5
	 */
	public Passager(boolean b) {
		this.exist = b;
	}
	
	/**
	 * Constructeur de Passager
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */	
	public Passager(Cell cellule_depart, Cell cellule_arrivee){
		Passager.nbPassagers ++;
		this.id = Passager.nbPassagers;
		positionPassagers[0] = cellule_depart;
		positionPassagers[1] = cellule_arrivee;
		this.exist = true;
	}
	
	/*
	  __  __      _   _               _           
	 |  \/  |    | | | |             | |          
	 | \  / | ___| |_| |__   ___   __| | ___  ___ 
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` |/ _ \/ __|
	 | |  | |  __/ |_| | | | (_) | (_| |  __/\__ \
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|\___||___/
	                                              
	 */	
	
	
	

	/**
     * Méthode qui construit un tableau de passager en fonction des passagers dispo.
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
	public static Passager[] buildPassager(ArrayList<Cell> list_client, ArrayList<Cell> list_client_depot) {
		Passager.nbPassagers = 0;
		Passager[] lesPassagers = new Passager[list_client_depot.size()];
		for(int i = 0; i < list_client_depot.size(); i ++){
			lesPassagers[i] = new Passager(list_client.get(i), list_client_depot.get(i));
		}
		return lesPassagers;
	}
	
	/*
	   _____      _               _____      _   
	  / ____|    | |     ___     / ____|    | |  
	 | |  __  ___| |_   ( _ )   | (___   ___| |_ 
	 | | |_ |/ _ \ __|  / _ \/\  \___ \ / _ \ __|
	 | |__| |  __/ |_  | (_>  <  ____) |  __/ |_ 
	  \_____|\___|\__|  \___/\/ |_____/ \___|\__|
	                                             
	  */                                           

                                            
	public boolean getExist() {
		return this.exist;
	}

	public Cell getArrivee() {
		return this.positionPassagers[0];
	}

	public Cell getDepart() {
		return this.positionPassagers[1];
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

	public static int getPireDistance(Passager[] lesPassagers) {
		int somme = 0;
		for(int i = 0; i < lesPassagers.length ; i++) {
			int distanceEntreDeuxPoints = 0;
     	   distanceEntreDeuxPoints += Math.abs(lesPassagers[i].getDepart().getColumn() - lesPassagers[i].getArrivee().getColumn());
     	   distanceEntreDeuxPoints += Math.abs(lesPassagers[i].getDepart().getRow() - lesPassagers[i].getArrivee().getRow());
     	   somme += distanceEntreDeuxPoints;
		}
		return somme;
	}


}