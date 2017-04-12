package Pack_Appli;

/** 
 * <h2>VERSION 6.3</h2>
 * Derni�re modification le mardi 29 novembre 2016 � 14:26 par Romain HAGEMANN <br>
 * Contact : etienne.callies@polytechnique.org <br>
 * Encodage ISO-8859-1, Java 1.6 ou 1.7 <br>
 * <br>
 * CarSharing : <br>
 * n voitures, m clients, une destination et une voiture par client, covoiturage dynamique dans une ville type Manhattan avec embouteillages <br>
 * Algorithme : <br>
 * m�thode d�terministe ou m�thode du recuit simul� <br>
 * Fonction de cout r�aliste param�trable <br>
 * Possibilit� de rajouter des clients de mani�re al�atoire et continue <br>
 * Possibilit� de r�gler la probabilit� qu'un usager pr�f�re sa voiture au syst�me de covoiturage<br>
 * Possibilit� de voir en temps r�el les diff�rentes donn�es de simulation<br>
 * 
 * <h3> Consignes : </h3>
 * 
 * Variables � privil�gier :
 * <ul> <li> i : abscisse en nombre de carr�s </li>
 * <li> j : ordonn�e en nombre de carr�s </li>
 * <li> k : num�ro de Car s�lectionn�e </li>
 * <li> l : num�ro du Client s�lectionn� </li>
 * <li> m : une matrice </li>
 * <li> n : num�ro de la simulation enregistr�e s�lectionn�e </li>
 * <li> p : un Point </li>
 * <li> q : une ordonn�e d'une matrice de passage, varie de 0 à 2*clientAlgoNumber-1,
 *      pair pour les positions, impair pour les target </li>
 * <li> t : un tableau </li>
 * <li> x : abscisse en nombre de pixels </li>
 * <li> y : ordonn�e en nombre de pixels </li>
 * <li> z : index d'un tableau quelconque </ul> <br>
 * Le reste en anglais en respectant la casse helloWorld
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 **/

class CarSharing
{
	/**
	 * lance l'application
	 * @param args
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public static void main(String args[])
	{
		new Application();
	} 
}
