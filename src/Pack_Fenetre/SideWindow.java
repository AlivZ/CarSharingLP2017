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
 * SideWindow est ou bien la fen�tre de donn�es ou bien la fen�tre d'instructions
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 */
public class SideWindow extends JDialog{
	
	private static final long serialVersionUID = -8863025054299250887L;
	
	/**
	 * contient le texte affich� si fen�tre de data
	 */
	JLabel dataLabel;
	
	/**
	 * l'appel de cette fonction met � jour les donn�es affich�es
	 * @param simu
	 * 
	 */
	public void setDataLabel(Simulation simu){
		String text ="<html>";
		text += "Time : "+simu.getTime()+"<br><br>";
		text += "Nombre de voyageurs : "+simu.getListeClients().size()+"<br><br>";
		text += "Nombre de voitures : "+simu.getListeVoitures().size()+"<br><br>";
		text += "Moyenne des vitesses instantan�es :<br>"+simu.getCarSpeedMean()+"<br><br>";
		text += "Moyenne des vitesses des trajets termin�s :<br>"+simu.getClientSpeedMean()+"<br><br>";
		text += "Moyenne des vitesses des trajets en cours :<br>"+simu.getClientRealSpeedMean()+"<br><br>";
		text += "Taux de voyageurs arriv�s :<br>"+simu.getArrivedRate()+"<br><br>";
		text += "Somme des distances parcourues :<br>"+simu.getDistSum()+"<br><br>";
		text += "Consommation de carburant :<br>"+simu.getCarbu()+"<br><br>";
		text += "Voitures participant au covoiturage :<br>";
		for(Car car: simu.getListeVoitures())
		{
			if(car.getIsDoingCarSharing()){
				text +="Voiture n� "+car.getIdCar()+"<br> Occupants : ";
				for(Client cli: car.getOccupantListCar())
					text += (cli.getStateClient() == 1)?cli.getIdClient()+" ":"<s>"+cli.getIdClient()+"</s> ";
				text += "<br><br>";
			}
		}
		text += "</html>";
		dataLabel.setText(text);
	}
	
	/**
	 * Initialisation de la fenêtre de côté (???)
	 * @version Build III -  v0.1
	 * @since Build III -  v0.0
	 * @param window
	 * @param title
	 */
	public SideWindow(Fenetre_Appli window, String title){
		//définit window comme la fenêtre parente
		super(window,title);
		//la variable text contiendra le texte
		Component text = null;
		//S'il s'agit de la fenêtre d'instructions
		if(title == "Help"){
			String fileName = "lib/Help.txt";
			File Help = new File(fileName);
			String stringHelp;
			try{
				stringHelp = FileUtils.readFileToString(Help, StandardCharsets.ISO_8859_1);
			} catch(IOException e) {
				stringHelp = "Le fichier n'a pas pu �tre charg�.";
			}
			JTextArea helpText = new JTextArea(
				stringHelp
					);
			helpText.setEditable(false);
			helpText.setLineWrap(true);
			helpText.setWrapStyleWord(true);
			text = helpText;
		}
		//S'il s'agit de la fen�tre de donn�es
		else if(title == "Datas"){
			dataLabel = new JLabel();
			text = dataLabel;
		}
		//cr�e un objet contenant des barres de d�filement
		JScrollPane pane = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(pane);
		//la fen�tre doit avoir la m�me hauteur que window et la colargeur de window
		setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width
				-window.getWidth(),
				window.getHeight());
		//on situe la fen�tre � droite de window
		setLocation(window.getWidth(),window.getLocation().y);
		//on affiche la fen�tre
		setVisible(true);
		//on d�cale window � gauche
		window.setLocation(0,window.getLocation().y);
	}
}
