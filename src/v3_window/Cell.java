package v3_window;

/**
* Helper class that represents the cell of the grid
*/
public class Cell {
	public int heuristicCost = 0; //Heuristic cost
	public int finalCost = 0; //G+H
   public int row;   // the row number of the cell(row 0 is the top)
   public int col;   // the column number of the cell (Column 0 is the left)
   /*int g;     // the value of the function g of A* and Greedy algorithms
   int h;     // the value of the function h of A* and Greedy algorithms
   int f;     // the value of the function h of A* and Greedy algorithms*/
   int dist;  // the distance of the cell from the initial position of the robot
              // Ie the label that updates the Dijkstra's algorithm
   public Cell parent; // Each state corresponds to a cell
              // and each state has a predecessor which
              // is stored in this variable
   
   States etat;
   
   public Cell(int row, int col){
      this.row = row;
      this.col = col;
   }
   
   public Cell(int row, int col, States etat){
	      this.row = row;
	      this.col = col;
	      this.etat = etat;
	   }
   
   public boolean isThisCell(int row, int col) {
	   boolean bool = true;
	   if(this.row!=row || this.col!=col) 
		   bool = false;
	   return bool;
   }
   
   /*public void setGHF(int n) {
	   this.g = n;
	   this.h = n;
	   this.f = n;
   }*/

	public States getStates() {
		return this.etat;
	}
	
	public void setStates(States etat) {
		this.etat = etat;
	}

	public int getRow() {
		return this.row;
	}

	public int getColumn() {
		return this.col;
	}
	
	public String toString() {
		return "[" + this.row + ";" + this.col + "]";
	}
} // end nested class Cell

