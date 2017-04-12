package Pack_Simu;

import java.util.ArrayList;

/**
 * Classe de l'Algorithme RecuitSimule.
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Algo_RecuitSimule implements I_Algorithme{
	
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
	
	/** on a deux compteurs qui évoluent au cours du temps et qui
	 * peuvent servir de condition d'arrêt du while : <br>
	 * le numero de l'étape en cours et une "température"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public Algo_RecuitSimule () {
		
		this.etape1 = 0;
		this.temperature1 = 1000;
	}
	/**
	 * Copie de la matrice courante
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private int[][] copieMatrice() {
		return this.simu.copyMatrix(this.matriceDePassage);
	}
	
	/**
	 * Selection de Client/Passager au hasard.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @return
	 */
	private int clientRandom() {
		return (int)Math.floor(this.clientAlgoNumber *Math.random());
	}
	
	/**
	 * Trouve la voiture qui le transporte.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param copy
	 * @param clientRandom
	 * @return
	 */
	private int getVoitureClient(int[][] copy, int clientRandom) {
		int car = 0;
		for(int k=0; k<carAlgoNumber; k++){
			if(copy[k][2*clientRandom+1]!=-1){
				car=k;
			}
		}
		return car;
	}
	
	/** 
	 * Fiesta.
	 */
	
	{
		// DEBUT DES ITERATIONS
		while (this.etape1 < this.simu.getStepMax() ){
			
			/* on commence par créer une copie de la matrice de passage,
			 * sur laquelle on va effectuer des modifications aléatoires,
			 * Ã  caractère élémentaire,
			 * pour ensuite la comparer avec la matrice courante
			 */
			
			int[][] copy = this.copieMatrice();
			int clientRandom = this.clientRandom();
			int car = this.getVoitureClient(copy,clientRandom);
			
			/* 2 cas dans l'état actuel des choses :
			 * soit le client est toujours sur le trottoir,
			 * soit il est dans une voiture
			 */
			
			if (clientRandom < clientWaitingNumber) {
				/* si le client est toujours sur le trottoir,
				 * l'idée est de le sortir de la matrice
				 * et de le réinjecter dedans à un endroit aléatoire
				 * mais néanmoins compatible avec occupantMax
				 */
				// on le retire de la matrice de passage
				int pos = copy[car][2*clientRandom];
				int target = copy[car][2*clientRandom+1];
				copy[car][2*clientRandom]=-1;
				copy[car][2*clientRandom+1]=-1;
				// il faut décaler les ordres de passage de la voiture pour combler les trous
				for (int q=0; q<2*clientAlgoNumber; q++){
					if(copy[car][q]>target){
						copy[car][q]=copy[car][q]-2;
					}
					else{
						if(copy[car][q]>pos){
							copy[car][q]=copy[car][q]-1;
						}
					}
				}
				// Ã  présent on réinjecte le client
				// on choisit au hasard la voiture qui va le transporter
				int quelleCar = (int)Math.floor(Math.random()*carAlgoNumber);
				/* on détermine p le nombre d'entiers positifs sur la ligne,
				 * ils vont de 0 à p-1
				 */
				int m = simu.nombreDeMoinsUn(copy[quelleCar]);
				int p = 2*clientAlgoNumber-m;
				/* on choisit un ordre de passage aléatoire pour
				 * la position de départ du client, sachant
				 * qu'il doit être compris entre 0 et p
				 * et doit respecter la condition occupantCapacity
				 */
				/* on commence donc par lister les indices entre 0 et p
				 * compatibles avec occupantCapacity
				 */
				ArrayList<Integer> compatiblePos = new ArrayList<Integer>();
				for (int x=0 ; x<=p ; x++){
					if (simu.passagers(copy[quelleCar],x)+carOccupantArray[quelleCar].length<simu.getOccupantCapacity()){
						compatiblePos.add(x);
					}
				}
				/* une fois la liste faite,
				 * il reste à y prendre un élément aléatoire
				 */
				int lenPos = compatiblePos.size();
				int randPos = compatiblePos.get((int)Math.floor(lenPos*Math.random()));
				/* on décale d'un cran les ordres de passage
				 * supérieurs Ã  randPos
				 */
				for(int q =0 ; q<2*clientAlgoNumber ; q++){
					if(copy[quelleCar][q]>=randPos){
						copy[quelleCar][q]=copy[quelleCar][q]+1;
					}
				}
				copy[quelleCar][2*clientRandom]=randPos;
				/* on choisit un ordre de passage aléatoire pour
				 * la position cible du client, sachant
				 * qu'il doit être compris entre randPos+1 et p+1
				 * et respecter la condition occupantCapacity
				 */
				/* Ã  partir de randPos+1 on liste donc tous les
				 * indices compatibles dans l'ordre croissant
				 * jusqu'à trouver un indice incompatible ou
				 * jusqu'à arriver à p+1
				 */
				boolean critere = true;
				ArrayList<Integer> compatibleTarget = new ArrayList<Integer>();
				for (int z = randPos+1 ; z <= p+1 ; z++){
					if (critere){
						if (simu.passagers(copy[quelleCar],z)+carOccupantArray[quelleCar].length
								<= simu.getOccupantCapacity()){
							compatibleTarget.add(z);
						}
						else {
							critere = false;
						}
					}
				}
				/* une fois la liste faite,
				 * il reste à y prendre un élément aléatoire
				 */
				int lenTarget = compatibleTarget.size();
				int randTarget = compatibleTarget.get((int)Math.floor(lenTarget*Math.random()));
				/* on décale d'un cran les ordres de passage
				 * supérieurs à randTarget
				 */
				for(int q =0 ; q<2*clientAlgoNumber ; q++){
					if(copy[quelleCar][q]>=randTarget){
						copy[quelleCar][q]=copy[quelleCar][q]+1;
					}
				}
				copy[quelleCar][2*clientRandom+1]=randTarget;
			}
			else {
				/* si le client est déjà dans une voiture
				 * (le cas où le client est arrivé n'apparaît pas car il
				 * a alors été sorti de clientArray),
				 * alors il ne bouge pas de voiture, on change simplement sa
				 * destination d'indice
				 */
				//on retire sa destination de la matrice de passage
				int target = copy[car][2*clientRandom+1];
				copy[car][2*clientRandom+1]=-1;
				// il faut décaler les ordres de passage de la voiture pour combler les trous
				for (int q=0; q<2*clientAlgoNumber; q++){
					if(copy[car][q]>target){
						copy[car][q]=copy[car][q]-1;
					}
				}
				// Ã  présent on réinjecte la destination
				/* on détermine p le nombre d'entiers positifs sur la ligne,
				 * ils vont de 0 à p-1
				 */
				int m = simu.nombreDeMoinsUn(copy[car]);
				int p = 2*clientAlgoNumber-m;
				/* on choisit un ordre de passage aléatoire pour
				 * la destination du client, sachant
				 * qu'il doit être compris entre 0 et p
				 * et respecter la condition occupantMax
				 */
				/* à partir de 0 on liste donc tous les
				 * indices compatibles dans l'ordre croissant
				 * jusqu'à trouver un indice incompatible ou
				 * jusqu'à arriver Ã  p
				 */
				boolean critere = true;
				ArrayList<Integer> compatibleTarget = new ArrayList<Integer>();
				for (int z = 0 ; z <= p ; z++){
					if (critere){
						if (simu.passagers(copy[car],z)+carOccupantArray[car].length<=simu.getOccupantCapacity()){
							compatibleTarget.add(z);
						}
						else {
							critere = false;
						}
					}
				}
				/* une fois la liste faite,
				 * il reste à y prendre un élément aléatoire
				 */
				int lenTarget = compatibleTarget.size();
				int randTarget = compatibleTarget.get((int)Math.floor(lenTarget*Math.random()));
				/* on dÃ©cale d'un cran les ordres de passage
				 * supérieurs à randTarget
				 */
				for(int q =0 ; q<2*clientAlgoNumber ; q++){
					if(copy[car][q]>=randTarget){
						copy[car][q]=copy[car][q]+1;
					}
				}
				copy[car][2*clientRandom+1]=randTarget;
			}
			int coutDeCopy = simu.cost(copy,carAlgoList,clientAlgoList);
			/* à présent vient le test comparatif de coût :
			 * si le nouveau coût est plus faible, on accepte la solution ;
			 * sinon on l'accepte avec une probabilité de
			 * exp(-(le module de la différence des coûts)/la température)
			 */
			if (coutDeCopy<=costMin){
				matriceDePassage=copy;
				costMin=coutDeCopy;
			}
			else{
				int diffDeCout = coutDeCopy-costMin;
				double r = Math.random();
				if (r<Math.exp(-((double)diffDeCout/temperature1))){
					matriceDePassage=copy;
					costMin=coutDeCopy;
				}
			}
			// pour finir on incrémente les compteurs
			etape1=etape1+1;
			this.temperature1=0.99*this.temperature1;
		}
	} 
}


//La Poubelle de l'algo :
/*
case 1:
{
	/* on a deux compteurs qui évoluent au cours du temps et qui
	 * peuvent servir de condition d'arrêt du while :
	 * le numero de l'étape en cours et une "température"
	 
	int etape = 0;
	double temperature = 1000;
	// DEBUT DES ITERATIONS
	while (etape < getStepMax() ){
		/* on commence par créer une copie de la matrice de passage,
		 * sur laquelle on va effectuer des modifications aléatoires,
		 * Ã  caractère élémentaire,
		 * pour ensuite la comparer avec la matrice courante
		 
		int[][] copy = copyMatrix(matriceDePassage);
		//on choisit un client au hasard
		int clientRandom = (int)Math.floor(clientAlgoNumber *Math.random());
		//on trouve quelle est la voiture qui le transporte
		int car = 0;
		for(int k=0; k<carAlgoNumber; k++){
			if(copy[k][2*clientRandom+1]!=-1){
				car=k;
			}
		}
		/* 2 cas dans l'état actuel des choses :
		 * soit le client est toujours sur le trottoir,
		 * soit il est dans une voiture
		 
		if (clientRandom < clientWaitingNumber) {
			/* si le client est toujours sur le trottoir,
			 * l'idée est de le sortir de la matrice
			 * et de le réinjecter dedans à un endroit aléatoire
			 * mais néanmoins compatible avec occupantMax
			 
			// on le retire de la matrice de passage
			int pos = copy[car][2*clientRandom];
			int target = copy[car][2*clientRandom+1];
			copy[car][2*clientRandom]=-1;
			copy[car][2*clientRandom+1]=-1;
			// il faut décaler les ordres de passage de la voiture pour combler les trous
			for (int q=0; q<2*clientAlgoNumber; q++){
				if(copy[car][q]>target){
					copy[car][q]=copy[car][q]-2;
				}
				else{
					if(copy[car][q]>pos){
						copy[car][q]=copy[car][q]-1;
					}
				}
			}
			// Ã  présent on réinjecte le client
			// on choisit au hasard la voiture qui va le transporter
			int quelleCar = (int)Math.floor(Math.random()*carAlgoNumber);
			/* on détermine p le nombre d'entiers positifs sur la ligne,
			 * ils vont de 0 à p-1
			 
			int m = nombreDeMoinsUn(copy[quelleCar]);
			int p = 2*clientAlgoNumber-m;
			/* on choisit un ordre de passage aléatoire pour
			 * la position de départ du client, sachant
			 * qu'il doit être compris entre 0 et p
			 * et doit respecter la condition occupantCapacity
			 */
			/* on commence donc par lister les indices entre 0 et p
			 * compatibles avec occupantCapacity
			 
			ArrayList<Integer> compatiblePos = new ArrayList<Integer>();
			for (int x=0 ; x<=p ; x++){
				if (passagers(copy[quelleCar],x)+carOccupantArray[quelleCar].length<getOccupantCapacity()){
					compatiblePos.add(x);
				}
			}
			/* une fois la liste faite,
			 * il reste à y prendre un élément aléatoire
			 
			int lenPos = compatiblePos.size();
			int randPos = compatiblePos.get((int)Math.floor(lenPos*Math.random()));
			/* on décale d'un cran les ordres de passage
			 * supérieurs Ã  randPos
			 
			for(int q =0 ; q<2*clientAlgoNumber ; q++){
				if(copy[quelleCar][q]>=randPos){
					copy[quelleCar][q]=copy[quelleCar][q]+1;
				}
			}
			copy[quelleCar][2*clientRandom]=randPos;
			/* on choisit un ordre de passage aléatoire pour
			 * la position cible du client, sachant
			 * qu'il doit être compris entre randPos+1 et p+1
			 * et respecter la condition occupantCapacity
			 */
			/* Ã  partir de randPos+1 on liste donc tous les
			 * indices compatibles dans l'ordre croissant
			 * jusqu'à trouver un indice incompatible ou
			 * jusqu'à arriver à p+1
			 
			boolean critere = true;
			ArrayList<Integer> compatibleTarget = new ArrayList<Integer>();
			for (int z = randPos+1 ; z <= p+1 ; z++){
				if (critere){
					if (passagers(copy[quelleCar],z)+carOccupantArray[quelleCar].length
							<= getOccupantCapacity()){
						compatibleTarget.add(z);
					}
					else {
						critere = false;
					}
				}
			}
			/* une fois la liste faite,
			 * il reste à y prendre un élément aléatoire
			 
			int lenTarget = compatibleTarget.size();
			int randTarget = compatibleTarget.get((int)Math.floor(lenTarget*Math.random()));
			/* on décale d'un cran les ordres de passage
			 * supérieurs à randTarget
			 
			for(int q =0 ; q<2*clientAlgoNumber ; q++){
				if(copy[quelleCar][q]>=randTarget){
					copy[quelleCar][q]=copy[quelleCar][q]+1;
				}
			}
			copy[quelleCar][2*clientRandom+1]=randTarget;
		}
		else {
			/* si le client est déjà dans une voiture
			 * (le cas où le client est arrivé n'apparaît pas car il
			 * a alors été sorti de clientArray),
			 * alors il ne bouge pas de voiture, on change simplement sa
			 * destination d'indice
			 
			//on retire sa destination de la matrice de passage
			int target = copy[car][2*clientRandom+1];
			copy[car][2*clientRandom+1]=-1;
			// il faut décaler les ordres de passage de la voiture pour combler les trous
			for (int q=0; q<2*clientAlgoNumber; q++){
				if(copy[car][q]>target){
					copy[car][q]=copy[car][q]-1;
				}
			}
			// Ã  présent on réinjecte la destination
			/* on détermine p le nombre d'entiers positifs sur la ligne,
			 * ils vont de 0 à p-1
			 
			int m = nombreDeMoinsUn(copy[car]);
			int p = 2*clientAlgoNumber-m;
			/* on choisit un ordre de passage aléatoire pour
			 * la destination du client, sachant
			 * qu'il doit être compris entre 0 et p
			 * et respecter la condition occupantMax
			 */
			/* à partir de 0 on liste donc tous les
			 * indices compatibles dans l'ordre croissant
			 * jusqu'à trouver un indice incompatible ou
			 * jusqu'à arriver Ã  p
			 
			boolean critere = true;
			ArrayList<Integer> compatibleTarget = new ArrayList<Integer>();
			for (int z = 0 ; z <= p ; z++){
				if (critere){
					if (passagers(copy[car],z)+carOccupantArray[car].length<=getOccupantCapacity()){
						compatibleTarget.add(z);
					}
					else {
						critere = false;
					}
				}
			}
			/* une fois la liste faite,
			 * il reste à y prendre un élément aléatoire
			 
			int lenTarget = compatibleTarget.size();
			int randTarget = compatibleTarget.get((int)Math.floor(lenTarget*Math.random()));
			/* on dÃ©cale d'un cran les ordres de passage
			 * supérieurs à randTarget
			 
			for(int q =0 ; q<2*clientAlgoNumber ; q++){
				if(copy[car][q]>=randTarget){
					copy[car][q]=copy[car][q]+1;
				}
			}
			copy[car][2*clientRandom+1]=randTarget;
		}
		int coutDeCopy = cost(copy,carAlgoList,clientAlgoList);
		/* à présent vient le test comparatif de coût :
		 * si le nouveau coût est plus faible, on accepte la solution ;
		 * sinon on l'accepte avec une probabilité de
		 * exp(-(le module de la différence des coûts)/la température)
		 
		if (coutDeCopy<=costMin){
			matriceDePassage=copy;
			costMin=coutDeCopy;
		}
		else{
			int diffDeCout = coutDeCopy-costMin;
			double r = Math.random();
			if (r<Math.exp(-((double)diffDeCout/temperature))){
				matriceDePassage=copy;
				costMin=coutDeCopy;
			}
		}
		// pour finir on incrémente les compteurs
		etape=etape+1;
		temperature=0.99*temperature;
	}
} break;
*/