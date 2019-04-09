package oop.board;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import oop.board.square.Square;

public class Board extends BorderPane{
	
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
	public boolean hasWon(String marker) {return true;}
	public boolean isEmptySpaceAvailable() {return true;}
	public boolean markBoard(int row, int col, String marker) {return true;}
} // end of Board