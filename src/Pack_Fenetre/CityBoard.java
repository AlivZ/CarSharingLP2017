package Pack_Fenetre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import Pack_Appli.Application;
import Pack_Simu.Car;
import Pack_Simu.Client;


/**
 * CityBoard est l'élément représentant la ville
 * La classe permet de représenter visuellement les différents élements.
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class CityBoard extends JPanel
{
	private static final long serialVersionUID = -4239432318493357189L;
	
	/*TAILLE DU WIDGET*/
	private final int boardWidth = 25;
	private final int boardHeight = 25;
	private final int squareSize = 20;
	
	/**
	 *  adresse de l'application pour accéder aux variables de simulation
	 */
	private Application app;
	
	/**
	 * Création d'un objet CityBoard.
	 * 
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @param consApp
	 */
	protected CityBoard(Application consApp){
		Dimension boardDimension = new Dimension(getBoardWidth() * getSquareSize(),getBoardHeight() * getSquareSize());
		//La taille du Panel est fixée à  sa dimension initiale
		setMinimumSize(boardDimension);
		setMaximumSize(boardDimension);
		setPreferredSize(boardDimension);
		//Les événements souris seront traités par la classe ci-dessous
		addMouseListener(consApp);
		app = consApp;
	}
	
	
	/* DESSIN */
	/**
	 * dessine le cityboard, appelé par la fonction repaint()(non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 0; i<getBoardHeight();i++)
			for(int j = 0; j<getBoardWidth(); j++)
				if(i%(app.getBlockSize()+1)!=0 && j%(app.getBlockSize()+1)!=0)
					g.fillRect(i*getSquareSize(),j*getSquareSize(),getSquareSize(),getSquareSize());
		g.setFont(new Font(g.getFont().getFontName(),Font.BOLD,2*getSquareSize()/3));
		//On dessine toutes les voitures
		for(Car car: app.getSimu().getListeVoitures()) drawCar(g,car);
		if(app.getSimu().getNotTargettedYet() != null) drawClientPos(g,app.getSimu().getNotTargettedYet());
		for(Client cli: app.getSimu().getListeClients()){
			//On dessine les positions des clients non embarquÃ©s
			if(cli.getStateClient() == 0) drawClientPos(g,cli);
			//On dessine les destinations des clients dÃ©finis et non arrivÃ©s
			if(cli.getStateClient() < 2) drawClientTarget(g,cli);
		}
	}
	
	/**
	 * Dessine une voiture au point p
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void drawCar(Graphics g, Car car)
	{
		g.setColor((car.getIsDoingCarSharing())?Color.RED:Color.GREEN);
		int x = (int) (car.getPosCar().getX()/getSquareSize()*getSquareSize());
		int y = (int) (car.getPosCar().getY()/getSquareSize()*getSquareSize());
		g.fillPolygon(
				new int[]{x, x + getSquareSize()/4, x + getSquareSize()/2, x + 3*getSquareSize()/4, x + getSquareSize(),
						x + getSquareSize(), x},
						new int[]{y + getSquareSize()/2,y + getSquareSize()/4,y + getSquareSize()/4,
						y+getSquareSize()/2,y + getSquareSize()/2,y + 3*getSquareSize()/4,y + 3*getSquareSize()/4}, 7);
		g.fillOval(x+1*getSquareSize()/8, y+5*getSquareSize()/8, getSquareSize()/4, getSquareSize()/4);
		g.fillOval(x+5*getSquareSize()/8, y+5*getSquareSize()/8, getSquareSize()/4, getSquareSize()/4);
		drawId(g,car.getIdCar(),x,y);
	}

	/**
	 * Dessine la position d'un client sur le trottoir
	 * @param g
	 * @param cli
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void drawClientPos(Graphics g, Client cli){
		g.setColor(Color.BLUE);
		int x = (int) (cli.getPosClient()[0].getX()/getSquareSize()*getSquareSize());
		int y = (int) (cli.getPosClient()[0].getY()/getSquareSize()*getSquareSize());
		g.drawPolygon( new int[]{ x + getSquareSize()/4, x + 3*getSquareSize()/4, x + 3*getSquareSize()/4,
						x + getSquareSize()/2, x + getSquareSize()/4},
						new int[]{ y + getSquareSize()/4, y + getSquareSize()/4, y + getSquareSize(),
						y + 3*getSquareSize()/4, y + getSquareSize()}, 5);
		g.drawOval(x+3*getSquareSize()/8, y, getSquareSize()/4, getSquareSize()/4);
		drawId(g,cli.getIdClient(),x,y);
	}

	/**
	 * Dessine la destination d'un client
	 * @param g
	 * @param cli
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void drawClientTarget(Graphics g, Client cli)
	{
		g.setColor((cli.getIsUsingCarSharing())?Color.BLUE:Color.CYAN);
		int x = (int) (cli.getPosClient()[1].getX()/getSquareSize()*getSquareSize());
		int y = (int) (cli.getPosClient()[1].getY()/getSquareSize()*getSquareSize());
		g.fillPolygon(
				new int[]{x + getSquareSize()/4, x + 3*getSquareSize()/4, x + getSquareSize()/4},
						new int[]{ y, y + getSquareSize()/4, y + getSquareSize()/2}, 3);
		g.drawLine(x + getSquareSize()/4, y, x + getSquareSize()/4,y+getSquareSize());
		drawId(g,cli.getIdClient(),x,y);
	}
	
	/**
	 * Dessine le numéro de la voiture ou du client
	 * @param g
	 * @param id
	 * @param x
	 * @param y
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
	void drawId(Graphics g, int id, int x, int y){
		g.setColor(Color.BLACK);
		g.drawString(""+id,x + getSquareSize()/3, y + getSquareSize());
	}

	/**
	 * Récupère la taille des carrés
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 * @return
	 */
	public int getSquareSize() {
		return squareSize;
	}

	/**
	 * 
	 * @return
	 */
	public int getBoardWidth() {
		return boardWidth;
	}

	/**
	 * 
	 * @return
	 */
	public int getBoardHeight() {
		return boardHeight;
	}
}
