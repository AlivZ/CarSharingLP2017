package Pack_Appli;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Pack_Fenetre.Fenetre_Appli;
import Pack_Fenetre.SideWindow;
import Pack_Simu.Car;
import Pack_Simu.Client;
import Pack_Simu.ParcoursStep;
import Pack_Simu.Simulation;

/**
 * Classe qui génére 
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Application implements ActionListener, MouseListener, ItemListener, ChangeListener, KeyEventDispatcher
{
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/
	
	
	/**
	 * Simulation
	 */
	private Simulation simu;
	/**
	 * Fenetre de l'Application
	 */
	private Fenetre_Appli window;
	/** 
	 * Déclare si la Fenetre est affiché. Par défaut, elle ne l'est pas.
	 */
	private SideWindow dataWindow = null;
	/**
	 * Taille des blocs de la ville.
	 */
	private int blockSize;
	/**
	 * sauvegarde de la simulation précédente
	 */
	private int[][] lastSimuArray;	
	/**
	 * Timer de la simulation
	 */
	private Timer timer = new Timer(0,this);	
	/**
	 * Compte à rebours
	 */
	private int addClientTime = -1;
	/**
	 * Type de drag and drop (déplacement de voiture/client)
	 */
	private int dragType = -1;
	
	
	/*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	  */
	
	
	/**
	 * initialise la simulation et crée et ouvre la fenêtre
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected Application()
	{
		this.simu = new Simulation(0,1,0,0);
		window = new Fenetre_Appli(this);
		if(Saving.getSavedSimuList().size()>0) 
			{
			window.getSavedSimuComboBox().setSelectedIndex(1);
			}
		window.getSavedSimuComboBox().setSelectedIndex(0);
	}

	
	/*
	              _   _                                       
	    /\       | | (_)                                      
	   /  \   ___| |_ _  ___  _ __  _ __   ___ _   _ _ __ ___ 
	  / /\ \ / __| __| |/ _ \| '_ \| '_ \ / _ \ | | | '__/ __|
	 / ____ \ (__| |_| | (_) | | | | | | |  __/ |_| | |  \__ \
	/_/    \_\___|\__|_|\___/|_| |_|_| |_|\___|\__,_|_|  |___/
	                                                          
	   
	 */
	
	
	/**
	 * actions aux "échéances" du timer : <br>
	 * <ul>
	 * <li> Rajoute un client automatiquement si demandé. </li>
	 * <li> Calcule le parcours si la configuration a changé </li>
	 * <li> On bouge les voirutes d'un cran </li>
	 * <li> On actualise l'affichage </li>
	 * </ul>
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void timerEvent()
	{
		addClient();
		if(getSimu().isRedoAlgorithme()) doAlgorithme();
		getSimu().OneMove();
		refresh();
	}
	
	/**
	 * Actions lorsque l'on appuie sur un des bouttons de la souris
	 * <ul>
	 * <li> Button1 : bouton gauche, sélectionne un client. </li>
	 * <li> Button3 : bouton droit, sélectionne une voiture. </li>
	 * </ul>
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public void mousePressed(MouseEvent event){
		int i = event.getX()/window.getBoard().getSquareSize();
		int j = event.getY()/window.getBoard().getSquareSize();
		if (event.getButton()==MouseEvent.BUTTON1) {
			getSimu().setSelectedClient(selectClient(i,j));
		}
		if (event.getButton()==MouseEvent.BUTTON3) {
			getSimu().setCarSimulation(selectCar(i,j));
		}
	}

	/**
	 * Actions lorsque la souris est relachée
	 * <br>
	 * <br>
	 * Si on est à l'intérieur du CityBoard, on crée ou modifie un client ou une voiture<br>
	 * Si on est à l'extérieur du cityBoard, on supprime la sélection
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void mouseReleased(MouseEvent event){
		int x = blockModulo(event.getX());
		int y = blockModulo(event.getY());
		if(x>=0 && x<window.getBoard().getHeight() && y>=0 && y<window.getBoard().getWidth())
		{
			switch (dragType) {
			case -1: 
				if (event.getButton()==MouseEvent.BUTTON1) getSimu().ajouterClientSimulation(x,y); //client
				if (event.getButton()==MouseEvent.BUTTON3) getSimu().ajouterVoitureSimulation(x,y); //voiture
				break;
			case 0: case 1: 
				getSimu().getSelectedClient().getPosClient()[dragType].setLocation(x,y);
				break;
			case 2:
				getSimu().getCarSimulation().getPosCar().setLocation(x,y);
				break;
			default: break;
			}
		}
		else {
			switch (dragType) {
			case 0: case 1: 
				getSimu().deleteClient();
				break;
			case 2:
				getSimu().deleteCar();
				break;
			default: break;
			}
		}
		dragType = -1;
		refresh();
	}
	
	/**
	 * Actions au clic des boutons et aux échéances du timer
	 * <ul>
	 * <li>F1 = Active le button "Start" </li>
	 * </ul>
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public void actionPerformed(ActionEvent evt)
	{
		Object Listen = evt.getSource();
		
		if(Listen == window.getQuitButton())
			window.dispose();
		else if(Listen == window.getStartButton())
			if(!timer.isRunning()) timerStart(); else timerStop();
		else if(Listen == window.getClearButton())
			clear();
		else if(Listen == window.getDataButton())
			dataWindow = new SideWindow(window,"Datas");
		else if(Listen == window.getHelpButton())
			new SideWindow(window,"Help");
		else if(Listen == window.getDisplaySavedSimuButton())
			displaySavedSimu();
		else if(Listen == window.getNewSavedSimuButton())
			newSavedSimulation();
		else if(Listen == window.getDeleteSavedSimuButton())
			Saving.deleteSavedSimu(window.getSavedSimuComboBox().getSelectedIndex()-1,window);
		else if(Listen == window.getAddClientCheckBox())
			window.addClientEvent();
		else if(Listen == timer)
			timerEvent();
	}
	
	
	
	/**
	 * Actions lorsque le curseur du slideur a bougé :
	 * <ul>
	 * <li> Si c'est le timer, on pause le temps. </li>
	 * <li> Si c'est la taille des blocks
	 * </ul>
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void stateChanged(ChangeEvent arg0)
	{
		Object Source = arg0.getSource();
		if(Source == window.getSpeedSlider()){
			if(timer.isRunning()) timerPause();
		}
		else if(Source == window.getBlockSizeSpinner()){
			setBlockSize((Integer) window.getBlockSizeSpinner().getValue());
			clear();
		}
	}

	/**
	 * Actions aux événements claviers <br>
	 * <ul>
	 * <li>F1 = Active le button "Start" </li>
	 * </ul>
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public boolean dispatchKeyEvent(KeyEvent arg0) {
		if (arg0.getID() == KeyEvent.KEY_PRESSED)
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_F1: window.getStartButton().doClick(); break;
			case KeyEvent.VK_F2: window.getClearButton().doClick(); break;
			case KeyEvent.VK_F3: window.getDataButton().doClick(); break;
			case KeyEvent.VK_F4: window.getQuitButton().doClick(); break;
			case KeyEvent.VK_F5: window.getDisplaySavedSimuButton().doClick(); break;
			case KeyEvent.VK_F6: window.getDeleteSavedSimuButton().doClick(); break;
			case KeyEvent.VK_F7: window.getNewSavedSimuButton().doClick(); break;
			case KeyEvent.VK_F12: window.getHelpButton().doClick(); break;
			default: break;
			}
		return false;
	}
	
	/**
	 * On récupêre le numéro de la simulation enregistrée <br>
	 * Si aucune simulation enregistrée n'est sélectionnée il s'agit d'une nouvelle <br>
	 * Sinon on modifie la simulation enregistrée <br>
	 * 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void newSavedSimulation()
	{
		int n = window.getSavedSimuComboBox().getSelectedIndex();
		if(n==0) Saving.newSavedSimu(getSimuArray(),window);
		else Saving.editSavedSimu(getSimuArray(),n-1,window);
	}

	/*
	 * 
	 _______ _                     
	|__   __(_)                    
	   | |   _ _ __ ___   ___ _ __ 
	   | |  | | '_ ` _ \ / _ \ '__|
	   | |  | | | | | | |  __/ |   
	   |_|  |_|_| |_| |_|\___|_|   
	                               
	      
	 *  
	*/
	
	
	
	/**
	 * Crée le timer à partir de la valeur du slideur et démarre
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	private void timerStart()
	{
		window.getStartButton().setText("Pause");
		timer = new Timer(500/window.getSpeedSlider().getValue(),this);
		timer.start();
	}
	
	/**
	 * Arrête le timer
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	private void timerStop()
	{
		timer.stop();
		window.getStartButton().setText("Start");
		
	}
	
	/**
	 * Pause le Timer
	 * @version Build III -  v0.1
	 * @since Build III -  v0.1
	 */
	private void timerPause() {
		timer.stop();
		timerStart();
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
	 * Affiche sur le cityboard la simulation enregistrée sélectionnée <br>
	 * Pour cela, ça récupère le numéro de la somulation enregistré et le tableau de la simulation. <br>
	 * On appelle à la foncton rebuild de Fentre_Application pour reconstruire visuellement l'ensemble. <br>
	 * Enfin, on affiche les voitures et les cliens dans la Simulation.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void displaySavedSimu()
	{
		clear();
		
		int n = window.getSavedSimuComboBox().getSelectedIndex();
		
		int[][] t = (n==0)? lastSimuArray:
		
		Saving.savedSimuArrayOfSavedSimuString(Saving.getSavedSimuList().get(n-1));
				
		window.rebuild(t);
		
		//on crée les voitures et les clients de la configuration enregistrÃ©e
		for(int k=0;k<t[1].length;k=k+2)
			getSimu().ajouterVoitureSimulation(t[1][k],t[1][k+1]);
		for(int l=0;l<t[2].length;l=l+2)
			getSimu().ajouterClientSimulation(t[2][l],t[2][l+1]);
	}
	
	/**
	 * Arrete et efface la simulation. Initialise les variables de simulation.
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	void clear()
	{
		timerStop();
		int wbss = window.getBoard().getSquareSize();
		setSimu(new Simulation(wbss/2,(getBlockSize()+1)*wbss,
				window.getBoard().getBoardWidth()*window.getBoard().getSquareSize(),
				window.getBoard().getBoardHeight()*window.getBoard().getSquareSize()));
		refresh();
	}
	
	/** 
	 * Mise à jour de la fenêtre : <br>
	 * Redessine le cityBoard et met à jour la fenetre de données.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private void refresh()
	{
		window.getBoard().repaint();
		if(dataWindow != null) dataWindow.setDataLabel(getSimu());
	}
	/**
	 * Met à jour les boutons de sélection ("afficher"/"supprimer") à chaque changement de Sauvegarde.
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 */
	public void itemStateChanged(ItemEvent arg0)
	{
		if(arg0.getStateChange() == ItemEvent.SELECTED)
		{
			setDisplaySavedSimuButton();
			setDeleteSavedSimuButton();
		}
	}
	

	/*
 ______                     _   _             
|  ____|                   | | (_)            
| |__  __  _____  ___ _   _| |_ _  ___  _ __  
|  __| \ \/ / _ \/ __| | | | __| |/ _ \| '_ \ 
| |____ >  <  __/ (__| |_| | |_| | (_) | | | |
|______/_/\_\___|\___|\__,_|\__|_|\___/|_| |_|
                                              
                                              
	 */
	
	
	/**
	 * Sauvegarde, informe la simulation des paramètres de l'algorithme et le lance.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void doAlgorithme()
	{
		//on sauvegarde la simulation avant de la modifier
		lastSimuArray = getSimuArray();
		//on met à jour le bouton "afficher"
		setDisplaySavedSimuButton();
		//On informe simu des paramètres d'algorithme
		getSimu().setAlgoId(window.getAlgorithmeId());
		System.out.println("Algo sélectionné :" + window.getAlgorithmeId());
		getSimu().setCostRate(window.getCostSlider().getValue());
		getSimu().setDivide(window.getDivideCheckBox().isSelected());
		getSimu().setStepMax((Integer) window.getStepSpinner().getValue());
		System.out.println((Integer) window.getStepSpinner().getValue());
		getSimu().setOccupantCapacity((Integer) window.getOccupantSpinner().getValue());
		//on lance l'algorithme
		getSimu().executeAlgorithme();
	}
	
	
	/**
	 * Cette fonction gêre l'ajout automatique d'un client aléatoire <br>
	 * Réinitialisation du compte à rebours si la case d'ajout automatique (fenêtre) est selectionnée. <br>
	 * Lorsque le compte à rebours est à zéro, on fait autant de fois demandé l'ajout : <br>
	 *  * On simule deux clics d'abscisse et d'ordonnée aléatoire <br>
	 *  * On décrémente le compte à rebours
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private void addClient(){
		//Si la case d'ajout automatique est sélectionnée on réinitialise le compte à rebours
		if(window.getAddClientCheckBox().isSelected() && addClientTime == -1) {
			addClientTime = (Integer) window.getIntervalSpinner().getValue();
		}
			
		if(addClientTime != -1){
			
			if(addClientTime == 0) {
				for(int z = 0;z< ((Integer) window.getAddClientSpinner().getValue());z++){
					getSimu().ajouterClientSimulation(randomWidth(), randomHeight());
					getSimu().ajouterClientSimulation(randomWidth(), randomHeight());
					//Si le voyageur ne participe pas au covoiturage (PARTIE PROBABLEMENT A VIRER CAR JE ME DEMANDE SI ON GARDE)
					
					if(getSimu().getSelectedClient() != null && Math.random()*100>window.getProbabilitySlider().getValue()){
						getSimu().getSelectedClient().setStateClient(1);
						getSimu().getSelectedClient().setIsUsingCarSharing(false);
						getSimu().ajouterVoitureSimulation((int) getSimu().getSelectedClient().getPosClient()[0].getX(),(int) getSimu().getSelectedClient().getPosClient()[0].getY());
						getSimu().getSelectedClient().setCarClient(getSimu().getCarSimulation());
						getSimu().getCarSimulation().setIsDoingCarSharing(false);
						getSimu().getCarSimulation().getParcoursListCar().add(new ParcoursStep(getSimu().getSelectedClient(),1));
					}
					
				}
			}
			//O
			addClientTime--;
		}
}
	
	
	
	
		/*
		 
		 ______               _   _               _    _ _   _ _           
		|  ____|             | | (_)             | |  | | | (_) |          
		| |__ ___  _ __   ___| |_ _  ___  _ __   | |  | | |_ _| | ___  ___ 
		|  __/ _ \| '_ \ / __| __| |/ _ \| '_ \  | |  | | __| | |/ _ \/ __|
		| | | (_) | | | | (__| |_| | (_) | | | | | |__| | |_| | |  __/\__ \
		|_|  \___/|_| |_|\___|\__|_|\___/|_| |_|  \____/ \__|_|_|\___||___/
		                                                                   
		     
		 */
	

	/**
	 * fonction qui à une coordonnée sur le board associe la coordonnée du carrefour proche
	 * @param x
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int blockModulo(int x){
		int gbss = window.getBoard().getSquareSize();
		int gbs = (int) Math.pow((getBlockSize()+1),2);
		return (x/gbss)/(gbs)*gbss;
	}
	
	
	/**
	 * Renvoie le client le plus récent aux coordonnées (i,j)
	 * @param i
	 * @param j
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private Client selectClient(int i, int j){
		if(getSimu().getNotTargettedYet() != null
				&& getSimu().getNotTargettedYet().getPosClient()[0].getX()/window.getBoard().getSquareSize() == i
				&& getSimu().getNotTargettedYet().getPosClient()[0].getY()/window.getBoard().getSquareSize() == j){
			dragType = 0;
			return getSimu().getNotTargettedYet();
			
		}	
		for(int l = getSimu().getListeClients().size()-1;l >=0; l--)
			for(int type = 1; type >= 0; type--)
				if(getSimu().getListeClients().get(l).getPosClient()[type].getX()/window.getBoard().getSquareSize() == i
				&& getSimu().getListeClients().get(l).getPosClient()[type].getY()/window.getBoard().getSquareSize() == j){
					dragType = type;
					return getSimu().getListeClients().get(l);
				}
		return null;
	}
	
	/**
	 * Renvoie la voiture la plus récente aux coordonnées (i,j)
	 * @param i
	 * @param j
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private Car selectCar(int i, int j){
		for(int k = getSimu().getListeVoitures().size()-1;k>=0; k--)
			if(getSimu().getListeVoitures().get(k).getPosCar().getX()/window.getBoard().getSquareSize() == i
			&& getSimu().getListeVoitures().get(k).getPosCar().getY()/window.getBoard().getSquareSize() == j){
				dragType = 2;
				return getSimu().getListeVoitures().get(k);
			}
		return null;
	}
	
	
	/*
		                                 _        _          
		                                | |      (_)         
		  __ _  ___ _ __   ___ _ __ __ _| |_ _ __ _  ___ ___ 
		 / _` |/ _ \ '_ \ / _ \ '__/ _` | __| '__| |/ __/ _ \
		| (_| |  __/ | | |  __/ | | (_| | |_| |  | | (_|  __/
		 \__, |\___|_| |_|\___|_|  \__,_|\__|_|  |_|\___\___|
		  __/ |                                              
		 |___/       
	 */
	
	
	
	/**
	 * Ces fonctions génèrent des coordonnées aléatoirement
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int randomWidth(){return blockModulo((int) (window.getBoard().getWidth()*Math.random()));}
	
	/**
	 * Ces fonctions génèrent des coordonnées aléatoirement
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int randomHeight(){return blockModulo((int) (window.getBoard().getHeight()*Math.random()));}
	

	/*
	   _____      _               _____      _   
	  / ____|    | |     ___     / ____|    | |  
	 | |  __  ___| |_   ( _ )   | (___   ___| |_ 
	 | | |_ |/ _ \ __|  / _ \/\  \___ \ / _ \ __|
	 | |__| |  __/ |_  | (_>  <  ____) |  __/ |_ 
	  \_____|\___|\__|  \___/\/ |_____/ \___|\__|
	                                             
	  */  
	
	/** 
	 * tableau de sauvegarde asocié aux paramètres et à simu
	 * @return int[][] 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int[][] getSimuArray()
	{
		return new int[][]{{window.getAlgorithmeId(),
			window.getCostSlider().getValue(),
			(window.getDivideCheckBox().isSelected())?1:0,
			(Integer) window.getStepSpinner().getValue(),
			(Integer) window.getOccupantSpinner().getValue(),
			(Integer) window.getBlockSizeSpinner().getValue(),
			window.getSpeedSlider().getValue(),
			(window.getAddClientCheckBox().isSelected())?(Integer)window.getAddClientSpinner().getValue():0,
			(Integer)window.getIntervalSpinner().getValue(),
			window.getProbabilitySlider().getValue()},
			getSimu().getCoordoneeDesVoitures(),getSimu().getCoordoneeDesClients()};
	}
	
	/**
	 * Active ou désactive le bouton "Afficher"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void setDisplaySavedSimuButton()
	{
		window.getDisplaySavedSimuButton().setEnabled(
				window.getSavedSimuComboBox().getSelectedIndex()!=0 || lastSimuArray!=null);
	}

	/**
	 * Active ou désactive le bouton "Supprimer"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void setDeleteSavedSimuButton()
	{
		window.getDeleteSavedSimuButton().setEnabled(window.getSavedSimuComboBox().getSelectedIndex()!=0);
	}
	
	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	
	public Simulation getSimu() {
		return simu;
	}

	public void setSimu(Simulation simu) {
		this.simu = simu;
	}
	
	/*
	                     _                 _                           _       
	                    (_)               | |                         | |      
	 _ __   ___  _ __    _ _ __ ___  _ __ | | ___ _ __ ___   ___ _ __ | |_ ___ 
	| '_ \ / _ \| '_ \  | | '_ ` _ \| '_ \| |/ _ \ '_ ` _ \ / _ \ '_ \| __/ _ \
	| | | | (_) | | | | | | | | | | | |_) | |  __/ | | | | |  __/ | | | ||  __/
	|_| |_|\___/|_| |_| |_|_| |_| |_| .__/|_|\___|_| |_| |_|\___|_| |_|\__\___|
	                                | |                                        
	                                |_|            
	 */
	
	/**
	 * événement de Souris non implémenté.
	 */
	public void mouseEntered(MouseEvent event){}
	
	/**
	 * événement de Souris non implémenté.
	 */
	public void mouseExited(MouseEvent event){}
	
	/**
	 * événement de Souris non implémenté.
	 */
	public void mouseClicked(MouseEvent arg0) {}
	
}
