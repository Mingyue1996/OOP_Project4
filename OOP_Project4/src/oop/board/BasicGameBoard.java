package oop.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import oop.board.square.Square;
import oop.board.square.Squares;

public class BasicGameBoard extends Board implements Squares {
	 
	// create a pane to hold squares
	public static GridPane gridPane;
	public static Square[][] basicTwoD;
	//private String marker1, marker2;
	private int boardID;
	private String boardMarker;
	private boolean isMarked = false;
	//private int boardFormat = 0;
	// constructor
	public BasicGameBoard(int id, String boardMarker) {
		boardID = id;
		this.boardMarker = boardMarker;
		gridPane = new GridPane();
		basicTwoD = new Square[3][3];
		int squareID = 0;
		// initialize basicTwoD
		for (int i = 0; i < 3; i ++) {
			//System.out.println("create ultimate game board");
			for (int j = 0; j < 3; j++, squareID++) {
				gridPane.add(basicTwoD[i][j] = new Square(squareID, boardID, "   "),j,i);
			}
		}
		//System.out.println("create board id: " + boardID);
		//Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER); 
	} // end of constructor 

	

	// check if there is a winner
	public boolean hasWon(String marker) {
		//System.out.println("boardID: " + this.boardID);
			// check if three same markers are in one row
			for (int i = 0; i < 3; i++) {
				if(basicTwoD[i][0].getMarker().equals(marker)
				   && basicTwoD[i][1].getMarker().equals(marker)
				   && basicTwoD[i][2].getMarker().equals(marker)) {
					return true;
				}
			}
			
			// check if three same markers are in one column
			for (int j = 0; j < 3; j++) {
				if(basicTwoD[0][j].getMarker().equals(marker)
				   && basicTwoD[1][j].getMarker().equals(marker)
				   && basicTwoD[2][j].getMarker().equals(marker)) {
					return true;
				}
			}
			
			//check if forward diagonal has three same markers
			if(basicTwoD[0][2].getMarker().equals(marker)
			   && basicTwoD[1][1].getMarker().equals(marker)
			   && basicTwoD[2][0].getMarker().equals(marker)) {
				return true;
			}
		
				
			//check if forward diagonal has three same markers
			if(basicTwoD[0][0].getMarker().equals(marker)
			   && basicTwoD[1][1].getMarker().equals(marker)
			   && basicTwoD[2][2].getMarker().equals(marker)) {
				return true;
			}

//					for (int i = 0; i< 3; i++) {
//						for (int j = 0; j <3; j++) {
//							System.out.print(board[i][j].getMarker()+ " ");
//						}
//						System.out.println();
//					}
			//System.out.println("basic id " + this.boardID);
			
	        return false;
	} // end of hasWon
			
			
			public void reset(int gameVersion) {
				for (int i = 0; i < 3; i ++) {
					for (int j = 0; j < 3; j++) {
						basicTwoD[i][j].setMarker("   ", true);
					}
				}
					
			} // end of reset
			
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
			
			// mark the board
			public boolean markBoard(int row, int col, String marker) {
				if((!basicTwoD[row][col].getIsMarked())) {
					basicTwoD[row][col].setMarker(marker, false);
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