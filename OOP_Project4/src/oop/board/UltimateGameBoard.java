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
	private int numberOfCells;
	// create a constructor --- create an ultimate game board
	public UltimateGameBoard(int numberOfCells) {
		
		int id = 0;
		this.numberOfCells = numberOfCells;
		gridPane.getStyleClass().add("ultimateBoard");
		ultimateTwoD = new BasicGameBoard[numberOfCells][numberOfCells];

		// initialize ultimateTwoD and give each basic board an ID
		for (int i = 0; i < numberOfCells; i ++) {
			for (int j = 0; j < numberOfCells; j++, id++) {
				//System.out.println("id: " + id);
				ultimateTwoD[i][j] = new BasicGameBoard(id, numberOfCells,"   ");
				MainView.ticTacToe.setBasicGameBoardList(ultimateTwoD[i][j]);
				gridPane.add(ultimateTwoD[i][j].display(),j,i);
				if (MainView.getGameVersion() == 2) {
					gridPane.getChildren().get(id).getStyleClass().add("validBoard");
				}
			}
		}
		//System.out.println("size: " + gridPane.getChildren().size());
		//Setting the Grid alignment 
		gridPane.setAlignment(Pos.CENTER); 
	} // end of constructor
	
	// display the game board
	public Pane display() {
		return gridPane;
	}
	
	// check if there is a winner
	public boolean hasWon(int newMoveRow, int newMoveCol, String marker) {
		// call the corresponding basicTwoD to check if there is a win/loss on basic game board
		
		int currentBasicBoardLocationRow = MainView.ticTacToe.getCurrentBasicBoardLocationRow();
		int currentBasicBoardLocationCol = MainView.ticTacToe.getCurrentBasicBoardLocationCol();
		
		//System.out.println("row col: " + newRow + "  " + newCol);
		
		if (ultimateTwoD[currentBasicBoardLocationRow][currentBasicBoardLocationCol].hasWon(newMoveRow, newMoveCol, marker)) {
			int basicBoardID = MainView.ticTacToe.transformIntoSquareID(currentBasicBoardLocationRow,currentBasicBoardLocationCol, -1, -1, false);
			if (MainView.ticTacToe.getPlayerID() == 1) {
				gridPane.getChildren().get(basicBoardID).getStyleClass().add("basicBoard_player1");
			}else {
				gridPane.getChildren().get(basicBoardID).getStyleClass().add("basicBoard_player2");
			}
			

			//System.out.println("checking global board win?");
			// if there is a result, do the followings; otherwise, do nothing
			// mark the basic board as isMarked AND 
			// show which marker belongs to this Basic Board AND
			// check if three same markers are in one row for the bigger board
			
			ultimateTwoD[currentBasicBoardLocationRow][currentBasicBoardLocationCol].setMarker(marker, false);

			
			for (int i = 0; i < numberOfCells; i++) {
				if(ultimateTwoD[i][0].getMarker().equals(marker)
				   && ultimateTwoD[i][1].getMarker().equals(marker)
				   && ultimateTwoD[i][2].getMarker().equals(marker)) {
					return true;
				}
			}
			
			// check if three same markers are in one column
			for (int j = 0; j < numberOfCells; j++) {
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
		
				
			//check if backward diagonal has three same markers
			if(ultimateTwoD[0][0].getMarker().equals(marker)
			   && ultimateTwoD[1][1].getMarker().equals(marker)
			   && ultimateTwoD[2][2].getMarker().equals(marker)) {
				return true;
			}
		} // end of checking basic game board win/loss/tie
		
			
	        return false;
	} // end of hasWon
			
			
		public void reset() {
			for (int i = 0; i < numberOfCells; i ++) {
				for (int j = 0; j < numberOfCells; j++) {
					ultimateTwoD[i][j].setMarker("   ", true);
					ultimateTwoD[i][j].reset();
					MainView.ticTacToe.setBasicGameBoardList(ultimateTwoD[i][j]);
				}
			}
			System.out.println("size in reset: " + gridPane.getChildren().size());
			// update valid basic game board AND remove all the color styles applied to the basic boards
			for (int i = 0; i < 9; i++) {
				MainView.ticTacToe.validBasicGameBoardMap.put(i, MainView.ticTacToe.getBasicGameBoardList(i).basicTwoD);
				gridPane.getChildren().get(i).getStyleClass().clear();
				gridPane.getChildren().get(i).getStyleClass().add("validBoard");
			}
			
		} // end of reset
			
		// check if there is at least an empty space in the board
		public boolean isEmptySpaceAvailable() {
			int currentBasicBoardLocationRow = MainView.ticTacToe.getCurrentBasicBoardLocationRow();
			int currentBasicBoardLocationCol = MainView.ticTacToe.getCurrentBasicBoardLocationCol();
			//System.out.println("new row & col: " + currentBasicBoardLocationRow + " " + currentBasicBoardLocationCol);
			if (ultimateTwoD[currentBasicBoardLocationRow][currentBasicBoardLocationCol].isEmptySpaceAvailable()) {
				return true;
			} else {
				// When we go to this else statement, the following thing would happen:
				// there is a tie in one basic game board, so we need to mark the basic game board
				// we also need to check if there is a tie in the Big game board
				ultimateTwoD[currentBasicBoardLocationRow][currentBasicBoardLocationCol].setMarker(" ", false);
				int basicBoardID = MainView.ticTacToe.transformIntoSquareID(currentBasicBoardLocationRow,currentBasicBoardLocationCol, -1, -1, false);
				gridPane.getChildren().get(basicBoardID).getStyleClass().add("tie");
				
				//int basicBoardID = MainView.ticTacToe.transformIntoSquareID(currentBasicBoardLocationRow,currentBasicBoardLocationCol, -1, -1, false);
				for (int i = 0; i < numberOfCells; i ++) {
					for (int j = 0; j < numberOfCells; j++) {
						if (!ultimateTwoD[i][j].getIsMarked()) {
							return true;
						}
					}
				} // end of for loop
				
				// all the cells are marked. There is a tie.
				return false;
			}
			
		} // end of isEmptySpaceAvailable() 
			
	
	
	
} // end of class UltimateGameBoard