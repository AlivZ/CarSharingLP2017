package Pack_Simu;

import java.util.ArrayList;

/**
 * Classe de l'Algorithme RecuitSimule.
 * 
 * @author Romain Duret
 * @version Build III -  v0.3
 * @since Build III -  v0.0
 */
public class Algo_RecuitSimule {
	
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/
	
	
	int clientWaitingNumber;
	int carAlgoNumber;
	ArrayList<Car> carAlgoList;
	int [][] carOccupantArray;
	ArrayList<Client> clientAlgoList;
	int clientAlgoNumber;
	int costMin;
	int [][] matriceDePassage;
	Simulation simu;
	int etape1;
	double temperature1;
	
	/**
	 * Valeur utilisée
	 */
	private int car;
	
	
	 /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	  */
	
	
	/** on a deux compteurs qui évoluent au cours du temps et qui
	 * peuvent servir de condition d'arrêt du while : <br>
	 * le numero de l'étape en cours et une "température"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public Algo_RecuitSimule(int costMin, int clientWaitingNumber, int clientAlgoNumber, int carAlgoNumber, int[][]matriceDePassage , int[][] carOccupantArray, ArrayList<Car> carAlgoList, ArrayList<Client> clientAlgoList, Simulation simu) {
		this.costMin = costMin;
		this.clientWaitingNumber = clientWaitingNumber;
		this.clientAlgoNumber = clientAlgoNumber;
		this.simu = simu;
		this.carAlgoNumber = carAlgoNumber;
		this.carOccupantArray = carOccupantArray;
		this.matriceDePassage = this.simu.copyMatrix(matriceDePassage);;
		this.clientAlgoList = clientAlgoList;
		this.carAlgoList = carAlgoList;
		this.etape1 = 0;
		this.temperature1 = 1000;
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
	 * On retire le client de la matrice de passage et on décale l'ordre pour combler les trous.
	 * @param copy
	 * @param clientRandom
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] decalerOrdrePassageClient(int[][] copy, int clientRandom) {
		int pos = copy[car][2*clientRandom];
		int target = copy[car][2*clientRandom+1];
		copy[car][2*clientRandom]=-1;
		copy[car][2*clientRandom+1]=-1;
		for (int q=0; q<2*this.clientAlgoNumber; q++){
			if(copy[car][q]>target){
				copy[car][q]=copy[car][q]-2;
			}
			else{
				if(copy[car][q]>pos){
					copy[car][q]=copy[car][q]-1;
				}
			}
		}
		return copy;
	}
	/**
	 * On retire la destination de la matrice de passage et on décale l'ordre pour combler les trous.
	 * @param copy
	 * @param clientRandom
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] decalerOrdrePassageDestination(int[][] copy, int clientRandom) {
		int target = copy[this.car][2*clientRandom+1];
		copy[this.car][2*clientRandom+1]=-1;
		copy = decalerOrdrePassagerAvecLimite(copy, this.car, target-1);
		return copy;
	}
	/**
	 * Donne ordre de Passage aléatoire pour la position de départ du client. <br>
	 * Doit être entre 0 et le nombre d'entier positif sur la ligne et respecter la condition occupantCapctiy.
	 * @param copy
	 * @param quelleCar
	 * @param clientRandom
	 * @return
	 * 
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] ordrePassageAleatoire(int[][] copy, int clientRandom) {
		
		int quelleCar = (int)Math.floor(Math.random()*this.carAlgoNumber); //voiture aléatoire qui va transporter le client.
		int p = 2*this.clientAlgoNumber-this.simu.nombreDeMoinsUn(copy[quelleCar]); //Nombre d'entier positif sur la ligne

		ArrayList<Integer> compatiblePos = getListPositionCompatible(copy, quelleCar, p);
		
		int randPos = compatiblePos.get((int)Math.floor(compatiblePos.size()*Math.random())); //On prends un élément aléatoire de la liste
		copy = this.decalerOrdrePassagerAvecLimite(copy, quelleCar, randPos);//Décale l'ordre de passage après l'élément.
		
		copy[quelleCar][2*clientRandom]=randPos;
		
		copy = this.ordrePassageAleatoireEtape2(copy, quelleCar, p+1, clientRandom, randPos+1);
		return copy;
		
	}
	
	/**
	 * Donne ordre de Passage aléatoire pour la position de départ du client. <br>
	 * Doit être entre randPos+1 et p+1 et respecter la condition occupantCapctiy.
	 * @param copy
	 * @param quelleCar
	 * @param clientRandom
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] ordrePassageAleatoireEtape2(int[][] copy, int quelleCar, int p, int clientRandom, int randPos) {
		ArrayList<Integer> compatibleTarget = this.getListeIndiceCompatible(copy, quelleCar, randPos, p); //Liste des Indices Compatibles

		int randTarget = compatibleTarget.get((int)Math.floor(compatibleTarget.size()*Math.random())); //Prend un élément aléatoire
		
		copy = this.decalerOrdrePassagerAvecLimite(copy, quelleCar, randTarget); //Décale l'ordre de passage avant l'élément.
		copy[quelleCar][2*clientRandom+1]=randTarget;
		
		return copy;
	}
	/**
	 * Décale l'ordre des passagers selon la limite donnée.
	 * @param copy
	 * @param quelleCar
	 * @param limite
	 * @param clientRandom
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] decalerOrdrePassagerAvecLimite(int[][] copy, int quelleCar, int limite) {
		for(int q =0 ; q<2*this.clientAlgoNumber ; q++){
			if(copy[quelleCar][q]>=limite){
				copy[quelleCar][q]=copy[quelleCar][q]+1;
			}
		}
		return copy;
	}
	
	
	/**
	 * Test de comparatif de coût. <br>
	 * Si le nouveau est moin couteux, on le garde, sinon, on l'accepte selon : <br>
	 * <i>exp(-(le module de la différence des coûts)/la température)</i>
	 * @param copy
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private void testComparatifCout(int[][] copy) {
		int coutDeCopy = this.simu.cost(copy,this.carAlgoList,this.clientAlgoList);
		if (coutDeCopy<=this.costMin){
			this.matriceDePassage=copy;
			this.costMin=coutDeCopy;
		}
		else{
			int diffDeCout = coutDeCopy-this.costMin;
			double r = Math.random();
			if (r<Math.exp(-((double)diffDeCout/this.temperature1))){
				this.matriceDePassage=copy;
				this.costMin=coutDeCopy;
			}
		}
	}
	/**
	 * Lance l'execution de l'algorithme
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	public int[][] launch() {
		
		while (this.etape1 < this.simu.getStepMax() ){ // DEBUT DES ITERATIONS
			
			int clientRandom = (int)Math.floor(this.clientAlgoNumber *Math.random());
			int[][] copy = this.getCopy(clientRandom);
			
			//Selon l'état du client :
			if (clientRandom < this.clientWaitingNumber) {  //Si le client est sur le trottoir :

				// Sorti d'un client pour le réinjecter dans un endroit aléatoire et compatible avec occupantMax :
				copy = this.decalerOrdrePassageClient(copy, clientRandom); //On retire le client
				
				//On l'injecte :
				copy = this.ordrePassageAleatoire(copy, clientRandom);
			}
			else { //Si le client est déjà dans une voiture, on change simplement sa destination d'indice.

				//Sorti d'une destination :
				copy = this.decalerOrdrePassageDestination(copy, clientRandom);
				
				//On réinjecte la destination : (p nombre d'entier positif sur la ligne)
				int p = 2*this.clientAlgoNumber-this.simu.nombreDeMoinsUn(copy[this.car]);
				copy = this.ordrePassageAleatoireEtape2(copy, this.car, p, clientRandom, 0);

			}
			
			this.testComparatifCout(copy);
			
			// pour finir on incrémente les compteurs
			this.etape1=this.etape1+1;
			this.temperature1=0.99*this.temperature1;
		}
		this.prompt(this.matriceDePassage);
		return this.matriceDePassage;
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
	 * @param t
	 */
	private void prompt(int[][] t) {
		for(int i=0;i<t.length;i++) {
			System.out.print("[");
			for(int j=0;j<t[i].length;j++) {
				System.out.print("["+t[i][j]+"]");
			}
			System.out.println("]");
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
	
	/**
	 * Génère une copie de la matrice de passage. <br>
	 * On y génère des modifications aléatoires à caractère "élémentaires" pour comparer
	 * @param clientRandom
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private int[][] getCopy(int clientRandom) {
		int[][] copy =  this.simu.copyMatrix(this.matriceDePassage);
		this.car = 0;
		for(int k=0; k<this.carAlgoNumber; k++){
			//if((2*clientRandom+1)<=copy[k].length) {
				if(copy[k][2*clientRandom+1]!=-1){
					this.car=k;
				}
			//}
		}
		return copy;
	}
	
	
	/**
	 * Renvoie la liste des Positions compatibles avec OccupantCapacity
	 * @param copy
	 * @param quelleCar
	 * @param p
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private ArrayList<Integer> getListPositionCompatible(int[][] copy, int quelleCar, int p) {
		ArrayList<Integer> compatiblePos = new ArrayList<Integer>();
		for (int x=0 ; x<=p ; x++){
			if (this.simu.passagers(copy[quelleCar],x)+this.carOccupantArray[quelleCar].length<this.simu.getOccupantCapacity()){
				compatiblePos.add(x);
			}
		}
		return compatiblePos;
	}
	
	/**
	 * à  partir de randPos+1 on liste tous les indices compatibles dans l'ordre croissant
	 * jusqu'à trouver un indice incompatible ou jusqu'à arriver à p+1
	 * @return
	 * @version Build III -  v0.3
	 * @since Build III -  v0.3
	 */
	private ArrayList<Integer> getListeIndiceCompatible(int[][] copy, int quelleCar, int randPos, int p) {
		ArrayList<Integer> compatible = new ArrayList<Integer>();
		boolean critere = true;
		for (int z = randPos ; z <= p ; z++){
			if (critere){
				if (this.simu.passagers(copy[quelleCar],z)+this.carOccupantArray[quelleCar].length
						<= this.simu.getOccupantCapacity()){
					compatible.add(z);
				}
				else {
					critere = false;
				}
			}
		}
		return compatible;
	}
}