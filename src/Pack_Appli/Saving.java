package Pack_Appli;

//Pour comprendre le nom des variables et ce qu'elles reprÃ©sentent, un petit exemple :
//savedSimuString = Exemple nÂ°1|[0, 0][11, 9][3, 5, 11, 6, 6, 13, 15, 13]
//savedSimuName = Exemple nÂ°1
//savedSimuArray = [[0, 0][11, 9][3, 5, 11, 6, 6, 13, 15, 13]]
//savedSimuList est la liste des savedSimuString, et se trouve dans la classe

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import Pack_Fenetre.Fenetre_Appli;
/**
 * Cette classe contient des fonctions statiques permettant de lire et Ã©crire dans des fichiers txt
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Saving
{
	/** 
	 * Fichier contenant les Simulations Sauvgardés
	 * <br> en .txt
	 */
	static final String fileName = "lib/SavedSimulations.txt";
	/**
	 * Liste de savedSimuString <br>
	 * <b>savedSimuString</b> : Concaténation de savedSimuName et savedSimuArray
	 */
	private static ArrayList<String> savedSimuList = new  ArrayList<String>();
	
	/**
	 * Permet le fichier du fileName.
	 * @return
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	static File getFile() {
		File fichier = new File(fileName);
		if(fichier.exists() == false)
			try {
				fichier.createNewFile();
				System.out.println("File "+fileName+" created");
			} catch (IOException e) {e.printStackTrace();}
		else System.out.println("Got file "+fileName);
		return fichier;
	}

	/**
	 * Remplit savedSimuList des lignes du fichier
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public static void setSavedSimuList(){
		File f = getFile();
		String line = "";
		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			while(line != null)
			{
				if((line = line.trim()).length()>0) savedSimuList.add(line);
				line = buff.readLine();
			}
			buff.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * RÃ©Ã©crit les lignes de savedSimuList dans le fichier
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	static void reWrite(){
		File f = getFile();
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter(f,false));
			for(int n = 0; n<getSavedSimuList().size();n++){
				buff.write(getSavedSimuList().get(n));
				buff.newLine();
			}
			buff.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/** 
	 *  Ajout d'une simulation 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0**/
	static void newSavedSimu(int[][] savedSimuArray,Fenetre_Appli window){
		String savedSimuName = (String)JOptionPane.showInputDialog(window,
				"Veuillez entrer le nom de la simulation à enregistrer :","Nom de la simulation",
				JOptionPane.INFORMATION_MESSAGE,null,null,"Exemple n°"+(getSavedSimuList().size()+1));
		if(savedSimuName != null)
		{
			String savedSimuString = savedSimuStringOfSavedSimuNameAndArray(savedSimuName, savedSimuArray);
			getSavedSimuList().add(savedSimuString);
			window.getSavedSimuComboBox().addItem(savedSimuName);
			File f = getFile();
			try {
				BufferedWriter buff = new BufferedWriter(new FileWriter(f,true));
				buff.write(savedSimuString);
				buff.newLine();
				buff.close();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	/**
	 * Suppression d'une simulation
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param n
	 * @param window
	 */
	static void deleteSavedSimu(int n,Fenetre_Appli window){
		int answer = JOptionPane.showConfirmDialog(window,"Supprimer la simulation \""
				+savedSimuNameOfSavedSimuString(getSavedSimuList().get(n))+"\" ?", "Suppression",
				JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION){
			getSavedSimuList().remove(n);
			window.getSavedSimuComboBox().removeItemAt(n+1);
			reWrite();
		}
	}
	/**
	 * Edition d'une simulation
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param savedSimuArray
	 * @param n
	 * @param window
	 */
	static void editSavedSimu(int[][] savedSimuArray, int n, Fenetre_Appli window){
		String savedSimuName = (String)JOptionPane.showInputDialog(window,
				"Vous pouvez modifier le nom de la simulation :","Modifier le nom de la simulation",
				JOptionPane.INFORMATION_MESSAGE,null,null,savedSimuNameOfSavedSimuString(getSavedSimuList().get(n)));
		if(savedSimuName != null)
		{
			String savedSimuString = savedSimuStringOfSavedSimuNameAndArray(savedSimuName, savedSimuArray);
			getSavedSimuList().set(n,savedSimuString);
			window.getSavedSimuComboBox().insertItemAt(savedSimuName,n+1);
			window.getSavedSimuComboBox().removeItemAt(n+2);
			reWrite();
		}
	}

	
	/**
	 * Fonction qui split le string 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 **/
	public static String savedSimuNameOfSavedSimuString(String savedSimuString ){
		return (savedSimuString.split("\\|"))[0];
	}
	/**
	 * Fonction ????
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param savedSimuString
	 * @return
	 */
	static int[][] savedSimuArrayOfSavedSimuString(String savedSimuString){
		String[] t =(savedSimuString.split("\\|\\["))[1].replaceAll("\\]", "").split("\\[",3);
		int[][] savedSimuArray = new int[t.length][];
		for (int x = 0; x < t.length; x++) {
			String [] u = (t[x].length()==0)? new String[0] : t[x].replaceAll("\\s", "").split(",");
			savedSimuArray[x] = new int[u.length];
			for(int y=0;y<u.length;y++)
			try {
				savedSimuArray[x][y] = Integer.parseInt(u[y]);
				}
			catch (NumberFormatException nfe) {System.out.println("Pb de passage vers les entiers");};
		}
		return savedSimuArray;
	}
	
	static String savedSimuStringOfSavedSimuNameAndArray(String savedSimuName, int[][] savedSimuArray){
		String aux = "";
		for(int i=0; i<savedSimuArray.length;i++)aux += Arrays.toString(savedSimuArray[i]);
		return savedSimuName+"|"+aux;
	}

	/**
	 * Getter de la liste de simulation sauvegardé
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @return
	 */
	public static ArrayList<String> getSavedSimuList() {
		return savedSimuList;
	}

	public static void setSavedSimuList(ArrayList<String> savedSimuList) {
		Saving.savedSimuList = savedSimuList;
	}
}
