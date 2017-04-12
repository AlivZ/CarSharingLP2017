package Pack_Genetique;
import java.util.Arrays;
import java.util.Collections;

/**
 * Classe qui permet de gérer l'Algorithme Génétique
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Algo_Genetique {
 
    /* Paramètres de l'algo */
    private static final double chanceDeCroisement = 0.8;
    private static final double mutationRate = 0.08;
    private static final int tournamentSize = 3;

 
    /**
     * Evoluer la population : <br>
     * On garde le meilleur Groupe, on créer un nouveau groupe de passager <br>
     * On croise
     * et on mute 
     * @param pop Population (voitures)
     * @return
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);
        
 
        // Garder le meilleur PassagerOnVoiture
        newPopulation.savePassagerOnVoiture(0, pop.getMoreCompetent());
        int elitismOffset = 1;
      
        // Loop over the population size and create new member with
        // Select new population
        for (int i = elitismOffset; i < pop.size(); i++) {
        	PassagerOnVoiture newPassagerOnVoiture = selection(pop, pop.getPassagerOnVoiture(i));
            newPopulation.savePassagerOnVoiture(i, newPassagerOnVoiture);
        }
        
        //Crossover
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            croisement(newPopulation.getPassagerOnVoiture(i));
        }
        
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutation(newPopulation.getPassagerOnVoiture(i));
        }
        
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
        	newPopulation.getPassagerOnVoiture(i).attribuerPointsDePassage();
        }

        return newPopulation;
    }
    
    /**
     * Croisement des voitures.
     * @param PassagerOnVoiture
     */
    private static void croisement(PassagerOnVoiture PassagerOnVoiture) {
    	int nbSwap = (int)(Math.random() * 2 + 1); 

        for (int i = 0; i < PassagerOnVoiture.nbVoitures(); i++) 
        	if (Math.random() <= chanceDeCroisement)
        			swapPassagers(PassagerOnVoiture, i, nbSwap);

    } 
    
	private static void swapPassagers(PassagerOnVoiture PassagerOnVoiture, int numVoiture, int nbSwap) {	
		if(nbSwap == 1){
			 Collections.swap(Arrays.asList(PassagerOnVoiture.passagersOrdonnes[numVoiture]), 0, 3);
		} 
		 else if(nbSwap == 2){
			Collections.swap(Arrays.asList(PassagerOnVoiture.passagersOrdonnes[numVoiture]), 0, 2);
			Collections.swap(Arrays.asList(PassagerOnVoiture.passagersOrdonnes[numVoiture]), 1, 3);
		}
		
	}

	// Selection members
    private static PassagerOnVoiture selection(Population pop, PassagerOnVoiture PassagerOnVoiture) {
        PassagerOnVoiture newMember = new PassagerOnVoiture();
        newMember = tournamentSelection(pop);
        return newMember;
    }
 
    // Mutate a member
    private static void mutation(PassagerOnVoiture PassagerOnVoiture) {
        // Loop through passagers
        for (int i = 0; i < PassagerOnVoiture.nbVoitures(); i++) {
        	for (int j = 0; j < PassagerOnVoiture.nbPassagers(); j++){
        		if (Math.random() <= mutationRate) {
        			// Swap two random passagers
        			randomSwapPassagers(PassagerOnVoiture);
        		}
            }
        }
    }
    
    /**
     * Swap de façon aléatoire les passagers
     * @param PassagerOnVoiture
     */
    private static void randomSwapPassagers(PassagerOnVoiture PassagerOnVoiture){
		int passager1 = (int)(Math.random() * 20 + 1);
		int passager2 = (int)(Math.random() * 20 + 1);
		Passager passagerTmp = null;
		int[] coord1 = new int[2];
		int[] coord2 = new int[2];

        for (int i = 0; i < PassagerOnVoiture.nbVoitures(); i++) {
        	for (int j = 0; j < PassagerOnVoiture.nbPassagers(); j++){
        		if(PassagerOnVoiture.passagersOrdonnes[i][j].getId() == passager1){
        			coord1[0] = i;
        			coord1[1] = j;
        		}
        		if(PassagerOnVoiture.passagersOrdonnes[i][j].getId() == passager2){
        			coord2[0] = i;
        			coord2[1] = j;        		
        		}		
        	}
        }
        passagerTmp = PassagerOnVoiture.passagersOrdonnes[coord1[0]][coord1[1]];
        PassagerOnVoiture.passagersOrdonnes[coord1[0]][coord1[1]] = PassagerOnVoiture.passagersOrdonnes[coord2[0]][coord2[1]];
        PassagerOnVoiture.passagersOrdonnes[coord2[0]][coord2[1]] = passagerTmp;
    }
    
    /**
     * Création d'un battle royale entre plusieurs PassagerOnVoitures de la population
     * @param pop
     * @return PassagerOnVoiture gagnant !
     */
    private static PassagerOnVoiture tournamentSelection(Population pop) {
        Population tournament = new Population(tournamentSize, false);
        // Les participants sont tirés au sort
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.savePassagerOnVoiture(i, pop.getPassagerOnVoiture(randomId));
        }
        return tournament.getMoreCompetent();
    }
}