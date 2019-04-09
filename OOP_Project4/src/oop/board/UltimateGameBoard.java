package oop.board;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import oop.board.BasicGameBoard;
import oop.view.MainView;


public class UltimateGameBoard extends Board {
	
	// create a pane to hold the board
	public static GridPane gridPane = new GridPane();
	
	public static BasicGameBoard[][] ultimateTwoD;
	// create a constructor --- create an ultimate game board
	public UltimateGameBoard() {
		
		int id = 0;
		gridPane.getStyleClass().add("ultimateBoard");
		ultimateTwoD = new BasicGameBoard[3][3];

		// initialize ultimateTwoD and give each basic board an ID
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3; j++, id++) {
				//System.out.println("id: " + id);
				ultimateTwoD[i][j] = new BasicGameBoard(id, "   ");
				MainView.ticTacToe.setBasicGameBoardList(ultimateTwoD[i][j]);
				MainView.ticTacToe.setValidBasicGameBoardID(id);
				gridPane.add(ultimateTwoD[i][j].display(),j,i);
			}
		}
		
		//Setting the Grid alignment 
		gridPane.setAlignment(Pos.CENTER); 
	} // end of constructor
	
	// display the game board
	public Pane display() {
		return gridPane;
	}
	
	// check if there is a winner
	public boolean hasWon(String marker) {
		//System.out.println("boardID: " + this.boardID);
			// check if three same markers are in one row
			for (int i = 0; i < 3; i++) {
				if(ultimateTwoD[i][0].getMarker().equals(marker)
				   && ultimateTwoD[i][1].getMarker().equals(marker)
				   && ultimateTwoD[i][2].getMarker().equals(marker)) {
					return true;
				}
			}
			
			// check if three same markers are in one column
			for (int j = 0; j < 3; j++) {
				if(ultimateTwoD[0][j].getMarker().equals(marker)
				   && ultimateTwoD[1][j].getMarker().equals(marker)
				   && ultimateTwoD[2][j].getMarker().equals(marker)) {
					return true;
				}
			}
			
			//check if forward diagonal has three same markers
			if(ultimateTwoD[0][2].getMarker().equals(marker)
			   && ultimateTwoD[1][1].getMarker().equals(marker)
			   && ultimateTwoD[2][0].getMarker().equals(marker)) {
				return true;
			}
		
				
			//check if forward diagonal has three same markers
			if(ultimateTwoD[0][0].getMarker().equals(marker)
			   && ultimateTwoD[1][1].getMarker().equals(marker)
			   && ultimateTwoD[2][2].getMarker().equals(marker)) {
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
			
//			
//			public void reset(int gameVersion) {
//				for (int i = 0; i < 3; i ++) {
//					for (int j = 0; j < 3; j++) {
//						ultimateTwoD[i][j].setMarker("   ", true);
//					}
//				}
//					
//			} // end of reset
//			
//			// check if there is at least an empty space in the board
//			public boolean isEmptySpaceAvailable() {
//				
//				for (int i = 0; i < 3; i ++) {
//					for (int j = 0; j < 3; j++) {
//						if (!ultimateTwoD[i][j].getIsMarked()) {
//							return true;
//						}
//					}
//				}
//				return false;
//		}
			
	
	
	
} // end of class UltimateGameBoard