package oop.controller;
import oop.board.BasicGameBoard;
import oop.board.Board;
import oop.board.UltimateGameBoard;
import oop.player.ComputerPlayer;
import oop.player.HumanPlayer;
import oop.player.Player;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.Pane;
public class TTTControllerImpl implements TTTControllerInterface {
	
	
	private int numberPlayer = 0;
	private int timeout = 0;
	private int gameVersion = 0;
	private Board board;
	private int playerID = 1;
	private int newMoveRow = -1;
	private int newMoveCol = -1;
	private String marker;
	private int gameState = 0;
	private boolean isReplay = false;
	private boolean isHumanPlayer = true;
	private boolean isLastMoveValid = true;
	public ArrayList<Player> player = new ArrayList<>();
	private HashMap<String, Player> userInfo = new HashMap<String, Player>();
	private ArrayList<BasicGameBoard> basicGameBoardList = new ArrayList<>();
	private ArrayList<Integer> validBasicGameBoardID = new ArrayList<>();
	
	
	// constructor
	public TTTControllerImpl () {
		retrieveUserInfo();
	}
	
	/**
	 * Initialize or reset game board. Set each entry back to a default value.
	 * 
	 * @param numPlayers Must be valid. 2 = two human players, 1 = human plays against computer
	 * @param timeoutInSecs Allow for a user's turn to time out. Any
	 * 						int <=0 means no timeout.  Any int > 0 means to time out
	 * 						in the given number of seconds.
	 */
	public void startNewGame(int numPlayers, int timeoutInSecs, int gameVersion) {
		this.gameVersion = gameVersion;
		basicGameBoardList.clear();
		validBasicGameBoardID.clear();
		
		if (numPlayers != 2 && numPlayers != 1) {
			System.out.println("The number of players is invalid.");
		}
		else {
			numberPlayer = numPlayers;
			timeout = timeoutInSecs;
			// create a new board if this is the first time to play
			if (!isReplay) {
				if (gameVersion == 1) {					
					board = new BasicGameBoard(0, "   ");	
					setBasicGameBoardList((BasicGameBoard) board);
					setValidBasicGameBoardID(0);
				}else if (gameVersion == 2) {
					board = new UltimateGameBoard();
				}
				
			}
			// reset the board if play again
			else if (isReplay) {
				board.reset();		
				gameState = 0;
				playerID = 1;
				newMoveRow = -1;
				newMoveCol = -1;
				isReplay = true;
				isHumanPlayer = true;
				isLastMoveValid = true;
				player.clear();
			}
			
		}
				
	} // end of startNewGame
	
	
	/**
	 * Create a player with specified user name, marker, 
	 * and player number (either 1 or 2) 
	 **/
	public void createPlayer(String username, String marker, int playerNum) {
		if (playerNum == 1 || playerNum == 2) {
			if (isHumanPlayer) {
				// add human player
				player.add(playerNum-1, new HumanPlayer(username, marker, playerNum));
				//board.setMarker(marker, playerNum);
				
			} else {
				// add computer player
				player.add(playerNum-1, new ComputerPlayer(username, marker, playerNum));
				//board.setMarker(marker, playerNum);
			}			
			
		}
		else {
			System.out.println("Invalid player number.");
		}
		
	}
	
	/**
	 * Allow user to specify location for marker.  
	 * Return true if the location is valid and available.
	 * 
	 * @param row Must be valid. 0,1,2
	 * @param col Must be valid. 0,1,2
	 * @param currentPlayer Must be valid. 1 = player1; 2 = player2
	 * @return
	 */
	public boolean setSelection(int row, int col, int currentPlayer) {
		newMoveRow = row;
		newMoveCol = col;
		if ((row == 0 || row == 1 || row == 2) && 
			(col == 0 || col == 1 || col == 2) && 
			(currentPlayer == 1 || currentPlayer == 2) &&
			(updatePlayerMove(row, col, currentPlayer))) {
			return true;
		}
			
		if (!(row == 0 || row == 1 || row == 2) ||
			!(col == 0 || col == 1 || col == 2)){
			System.out.println("Invalid Move. Row and Column numbers should be between 0 and 2 (including). Try again.");
		}
		if ((currentPlayer != 1) && (currentPlayer !=2)) {
			System.out.println("Invalid player ID.");
		}
		return false;
	}
	
	/**
	 * Determines if there is a winner and returns the following:
	 * 
	 * 0=no winner / game in progress / not all spaces have been selected; 
	 * 1=player1; 
	 * 2=player2; 
	 * 3=tie/no more available locations
	 * 
	 * @return
	 */
	
	public int determineWinner() {		
		marker = player.get(playerID-1).getMarker();
		// check if there is a winner
		if (board.hasWon(marker)) {
			if (playerID == 1) {
				gameState = 1;
				return 1;
			}
				
			else {
				gameState = 2;
				return 2;
			}
				
		}
		
		// check if the game is in progress
		else if (board.isEmptySpaceAvailable()) {
			return 0;
		}
		
		// else, there is a tie
		gameState = 3;
		return 3;
	}
	
	public Pane getGameDisplay() {
		return board.display();
	}
	
	/*
	 * call makeMove() in player method and update moves
	 * */
	
	public boolean updatePlayerMove(int row, int col, int playerID) {
		marker = player.get(playerID-1).getMarker();
		this.playerID = playerID;
		if (board.markBoard(row, col, marker)) {
			player.get(playerID-1).makeMove(row, col);
			// check win
			// change turn if the game is in progress
			newMoveRow = row;
			newMoveCol = col;
			if (determineWinner() == 0) {			
				// if no win/tie, change turn
				setCurrentPlayer(playerID);
			}
			
			return true;
		}
		return false;
       
	} // end of updatePlayerMove
	
	// change turn
	public int setCurrentPlayer(int playerID) {
		if (playerID == 1)
			this.playerID = 2;
		else
			this.playerID = 1;
		return this.playerID;
	} // end of change turn
	
	
	// return playerID
	public int getPlayerID() {
		return playerID;
	}
	
	// return game state
	public int getGameState() {
		return gameState;
	}
	
	// set isReplay
	public void setIsReplay(boolean isPlayAgain) {
		isReplay = isPlayAgain;
	}
	
	// return isReplay
	public boolean getIsReplay() {
		return isReplay;
	}
	
	// return isHumanPlayer
	public void setIsHumanPlayer(boolean isHumanPlayer) {
		this.isHumanPlayer =  isHumanPlayer;
	}
	
	// return isLastMoveValid
	public boolean getIsLastMoveValid () {
		return isLastMoveValid;
	}
	
	// change the value of isLastMoveValid
	public void setIsLastMoveValid (boolean isValid) {
		isLastMoveValid = isValid;
	}
	
	// get number of players
	public int getNumberPlayers () {
		return numberPlayer;
	}
	
	// update basicGameBoardList
	public void setBasicGameBoardList (BasicGameBoard board) {
		basicGameBoardList.add(board);
	}
	
	// get basicGameBoardList
	public BasicGameBoard getBasicGameBoardList (int index) {
		return basicGameBoardList.get(index);
	}
	
	// update validBasicGameBoardID
	public void setValidBasicGameBoardID (int boardID) {
		validBasicGameBoardID.add(boardID);
	}
	
	// get validBasicGameBoardID
	public int getValidBasicGameBoardID (int index) {
		return validBasicGameBoardID.get(index);
	}
	
	public boolean isBoardValid (int boardID) {
		return validBasicGameBoardID.contains(boardID);
	}
	
	public void clearValidBasicGameBoardID() {
		validBasicGameBoardID.clear();
	}
	
	// save user info including user names, markers, and win/loses
	public void saveInfo () {
		// add user info to hash map
		for (int i = 0; i< player.size(); i++) {
			// ignore Computer
			if (!player.get(i).getUsername().equals("Computer")) {
				userInfo.put(player.get(i).getUsername(), player.get(i));
			}
			
		} // end of for loop
		
		 try
         {
                FileOutputStream fos = new FileOutputStream("userInfo.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(userInfo);
                oos.close();
                fos.close();
               // System.out.printf("Serialized HashMap data is saved in userinfo.ser");
         }catch(IOException ioe)
          {
                ioe.printStackTrace();
          }
		
		// check whether the user name(key) already exists
		
		
	} // end of saveInfo
	
	// retrieve user info

	public void retrieveUserInfo () {
		try
	      {
	         FileInputStream fis = new FileInputStream("userinfo.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         userInfo = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found. Please check again.");
	         c.printStackTrace();
	         return;
	      }
	    //  System.out.println("Deserialized HashMap..");
	      // Display content using Iterator
	} // end of retrieve user info
	
	public HashMap<String, Player> getHashMap () {
		return userInfo;
	}
	
}