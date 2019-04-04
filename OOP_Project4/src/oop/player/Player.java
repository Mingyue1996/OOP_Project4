package oop.player;

import java.io.Serializable;

public class Player implements Serializable {
	String username;
	String marker;
	int playerID;
	int newMoveRow = -1;
	int newMoveCol = -1;
	
	private int winNum; 
	private int loseNum;
	
	public Player() {
		
	}
	
	
	// return new row
	public int getNewMoveRow() {
		return newMoveRow;
	}
	
	//  return new column
	public int getNewMoveCol() {
		return newMoveCol;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public void makeMove(int row, int col) {
		newMoveRow = row;
		newMoveCol = col;
	}
	
	public String getUsername() {
		return username;
	}
	public int getWin() {
		return winNum;
	}
	
	public int getLose() {
		return loseNum;
	}
	public void setWin() {
		winNum ++;
	}
	public void setLose() {
		loseNum ++;
	}




}