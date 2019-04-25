package oop.board;

import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import oop.board.square.Square;
import oop.board.square.Squares;
import oop.view.MainView;

public class BasicGameBoard extends Board implements Squares {
	 
	// create a pane to hold squares
	private GridPane gridPaneBasic;
	
	//private String marker1, marker2;
	private int boardID;
	private int uniqueTileID;
	private int numberOfCells;
	private int[] territorySpots = new int[4];
	
	private boolean isMarked = false;
	private boolean containsUniqueTile = false;
	
	private String boardMarker;
	
	public Square[][] basicTwoD;
	//private int boardFormat = 0;
	// constructor
	public BasicGameBoard(int id, boolean hasUniqueTile, int numberOfCells, String boardMarker) {
		boardID = id;
		this.numberOfCells = numberOfCells;
		this.boardMarker = boardMarker;
		gridPaneBasic = new GridPane();
		basicTwoD = new Square[numberOfCells][numberOfCells];
		int squareID = 0;
		this.containsUniqueTile = hasUniqueTile;
		
		// generate four random spots (neutral spots and unpickable spots) if gameVersion is 5
		if (MainView.getGameVersion() == 5) {
			territorySpots = new Random().ints(0, 25).distinct().limit(4).toArray();
			//System.out.println(territorySpots[0] + " " + territorySpots[1] + " " + territorySpots[2] + " " + territorySpots[3] + " ");
		}
		
		// unique tile
		if (this.containsUniqueTile) {
			uniqueTileID = (int)(Math.random()*(Math.pow(numberOfCells, 2)));
		}
		
		
		// initialize basicTwoD
		for (int i = 0; i < numberOfCells; i ++) {
			//System.out.println("create ultimate game board");
			for (int j = 0; j < numberOfCells; j++, squareID++) {
				gridPaneBasic.add(basicTwoD[i][j] = new Square(squareID, boardID, "   "),j,i);
				if (this.containsUniqueTile && squareID == uniqueTileID) {
					basicTwoD[i][j].setIsTrap(true);
					System.out.println("trap tile row: " + i + " col: " + j);
				}
				
				if (MainView.getGameVersion() == 5 && (squareID == territorySpots[0] || squareID == territorySpots[1])) {
					basicTwoD[i][j].setIsUnpickable(true);
					basicTwoD[i][j].setMarker("✖ ", false);
					basicTwoD[i][j].getStyleClass().add("unpickable");
				}else if (MainView.getGameVersion() == 5 && (squareID == territorySpots[2] || squareID == territorySpots[3])) {
					basicTwoD[i][j].setIsNeutral(true);
					basicTwoD[i][j].setMarker("✔ ", false);
					basicTwoD[i][j].getStyleClass().add("neutral");
				}

			}
		}

		MainView.ticTacToe.validBasicGameBoardMap.put(boardID, basicTwoD);
		//System.out.println("create board id: " + boardID);
		//Setting the Grid alignment 
        gridPaneBasic.setAlignment(Pos.CENTER);
       // gridPaneBasic.getStyleClass().add("basicBoard");
	} // end of constructor 
	
	
	// return basicTwoD
	public  Square[][] getBasicTwoD() {
		return this.basicTwoD;
	}

	// check if there is a winner
	public boolean hasWon(int row, int col, String marker) {
		
		if (basicTwoD[row][col].getIsTrap()) {
			MainView.ticTacToe.setUniqueTileClicked(true);
          	return false;
		}
		
		boolean hasWon = true;
		// check row
        for(int i=0; i< numberOfCells; i++){
        	if(!(basicTwoD[row][i].getMarker()).equals(marker) && !(basicTwoD[row][i].getIsNeutral())){
            	hasWon = false;
            } 
        }
        
        if (hasWon) {
        	return hasWon;
        }
        
        hasWon = true;
    	// check column
        for(int i=0; i<numberOfCells; i++){
        	if(basicTwoD[i][col].getIsTrap() || (!(basicTwoD[i][col].getMarker()).equals(marker) && !(basicTwoD[i][col].getIsNeutral()))){
            	hasWon = false;
            }
        }
	    
	    if (hasWon) return hasWon;
	    
	    hasWon = true;
        // check back diagonal
        for(int i=0; i<numberOfCells; i++){
        	if(basicTwoD[i][i].getIsTrap() || (!(basicTwoD[i][i].getMarker()).equals(marker) && !(basicTwoD[i][i].getIsNeutral()))){		        		
        		hasWon = false;
        	}
        	
        }

        if (hasWon) return hasWon;
        
        hasWon = true;
        // check forward diagonal
        for(int i=0, j=numberOfCells-1; i<numberOfCells; i++, j--){
        	if(basicTwoD[i][j].getIsTrap() || (!(basicTwoD[i][j].getMarker()).equals(marker) && !(basicTwoD[i][j].getIsNeutral()))){
        		hasWon = false;	
        	}
        	 
        }
    
	    
        return hasWon;
	} // end of hasWon
			
			
		public void reset() {
			this.containsUniqueTile = MainView.ticTacToe.getContainsUniqueTile();
			if (MainView.getGameVersion() != 2) {
				gridPaneBasic.getChildren().clear();
			}
			// generate four random spots (neutral spots and unpickable spots) if gameVersion is 5
			if (MainView.getGameVersion() == 5) {
				territorySpots = new Random().ints(0, 25).distinct().limit(4).toArray();
				//System.out.println(territorySpots[0] + " " + territorySpots[1] + " " + territorySpots[2] + " " + territorySpots[3] + " ");
			}
			
			// unique tile
			if (this.containsUniqueTile) {
				uniqueTileID = (int)(Math.random()*(Math.pow(numberOfCells, 2)));
			}
			
			
			//System.out.println("basic gmae board id: " + boardID + " number of cells: " + numberOfCells);
			//MainView.ticTacToe.validBasicGameBoardMap.put(0, this.basicTwoD);
			for (int i = 0; i < numberOfCells; i ++) {
				for (int j = 0; j < numberOfCells; j++) {
					this.basicTwoD[i][j].setMarker("   ", true);
					this.basicTwoD[i][j].getStyleClass().removeIf(style -> style.equals("unpickable"));
					this.basicTwoD[i][j].getStyleClass().removeIf(style -> style.equals("neutral"));
					if (MainView.getGameVersion() != 2) {
						gridPaneBasic.add(basicTwoD[i][j], j, i);
					}
					basicTwoD[i][j].setIsTrap(false);
					
					if (this.containsUniqueTile && this.basicTwoD[i][j].getSquareID() == uniqueTileID) {
						basicTwoD[i][j].setIsTrap(true);
						System.out.println("trap tile row: " + i + " col: " + j);
					}
					
					if (MainView.getGameVersion() == 5 && (this.basicTwoD[i][j].getSquareID() == territorySpots[0] || this.basicTwoD[i][j].getSquareID() == territorySpots[1])) {
						basicTwoD[i][j].setIsUnpickable(true);
						basicTwoD[i][j].setMarker("✖ ", false);
						basicTwoD[i][j].getStyleClass().add("unpickable");
					}else if (MainView.getGameVersion() == 5 && (this.basicTwoD[i][j].getSquareID() == territorySpots[2] || this.basicTwoD[i][j].getSquareID() == territorySpots[3])) {
						basicTwoD[i][j].setIsNeutral(true);
						basicTwoD[i][j].setMarker("✔ ", false);
						basicTwoD[i][j].getStyleClass().add("neutral");
					}else {
						basicTwoD[i][j].setIsNeutral(false);
						basicTwoD[i][j].setIsUnpickable(false);
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
				if (MainView.getGameVersion() == 2) {
					// REMOVE style of previous moves
					if (MainView.ticTacToe.getPreviousMove() != null) {
						MainView.ticTacToe.getPreviousMove().getStyleClass().removeIf(style -> style.equals("previousMove"));
					}
				
					// ADD style to the square to show the latest move
					this.basicTwoD[row][col].getStyleClass().add("previousMove");
					MainView.ticTacToe.setPreviousMove(this.basicTwoD[row][col]);
				}
				
				
				return true;
			}
			return false;
			
		}
	
	// return the basic board
	public Pane display() {
		return gridPaneBasic;
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