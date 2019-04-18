package oop.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import oop.board.square.Square;
import oop.board.square.Squares;
import oop.view.MainView;

public class BasicGameBoard extends Board implements Squares {
	 
	// create a pane to hold squares
	public static GridPane gridPane;
	public Square[][] basicTwoD;
	//private String marker1, marker2;
	private int boardID;
	private String boardMarker;
	private boolean isMarked = false;
	private int numberOfCells;
	//private int boardFormat = 0;
	// constructor
	public BasicGameBoard(int id, int numberOfCells, String boardMarker) {
		boardID = id;
		this.numberOfCells = numberOfCells;
		this.boardMarker = boardMarker;
		gridPane = new GridPane();
		basicTwoD = new Square[numberOfCells][numberOfCells];
		int squareID = 0;		
		// initialize basicTwoD
		for (int i = 0; i < numberOfCells; i ++) {
			//System.out.println("create ultimate game board");
			for (int j = 0; j < numberOfCells; j++, squareID++) {
				gridPane.add(basicTwoD[i][j] = new Square(squareID, boardID, "   "),j,i);

			}
		}

		MainView.ticTacToe.validBasicGameBoardMap.put(boardID, basicTwoD);
		//System.out.println("create board id: " + boardID);
		//Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER);
       // gridPane.getStyleClass().add("basicBoard");
	} // end of constructor 
	
	
	// return basicTwoD
	public  Square[][] getBasicTwoD() {
		return this.basicTwoD;
	}

	// check if there is a winner
	public boolean hasWon(int row, int col, String marker) {
		boolean hasWon = true;
		// check row
        for(int i=0; i< numberOfCells; i++){
            if(!(basicTwoD[row][i].getMarker()).equals(marker)){
            	hasWon = false;
            }
        }
        
        if (hasWon) {
        	return hasWon;
        }
        
        hasWon = true;
    	// check column
        for(int i=0; i<numberOfCells; i++){
            if(!(basicTwoD[i][col].getMarker()).equals(marker)){
            	hasWon = false;
            }
        }
	    
	    if (hasWon) return hasWon;
	    
	    hasWon = true;
        // check back diagonal
        for(int i=0; i<numberOfCells; i++){
        	if(!(basicTwoD[i][i].getMarker()).equals(marker)){		        		
        		hasWon = false;
        	}
        }

        if (hasWon) return hasWon;
        
        hasWon = true;
        // check forward diagonal
        for(int i=0, j=numberOfCells-1; i<numberOfCells; i++, j--){
        	if(!(basicTwoD[i][j].getMarker()).equals(marker)){
        		hasWon = false;	
        	}
        }
    
	    
        return hasWon;
	} // end of hasWon
			
			
		public void reset() {
			if (MainView.getGameVersion() != 2) {
				gridPane.getChildren().clear();
			}
			
			//System.out.println("basic gmae board id: " + boardID + " number of cells: " + numberOfCells);
			//MainView.ticTacToe.validBasicGameBoardMap.put(0, this.basicTwoD);
			for (int i = 0; i < numberOfCells; i ++) {
				for (int j = 0; j < numberOfCells; j++) {
					this.basicTwoD[i][j].setMarker("   ", true);
					if (MainView.getGameVersion() != 2) {
						gridPane.add(basicTwoD[i][j], j, i);
					}
				}
			}
			
			
				
		} // end of reset
			
		// check if there is at least an empty space in the board
		public boolean isEmptySpaceAvailable() {
			
			for (int i = 0; i < numberOfCells; i ++) {
				for (int j = 0; j < numberOfCells; j++) {
					if (!this.basicTwoD[i][j].getIsMarked()) {
						return true;
					}
				}
			}
			return false;
		}
		
		// mark the board
		public boolean markBoard(int row, int col, String marker) {
			if((!this.basicTwoD[row][col].getIsMarked())) {
				this.basicTwoD[row][col].setMarker(marker, false);
				return true;
			}
			return false;
			
		}
	
	// return the basic board
	public Pane display() {
		return gridPane;
	}
	
	// return the marker for the basic board
	public String getMarker() {
		return boardMarker;
	}

	@Override
	public int getSquareID() {
		// TODO Auto-generated method stub
		return boardID;
	}

	@Override
	public void setMarker(String marker, boolean isReset) {
		// TODO Auto-generated method stub
		boardMarker = marker;
		if (!isReset) {
			isMarked = true;
			//System.out.println("Mark board: " + this.boardID);
			this.setCenter(new Text(marker));
			
		} // end of !isReset
		else {
			isMarked = false;
			this.setCenter(new Text(marker));
		} // end of isReset	
	}

	@Override
	public boolean getIsMarked() {
		// TODO Auto-generated method stub
		return isMarked;
	}
	

}