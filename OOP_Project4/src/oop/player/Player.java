package oop.player;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1682581207599914438L;
	//private static final long serialVersionUID = 1L;
	String username;
	String marker;
	int playerID;
	int newMoveRow = -1;
	int newMoveCol = -1;
	
	private int winNum; 
	private int loseNum;
	private ArrayList<String> personalMarkers = new ArrayList<>();
	private ArrayList<String> personalMarkerWins = new ArrayList<>();
	
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
	
	public void setMarker(String newMarker) {
		marker = newMarker;
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
	
	public ArrayList<String> getPersonalMarkers() {
		return personalMarkers;
	}
	
	public void setPersonalMarkers(ArrayList<String> al) {
		personalMarkers.addAll(al);
	}
	
	public ArrayList<String> getPersonalMarkerWins() {
		return this.personalMarkerWins;
	}
	
	public void setPersonalMarkerWins(ArrayList<String> al) {
		personalMarkerWins.addAll(al);
	}
	
	
	




}