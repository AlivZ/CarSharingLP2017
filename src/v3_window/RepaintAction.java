package v3_window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe responsable de l'animation.
 * @author AirDur
 * @version Build III -  v0.5
 * @since Build III -  v0.5
 */
public class RepaintAction implements ActionListener {
	
	private Window win;
	
	/**
	 * Constructeur de RepaintAction
	 * @param win
	 */
	public RepaintAction(Window win) {
		this.win =win;
	}
	
    @Override
    /**
     * A chaque fois que le temps avance, on va faire : <br>
     * - soit on a selectionné la recherche de solution
     * - soit on a selectionné l'execution de la solution
     * @version Build III -  v0.5
     * @since Build III -  v0.5
     */
    public void actionPerformed(ActionEvent evt) {
    	if(win.promptSelected) { //Affiche pour chaque partie la "solution" de l'algorithme.
    		
    	} else if (!win.promptSelected) { //Affiche une étape d'execution
    		
    	}
        // Here we decide whether we can continue or not
        // the search with 'Animation'.
        // In the case of DFS, BFS, A* and Greedy algorithms
        // here we have the second step:
        // 2. If OPEN SET = [], then terminate. There is no solution.
        win.checkTermination();
        if (win.found) {
            win.timer.stop();
        }
        if (!win.realTime) {
            win.repaint();
        }
    }
} 
