package v3_window;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Classe qui permet d'afficher la fenetre.
 * 
 * 
 * @author AirDur
 * @author Nikos Kanargias, Hellenic Open University student, PLI31 2012-13 - (http://youtu.be/0ol_PptA7rM)
 *
 */
public class Main {
	
	/**
	 * Le contenu principal du programme
	 */
    public static JFrame mazeFrame; 
    /**
     * Largeur
     */
    public static final int width = 693;
    /**
     * Hauteur
     */
    public static final int height = 545;
    /**
     * Titre-légende de la fenetre
     */
    public static final String title = "Algorithme Génétique";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mazeFrame = new JFrame(title);
        
        mazeFrame.setContentPane(new Window(width,height));
        
        mazeFrame.pack();
        mazeFrame.setResizable(false);
        
        // the form is located in the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double ScreenHeight = screenSize.getHeight();
        int x = ((int)screenWidth-width)/2;
        int y = ((int)ScreenHeight-height)/2;

        mazeFrame.setLocation(x,y);
        mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mazeFrame.setVisible(true);
        
    } // end main()
}
