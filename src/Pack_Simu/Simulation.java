package Pack_Simu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.awt.Point;

/**
 * Classe simulation
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 *
 */
public class Simulation{

	private int time;
	private boolean needAlgorithme;
	private ArrayList<Car> ListeVoitures; 
	private ArrayList<Client> ListeClients;
	private int nbVoituresSimulation;
	private int nbClientsSimulation;
	private Client notTargettedYet;
	private Car carSimulation;
	private Client selectedClient;
	private double carSpeedMean;
	double clientSpeedSum;
	int arrivedClient;
	private double clientSpeedMean; //Simulation
	private double clientRealSpeedMean; //Simulation
	private double arrivedRate;
	private int distSum;
	private int carbu;
	private Model model;
	private City city;
	private int occupantMax;
	
	/**
	 * Algorithme sélectionné
	 */
	private int algoId;
	/**
	 * Numero de la fonction de coût sélectionnée
	 */
	private int costRate;
	/**
	 * booléen indiquant si le crible diviser pour mieux régner doit être effectué
	 */
	private boolean divide;
	/**
	 * Nombre d'étapes maximum sélectionné
	 */
	private int stepMax;
	/**
	 * Nombre de passagers maximum
	 */
	private int occupantCapacity;
	
	/**
	 * "Tableau" d'itineraires <br>
	 * Le tableau contient 3 couches : <br>
	 * <ol>
	 * <li> La première couche contient la liste des véhicules qui se balandent. </li>
	 * <li> La deuxième couche contient soit un 0 soit un 1. </li>
	 * <li> itinairaires[0][0] renvoi une liste de client. </li>
	 * <li> itinairaires[0][1] renvoi la liste des points dans l'ordre </li>
	 * </ol>
	 * 
	 * La voiture va passer par les points dans l'ordre du tableau. (puis "incrémentation" Itinairaires[0][1][0] = Itinairaires[0][1][1])
	 */
	private Object[][] itineraires;
	
	/**
	 * Nombre de client en attente.
	 */
	private int clientWaitingNumber;

	/**
	 * Initialisation des variables
	 * @param cLength
	 * @param sLength
	 * @param width
	 * @param height
	 */
	public Simulation(int cLength, int sLength, int width, int height) {
		this.model = new Model(cLength, sLength);
		this.city = new City(width, height, this.model);
		
		this.needAlgorithme = false;
		
		this.ListeVoitures = new ArrayList<Car>();
		this.ListeClients = new ArrayList<Client>();
		
		this.nbVoituresSimulation = 0;
		this.nbClientsSimulation = 0;
		this.time = 0;
		this.arrivedClient = 0;
		this.arrivedRate = 0;
		this.distSum = 0;
		this.carbu = 0;
		this.notTargettedYet = null;
		this.carSimulation = null;
		this.selectedClient = null;
	}
	
	/**
	 * Ajout d'une voiture dans la simulation.
	 * @param CoordX
	 * @param CoordY
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void ajouterVoitureSimulation (int CoordX, int CoordY) {
		this.ListeVoitures.add(new Car(CoordX,CoordY));
		nbVoituresSimulation++;
		this.carSimulation = (this.ListeVoitures.get(nbVoituresSimulation-1));
		this.needAlgorithme = true;
	}

	/**
	 * Ajout d'un client dans la simulation <br>
	 * <h3> Deux cas possible </h3>
	 * Soit le client n'as pas encore de destination, auquel cas on rajoute une destination <br>
	 * Soit le client a déjà une destination donc on créer un client.
	 * @param x
	 * @param y
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void ajouterClientSimulation(int x,int y) {
		if(this.notTargettedYet != null){
			this.newDestination(x, y);
		} else {
				this.newClient(x, y);
			} 
	}
	
	/**
	 * Créer un nouveau client
	 * @param x
	 * @param y
	 * @version Build III -  v0.1
	 * @since Build III -  v0.1
	 */
	private void newClient(int x, int y) {
		this.needAlgorithme = true;
		this.notTargettedYet = new Client(this.time,x,y);
	}
	
	/**
	 * Créer la distination d'un client
	 * @param x
	 * @param y
	 * @version Build III -  v0.1
	 * @since Build III -  v0.1
	 */
	private void newDestination(int x, int y) {
		this.notTargettedYet.getPosClient()[1] = new Point(x, y);
		this.ListeClients.add(this.notTargettedYet);
		this.nbClientsSimulation++;
		this.selectedClient = this.notTargettedYet;
		this.notTargettedYet = null;
		if(dist(this.selectedClient.getPosClient()[0],this.selectedClient.getPosClient()[1])==0) {
			deleteClient();
		}  else {
			this.needAlgorithme = true;
		}
	}
	
	/** 
	 * Suppression d'une voiture.
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void deleteCar(){
		this.ListeVoitures.remove(this.carSimulation);
		this.nbVoituresSimulation--;
		this.carSimulation = null;
		this.needAlgorithme = true;
	}
	
	/**
	 * Suppression d'un passager
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void deleteClient(){
		if(this.selectedClient == this.notTargettedYet){
			this.notTargettedYet = null;
		} else {
			if(this.selectedClient.getStateClient() == 1){
				this.selectedClient.getCarClient().getOccupantListCar().remove(this.selectedClient);
			}
			this.ListeClients.remove(this.selectedClient);
			this.nbClientsSimulation--;
		}
		this.selectedClient = null;
		this.needAlgorithme = true;
	}

	/**
	 * Renvoie un tableau contenant les coordonnées des voitures
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public int[] getCoordoneeDesVoitures()
	{
		int[] t = new int[nbVoituresSimulation*2];
		for(int k=0;k<nbVoituresSimulation;k++)
		{
			t[2*k]=(int) this.ListeVoitures.get(k).getPosCar().getX();
			t[2*k+1]=(int) this.ListeVoitures.get(k).getPosCar().getY();
		}
		return t;
	}

	/**
	 * Renvoie un tableau contenant les coordonnées des clients
	 * @return
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public int[] getCoordoneeDesClients() {
		int[] coord = new int[this.nbClientsSimulation*4 + 2*((this.notTargettedYet!=null)?1:0)];
		for(int l=0;l<this.nbClientsSimulation;l++) {
			coord[4*l]=(int) this.ListeClients.get(l).getPosClient()[0].getX();
			coord[4*l+1]=(int) this.ListeClients.get(l).getPosClient()[0].getY();
			coord[4*l+2]=(int) this.ListeClients.get(l).getPosClient()[1].getX();
			coord[4*l+3]=(int) this.ListeClients.get(l).getPosClient()[1].getY();
		}
		if(this.notTargettedYet!=null) {
			coord[4*nbClientsSimulation]=(int) this.notTargettedYet.getPosClient()[0].getX();
			coord[4*nbClientsSimulation+1]=(int) this.notTargettedYet.getPosClient()[0].getY();	
		}
		return coord;
	}

	
	

	/**
<<<<<<< HEAD
	 * On ne sélectionne que les voitures qui participent au covoiturage
	 * @return 
	 * @version Build III -  v0.1
	 * @since Build III -  v0.1
=======
	 * Fonction algorithme déterminant le parcours le moins couteux
	 * @deprecated Use executeAlgo instead
>>>>>>> refs/remotes/origin/Implementation_Algorithme
	 */
	private ArrayList<Car> getVoitureCovoiturage() {
		ArrayList<Car> carAlgoList = new ArrayList<Car>();
		for(int i=0;i<this.ListeVoitures.size();i++) {
			if(this.ListeVoitures.get(i).isDoingCarSharing) carAlgoList.add(this.ListeVoitures.get(i));
		}
		return carAlgoList;
	}

	/**
	 * Cherche le nombre de client à prendre. <br>
	 * Et créer la liste des clients à prendre en compte. <br>
	 * Les clients embarqués sont ajoutés en fin de liste, les autres en début.
	 * @return
	 */
	private ArrayList<Client> getClientAPrendre() {
		ArrayList<Client> clientAlgoList = new ArrayList<Client>();
		for(Client cli: this.ListeClients){
			if(cli.getStateClient() == 0 && cli.getIsUsingCarSharing()){
				this.clientWaitingNumber++;
				clientAlgoList.add(0,cli);
			}
			else if(cli.getStateClient() == 1 && cli.getIsUsingCarSharing())
				clientAlgoList.add(cli);
		}		
		return clientAlgoList;
	}
	/**
	 * Fonction qui configure l'execution de l'Algorithme.
	 * <br>
	 * //1. DEFINITION DU CADRE D'ETUDE
	 * //2. RECHERCHE DE LA MATRICE DE PASSAGE MINIMISANT LE COUT
	 * //3. CONFIGURATION DES PARCOURS DES VOITURES
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	
	public void executeAlgorithme(){
		
		this.clientWaitingNumber = 0;
		
		ArrayList<Car> carAlgoList = this.getVoitureCovoiturage();
		ArrayList<Client> clientAlgoList = this.getClientAPrendre();
		
		int[][] matriceDePassage = searchMatriceDePassage(carAlgoList,clientAlgoList,clientWaitingNumber);

		setParcours(matriceDePassage,carAlgoList,clientAlgoList);
	
		this.needAlgorithme = false;
	}
	
	public void executeAlgo() {
		switch (this.algoId) {
		case 0: //déterministe
			break;
		case 1: //RecuitSimule
			break;
		case 2: //Génétique
			break;
		}
	}
	
	/**
	 * fonction déterminant la matrice de passage la moins couteuse <br><br>
	 * Une matrice de passage est définie de la manière suivante : <br>
	 * taille : carNumber lignes, 2*clientAlgoNumber colonnes <br>
	 * dans la case (k,2*l) : l'ordre de passage de la voiture k quand elle prend le client l <br>
	 * dans la case (k,2*l+1) : l'ordre de passage de la voiture k quand elle depose le client l <br>
	 * si la voiture k ne prend ni ne depose le client l, -1 dans les cases (k,2*l) et (k,2*l+1) <br>
	 * <i>Exemple : </i> <br>
	 * voiture 0 : prend 1, prend 0, depose 0, depose 1 <br>
	 * voiture 1 : prend 2, depose 2 <br>
	 * matriceDePassage : <br>
	 * | 1| 2| 0| 3|-1|-1| <br>
	 * |-1|-1|-1|-1| 0| 1| <br>
	 * 
	 * @param carAlgoList
	 * @param clientAlgoList
	 * @param clientWaitingNumber
	 * @return
	 */
	private int[][] searchMatriceDePassage(ArrayList<Car> carAlgoList,
			ArrayList<Client> clientAlgoList, int clientWaitingNumber)
	{
		this.occupantMax = 0;
		int carAlgoNumber = carAlgoList.size(); //Le nombre de voiture de l'alogrithme
		int clientAlgoNumber = clientAlgoList.size(); //Le nombre de clients de l'alogrithme
		//S'il n'y a pas de voiture ou pas de client dans le cadre d'Ã©tude, on ne lance pas l'algorithme
		if(carAlgoNumber == 0 || clientAlgoNumber == 0) return new int[0][0];
		
		int[][] matriceDePassage = this.creationMatriceInitiale(carAlgoList, clientAlgoList, carAlgoNumber, clientAlgoNumber);
		
		//On cherchera le coût minimum des matrices de passages créées
		int costMin = cost(matriceDePassage,carAlgoList,clientAlgoList);

		/*
        Exemple : clientWaitingNumber = 3, deux occupants dans les voitures 0 et 1
        [ 0, 1, 2, 3, 4, 5,-1,-1,-1, 6]
		[-1,-1,-1,-1,-1,-1,-1, 0,-1,-1]
		Les six premiers indices correspondent aux trois clients sur le trottoir
		Les quatre derniers aux deux clients respectivement dans les voitures 1 et 0
		 */

		//On effectue une disjonction de cas selon l'algorithme sélectionné
		switch(this.algoId) {
			case 0: //déterministe
				Algo_Deterministe algo = new Algo_Deterministe(costMin, clientWaitingNumber, clientAlgoNumber, carAlgoNumber,matriceDePassage, carAlgoList, clientAlgoList, this);
				matriceDePassage = algo.launch();
				
				break;
			case 1: //RecuitSimule
				break;
			case 2: //Génétique
				break;
		
		}
		/*
		//On affiche la matrice de passage dans la console ainsi que son coût
		System.out.println("Matrice de passage :");
		printMatrix(matriceDePassage);
		System.out.println("cost : "+costMin);
		*/
		return(matriceDePassage);
	}


	/**
	 * Création de la matrice initiale. <br>
	 * 
	 * @param carAlgoList
	 * @param clientAlgoList
	 * @param carAlgoNumber
	 * @param clientAlgoNumber
	 * @return
	 */
	private int[][] creationMatriceInitiale(ArrayList<Car> carAlgoList,
			ArrayList<Client> clientAlgoList, int carAlgoNumber, int clientAlgoNumber) {
		//On crée dans le même temps le tableau des occupants de la voiture renumérotés
		int[][] matriceDePassage = new int[carAlgoNumber][2*clientAlgoNumber];
		int[][] carOccupantArray = new int[carAlgoNumber][]; 
		
		this.occupantMax = 0;
		
		for(int k = 0; k<carAlgoNumber;k++){ //et on cherche le plus grand nombre d'occupants
			ArrayList<Client> occupantList = carAlgoList.get(k).getOccupantListCar();
			int occupantNumber = occupantList.size();
			for(int q=0;q<2*clientAlgoNumber;q++)
				//Les clients sur le trottoir sont rajoutés au début du parcours de la voiture 0
				//On remplit le reste de la matrice avec des -1
			matriceDePassage[k][q]=(k==0 && q<2*clientWaitingNumber)?q+occupantNumber:-1;
			this.occupantMax = Math.max(this.occupantMax,occupantNumber);
			carOccupantArray[k] = new int[occupantNumber];
			for(int z = 0; z<occupantNumber; z++){
				//On récupére le numero du client dans le reférentiel de l'algorithme
				int lAlgo = clientAlgoList.indexOf((Client)occupantList.get(z));
				//On l'ajoute dans la liste des occupants
				carOccupantArray[k][z] = lAlgo;
				//On l'ajoute dans le parcours de la voiture, à la suite des clients à prendre (car n°0)
				matriceDePassage[k][2*lAlgo+1] = z;
			}
		}
		return matriceDePassage;
	}

	/** la fonction suivante détermine, pour un indice d'un point,
	 * le nombre de clients dans la voiture juste avant d'atteindre ce point
	 */
	public int passagers ( int[] t, int indice) {
		int pass =0 ;
		int clientNumber = t.length/2;
		/*for (int l=0 ; l< clientNumber ; l++){
			if ((t[2*l]==-1)&&(t[2*l+1]>-1)){
				pass+=1;
			}
		}*/
		for (int x=0 ; x<indice ; x++ ){
			for (int q=0; q<2*clientNumber ; q++){
				if ((t[q]==x)&&(q%2==0)){
					pass+=1;
				}
				else {
					if (t[q]==x){
						pass-=1;
					}
				}
			}
		}
		return pass;
	}
	
	/** nombreDeMoinsUn détermine le nombre de cases
	 * qui sont des -1 dans un tableau d'entiers
	 */
	public int nombreDeMoinsUn(int[] t){
		int compteur = 0;
		for(int z = 0 ; z < t.length ; z++)
			if(t[z]==-1) compteur++;
		return compteur;
	}

	/** copyMatrix copie une matrice dans une nouvelle matrice
	 * 
	 * @param m
	 * @return
	 */
	public int[][] copyMatrix(int[][] m){
		int n = m.length;
		int[][] mat= new int[n][m[0].length];
		for(int x=0;x<n;x++)
			mat[x]=m[x].clone();
		return mat;
	}

	/** printMatrix affiche une matrice dans la console
	 * 
	 * @param m
	 */
	public void printMatrix(int[][] m){
		System.out.print("[");
		for(int x=0;x<m.length;x++)
			System.out.println(Arrays.toString(m[x]));
	}

	/**
	 * il s'agit de la distance en norme 1, nous sommes à Manhattan
	 * @param p1
	 * @param p2
	 * @return
	 */
	public int dist(Point p1, Point p2)
	{
		return (int) (Math.abs(p1.getX()-p2.getX()) + Math.abs(p1.getY()-p2.getY()));
	}

	/**
	 * recherche d'index d'une valeur dans un tableau
	 * @param a
	 * @param t
	 * @return
	 */
	public int searchInArray(int a, int[] t)
	{
		for(int z = 0; z<t.length; z++)
			if(t[z] == a) return z;
		return -1;
	}

	/**
	 * Cette fonction calcule le cout d'un parcours défini par une matrice de passage
	 * @param matriceDePassage
	 * @param carAlgoList
	 * @param clientAlgoList
	 * @return
	 */
	public int cost(int[][] matriceDePassage, ArrayList<Car> carAlgoList, ArrayList<Client> clientAlgoList)
	{
		int carAlgoNumber = carAlgoList.size();
		int cost = 0;
		for(int k=0;k<carAlgoNumber;k++)
		{
			int clientCost = 0;
			int bestDistSum = 0;
			int carDist = 0;
			//rank est le numéro d'ordre de l'étape de la voiture
			int rank = 0;
			Point p0 = null;
			int q1;
			//On cherche la rankiême étape
			while((q1 = searchInArray(rank,matriceDePassage[k]))!=-1)
			{
				//On récupêre le point correspondant à q1
				Point p1 = clientAlgoList.get(q1/2).getPosClient()[q1%2];
				//On ajoute la distance entre la voiture et p1 ou entre p0 et p1 si p0 non null
				carDist += dist((p0==null)?carAlgoList.get(k).getPosCar():p0, p1);
				if(q1%2==1){
					int bestDist = dist(clientAlgoList.get(q1/2).getPosClient()[0],clientAlgoList.get(q1/2).getPosClient()[1]);
					int realDist =
							((this.time-clientAlgoList.get(q1/2).appearanceMoment)*this.model.getCarLength()+carDist);
					clientCost += realDist * realDist /(bestDist*bestDist);
					bestDistSum += bestDist;
				}
				//On passe à l'étape suivante
				rank++;
				p0 = p1;
			}
			if(bestDistSum != 0) cost += this.costRate*clientCost
					+ (100-this.costRate)*carDist*carDist/(bestDistSum*bestDistSum);
		}
		return cost;
	}

	/**
	 * Cette fonction remplit les listes parcoursList des voitures à partir d'une matriceDePassage
	 
	* Logiquement cette fonction est appelée lorsqu'on a trouvé la matriceDePassage qui minimise le cout
	* */
	
	public void setParcours(int[][] matriceDePassage,ArrayList<Car> carAlgoList, ArrayList<Client> clientAlgoList)
	{
		int carAlgoNumber = carAlgoList.size();
		for(int k=0; k<carAlgoNumber; k++)
		{
			//On réinitialise le parcours
			carAlgoList.get(k).setParcoursList(new ArrayList<ParcoursStep>());
			int rank = 0;
			int q;
			//On cherche la stepNumiême étape dans la matrice de passage
			while((q = searchInArray(rank,matriceDePassage[k]))!=-1)
			{
				//on ajoute l'étape au parcours de la voiture
				carAlgoList.get(k).getParcoursListCar().add(new ParcoursStep(clientAlgoList.get(q/2), q%2));
				rank++;
			}
		}
	}

	/**
	 * avance les Car d'une case vers leur prochaine étape
	 */
	public void OneMove() {
		//On incrémente le temps de la simulation
		this.time = this.time+1;
		//Nombre de voitures en circulation
		int activeCar = 0;
		//Somme des vitesses des voitures
		int speedSum = 0;
		//Somme des vitesses instantanées des clients
		double clientRealSpeedSum = 0;
		for(Iterator<Car> it= this.ListeVoitures.iterator();it.hasNext();)
		{
			Car car = it.next();
			//Si la voiture n'a pas terminé son parcours
			if(!car.getParcoursListCar().isEmpty()){
				activeCar++;
				int X = (int) car.getPosCar().getX();
				int Y = (int) car.getPosCar().getY();
				ParcoursStep step = car.getParcoursListCar().get(0);
				Point p = step.cli.getPosClient()[step.type];
				//Par défaut la voiture est de vitesse la moitié de sa longueur par unité de temps
				int v = this.model.getCarLength()/2;
				//Si la voiture se trouve dans une rue
				if(car.streetId != -1)
					v = v-v*2*this.model.getCarLength()*(-1+
							//On calcule et on dÃ©crÃ©mente le nombre de voitures dans la rue
					this.city.getStreetArray()[(car.streetId/2)%this.city.getCityWidth()][(car.streetId/2)/this.city.getCityWidth()][car.streetId%2]--
					)/this.model.getStreetLength();
				//Si la vitesse est nulle, on donne une vitesse de 1 px à une probabilité de 20%
				if(v<=0) v=(Math.random()>0.8)?1:0;
				//On incrémente les données de simulations
				speedSum += v;
				//this.distSum = this.distSum + v;
				//this.carbu = this.carbu + ((v== 0 || v == 1)?this.model.getCarLength()/2:v);
				if(X < p.getX()) car.getPosCar().setLocation((X+v>p.getX())?p.getX():X+v,Y);
				else if(X > p.getX()) car.getPosCar().setLocation((X-v<p.getX())?p.getX():X-v,Y);
				else if(Y < p.getY()) car.getPosCar().setLocation(X,(Y+v>p.getY())?p.getY():Y+v);
				else if(Y > p.getY()) car.getPosCar().setLocation(X,(Y-v<p.getX())?p.getY():Y-v);
				else {
					//Si la voiture est arrivée à une étape du parcours
					//On change l'état du client embarqué ou déposé
					step.cli.setStateClient(step.cli.getStateClient() + 1);
					//On l'ajoute à la liste des occupants s'il est embarqué
					if(step.cli.getStateClient() == 1){
						car.getOccupantListCar().add(step.cli);
						step.cli.setCarClient(car);
					}
					//Sinon on l'y enlève
					else{
						arrivedClient++;
						clientSpeedSum += (double)dist(step.cli.getPosClient()[0],step.cli.getPosClient()[1]) 
								/(double)(this.time-step.cli.appearanceMoment);
						car.getOccupantListCar().remove(step.cli);
						this.ListeClients.remove(step.cli);
						this.nbClientsSimulation--;
					}
					//On supprime l'étape du parcours de la voiture
					car.getParcoursListCar().remove(0);
				}
				//Si la voiture est toujours en circulation, on calcule le numéro de sa rue
				if(!car.getParcoursListCar().isEmpty()){
					X = (int) (car.getPosCar().getX()/this.model.getStreetLength());
					Y = (int) (car.getPosCar().getY()/this.model.getStreetLength());
					if(car.getPosCar().getX() % this.model.getStreetLength() == 0 && car.getPosCar().getY() % this.model.getStreetLength() == 0)
						car.streetId = -1;
					else car.streetId = 2*Y*this.city.getCityWidth()+2*X+((car.getPosCar().getX() % this.model.getStreetLength() == 0)?0:1);
					//On incrémente le nombre de voitures dans la rue de la voiture
					if(car.streetId != -1) {
						this.city.getStreetArray()[X][Y][car.streetId%2]++;
					}
				//On supprime la voiture si elle ne fait pas de covoiturage
				else if(!car.getIsDoingCarSharing()){
					it.remove();
					this.nbVoituresSimulation--;
				}
			}
		}
		//CALCUL DES VITESSES MOYENNES
		this.model.setCarSpeedMean((activeCar!=0)?((double) speedSum / (double) activeCar):0);
		this.model.setClientSpeedMean((arrivedClient!=0)?(clientSpeedSum/(double)arrivedClient):0);
		for(Client cli: this.ListeClients)
			clientRealSpeedSum += (cli.getCarClient() == null)?0:(double)dist(cli.getPosClient()[0],cli.getCarClient().getPosCar()) 
			/(double)(this.time-cli.appearanceMoment);
		this.model.setClientRealSpeedMean((this.nbClientsSimulation!=0)?(clientRealSpeedSum/(double)this.nbClientsSimulation):0);
		this.arrivedRate = (double) arrivedClient/ (double)(arrivedClient+this.nbClientsSimulation);
	}
	}

	
	
	
	
	
	
	
	
	public ArrayList<Car> getListeVoitures() {
		return ListeVoitures;
	}

	public void setListeVoitures(ArrayList<Car> listeVoitures) {
		ListeVoitures = listeVoitures;
	}

	public ArrayList<Client> getListeClients() {
		return ListeClients;
	}

	public void setListeClients(ArrayList<Client> listeClients) {
		ListeClients = listeClients;
	}

	public Client getNotTargettedYet() {
		return notTargettedYet;
	}

	public void setNotTargettedYet(Client notTargettedYet) {
		this.notTargettedYet = notTargettedYet;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isNeedAlgorithme() {
		return needAlgorithme;
	}

	public void setNeedAlgorithme(boolean needAlgorithme) {
		this.needAlgorithme = needAlgorithme;
	}

	public int getNbVoituresSimulation() {
		return nbVoituresSimulation;
	}

	public void setNbVoituresSimulation(int nbVoituresSimulation) {
		this.nbVoituresSimulation = nbVoituresSimulation;
	}

	public int getNbClientsSimulation() {
		return nbClientsSimulation;
	}

	public void setNbClientsSimulation(int nbClientsSimulation) {
		this.nbClientsSimulation = nbClientsSimulation;
	}

	public Car getCarSimulation() {
		return carSimulation;
	}

	public void setCarSimulation(Car carSimulation) {
		this.carSimulation = carSimulation;
	}

	public Client getSelectedClient() {
		return selectedClient;
	}

	public void setSelectedClient(Client selectedClient) {
		this.selectedClient = selectedClient;
	}

	public double getCarSpeedMean() {
		return carSpeedMean;
	}

	public void setCarSpeedMean(double carSpeedMean) {
		this.carSpeedMean = carSpeedMean;
	}

	public double getClientSpeedSum() {
		return clientSpeedSum;
	}

	public void setClientSpeedSum(double clientSpeedSum) {
		this.clientSpeedSum = clientSpeedSum;
	}

	public int getArrivedClient() {
		return arrivedClient;
	}

	public void setArrivedClient(int arrivedClient) {
		this.arrivedClient = arrivedClient;
	}

	public double getClientSpeedMean() {
		return clientSpeedMean;
	}

	public void setClientSpeedMean(double clientSpeedMean) {
		this.clientSpeedMean = clientSpeedMean;
	}

	public double getClientRealSpeedMean() {
		return clientRealSpeedMean;
	}

	public void setClientRealSpeedMean(double clientRealSpeedMean) {
		this.clientRealSpeedMean = clientRealSpeedMean;
	}

	public double getArrivedRate() {
		return arrivedRate;
	}

	public void setArrivedRate(double arrivedRate) {
		this.arrivedRate = arrivedRate;
	}

	public int getDistSum() {
		return distSum;
	}

	public void setDistSum(int distSum) {
		this.distSum = distSum;
	}

	public int getCarbu() {
		return carbu;
	}

	public void setCarbu(int carbu) {
		this.carbu = carbu;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getAlgoId() {
		return algoId;
	}

	public void setAlgoId(int algoId) {
		this.algoId = algoId;
	}

	public int getCostRate() {
		return costRate;
	}

	public void setCostRate(int costRate) {
		this.costRate = costRate;
	}

	public boolean isDivide() {
		return divide;
	}

	public void setDivide(boolean divide) {
		this.divide = divide;
	}

	public int getStepMax() {
		return stepMax;
	}

	public void setStepMax(int stepMax) {
		this.stepMax = stepMax;
	}

	public int getOccupantCapacity() {
		return occupantCapacity;
	}

	public void setOccupantCapacity(int occupantCapacity) {
		this.occupantCapacity = occupantCapacity;
	}
	
	public int getOccupantMax() {
		return this.occupantMax;
	}
	
}
