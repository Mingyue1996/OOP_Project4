package oop.board;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Board extends BorderPane {
	
	String boardMarker;
	Pane pane;
	//public static Board[][] boardParent;


	
	// hasWon method
	
		
		public String getMarker () {
			return boardMarker;
		}
		
		public Pane display() {
			return pane;
		}
		
	
	public void reset() {}
	public boolean hasWon(int newMakeRow, int newMakeCol, String marker) {return true;}
	public boolean isEmptySpaceAvailable() {return true;}
	public boolean markBoard(int row, int col, String marker) {return true;}
} // end of Board