package v3_window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * Class that handles mouse movements as we "paint"
         * obstacles or move the robot and/or target.
 */
public class MouseHandler implements MouseListener, MouseMotionListener {
    //private int cur_row, cur_col, val_car, val_client_depart, val_cient_arrivee;
    private States cur_val;
    private Cell cur_cell;
    private Window win;
    
    public MouseHandler(Window win) {
    	this.win = win;
    }
    
    @Override
    public void mousePressed(MouseEvent evt) {
    	int row = (evt.getY() - 10) / this.win.squareSize;
        int col = (evt.getX() - 10) / this.win.squareSize;
        if (row >= 0 && row < this.win.rows && col >= 0 && col < this.win.columns) {
            if (this.win.realTime ? true : !this.win.found && !this.win.searching){
                if (this.win.realTime) {
                	this.win.searching = true;
                	this.win.fillGrid();
                }
                cur_val = this.win.grid[row][col].getStates();
                cur_cell = this.win.grid[row][col];
                States st = this.win.grid[row][col].getStates();
                if(win.typeColoriage) {
            		if(evt.getButton()==MouseEvent.BUTTON1 && !(st == States.START_CLIENT || st == States.END_CLIENT)) {
        				this.win.grid[row][col].setStates(States.CAR);
            			this.win.list_car.add(this.win.grid[row][col]);
            		} else if (evt.getButton()==MouseEvent.BUTTON3 && !(st == States.CAR)) {
        				if((this.win.list_client_depot.size()+this.win.list_client_.size())%2 == 0) {
            				this.win.grid[row][col].setStates(States.START_CLIENT);
                			this.win.list_client_.add(this.win.grid[row][col]);
            			} else {
            				this.win.grid[row][col].setStates(States.END_CLIENT);
                			this.win.list_client_depot.add(this.win.grid[row][col]);
            			}
        			}
            	} else {
	                if (st == States.WALL){
	                	win.list_block.remove(this.win.grid[row][col]);
	                	this.win.grid[row][col].setStates(States.VOID);
	                } else if(st == States.VOID && !(st == States.START_CLIENT || st == States.END_CLIENT)) {
	                	this.win.grid[row][col].setStates(States.WALL);
	                	win.list_block.add(this.win.grid[row][col]);
	                }
	            }
	        }
	        if (this.win.realTime) {
	        	this.win.timer.setDelay(0);
	        	this.win.timer.start();
	        	this.win.checkTermination();
	        } else {
	        	this.win.repaint();
	        }
    	}
        
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
    	if(win.typeColoriage) {
    		
    	} else {	
	        int row = (evt.getY() - 10) / this.win.squareSize;
	        int col = (evt.getX() - 10) / this.win.squareSize;
	        if (row >= 0 && row < this.win.rows && col >= 0 && col < this.win.columns){
	            if (this.win.realTime ? true : !this.win.found && !this.win.searching){
	                if (this.win.realTime) {
	                	this.win.searching = true;
	                	this.win.fillGrid();
	                }
	                if ((row*this.win.columns+col != row*this.win.columns+col) && (cur_val == States.CAR || cur_val == States.START_CLIENT || cur_val == States.END_CLIENT)){
	                	
	                    States new_val = this.win.grid[row][col].getStates();
	                    if (new_val == States.VOID){
	                    	this.win.grid[row][col].setStates(cur_val);
	                    	if (cur_val == States.CAR) {
	                        	this.win.list_car.add(this.win.grid[row][col]);
	                        } else if(cur_val == States.START_CLIENT) {
	                        	this.win.list_client_.add(this.win.grid[row][col]);
	                        } else {
	                        	this.win.list_client_depot.add(this.win.grid[row][col]);
	                        	
	                        }
	                        this.win.grid[row][col].setStates(new_val);
	                        if (cur_val == States.CAR) {
	                        	this.win.list_car.add(this.win.grid[row][col]);
	                        } else if(cur_val == States.START_CLIENT) {
	                        	this.win.list_client_.add(this.win.grid[row][col]);
	                        } else {
	                        	this.win.list_client_depot.add(this.win.grid[row][col]);
	                        }
	                       
	                        cur_val = this.win.grid[row][col].getStates();
	                    }
	                } else if (this.win.grid[row][col].getStates() != States.WALL && this.win.grid[row][col].getStates() != States.CAR && this.win.grid[row][col].getStates() != States.START_CLIENT && this.win.grid[row][col].getStates() != States.END_CLIENT){
	                    	this.win.grid[row][col].setStates(States.WALL);
	                    	win.list_block.add(this.win.grid[row][col]);
	                }
	            }
	        }
	        if (this.win.realTime) {
	        	this.win.timer.setDelay(0);
	        	this.win.timer.start();
	        	this.win.checkTermination();
	        } else {
	        	this.win.repaint();
	        }
    	}
    }
    
    
    @Override
    public void mouseReleased(MouseEvent evt) { }
    @Override
    public void mouseEntered(MouseEvent evt) { }
    @Override
    public void mouseExited(MouseEvent evt) { }
    @Override
    public void mouseMoved(MouseEvent evt) { }
    @Override
    public void mouseClicked(MouseEvent evt) { }
    
} // end nested class MouseHandler
