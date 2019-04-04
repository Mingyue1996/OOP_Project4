package oop.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import oop.board.square.Square;

public class BasicGameBoard {
	//private boolean hasWon = true;
	public static Square[][] basicTwoD = new Square[3][3];
	 
	// create a pane to hold squares
	GridPane gridPane = new GridPane();
	
	private String marker1, marker2;
	//private int boardFormat = 0;
	// constructor
	public BasicGameBoard() {
		int id = 0;
		// initialize basicTwoD
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3; j++, id++) {
				gridPane.add(basicTwoD[i][j] = new Square(id, "   "),j,i);
			}
		}
		 //Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER); 
	} // end of constructor 

	
	// reset the board
	public void reset() {
		//gridPane.getChildren().clear();
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3; j++) {
				basicTwoD[i][j].setMarker("   ", true);
				//gridPane.add(basicTwoD[i][j] = new Square(id, "   "),j,i);
			}
		}
		 //Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER); 
	}
	
	// display
	public Pane display() {
		return gridPane;
	}
	
	// mark the board
	public boolean markBoard(int row, int col, String marker) {
		if((!basicTwoD[row][col].getIsMarked())) {
			basicTwoD[row][col].setMarker(marker, false);
			return true;
		}
		return false;
		
	}
	
	// check if there is at least an empty space in the board
	public boolean isEmptySpaceAvailable() {
		
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3; j++) {
				if (!basicTwoD[i][j].getIsMarked()) {
					return true;
				}
			}
		}
		return false;
	}
	
	// check if there is a winner
	public boolean hasWon(int row, int col, String marker) {
//			System.out.println("marker: " + marker);
			// check if three same markers are in one row
			for (int i = 0; i < 3; i++) {
				if(basicTwoD[i][0].display().equals(marker)
				   && basicTwoD[i][1].display().equals(marker)
				   && basicTwoD[i][2].display().equals(marker)) {
					return true;
				}
			}
			
			// check if three same markers are in one column
			for (int j = 0; j < 3; j++) {
				if(basicTwoD[0][j].display().equals(marker)
				   && basicTwoD[1][j].display().equals(marker)
				   && basicTwoD[2][j].display().equals(marker)) {
					return true;
				}
			}
			
			//check if forward diagonal has three same markers
			if(basicTwoD[0][2].display().equals(marker)
			   && basicTwoD[1][1].display().equals(marker)
			   && basicTwoD[2][0].display().equals(marker)) {
				return true;
			}
		
				
			//check if forward diagonal has three same markers
			if(basicTwoD[0][0].display().equals(marker)
			   && basicTwoD[1][1].display().equals(marker)
			   && basicTwoD[2][2].display().equals(marker)) {
				return true;
			}

//			for (int i = 0; i< 3; i++) {
//				for (int j = 0; j <3; j++) {
//					System.out.print(basicTwoD[i][j].display()+ " ");
//				}
//				System.out.println();
//			}
			
	        return false;
	} // end of hasWon
	
	// set marker
	public void setMarker (String marker, int id) {
		if (id == 1) {
			marker1 = marker;
		}
		else {
			marker2 = marker;

		}
	} // end of setMarker
	

}