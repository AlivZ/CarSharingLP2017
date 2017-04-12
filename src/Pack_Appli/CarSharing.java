package Pack_Appli;

/** 
 * <h2>VERSION 6.3</h2>
 * Dernière modification le mardi 29 novembre 2016 à 14:26 par Romain HAGEMANN <br>
 * Contact : etienne.callies@polytechnique.org <br>
 * Encodage ISO-8859-1, Java 1.6 ou 1.7 <br>
 * <br>
 * CarSharing : <br>
 * n voitures, m clients, une destination et une voiture par client, covoiturage dynamique dans une ville type Manhattan avec embouteillages <br>
 * Algorithme : <br>
 * méthode déterministe ou méthode du recuit simulé <br>
 * Fonction de cout réaliste paramétrable <br>
 * Possibilité de rajouter des clients de maniêre aléatoire et continue <br>
 * Possibilité de régler la probabilité qu'un usager préfére sa voiture au systême de covoiturage<br>
 * Possibilité de voir en temps rêel les différentes données de simulation<br>
 * 
 * <h3> Consignes : </h3>
 * 
 * Variables à privilégier :
 * <ul> <li> i : abscisse en nombre de carrés </li>
 * <li> j : ordonnée en nombre de carrés </li>
 * <li> k : numéro de Car sélectionnée </li>
 * <li> l : numéro du Client sélectionné </li>
 * <li> m : une matrice </li>
 * <li> n : numéro de la simulation enregistrée sélectionnée </li>
 * <li> p : un Point </li>
 * <li> q : une ordonnée d'une matrice de passage, varie de 0 Ã  2*clientAlgoNumber-1,
 *      pair pour les positions, impair pour les target </li>
 * <li> t : un tableau </li>
 * <li> x : abscisse en nombre de pixels </li>
 * <li> y : ordonnée en nombre de pixels </li>
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
