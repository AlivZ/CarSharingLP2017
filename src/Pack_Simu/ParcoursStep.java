package Pack_Simu;

/** Cette classe repr�sente un point de passage d'une voiture faisant du covoiturage
 * Il s'agit d'un point de mont�e ou de descente d'un client
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class ParcoursStep
{
	Client cli;
	int type;

	public ParcoursStep(Client cliCons, int typeCons){
		cli = cliCons;
		type = typeCons;
	}
}
