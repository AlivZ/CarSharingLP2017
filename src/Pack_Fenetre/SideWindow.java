package Pack_Fenetre;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import Pack_Simu.Car;
import Pack_Simu.Client;
import Pack_Simu.Simulation;

import org.apache.commons.io.FileUtils;

/**
 * SideWindow est ou bien la fenêtre de données ou bien la fenêtre d'instructions
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 */
public class SideWindow extends JDialog{
	
	private static final long serialVersionUID = -8863025054299250887L;
	
	/**
	 * contient le texte affiché si fenêtre de data
	 */
	JLabel dataLabel;
	
	/**
	 * l'appel de cette fonction met à  jour les données affichées
	 * @param simu
	 * 
	 */
	public void setDataLabel(Simulation simu){
		String text ="<html>";
		text += "Time : "+simu.getTime()+"<br><br>";
		text += "Nombre de voyageurs : "+simu.getListeClients().size()+"<br><br>";
		text += "Nombre de voitures : "+simu.getListeVoitures().size()+"<br><br>";
		text += "Moyenne des vitesses instantanées :<br>"+simu.getCarSpeedMean()+"<br><br>";
		text += "Moyenne des vitesses des trajets terminés :<br>"+simu.getClientSpeedMean()+"<br><br>";
		text += "Moyenne des vitesses des trajets en cours :<br>"+simu.getClientRealSpeedMean()+"<br><br>";
		text += "Taux de voyageurs arrivés :<br>"+simu.getArrivedRate()+"<br><br>";
		text += "Somme des distances parcourues :<br>"+simu.getDistSum()+"<br><br>";
		text += "Consommation de carburant :<br>"+simu.getCarbu()+"<br><br>";
		text += "Voitures participant au covoiturage :<br>";
		for(Car car: simu.getListeVoitures())
		{
			if(car.getIsDoingCarSharing()){
				text +="Voiture n° "+car.getIdCar()+"<br> Occupants : ";
				for(Client cli: car.getOccupantListCar())
					text += (cli.getStateClient() == 1)?cli.getIdClient()+" ":"<s>"+cli.getIdClient()+"</s> ";
				text += "<br><br>";
			}
		}
		text += "</html>";
		dataLabel.setText(text);
	}
	
	/**
	 * Initialisation de la fenÃªtre de cÃ´tÃ© (???)
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 * @param window
	 * @param title
	 */
	public SideWindow(Fenetre_Appli window, String title){
		//dÃ©finit window comme la fenÃªtre parente
		super(window,title);
		//la variable text contiendra le texte
		Component text = null;
		//S'il s'agit de la fenÃªtre d'instructions
		if(title == "Help"){
			String fileName = "lib/Help.txt";
			File Help = new File(fileName);
			String stringHelp;
			try{
				stringHelp = FileUtils.readFileToString(Help, StandardCharsets.ISO_8859_1);
			} catch(IOException e) {
				stringHelp = "Le fichier n'a pas pu être chargé.";
			}
			JTextArea helpText = new JTextArea(
				stringHelp
					);
			helpText.setEditable(false);
			helpText.setLineWrap(true);
			helpText.setWrapStyleWord(true);
			text = helpText;
		}
		//S'il s'agit de la fenêtre de données
		else if(title == "Datas"){
			dataLabel = new JLabel();
			text = dataLabel;
		}
		//crée un objet contenant des barres de défilement
		JScrollPane pane = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(pane);
		//la fenêtre doit avoir la même hauteur que window et la colargeur de window
		setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width
				-window.getWidth(),
				window.getHeight());
		//on situe la fenêtre à droite de window
		setLocation(window.getWidth(),window.getLocation().y);
		//on affiche la fenêtre
		setVisible(true);
		//on décale window à gauche
		window.setLocation(0,window.getLocation().y);
	}
}
