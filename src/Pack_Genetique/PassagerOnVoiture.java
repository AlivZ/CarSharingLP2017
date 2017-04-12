package Pack_Genetique;
import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 */
public class PassagerOnVoiture {
	
	/**
	 * Matrice de passagers
	 */
	Passager[][] passagersOrdonnes = new Passager[5][4];
	/**
	 * Matrice de point de passage <br>
	 * M(i,2*j)
	 */
	Point[][] pointsDePassage = new Point[5][8];
	private int competence = 0;
	
    /** 
     * Create a random passager group
     * @version Build III -  v0.0
	 * @since Build III -  v0.0
     */
    public void generatePassagerOnVoiture() {
    	//on repartit aléatoirement les 20 passagers dans les 5 voitures
    	
    	Passager[] listeDesPassagers= new Passager[20];
    	for(int i = 0; i < 20; i++)
    		listeDesPassagers[i] = Main.lesPassagers[i];
		Collections.shuffle(Arrays.asList(listeDesPassagers));
		int index = 0;
		for(int i = 0 ; i < 5 ; i++)
			for(int j = 0 ; j < 4 ; j++){
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
		Point[] listeDesPoints = new Point[8];
		int index;
		for (int i = 0; i < 5; i++){
			index = 0;
			for(int j = 0; j < 4; j++){
				listeDesPoints[index] = passagersOrdonnes[i][j].getDepart();
				index++;
			}
			for(int j = 0; j < 4; j++){
				listeDesPoints[index] = passagersOrdonnes[i][j].getArrivee();
				index++;
			}
			for(int j = 0; j < 8; j++){
				pointsDePassage[i][j] = listeDesPoints[j];
			}
		}
	}
 
    /* Getters and setters */

 
    public Passager getPassager(int voiture,int index) {
        return passagersOrdonnes[voiture][index];
    }
 
    public void setPassager(int voiture, int index, Passager passager) {
        passagersOrdonnes[voiture][index] = passager;
    }
 
    /* Public methods */
    public int nbPassagers() {
        return passagersOrdonnes[0].length;
    }
    public int nbVoitures() {
        return passagersOrdonnes.length;
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
 

    public void afficherPassagerOnVoitures() {
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0 ; j < 4 ; j++){
				System.out.print(passagersOrdonnes[i][j].getId() + " ");
			}
		System.out.println("");
		}
     }
    
    public void afficherPoints() {
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0 ; j < 8 ; j++){
				System.out.print(pointsDePassage[i][j].toString() + " ");
			}
		System.out.println("");
		}
     }
}