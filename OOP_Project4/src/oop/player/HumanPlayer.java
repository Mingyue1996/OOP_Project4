package oop.player;


public class HumanPlayer extends Player {
	
	private static final long serialVersionUID = -7963161443526860347L;
	
	public HumanPlayer(String username, String marker, int playerID) {
		super.username = username;
		super.marker = marker;
		super.playerID = playerID;
	}
	
}