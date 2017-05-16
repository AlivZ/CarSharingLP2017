package Pack_Genetique;
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
	private PositionPassager[] positionPassagers = new PositionPassager[2] ;
	/**
	 * Nombre de passager totaux (inutile si Base de donnée : SELECT COUNT(*)
	 */
	private static int nbPassagers = 0;
	
	
    /*                                                   
	    _____                _                   _                  
	   / ____|              | |                 | |                 
	  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
	  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
	  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
	   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	
	*/

	/**
	 * Constructeur de Passager
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public Passager(){
		Passager.nbPassagers ++;
		this.id = Passager.nbPassagers;
		positionPassagers[0] = new PositionPassager();
		positionPassagers[1] = new PositionPassager();

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
     * Méthode qui permet de générer de façon aléatoire nbPassager passager
     * @param nbPassager nombre de passager créé de façon aléatoire
     * @return Tableau de passager généré aléatoirement
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
	public static Passager[] generatePassagers(int nbPassager) {
		Passager[] lesPassagers = new Passager[nbPassager];
		Passager unPassager;
		for(int i = 0; i < nbPassager; i ++){
			unPassager = new Passager();
			lesPassagers[i] = unPassager;
		}
		System.out.println(lesPassagers.length);
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

                                            


	public PositionPassager getArrivee() {
		return this.positionPassagers[0];
	}

	public PositionPassager getDepart() {
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
     	   distanceEntreDeuxPoints += Math.abs(lesPassagers[i].getDepart().getPos_y() - lesPassagers[i].getArrivee().getPos_y());
     	   distanceEntreDeuxPoints += Math.abs(lesPassagers[i].getDepart().getPos_x() - lesPassagers[i].getArrivee().getPos_x());
     	   somme += distanceEntreDeuxPoints;
		}
		return somme;
	}

}