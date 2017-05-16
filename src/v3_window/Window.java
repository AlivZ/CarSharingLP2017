package v3_window;



import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.Timer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Pack_Genetique.Passager;
import v3_algo.Execut_Algo_Genetique;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Classe qui gére la création de la fenetre
 * @author AirDur
 * @version Build III -  v0.5
 * @since Build III -  v0.5
 */
public class Window extends JPanel{
	
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/      	

		/**
	   * Adresse vers le document qui comporte tout les textes
	   */
	  private final static String file = "lib/Textes.json";
	  
	  private static final long serialVersionUID = 1L;
	  // Objets qui permet de lire le document :   
	  private JSONParser parser = new JSONParser();
	  protected JSONObject JSON_Window, JSON_Boutons;
	  
	  //Selecteurs pour le nombre de colonnes et de lignes : 
	  JSpinner rowsSpinner, columnsSpinner; // Spinners for entering # of rows and columns
	  
	  //Variables pour la gestion de la grille : (nombre de ligne, de colonne, taille de la cellule et taille de la flèche 
	  public int rows, columns, squareSize/*, arrowSize*/;
	  
	  //Listes de cellules : 
	  public ArrayList<Cell> list_car  = new ArrayList<Cell>(); // the initial position of cars
	  public ArrayList<Cell> list_client_  = new ArrayList<Cell>(); // the initial positions of clients
	  public ArrayList<Cell> list_client_depot  = new ArrayList<Cell>(); //the wanted position of clients
	  public ArrayList<Cell> list_block  = new ArrayList<Cell>(); //the wanted position of clients
	  
	  Passager liste_passager;
	  
	  //Les messages :
	  JLabel message, car, client, closed, frontier;  
	  
	  // Les buttons :
	  JButton newgridButton, randomButton, clearButton, clear_2Button, realTimeButton, stepButton, animationButton, aboutButton,changeStateButton;
	  
	  // Selecteurs : 
	  JSlider slider;
	  JLabel velocity, rowsLbl, columnsLbl;
	  
	  //La Grille : 
	  public Cell[][] grid;        // the grid
	  
	  //L'Algorithme :
	  Execut_Algo_Genetique algo;
	  
	  //Valeurs importantes :
	  boolean typeColoriage = false; //type de 
	  boolean promptSelected; //Déclare ce qu'on regarde.
	  boolean realTime;    // Solution is displayed instantly
	  boolean found;       // flag that the goal was found
	  boolean searching;   // flag that the search is in progress
	  boolean endOfSearch; // flag that the search came to an end
	  int delay;           // time delay of animation (in msec)
	  int expanded;        // the number of nodes that have been expanded
	  Timer timer; // Vitesse d'execution de l'algorithme
	  
	  // Controleur de l'animation : 
	  RepaintAction action;
	  
	// A vérifier utilité : 
		  ArrayList<Cell> openSet   = new ArrayList<Cell>();// the OPEN SET
		  ArrayList<Cell> closedSet = new ArrayList<Cell>();// the CLOSED SET
		  ArrayList<Cell> graph     = new ArrayList<Cell>();// the set of vertices of the graph
		                                              // to be explored by Dijkstra's algorithm
	 
	  
	
	  
	  /*
	   _____                _                   _                  
	  / ____|              | |                 | |                 
	 | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
	 | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
	 | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
	  \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|   
	  
	  */
    
	/**
	 * Constructeur de la fenetre
	 * @version Build III -  v0.5
	 * @since Build III -  v0.5
	 */
	 public Window(int width, int height) {
		 //Initialisations :
		 
		 	this.rows    = 41;         
			this.columns = 41;
			this.squareSize = 500/rows;
			
			//Récupération de document !
		    try 							{ this.ouvertureFichier(); } 
		    catch (FileNotFoundException e) { e.printStackTrace(); }
		    catch (IOException e) 			{ e.printStackTrace(); }
		    catch (ParseException e) 		{ e.printStackTrace(); }
		    
		    // Paramètres général :
	        setLayout(null);
	        this.action = new RepaintAction(this);
	        MouseHandler listener = mouseListener(); 
	        setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.blue));
	        setPreferredSize( new Dimension(width,height) );

	        // Création du message : 
	        this.initialistionMessage();

	        // Création des Selectors : 
	        this.rowsLbl = this.createRowSelector();
	        this.columnsLbl = this.createColumnSelector();
	        this.velocity = this.createSpeedSelector();

	        //Création des buttons : 
	        this.newgridButton = createButton("New Grid",this.newgridButton);
	        this.randomButton = createButton("Random",this.randomButton);
	        this.clearButton = createButton("Clear",this.clearButton);
	        this.clear_2Button = createButton("Clear2", this.clear_2Button);
	        this.realTimeButton = createButton("RealTime",this.realTimeButton);
	        this.stepButton = createButton("StepByStep",this.stepButton);
	        this.animationButton = createButton("Animation",this.animationButton);							
	        this.aboutButton = createButton("About",this.aboutButton);
	        this.changeStateButton = createButton("ChangeState",this.changeStateButton);
	     
	        // we add the contents of the panel
	        add(this.message);
	        add(this.rowsLbl);
	        add(this.rowsSpinner);
	        add(this.columnsLbl);
	        add(this.columnsSpinner);
	        add(this.newgridButton);
	        add(this.randomButton);
	        add(this.clearButton);
	        add(this.clear_2Button);
	        add(this.realTimeButton);
	        add(this.stepButton);
	        add(this.animationButton);
	        add(this.changeStateButton);
	        add(this.slider);
	        add(this.velocity);
	        add(this.aboutButton);

	        // we regulate the sizes and positions
	        this.message.setBounds(0, 515, 500, 23);
	        this.rowsLbl.setBounds(520, 5, 130, 25);
	        this.rowsSpinner.setBounds(655, 5, 35, 25);
	        this.columnsLbl.setBounds(520, 35, 130, 25);
	        this.columnsSpinner.setBounds(655, 35, 35, 25);
	        this.newgridButton.setBounds(520, 65, 170, 25);
	        this.randomButton.setBounds(520, 95, 170, 25);
	        this.clearButton.setBounds(520, 125, 170, 25);
	        this.clear_2Button.setBounds(520, 155, 170, 25);
	        this.realTimeButton.setBounds(520, 185, 170, 25);
	        this.stepButton.setBounds(520, 215, 170, 25);
	        this.animationButton.setBounds(520, 245, 170, 25);
	        this.changeStateButton.setBounds(520, 275, 170, 25);
	        this.velocity.setBounds(520, 315, 170, 10);
	        this.slider.setBounds(520, 325, 170, 25);
	        this.aboutButton.setBounds(520, 515, 170, 25);

	        // we create the timer
	        this.timer = new Timer(this.delay, this.action);
	       
	        //this.randomButton.setEnabled(false); 
	        
	        //Création de la grille :
	        this.createGrid();
	        this.fillGrid();
	
	    } // end constructor
	
	 /*
	  __  __      _   _               _     _                   _   _             
	 |  \/  |    | | | |             | |   | |                 | | (_)            
	 | \  / | ___| |_| |__   ___   __| |___| |_ _ __ _   _  ___| |_ _  ___  _ __  
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` / __| __| '__| | | |/ __| __| |/ _ \| '_ \ 
	 | |  | |  __/ |_| | | | (_) | (_| \__ \ |_| |  | |_| | (__| |_| | (_) | | | |
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|___/\__|_|   \__,_|\___|\__|_|\___/|_| |_|
	                                                                              
	  */                                                                           
	

	/**
		 * Ajoute un écouteur sur la souris
		 * @return Un écouteur sur la souris
		 * @see MouseHandler
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		private MouseHandler mouseListener() {
			MouseHandler listener = new MouseHandler(this);
	        addMouseListener(listener);
	        addMouseMotionListener(listener);
	        return listener;
		}

	    /**
	     * Ouvre le fichier contenant les données.
	     * Permet à terme de généraliser afin d'avoir plusieurs langages.
	     * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     * @throws FileNotFoundException
	     * @throws IOException
	     * @throws ParseException
	     */
	    private void ouvertureFichier() throws FileNotFoundException, IOException, ParseException {
	    	Object obj = this.parser.parse(new FileReader(Window.file));
	    	JSONObject tampon = (JSONObject) obj;
	    	this.JSON_Window = (JSONObject) tampon.get("Window");
			this.JSON_Boutons = (JSONObject) this.JSON_Window.get("Button");
	    }
	    
	    /**
	     * Créer le curseur de sélection de vitesse
	     * @return
	     * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     */
	    private JLabel createSpeedSelector() {
	    	JLabel velocity = new JLabel((String) this.JSON_Window.get("labelSpeedSelector"), JLabel.CENTER);
	        velocity.setFont(new Font("Helvetica",Font.PLAIN,10));
	        
	        this.slider = new JSlider(0,2000,1000);
	        this.slider.setToolTipText((String) this.JSON_Window.get("tooltipSpeedSelector"));
	        
	        this.delay = 2000-this.slider.getValue();
	        this.slider.addChangeListener((ChangeEvent evt) -> {
	            JSlider source = (JSlider)evt.getSource();
	            if (!source.getValueIsAdjusting()) {
	                this.delay = 2000-source.getValue();
	            }
	        });
	        return velocity;
	    }
	    
		/*
		   _____      _           _                      
		  / ____|    | |         | |                     
		 | (___   ___| | ___  ___| |_ ___ _   _ _ __ ___ 
		  \___ \ / _ \ |/ _ \/ __| __/ _ \ | | | '__/ __|
		  ____) |  __/ |  __/ (__| ||  __/ |_| | |  \__ \
		 |_____/ \___|_|\___|\___|\__\___|\__,_|_|  |___/
		 */                                                
		
		
		/**
		 * Initialise l'affichage du message
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		private void initialistionMessage() {
			this.message = new JLabel((String) this.JSON_Window.get("msgDrawAndSelect"), JLabel.CENTER);
	        this.message.setForeground(Color.blue);
	        this.message.setFont(new Font("Helvetica",Font.PLAIN,16));
		}
		
		/**
		 * Créer un sélecteur de ligne
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		private JLabel createRowSelector() {
			JLabel rowsLbl = new JLabel((String) this.JSON_Window.get("labelRow"), JLabel.RIGHT);
		    rowsLbl.setFont(new Font("Helvetica",Font.PLAIN,13));
		    SpinnerModel rowModel = new SpinnerNumberModel(this.rows /*initial*/, 5 /*min*/,83 /* max */, 1 /* step */);	    
		    this.rowsSpinner = new JSpinner(rowModel);
		    return rowsLbl;
		}
		
		/**
		 * Créer un sélecteur de colonne
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		private JLabel createColumnSelector() {
		    JLabel columnsLbl = new JLabel((String) this.JSON_Window.get("labelColumn"), JLabel.RIGHT);
		    columnsLbl.setFont(new Font("Helvetica",Font.PLAIN,13));
		    SpinnerModel colModel = new SpinnerNumberModel(this.columns /*initial*/, 5 /*min*/,83 /* max */, 1 /* step */);
		    this.columnsSpinner = new JSpinner(colModel);
		    return columnsLbl;
		}
		
		/*
		 ____              _                  
		 |  _ \            | |                 
		 | |_) | ___  _   _| |_ ___  _ __  ___ 
		 |  _ < / _ \| | | | __/ _ \| '_ \/ __|
		 | |_) | (_) | |_| | || (_) | | | \__ \
		 |____/ \___/ \__,_|\__\___/|_| |_|___/
		                                       
		*/
		
		/**
		 * Créateur Générique de bouton
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		protected JButton createButton(String name, JButton button) {
			JSONArray texte = (JSONArray) this.JSON_Boutons.get(name);
			button = new JButton((String) texte.get(0));
			button.addActionListener(new ActionHandler(this, name));
			button.setBackground(Color.white);
			button.setToolTipText((String) texte.get(1));
			return button;
		}
		
		protected void editButton(String name, JButton button) {
			JSONArray texte = (JSONArray) this.JSON_Boutons.get(name);
			button.setText((String) texte.get(0));
			button.removeActionListener(button.getActionListeners()[0]);
			button.addActionListener(new ActionHandler(this, name));
			button.setBackground(Color.white);
			button.setToolTipText((String) texte.get(1));
		}
		
		
		/*
		   _____      _ _ _      
		  / ____|    (_) | |     
		 | |  __ _ __ _| | | ___ 
		 | | |_ | '__| | | |/ _ \
		 | |__| | |  | | | |  __/
		  \_____|_|  |_|_|_|\___|
		                         
		                         
		*/

		/**
	     * Création de la grille (que du vide)
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     */
		private void createGrid() {
			this.grid = new Cell[this.rows][this.columns];
			for (int r = 0; r < this.rows; r++) {
                for (int c = 0; c < this.columns; c++) {
                	this.grid[r][c] = new Cell(r, c, States.VOID);
                	this.grid[r][c].setStates(States.VOID);
                }
            }
		}
		
	    /**
	     * Remplissage de la grille 
	     * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     */
	    public void fillGrid() {
            if (this.searching || this.endOfSearch){ 
                for (int r = 0; r < this.rows; r++) {
                    for (int c = 0; c < this.columns; c++) {
                        if (this.grid[r][c].getStates() == States.CAR){
                            this.list_car.add(this.grid[r][c]);
                        }
                        if (this.grid[r][c].getStates() == States.START_CLIENT){
                            this.list_client_.add(this.grid[r][c]);
                        }
                        if(this.grid[r][c].getStates() == States.END_CLIENT) {
                        	this.list_client_depot.add(this.grid[r][c]);
                        }
                    }
                }
                //this.searching = false;
            } else {
                this.resetGrid();
            }
            /*for(int i = 0; i<this.list_car.size(); i++) {
            	this.list_car.get(i).setGHF(0);
            }*/
            
            this.expanded = 0;
            this.found = false;
            this.searching = false;
            this.endOfSearch = false;
         
            // The first step of the other four algorithms is here
            // 1. OPEN SET: = [So], CLOSED SET: = []
            this.openSet.removeAll(this.openSet);
            for(int i = 0; i<this.list_car.size(); i++) {
        		 this.openSet.add(this.list_car.get(i));
        	}
            this.closedSet.removeAll(this.closedSet);
            
            for(int i = 0; i<this.list_car.size(); i++) {
       		 this.grid[this.list_car.get(i).row][this.list_car.get(i).col].setStates(States.CAR);
            }
            for(int i = 0; i<this.list_client_.size(); i++) {
            	this.grid[this.list_client_.get(i).row][this.list_client_.get(i).col].setStates(States.START_CLIENT);
            }
            for(int i = 0; i<this.list_car.size(); i++) {
            	this.grid[this.list_client_depot.get(i).row][this.list_client_depot.get(i).col].setStates(States.END_CLIENT);
            }
            this. message.setText((String) this.JSON_Window.get("msgDrawAndSelect"));
            this.timer.stop();
            super.repaint();   
        }
	    
	    /**
	     * Reset de la grille 
	     * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     */
	    public void resetGrid() {
	    	for (int r = 0; r < this.rows; r++) {
                for (int c = 0; c < this.columns; c++) {
                	this.grid[r][c] = new Cell(r, c, States.VOID);
                }
            }
            this.list_car  = new ArrayList<Cell>(); // the initial position of cars
            this.list_client_  = new ArrayList<Cell>(); // the initial positions of clients
          	this.list_client_depot  = new ArrayList<Cell>(); //the wanted position of clients
          	this.list_block  = new ArrayList<Cell>();
	    }
	    
	    /**
		 * Réinitialise la grille selon de nouveaux paramètres (taille de lignes et colonnes)
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		public void initializeGrid() {
            this.rows    = (int)(this.rowsSpinner.getValue());
            this.columns = (int)(this.columnsSpinner.getValue());
            this.squareSize = 500/(this.rows > this.columns ? this.rows : this.columns);
           
            this.grid = new Cell[this.rows][this.columns];
            this.resetGrid();
            
            this.slider.setValue(1000);
            this.fillGrid();
        }
		
		/**
		 * Supprime les obstacles
		 * @version Build III -  v0.5
		 * @since Build III -  v0.5
		 */
		public void deleteWallGrid() {
			 for (int r = 0; r < this.rows; r++) {
                 for (int c = 0; c < this.columns; c++) {
                     if (this.grid[r][c].getStates() == States.WALL) {
                         this.grid[r][c].setStates(States.VOID);
                     }
                 }
             }
			 this.list_block  = new ArrayList<Cell>();
			 super.repaint();
		}
	    
	    public void repaint() {
	    	super.repaint();
	    }
	    
	    
	    /*
	     _____ _             
	    / ____| |            
	   | (___ | |_ ___ _ __  
	    \___ \| __/ _ \ '_ \ 
	    ____) | ||  __/ |_) |
	   |_____/ \__\___| .__/ 
	                  | |    
	                  |_|    
	     */
	    
	    
	    @Override
	    /**
	     * Affiche la grille. 
	     * @version Build III -  v0.5
		 * @since Build III -  v0.5
	     */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);  // Fills the background color.

            g.setColor(Color.DARK_GRAY);
            g.fillRect(10, 10, columns*squareSize+1, rows*squareSize+1);

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                	switch (this.grid[r][c].getStates()) {
                		case VOID : 
                			g.setColor(Color.WHITE); break;
                		case CAR :
                			 g.setColor(Color.RED); break;
                		case START_CLIENT :
                			g.setColor(Color.GREEN); break;
                		case END_CLIENT :
                			g.setColor(Color.YELLOW); break;
                		case WALL :
                			g.setColor(Color.BLACK); break;
                	}
                	g.fillRect(11 + c*squareSize, 11 + r*squareSize, squareSize - 1, squareSize - 1);
                }
            }
	    }

		public void checkTermination() {
			// TODO Auto-generated method stub
	        this.expandNode();
            if (this.found) {
                this.endOfSearch = true;
                this.plotRoute();
                this.stepButton.setEnabled(false);
                this.animationButton.setEnabled(false);
                this.slider.setEnabled(false);
                repaint();
            }
		}

		private void plotRoute() {
			// TODO Auto-generated method stub
			
		}

		 /**
         * Expands a node and creates his successors
         */
        private void expandNode(){
           /*
                Cell current;
                
                    // Here is the 3rd step of the algorithms A* and Greedy
                    // 3. Remove the first state, Si, from OPEN SET,
                    // for which f(Si) â‰¤ f(Sj) for all other
                    // open states Sj  ...
                    // (sort first OPEN SET list with respect to 'f')
                    Collections.sort(openSet, new CellComparatorByF());
                    current = openSet.remove(0);
                
                // ... and add it to CLOSED SET.
                closedSet.add(0,current);
                // Update the color of the cell
                grid[current.row][current.col] = CLOSED;
                // If the selected node is the target ...
                if (current.row == targetPos.row && current.col == targetPos.col) {
                    // ... then terminate etc
                    Cell last = targetPos;
                    last.prev = current.prev;
                    closedSet.add(last);
                    found = true;
                    return;
                }
                // Count nodes that have been expanded.
                expanded++;
                // Here is the 4rd step of the algorithms
                // 4. Create the successors of Si, based on actions
                //    that can be implemented on Si.
                //    Each successor has a pointer to the Si, as its predecessor.
                //    In the case of DFS and BFS algorithms, successors should not
                //    belong neither to the OPEN SET nor the CLOSED SET.
                ArrayList<Cell> succesors;
                succesors = createSuccesors(current, false);
                // Here is the 5th step of the algorithms
                // 5. For each successor of Si, ...
                succesors.stream().forEach((cell) -> {
                    // ... if we are running DFS ...
                   
                        int dxg = current.col-cell.col;
                        int dyg = current.row-cell.row;
                        int dxh = targetPos.col-cell.col;
                        int dyh = targetPos.row-cell.row;
                       
                            
                                cell.g = current.g+Math.abs(dxg)+Math.abs(dyg);
                            
                            cell.h = Math.abs(dxh)+Math.abs(dyh);
                        
                        cell.f = cell.g+cell.h;
                        // ... If Sj is neither in the OPEN SET nor in the CLOSED SET states ...
                        int openIndex   = isInList(openSet,cell);
                        int closedIndex = isInList(closedSet,cell);
                        if (openIndex == -1 && closedIndex == -1) {
                            // ... then add Sj in the OPEN SET ...
                            // ... evaluated as f(Sj)
                            openSet.add(cell);
                            // Update the color of the cell
                            grid[cell.row][cell.col] = FRONTIER;
                            // Else ...
                        } else {
                            // ... if already belongs to the OPEN SET, then ...
                            if (openIndex > -1){
                                // ... compare the new value assessment with the old one. 
                                // If old <= new ...
                                if (openSet.get(openIndex).f <= cell.f) {
                                    // ... then eject the new node with state Sj.
                                    // (ie do nothing for this node).
                                    // Else, ...
                                } else {
                                    // ... remove the element (Sj, old) from the list
                                    // to which it belongs ...
                                    openSet.remove(openIndex);
                                    // ... and add the item (Sj, new) to the OPEN SET.
                                    openSet.add(cell);
                                    // Update the color of the cell
                                    grid[cell.row][cell.col] = FRONTIER;
                                }
                                // ... if already belongs to the CLOSED SET, then ...
                            } else {
                                // ... compare the new value assessment with the old one. 
                                // If old <= new ...
                                if (closedSet.get(closedIndex).f <= cell.f) {
                                    // ... then eject the new node with state Sj.
                                    // (ie do nothing for this node).
                                    // Else, ...
                                } else {
                                    // ... remove the element (Sj, old) from the list
                                    // to which it belongs ...
                                    closedSet.remove(closedIndex);
                                    // ... and add the item (Sj, new) to the OPEN SET.
                                    openSet.add(cell);
                                    // Update the color of the cell
                                    grid[cell.row][cell.col] = FRONTIER;
                                }
                            }
                        }
                    
                });
            */
        } //end expandNode()
        
        /**
         * Creates the successors of a state/cell
         * 
         * @param current       the cell for which we ask successors
         * @param makeConnected flag that indicates that we are interested only on the coordinates
         *                      of cells and not on the label 'dist' (concerns only Dijkstra's)
         * @return              the successors of the cell as a list
         */
        private ArrayList<Cell> createSuccesors(Cell current, boolean makeConnected){
        	return null;
           /*
             int r = current.row;
           
            int c = current.col;
            // We create an empty list for the successors of the current cell.
            ArrayList<Cell> temp = new ArrayList<>();
            // With diagonal movements priority is:
            // 1: Up 2: Up-right 3: Right 4: Down-right
            // 5: Down 6: Down-left 7: Left 8: Up-left
            
            // Without diagonal movements the priority is:
            // 1: Up 2: Right 3: Down 4: Left
            
            // If not at the topmost limit of the grid
            // and the up-side cell is not an obstacle ...
            if (r > 0 && grid[r-1][c] != OBST &&
                    // ... and (only in the case are not running the A* or Greedy)
                    // not already belongs neither to the OPEN SET nor to the CLOSED SET ...
                    ((aStar.isSelected() || greedy.isSelected() || dijkstra.isSelected()) ? true :
                          isInList(openSet,new Cell(r-1,c)) == -1 &&
                          isInList(closedSet,new Cell(r-1,c)) == -1)) {
                Cell cell = new Cell(r-1,c);
                // In the case of Dijkstra's algorithm we can not append to
                // the list of successors the "naked" cell we have just created.
                // The cell must be accompanied by the label 'dist',
                // so we need to track it down through the list 'graph'
                // and then copy it back to the list of successors.
                // The flag makeConnected is necessary to be able
                // the present method createSuccesors() to collaborate
                // with the method findConnectedComponent(), which creates
                // the connected component when Dijkstra's initializes.
              
                    // ... update the pointer of the up-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the up-side cell to the successors of the current one. 
                    temp.add(cell);
                 
            }
           
            // If not at the rightmost limit of the grid
            // and the right-side cell is not an obstacle ...
            if (c < columns-1 && grid[r][c+1] != OBST &&
                    // ... and (only in the case are not running the A* or Greedy)
                    // not already belongs neither to the OPEN SET nor to the CLOSED SET ...
                    ((aStar.isSelected() || greedy.isSelected() || dijkstra.isSelected())? true :
                          isInList(openSet,new Cell(r,c+1)) == -1 &&
                          isInList(closedSet,new Cell(r,c+1)) == -1)) {
                Cell cell = new Cell(r,c+1);
                
                    // ... update the pointer of the right-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the right-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
          
            // If not at the lowermost limit of the grid
            // and the down-side cell is not an obstacle ...
            if (r < rows-1 && grid[r+1][c] != OBST &&
                    // ... and (only in the case are not running the A* or Greedy)
                    // not already belongs neither to the OPEN SET nor to the CLOSED SET ...
                    ((aStar.isSelected() || greedy.isSelected() || dijkstra.isSelected()) ? true :
                          isInList(openSet,new Cell(r+1,c)) == -1 &&
                          isInList(closedSet,new Cell(r+1,c)) == -1)) {
                Cell cell = new Cell(r+1,c);
                
                   // ... update the pointer of the down-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the down-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
            
            // If not at the leftmost limit of the grid
            // and the left-side cell is not an obstacle ...
            if (c > 0 && grid[r][c-1] != OBST && 
                    // ... and (only in the case are not running the A* or Greedy)
                    // not already belongs neither to the OPEN SET nor to the CLOSED SET ...
                    ((aStar.isSelected() || greedy.isSelected() || dijkstra.isSelected()) ? true :
                          isInList(openSet,new Cell(r,c-1)) == -1 &&
                          isInList(closedSet,new Cell(r,c-1)) == -1)) {
                Cell cell = new Cell(r,c-1);
               
                   // ... update the pointer of the left-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the left-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
            
          
            return temp;
            */
        } // end createSuccesors()
}
