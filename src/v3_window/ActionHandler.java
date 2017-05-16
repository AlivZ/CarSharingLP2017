package v3_window;

import java.awt.Color;
import java.awt.event.*;

import org.json.simple.JSONArray;

import v3_algo.Execut_Algo_Genetique;

/**
 * When the user presses a button performs the corresponding functionality
 */
public class ActionHandler implements ActionListener {
    
	protected Window win;
	protected String name;
    
    protected ActionHandler(Window win, String name) {
    	this.win = win;
    	this.name = name;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        switch(this.name) {
	        case "New Grid" : 
	        	//Affiche une nouvelle grille selon les paramètres
	        	win.realTime = false;
	        	win.realTimeButton.setEnabled(true);
	        	win.realTimeButton.setForeground(Color.black);
	        	win.stepButton.setEnabled(true);
	        	win.animationButton.setEnabled(true);
	        	win.slider.setEnabled(true);
	        	win.initializeGrid();
	        	break;
	        case "Random" : 
	        	//En gros :
	        	//Va créer 5 voitures de taille 4 et 20 passagers disposés de façon random.
	        	//Et les affiche aussi.
	        	win.realTime = false;
	        	win.realTimeButton.setEnabled(true);
	        	win.realTimeButton.setForeground(Color.black);
	        	win.stepButton.setEnabled(true);
	        	win.animationButton.setEnabled(true);
	        	win.slider.setEnabled(true);
	        	//win.initializeGrid(true);
	        	break;
	        case "Clear" : 
	        	//Efface le parcours (Animation)
	        	break;
	        case "Clear2" :
	        	//Enlève tout les murs de la grille.
	        	win.deleteWallGrid();
	        	break;
	        case "RealTime" : 
	        	//Fait parcourir les voitures dans le système. On peut en rajouter avec.
	        	if(!win.realTime) {
	        		win.promptSelected = false;
	        		win.realTime = true;
	                win.searching = true;
	                win.realTimeButton.setForeground(Color.red);
	                win.stepButton.setEnabled(false);
	                win.animationButton.setEnabled(false);
	                win.realTimeButton.setEnabled(false);
	                win.slider.setEnabled(false);
	                win.timer.setDelay(0);
	                win.timer.start();
	                win.checkTermination();
	        	}
	        	break;
	        case "StepByStep" : 
	        	//OSEF
	        	break;
	        case "Animation" :
	        	//Affiche pour chaque étape de l'algo génétique le parcours des voitures.
	        	if(!win.endOfSearch) { //Ne peut pas se déclancher si le truc marche pas.
	        		win.realTime = false;
	                win.searching = true;
	                win.message.setText((String) win.JSON_Window.get("msgSelectStepByStepEtc"));
	                win.realTimeButton.setEnabled(false);
	                win.promptSelected = true;
	                win.algo = new Execut_Algo_Genetique(win);
	                win.timer.setDelay(win.delay);
	                win.timer.start();
	        	}
	        	break;
	        case "ChangeState" : 
	        	win.editButton("ChangeState_2", win.changeStateButton);
	        	win.typeColoriage = true;
	        	break;
	        case "ChangeState_2" : 
	        	win.editButton("ChangeState", win.changeStateButton);
	        	win.typeColoriage = false;
	        	break;
	        case "About" :
	            /*AboutBox aboutBox = new AboutBox(win.mazeFrame,true);
	            aboutBox.setVisible(true);*/
	            break;
	            
        }
        
    }
} // end nested class ActionHandler
