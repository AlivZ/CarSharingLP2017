package v3_algo;

import java.util.ArrayList;

import v3_window.Cell;
import v3_window.Window;

public class Execut_Algo_Genetique {


	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/                                                             
	      
	/**
	 * Tableau des passagers 
	 */
	protected static Passager[] lesPassagers;
	/**
	 * Nombre d'it�ration � affectuer apr�s la derni�re diminution de taille
	 */
	protected static final int nbIterations = 30;
	/**
	 * Nombre de passager en tout
	 */
    protected static int nbPassager;

    /**
     * Taille de la population utilis�e par l'algo g�n�tique
     */
    protected static int taillePopulation = 10;
    /**
     * Nombre de voiture
     */
    protected static  int nbVoiture;
    /** 
     * Nombre de place par voiture.
     */
    protected static final int nbPlaceVoiture = 4;
    /**
     * Taille de la grille
     */
    protected static int sizeGrille_X;
    protected static int sizeGrille_Y;
    /**
     * Premiere solution trouv�e (distance)
     */
    int premiereSolution = 0;
    /**
     * Meilleure soltuion trouv�e (distance)
     */
    int meilleureSolution;
    /**
     * Solution �goiste (1 voiture / passager)
     */
    int egoisteSolution;
    /**
     * Meilleure combinaison de Passager distribu� dans des voitures
     */
    PassagerParVoiture meilleurPassagerParVoiture;
    /**
     * Conteur de g�n�ration
     */
    int generationCount;
    
    /**
     * Grille
     * @param grid
     * @param list_car
     * @param list_client
     * @param list_client_depot
     * @param rows
     * @param columns
     */
    static Cell[][] grid;
    
    /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  

	  */
   
    /**
     * On initialise le programme. <br>
     * On g�n�re la liste des Passagers
     * @param win
     */
    public Execut_Algo_Genetique(Window win) {
    	long debut = System.currentTimeMillis(); //Debut du compteur
    	
    	Execut_Algo_Genetique.nbPassager = win.list_client_depot.size();
    	Execut_Algo_Genetique.nbVoiture = win.list_car.size();
    	Execut_Algo_Genetique.sizeGrille_X = win.rows;
    	Execut_Algo_Genetique.sizeGrille_Y = win.columns;
    	Execut_Algo_Genetique.grid = win.grid;
    	
 		Execut_Algo_Genetique.lesPassagers = Passager.buildPassager(win.list_client_, win.list_client_depot); 
 		
 		Population myPop = this.execute(win.list_block); //exuction de l'algo g�n�tique 
 		
 		 this.meilleureSolution = myPop.getMoreCompetent().getU(); //taille de la meilleure solution
 		this.affichage(debut);
    }
 
    /**
     * Execution des it�rations
     * @return
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
    public Population execute(ArrayList<Cell> l_b) {
    	int nbIterationMeilleureSolution = 0;
    	 Population myPop = new Population(taillePopulation, true, l_b); //cr�ation de la population initialie
    	 Population bestPop = myPop;
    	 this.generationCount = 0;
    	 this.meilleureSolution =  myPop.getMoreCompetent().getU();
    	 this.egoisteSolution = Passager.getPireDistance(lesPassagers);
    	 if(Execut_Algo_Genetique.nbVoiture==1) {
             myPop = Algo_Genetique.evolvePopulation(myPop, l_b);
             bestPop = myPop;
        	this.meilleureSolution = myPop.getMoreCompetent().getU();
        	this.premiereSolution = this.meilleureSolution;
         	this.meilleurPassagerParVoiture = myPop.getMoreCompetent();
         	nbIterationMeilleureSolution = 1;
    	 } else {
    		 while (nbIterationMeilleureSolution < Execut_Algo_Genetique.nbIterations) {
                 this.generationCount++;
                 // if(this.generationCount%10==0)
        	 		System.out.println("Generation: " + this.generationCount + " distance parcourue: " + this.meilleureSolution+"m");
                 myPop = Algo_Genetique.evolvePopulation(myPop, l_b);
                 if (myPop.getMoreCompetent().getU() < this.meilleureSolution){
                	 this.meilleureSolution = myPop.getMoreCompetent().getU();
                	 bestPop = myPop;
                 	nbIterationMeilleureSolution = 0;
                 	this.meilleurPassagerParVoiture = myPop.getMoreCompetent();
                 }
                 if (this.generationCount == 1 ) {
                	 this.premiereSolution = this.meilleureSolution;
                 }
                 else 
                 	nbIterationMeilleureSolution ++;  
             }
    	 }
    	 return bestPop;
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
     * Affichage des donn�es dans la console
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
    public void affichage(long debut) {
    	for(int i = 0; i < Execut_Algo_Genetique.nbPassager; i++){
        	 System.out.println(Execut_Algo_Genetique.lesPassagers[i].toString());
        }
    	System.out.println("---------------------");
        System.out.println("Solution found ! Number of generations created : "+ this.generationCount);
        System.out.println("Distance: " + (float)this.meilleureSolution/100+" Km");
        System.out.println("Distance au d�but : " + (float)this.premiereSolution/100+" Km");
        System.out.println("Distance total 1 par 1 : " + (float)this.egoisteSolution/100 + "Km");

        System.out.println("---------------------");
        System.out.println("R�partition des passagers :");
        this.meilleurPassagerParVoiture.afficherPassagerOnVoitures();
    
        System.out.println("---------------------");
        System.out.println("Matrice des points � parcourir : ");
        this.meilleurPassagerParVoiture.afficherPoints();
        
        long fin = System.currentTimeMillis();
        System.out.println("M�thode ex�cut�e en " + Long.toString(fin - debut) + " millisecondes");

    }
    
    
	
	
	
}
