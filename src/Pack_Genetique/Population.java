package Pack_Genetique;
/**
 * Classe Population
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Population {
 
	/** 
	 * Liste de passager dans des voitures
	 */
    PassagerOnVoiture[] PassagerOnVoitures;

    
    /**
     *  Create a population and PassagerOnVoiture inside
     * @param populationSize
     * @param initialise
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public Population(int populationSize, boolean initialise) {
        PassagerOnVoitures = new PassagerOnVoiture[populationSize];
        // Initialise population
        if (initialise) {
            for (int i = 0; i < populationSize; i++) {
            	PassagerOnVoiture newPassagerOnVoiture = new PassagerOnVoiture();
                newPassagerOnVoiture.generatePassagerOnVoiture();
                savePassagerOnVoiture(i, newPassagerOnVoiture);
            }
        }
    }
 
    /* Getters */
    public PassagerOnVoiture getPassagerOnVoiture(int index) {
        return PassagerOnVoitures[index];
    }
 
    public PassagerOnVoiture getMoreCompetent() {
    	PassagerOnVoiture moreCompetent = PassagerOnVoitures[0];
        // Loop through passagers to find more competent
        for (int i = 0; i < size(); i++) {
            if (moreCompetent.getDistanceChemin() >= getPassagerOnVoiture(i).getDistanceChemin()) {
                moreCompetent = getPassagerOnVoiture(i);
            }
        }
        return moreCompetent;
    }
 
    /**
     *  Get population size
     * @return
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public int size() {
        return PassagerOnVoitures.length;
    }
 
    /**
     *  Save passager
     * @param index
     * @param PassagerOnVoiture
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public void savePassagerOnVoiture(int index, PassagerOnVoiture PassagerOnVoiture) {
    	PassagerOnVoitures[index] = new PassagerOnVoiture();
    	for( int i = 0 ; i < PassagerOnVoiture.nbVoitures() ; i++){
        	for( int j = 0 ; j < PassagerOnVoiture.nbPassagers() ; j++){
    		PassagerOnVoitures[index].passagersOrdonnes[i][j] =  PassagerOnVoiture.passagersOrdonnes[i][j];
        	}
    	}
    	PassagerOnVoitures[index].attribuerPointsDePassage();
    }
}