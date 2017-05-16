package v3_algo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import v3_window.Main;
import v3_window.Cell;

/**
 * Classe qui permet de g�rer l'Algorithme G�n�tique
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Algo_Genetique {
 
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/                                                             
	    
	
    
    private static final double chanceDeCroisement = 0.8;
    private static final double mutationRate = 0.15;
    private static final int tournamentSize = 3;

 
    /*
	  __  __      _   _               _           
	 |  \/  |    | | | |             | |          
	 | \  / | ___| |_| |__   ___   __| | ___  ___ 
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` |/ _ \/ __|
	 | |  | |  __/ |_| | | | (_) | (_| |  __/\__ \
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|\___||___/
	                                              
	 */	
    
    /**
     * Evoluer la population : <br>
     * On garde le meilleur Groupe, on cr�er un nouveau groupe de passager <br>
     * On croise
     * et on mute 
     * @param pop Population (voitures)
     * @return
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public static Population evolvePopulation(Population pop, ArrayList<Cell> l_b) {
        Population newPopulation = new Population(pop.getSize(), false, l_b);
        
 
        // Garder le meilleur PassagerParVoiture
        newPopulation.savePassagerOnVoiture(0, pop.getMoreCompetent());
        int elitismOffset = 1;
      
        // Loop over the population size and create new member with
        // Select new population
        for (int i = elitismOffset; i < pop.getSize(); i++) {
        	PassagerParVoiture newPassagerOnVoiture = selection(pop, pop.getPassagerOnVoiture(i), l_b);
            newPopulation.savePassagerOnVoiture(i, newPassagerOnVoiture);
        }
         
        //Crossover
        /*for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
            croisement(newPopulation.getPassagerOnVoiture(i));
        } */
        
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
            mutation(newPopulation.getPassagerOnVoiture(i));
        }
        
        for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
        	newPopulation.getPassagerOnVoiture(i).attribuerPointsDePassage();
        }

        return newPopulation;
    }
    
    /**
     * Croisement des voitures.
     * @param PassagerParVoiture
     */
    private static void croisement(PassagerParVoiture PassagerParVoiture) {
    	int nbSwap = (int)(Math.random() * 2 + 1); 

        for (int i = 0; i < PassagerParVoiture.getNbVoitures(); i++) 
        	if (Math.random() <= chanceDeCroisement)
        			swapPassagers(PassagerParVoiture, i, nbSwap);

    } 
    
	private static void swapPassagers(PassagerParVoiture PassagerParVoiture, int numVoiture, int nbSwap) {
		System.out.println("numVoiture : " + numVoiture + " / Taille : " + PassagerParVoiture.nbPassagerParVoiture[numVoiture]);
		if(nbSwap == 1){
			 Collections.swap(Arrays.asList(PassagerParVoiture.passagersOrdonnes[numVoiture]), 0, 3);
		} 
		 else if(nbSwap == 2){
			Collections.swap(Arrays.asList(PassagerParVoiture.passagersOrdonnes[numVoiture]), 0, 2);
			Collections.swap(Arrays.asList(PassagerParVoiture.passagersOrdonnes[numVoiture]), 1, 3);
		}
		
	}

	// Selection members
    private static PassagerParVoiture selection(Population pop, PassagerParVoiture PassagerParVoiture, ArrayList<Cell> l_b) {
        PassagerParVoiture newMember = new PassagerParVoiture();
        newMember = tournamentSelection(pop, l_b);
        return newMember;
    }
 
    // Mutate a member
    private static void mutation(PassagerParVoiture PassagerParVoiture) {
        // Loop through passagers
        for (int i = 0; i < PassagerParVoiture.getNbVoitures(); i++) {
        	for (int j = 0; j < PassagerParVoiture.getNbPassagers(); j++){
        		if (Math.random() <= mutationRate) {
        			// Swap two random passagers
        			randomSwapPassagers(PassagerParVoiture);
        		}
            }
        }
    }
    
    /**
     * Swap de fa�on al�atoire les passagers
     * @param PassagerParVoiture
     */
    private static void randomSwapPassagers(PassagerParVoiture PassagerParVoiture){
		int passager1 = (int)(Math.random() * Execut_Algo_Genetique.nbPassager + 1);
		int passager2 = (int)(Math.random() * Execut_Algo_Genetique.nbPassager + 1);

		Passager passagerTmp = null;
		int[] coord1 = new int[2];
		int[] coord2 = new int[2];

        for (int i = 0; i < PassagerParVoiture.getNbVoitures(); i++) {
        	for (int j = 0; j < PassagerParVoiture.getNbPassagers(); j++){
        		if(PassagerParVoiture.passagersOrdonnes[i][j].getId() == passager1){
        			coord1[0] = i;
        			coord1[1] = j;
        		}
        		if(PassagerParVoiture.passagersOrdonnes[i][j].getId() == passager2){
        			coord2[0] = i;
        			coord2[1] = j;        		
        		}		
        	}
        }
        passagerTmp = PassagerParVoiture.passagersOrdonnes[coord1[0]][coord1[1]];
        PassagerParVoiture.passagersOrdonnes[coord1[0]][coord1[1]] = PassagerParVoiture.passagersOrdonnes[coord2[0]][coord2[1]];
        PassagerParVoiture.passagersOrdonnes[coord2[0]][coord2[1]] = passagerTmp;
    }
    
    /**
     * Cr�ation d'un battle royale entre plusieurs PassagerParVoitures de la population
     * @param pop
     * @return PassagerParVoiture gagnant !
     */
    private static PassagerParVoiture tournamentSelection(Population pop, ArrayList<Cell> l_b) {
        Population tournament = new Population(tournamentSize, false, l_b);
        // Les participants sont tir�s au sort
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getSize());
            tournament.savePassagerOnVoiture(i, pop.getPassagerOnVoiture(randomId));
        }
        return tournament.getMoreCompetent();
    }
}