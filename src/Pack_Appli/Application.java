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
 * Classe qui g�n�re 
 * 
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Application implements ActionListener, MouseListener, ItemListener, ChangeListener, KeyEventDispatcher
{
	/**
	 * Simulation
	 * 
	 */
	private Simulation simu;
	/**
	 * Fenetre de l'Application
	 */
	private Fenetre_Appli window;
	/** 
	 * D�clare si la Fenetre est affich�. Par d�faut, elle ne l'est pas.
	 */
	private SideWindow dataWindow = null;
	/**
	 * Taille des blocs de la ville.
	 */
	private int blockSize;
	/**
	 * sauvegarde de la simulation pr�c�dente
	 */
	private int[][] lastSimuArray;	
	/**
	 * Timer de la simulation
	 */
	private Timer timer = new Timer(0,this);	
	/**
	 * Compte � rebours
	 */
	private int addClientTime = -1;
	/**
	 * Type de drag and drop (d�placement de voiture/client)
	 */
	private int dragType = -1;
	
	
	/*
	 *  CONSTRUCTEUR
	 */
	
	
	/**
	 * initialise la simulation et cr�e et ouvre la fen�tre
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
	 * 
	 * 
	 * 
	 *  
	 *  AFFICHAGE DE SIMULATIONS ENREGISTREES 
	 *  
	 *  
	 *  
	 * 
	 */
	
	
	/**
	 * Active ou d�sactive le bouton "Afficher"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void setDisplaySavedSimuButton()
	{
		window.getDisplaySavedSimuButton().setEnabled(
				window.getSavedSimuComboBox().getSelectedIndex()!=0 || lastSimuArray!=null);
	}

	/**
	 * Active ou d�sactive le bouton "Supprimer"
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void setDeleteSavedSimuButton()
	{
		window.getDeleteSavedSimuButton().setEnabled(window.getSavedSimuComboBox().getSelectedIndex()!=0);
	}

	/** 
	 * Affiche sur le cityboard la simulation enregistr�e s�lectionn�e
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	protected void displaySavedSimu()
	{
		clear();
		//On r�cup�re le num�ro de la simulation enregistr�e
		int n = window.getSavedSimuComboBox().getSelectedIndex();
		//S'il s'agit de la derni�re simulation
		int[][] t = (n==0)? lastSimuArray:
		//Sinon on r�cup�re le tableau de la simulation
		Saving.savedSimuArrayOfSavedSimuString(Saving.getSavedSimuList().get(n-1));
		//on ajuste tous les param�tres
		window.getAlgorithmeArray()[t[0][0]].setSelected(true);
		window.getCostSlider().setValue(t[0][1]);
		window.getDivideCheckBox().setSelected(t[0][2]==1);
		window.getStepSpinner().setValue(t[0][3]);
		window.getOccupantSpinner().setValue(t[0][4]);
		window.getBlockSizeSpinner().setValue(t[0][5]);
		window.getSpeedSlider().setValue(t[0][6]);
		window.getAddClientCheckBox().setSelected(t[0][7]!=0);
		addClientEvent(t[0][7]!=0);
		if(t[0][7]!=0){
			window.getAddClientSpinner().setValue(t[0][7]);
			window.getIntervalSpinner().setValue(t[0][8]);
			window.getProbabilitySlider().setValue(t[0][9]);
		}
		//on cr�e les voitures et les clients de la configuration enregistrée
		for(int k=0;k<t[1].length;k=k+2)
			getSimu().ajouterVoitureSimulation(t[1][k],t[1][k+1]);
		for(int l=0;l<t[2].length;l=l+2)
			getSimu().ajouterClientSimulation(t[2][l],t[2][l+1]);
	}
	
	/**
	 * On r�cup�re le num�ro de la simulation enregistr�e
	 * Si aucune simulation enregistr�e n'est s�lectionn�e il s'agit d'une nouvelle
	 * Sinon on modifie la simulation enregistr�e
	 * 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void newSavedSimulation()
	{
		int n = window.getSavedSimuComboBox().getSelectedIndex();
		if(n==0) Saving.newSavedSimu(simuArray(),window);
		else Saving.editSavedSimu(simuArray(),n-1,window);
	}

	

	/*
	 * 
	 * 
	 * 
	 * 
	 *  LANCEMENT ET REMISE A ZERO DE LA SIMULATION 
	 *  
	 *  
	 *  
	 *  
	 */
	
	
	
	/** 
	 * tableau de sauvegarde asoci� aux param�tres et � simu
	 * @return int[][] 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int[][] simuArray()
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
	 * Sauvegarde, informe la simulation des param�tres de l'algorithme et le lance.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void doAlgorithme()
	{
		//on sauvegarde la simulation avant de la modifier
		lastSimuArray = simuArray();
		//on met � jour le bouton "afficher"
		setDisplaySavedSimuButton();
		//On informe simu des param�tres d'algorithme
		getSimu().setAlgoId(window.getAlgorithmeId());
		System.out.println("Algo s�lectionn� :" + window.getAlgorithmeId());
		getSimu().setCostRate(window.getCostSlider().getValue());
		getSimu().setDivide(window.getDivideCheckBox().isSelected());
		getSimu().setStepMax((Integer) window.getStepSpinner().getValue());
		System.out.println((Integer) window.getStepSpinner().getValue());
		getSimu().setOccupantCapacity((Integer) window.getOccupantSpinner().getValue());
		//on lance l'algorithme
		getSimu().executeAlgorithme();
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

	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 *  TIMER : DEMARRAGE, PAUSE ET EVENEMENTS 
	 *  
	 *  
	 *  
	 *  
	*/
	
	
	
	/**
	 * Cr�e le timer � partir de la valeur du slideur et d�marre
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
	 * Arr�te le timer
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

	/**
	 * actions aux "�ch�ances" du timer : <br>
	 * <ul>
	 * <li> Rajoute un client automatiquement si demand�. </li>
	 * <li> Calcule le parcours si la configuration a chang� </li>
	 * <li> On bouge les voirutes d'un cran </li>
	 * <li> On actualise l'affichage </li>
	 * </ul>
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void timerEvent()
	{
		addClient();
		if(getSimu().isNeedAlgorithme()) doAlgorithme();
		getSimu().OneMove();
		refresh();
	}

	/** 
	 * Mise � jour de la fen�tre : <br>
	 * Redessine le cityBoard et met � jour la fenetre de donn�es.
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private void refresh()
	{
		window.getBoard().repaint();
		if(dataWindow != null) dataWindow.setDataLabel(getSimu());
	}
	
	/**
	 * Cette fonction g�re l'ajout automatique d'un client al�atoire
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	private void addClient(){
		//Si la case d'ajout automatique est s�lectionn�e on r�initialise le compte � rebours
		if(window.getAddClientCheckBox().isSelected() && addClientTime == -1)
			addClientTime = (Integer) window.getIntervalSpinner().getValue();
		if(addClientTime != -1){
			//Lorsque le compte � rebours est � z�ro, on fait autant de fois demand� l'ajout
			if(addClientTime == 0) for(int z = 0;z<(Integer) window.getAddClientSpinner().getValue();z++){
				//On simule deux clics d'abscisse et d'ordonn�e al�atoire
				getSimu().ajouterClientSimulation(randomWidth(), randomHeight());
				getSimu().ajouterClientSimulation(randomWidth(), randomHeight());
				//Si le voyageur ne participe pas au covoiturage
				if(getSimu().getSelectedClient() != null && Math.random()*100>window.getProbabilitySlider().getValue()){
					getSimu().getSelectedClient().setStateClient(1);
					getSimu().getSelectedClient().setIsUsingCarSharing(false);
					getSimu().ajouterVoitureSimulation((int) getSimu().getSelectedClient().getPosClient()[0].getX(),(int) getSimu().getSelectedClient().getPosClient()[0].getY());
					getSimu().getSelectedClient().setCarClient(getSimu().getCarSimulation());
					getSimu().getCarSimulation().setIsDoingCarSharing(false);
					getSimu().getCarSimulation().getParcoursListCar().add(new ParcoursStep(getSimu().getSelectedClient(),1));
				}
			}
			//On d�cr�mente le compte � rebours
			addClientTime--;
		}
}
	
	/**
	 * fonction qui � une coordonn�e sur le board associe la coordonn�e du carrefour proche
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
	 * Renvoie le client le plus r�cent aux coordonn�es (i,j)
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
	 * Renvoie la voiture la plus r�cente aux coordonn�es (i,j)
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
	 * 
	 *  
	 *  
	 *  
	 *  EVENEMENTS BOUTONS, TIMER, LISTE, SLIDER ET CLAVIER 
	 *  
	 *  
	 *  
	 *  
	 *  */
	

	/**
	 * Actions lorsque l'on appuie sur un des bouttons de la souris
	 * <ul>
	 * <li> Button1 : bouton gauche, s�lectionne un client. </li>
	 * <li> Button3 : bouton droit, s�lectionne une voiture. </li>
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
	 * Actions lorsque la souris est relach�e
	 * <br>
	 * <br>
	 * Si on est � l'int�rieur du CityBoard, on cr�e ou modifie un client ou une voiture<br>
	 * Si on est � l'ext�rieur du cityBoard, on supprime la s�lection
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
	 * Actions au clic des boutons et aux �ch�ances du timer
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
			addClientEvent(window.getAddClientCheckBox().isSelected());
		else if(Listen == timer)
			timerEvent();
	}

	/**
	 * Actions lorsque la case d'ajout de clients est coch�e ou d�coch�e
	 * @param isChecked
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void addClientEvent(boolean isChecked){
		window.getAddClientSpinner().setEnabled(isChecked);
		window.getIntervalSpinner().setEnabled(isChecked);
		window.getProbabilitySlider().setEnabled(isChecked);
	}
	
	/**
	 * Met � jour les boutons de s�lection ("afficher"/"supprimer") � chaque changement de Sauvegarde.
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
	
	/**
	 * Actions lorsque le curseur du slideur a boug� :
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
	 * Actions aux �v�nements claviers <br>
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

	
	
	/*
	 * FONCTION GENERATRICES

	 */
	
	
	
	/**
	 * Ces fonctions g�n�rent des coordonn�es al�atoirement
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int randomWidth(){return blockModulo((int) (window.getBoard().getWidth()*Math.random()));}
	
	/**
	 * Ces fonctions g�n�rent des coordonn�es al�atoirement
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	int randomHeight(){return blockModulo((int) (window.getBoard().getHeight()*Math.random()));}
	

	/*
	 * 
	 * 
	 * 
	 * GETTERS ET SETTERS
	 * 
	 * 
	 * 
	 */
	
	
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
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * NON IMPLEMENTE
	 * 
	 * 
	 * 
	 * 
	 */
	
	/**
	 * �v�nement de Souris non impl�ment�.
	 */
	public void mouseEntered(MouseEvent event){}
	
	/**
	 * �v�nement de Souris non impl�ment�.
	 */
	public void mouseExited(MouseEvent event){}
	
	/**
	 * �v�nement de Souris non impl�ment�.
	 */
	public void mouseClicked(MouseEvent arg0) {}
	
}
