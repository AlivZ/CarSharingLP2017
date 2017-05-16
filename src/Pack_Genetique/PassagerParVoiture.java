package Pack_Genetique;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import v3_window.Cell;

/**
 * 
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 */
public class PassagerParVoiture {
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/
	
	/**
	 * Matrice de passagers
	 */
	Passager[][] passagersOrdonnes;
	/**
	 * Matrice de point de passage <br>
	 * M(i,2*j)
	 */
	PositionPassager[][] pointsDePassage;
	
	/**
	 * Recursivité : poids du chemin optimal
	 */
	private int U;
	/**
	 * Récursivité : chemin le plus optimal
	 */
	private PositionPassager[][] passageOptimal;
	
	
	 /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	  */
	
	/**
	 * Constructeur simple
	 */
	public PassagerParVoiture() {
		this.passagersOrdonnes = new Passager[Main.nbVoiture][Main.nbPlaceVoiture];
		//this.prompt(this.passagersOrdonnes);
		this.pointsDePassage = new PositionPassager[Main.nbVoiture][Main.nbPlaceVoiture*2];
		this.passageOptimal = new PositionPassager[Main.nbVoiture][Main.nbPlaceVoiture*2];
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
   * on repartit aléatoirement nbPassager (dans Main) passagers dans (nbVoiture) voitures
   * @version Build III -  v0.2
	 * @since Build III -  v0.0
   */
	
    public void generatePassagerOnVoiture() {
    	this.pointsDePassage = new PositionPassager[Main.nbVoiture][Main.nbPlaceVoiture*2];
    	Passager[] listeDesPassagers= new Passager[Main.nbPassager];
    	for(int i = 0; i < Main.nbPassager; i++)
    		listeDesPassagers[i] = Main.lesPassagers[i];
		Collections.shuffle(Arrays.asList(listeDesPassagers));
		int index = 0;
		for(int i = 0 ; i < Main.nbVoiture ; i++)
			for(int j = 0 ; j < Main.nbPlaceVoiture ; j++){
				if(listeDesPassagers.length>index) 
					this.setPassager(i, j, listeDesPassagers[index]);
				index++;
			}
		
		this.attribuerPointsDePassage();
		}

	/**
	 * Pour chaque voiture on attribue l'ordre de passage à chaque départ et déstination de chaque passager
	 * <br>
	 * Effectué dans l'ordre "normale" (pas de dépot avant
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
    public void attribuerPointsDePassage() {
    	
    	for (int i = 0; i < Main.nbVoiture; i++){ // Pour chaque voiture ayant x Place
    		 Pre_Algo_DeterministePourPoint(this.passagersOrdonnes[i], i);
    		for(int j = 0; j < this.passageOptimal[i].length ; j++) {
    			this.pointsDePassage[i][j] = this.passageOptimal[i][j];
    		}
    		this.passageOptimal = new PositionPassager[Main.nbVoiture][Main.nbPlaceVoiture*2];
    	}
    	
	}
    
    /*
    * Méthode ré-récursive qui initialise les variables
    * @since Build III - v0.2
    * @version Build III - v0.2
    * @param passagers liste des passagers
    */
   private void Pre_Algo_DeterministePourPoint(Passager[] passagers, int numeroVoiture) {
	   	boolean[] selected = new boolean[passagers.length]; //déclare si déjà sélectionné.
	   	for(int k=0; k < selected.length; k++) { selected[k] = false; }
	   	
	   	boolean[] possable = new boolean[passagers.length]; //déclare si déjà sélectionné.
	   	for(int k=0; k < selected.length; k++) { possable[k] = true; }
	   	
	   	PositionPassager[] listeDesPoints = new PositionPassager[Main.nbPlaceVoiture*2];
	   	
	   	this.U = 0;
	   	
	   	this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, possable, selected, 0, numeroVoiture);
	}
   
   /**
    * Récursif qui teste toutes les combinaisons possibles <br>
    * En s'inspirant de l'algorithme sac à dos, on ne sélectionne que le parcours le plus petit et on le stock (this.passageOptimal) <br>
    * On utilise this.U pour connaitre la taille du chemin
    * @since Build III - v0.0
    * @version Build III - v0.2
    * @param passagers liste des passagers
    * @param listeDesPoints "conteneur de point"
    * @param possable est-ce que tel passager est possable
    * @param selected est-ce que tel passager est prenable
    * @param n index 
    */
   private void Algo_DeterministeParRecursivite(Passager[] passagers, PositionPassager[] listeDesPoints, boolean[] possable, boolean[] selected, int n, int numeroVoiture) {
   	if(n==((Main.nbPlaceVoiture*2)-1)) { //cas de base
   		for(int i=0;i<Main.nbPlaceVoiture;i++) {
   			if(possable[i]==true) {
   				listeDesPoints[n] = passagers[i].getArrivee();
   				int poids = this.getDistanceCheminPourU(listeDesPoints);
       			if(this.U>poids || this.U==0){
       				this.U = poids;
       				for(int k=0; k<listeDesPoints.length;k++) {
       					this.passageOptimal[numeroVoiture][k] = listeDesPoints[k];
       				}
       			}
   			}		
   		}
   	} else { //cas récursif
   		for(int i=0;i<Main.nbPlaceVoiture;i++) {
   			
   			boolean[] tempon1 = new boolean[selected.length];
   			boolean[] tempon2 = new boolean[possable.length];
   			if(selected[i]==false) {
   				System.out.println(listeDesPoints.length + " / n : " + n);
   				System.out.println(passagers.length + " / i : " + i);
   				listeDesPoints[n] = passagers[i].getDepart();
   				for(int k=0; k<selected.length;k++) {
   					tempon1[k] = selected[k];
   				}
   				tempon1[i] = true;
   				this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, possable, tempon1, n+1, numeroVoiture);
   			} else if(possable[i]==true) {
   				
   				listeDesPoints[n] = passagers[i].getArrivee();
   				for(int k=0; k<possable.length;k++) {
   					tempon2[k] = possable[k];
   				}
   				tempon2[i] = false;
   	    			
   				this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, tempon2, selected, n+1, numeroVoiture);
   			}  //Il y a des cas où rien ne se passe et on saute
   		}	
   	}
   }
  
 


/*
           __  __ _      _                      
    /\    / _|/ _(_)    | |                     
   /  \  | |_| |_ _  ___| |__   __ _  __ _  ___ 
  / /\ \ |  _|  _| |/ __| '_ \ / _` |/ _` |/ _ \
 / ____ \| | | | | | (__| | | | (_| | (_| |  __/
/_/    \_\_| |_| |_|\___|_| |_|\__,_|\__, |\___|
                                     __/ |     
                                    |___/      
 */  
   
   
   public void afficherPassagerOnVoitures() {
		for(int i = 0 ; i < Main.nbVoiture  ; i++){
			for(int j = 0 ; j < Main.nbPlaceVoiture ; j++){
				System.out.print(passagersOrdonnes[i][j].getId() + " ");
			}
		System.out.println("");
		}
    }
   
   public void afficherPoints() { 
		for(int i = 0 ; i < Main.nbVoiture  ; i++){
			for(int j = 0 ; j < (Main.nbPlaceVoiture*2) ; j++){
				System.out.print(pointsDePassage[i][j].toString() + " ");
			}
		System.out.println("");
		}
    }
   
   
	/*
	   _____      _               _____      _   
	  / ____|    | |     ___     / ____|    | |  
	 | |  __  ___| |_   ( _ )   | (___   ___| |_ 
	 | | |_ |/ _ \ __|  / _ \/\  \___ \ / _ \ __|
	 | |__| |  __/ |_  | (_>  <  ____) |  __/ |_ 
	  \_____|\___|\__|  \___/\/ |_____/ \___|\__|
	                                             
	  */                                           

   public int getNbPassagers() {
       return passagersOrdonnes[0].length;
   }
   public int getNbVoitures() {
       return passagersOrdonnes.length;
   }
   
   public PositionPassager[][] getPointsDePassage() {
   	return this.pointsDePassage;
   }
   
   public Passager getPassager(int voiture,int index) {
       return passagersOrdonnes[voiture][index];
   }

   public void setPassager(int voiture, int index, Passager passager) {
       passagersOrdonnes[voiture][index] = passager;
   }
   
   /**
    *  Trouver le resultat d'un GroupePassager, la somme des distances entre le 1er et le 2nd, puis le 2nd et le 3ème.... 
    * @param GroupePassager
    * @return
    * @version Build III -  v0.1
	 * @since Build III -  v0.0
    */
   public int getDistanceChemin() {
   	int resultat = 0;
       int distanceEntreDeuxPoints = 0;
       
       /* si la voiture part de (0,0)
       distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_y());
   	distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_x());
   	resultat += distanceEntreDeuxPoints;
        */
       
       for (int i = 0; i < this.pointsDePassage.length - 1 ; i++){
           for (int j = 0; j < this.pointsDePassage[i].length - 1 ; j++){
        	   distanceEntreDeuxPoints = 0;
        	   distanceEntreDeuxPoints += Math.abs(this.pointsDePassage[i][j].getPos_y() - this.pointsDePassage[i][j+1].getPos_y());
        	   distanceEntreDeuxPoints += Math.abs(this.pointsDePassage[i][j].getPos_x() - this.pointsDePassage[i][j+1].getPos_x());
        	   resultat += distanceEntreDeuxPoints;
           }
       }
   	
       return resultat;
   }
   
   
   /**
    * Trouve la samme des distances pour un seul véhicule.
    * @version Build III -  v0.2
	 * @since Build III -  v0.2
    * @param listeDesPoints
    * @return
    */
   public int getDistanceCheminPourU(PositionPassager[] listeDesPoints) {
	   //TODO
	   
   	int resultat = 0;
       int distanceEntreDeuxPoints = 0;
       
	   	for (int j = 0; j < listeDesPoints.length - 1 ; j++){
		   	distanceEntreDeuxPoints = 0;
		   	distanceEntreDeuxPoints += Math.abs(listeDesPoints[j].getPos_y() - listeDesPoints[j+1].getPos_y());
		   	distanceEntreDeuxPoints += Math.abs(listeDesPoints[j].getPos_x() - listeDesPoints[j+1].getPos_x());
		   	resultat += distanceEntreDeuxPoints;
       }
   	
   	return resultat;
   }
   
	/* 
		           __  __ _      _                      
		    /\    / _|/ _(_)    | |                     
		   /  \  | |_| |_ _  ___| |__   __ _  __ _  ___ 
		  / /\ \ |  _|  _| |/ __| '_ \ / _` |/ _` |/ _ \
		 / ____ \| | | | | | (__| | | | (_| | (_| |  __/
		/_/    \_\_| |_| |_|\___|_| |_|\__,_|\__, |\___|
		                      __/ |     
		                     |___/      
		*/  
		
		/**
		* Affichage de la matrice.
		* @param passagersOrdonnes2
		*/
		private void prompt(Passager[][] passagersOrdonnes2) {
		for(int i=0;i<passagersOrdonnes2.length;i++) {
			System.out.print("[");
			for(int j=0;j<passagersOrdonnes2[i].length;j++) {
				System.out.print("["+passagersOrdonnes2[i][j].getId()+"]");
			}
			System.out.println("]");
		}

}
}

