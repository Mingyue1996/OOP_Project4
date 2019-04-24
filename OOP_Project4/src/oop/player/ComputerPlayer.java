package oop.player;

/* 
 * Computer player would have more properties when I am working on ultimate Tic Tac Toe game
 * */
public class ComputerPlayer extends Player {
	private static final long serialVersionUID = -1682581207599914438L;
	
	public ComputerPlayer(String username, String marker, int playerID) {
		super.username = username;
		super.marker = marker;
		super.playerID = playerID;
	}
	
}
