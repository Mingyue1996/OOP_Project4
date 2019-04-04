package oop.controller;
import oop.board.BasicGameBoard;
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
	private BasicGameBoard basicGameBoard;
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
	public void startNewGame(int numPlayers, int timeoutInSecs) {
		if (numPlayers != 2 && numPlayers != 1) {
			System.out.println("The number of players is invalid.");
		}
		else {
			numberPlayer = numPlayers;
			timeout = timeoutInSecs;
			// create a new board if this is the first time to play
			if (!isReplay && basicGameBoard == null) {
				basicGameBoard = new BasicGameBoard();	
			}
			// reset the board if play again
			else if (isReplay) {
				basicGameBoard.reset();
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
				if (basicGameBoard == null) {
					basicGameBoard = new BasicGameBoard();	
				}
				basicGameBoard.setMarker(marker, playerNum);
				
			} else {
				// add computer player
				player.add(playerNum-1, new ComputerPlayer(username, marker, playerNum));
				basicGameBoard.setMarker(marker, playerNum);
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
			(updatePlayerMove(currentPlayer))) {
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
		if (basicGameBoard.hasWon(newMoveRow, newMoveCol, marker)) {
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
		else if (basicGameBoard.isEmptySpaceAvailable()) {
			return 0;
		}
		
		// else, there is a tie
		gameState = 3;
		return 3;
	}
	
	public Pane getGameDisplay() {
		return basicGameBoard.display();
	}
	
	/*
	 * call makeMove() in player method and update moves
	 * */
	public boolean updatePlayerMove(int playerID) {
		marker = player.get(playerID-1).getMarker();
		if (basicGameBoard.markBoard(newMoveRow, newMoveCol, marker)) {
			player.get(playerID-1).makeMove(newMoveRow, newMoveCol);
			// check win
			// change turn if the game is in progress
			if (determineWinner() == 0) {
				// if no win/tie, change turn
				setCurrentPlayer(playerID);
			}
			
			return true;
		}
		System.out.println("Invalid move. The location has been marked. Try again.");
		return false;
       
	} // end of updatePlayerMove
	
	public boolean updatePlayerMove(int row, int col, int playerID) {
		marker = player.get(playerID-1).getMarker();
		this.playerID = playerID;
		if (basicGameBoard.markBoard(row, col, marker)) {
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
	
	// get player's row and column
	public void getRowCol (BufferedReader in)  throws IOException {
		// if there is an input
		if (in.ready()) {
			// get the input and remove leading and trailing white spaces
			String inString = (in.readLine()).trim();
			String[] inStringArray = inString.split("\\s+"); 
			
			// check if there are exactly two inputs
			if (inStringArray.length == 2) {
				
				// check if inputs are numbers
				try {
					newMoveRow = Integer.parseInt(inStringArray[0]);
					newMoveCol = Integer.parseInt(inStringArray[1]);
					
					// mark the board if a move is valid
					if (setSelection(newMoveRow, newMoveCol, getPlayerID())) {
						isLastMoveValid = true;
					}
					else {
						isLastMoveValid = false;
					}
				}
				catch (NumberFormatException e) { 
					System.out.println("Invalid inputs. Please enter numbers. Try again.");
					isLastMoveValid = false;
				}
				
			}
			else if (inStringArray.length == 1 && inStringArray[0].equalsIgnoreCase("Quit")) {
				System.out.println("You quitted the game. Bye~");
				System.exit(0);
			}
			else {
				System.out.println("Invalid input. You need to enter two numbers: row and column. Try again.");
				isLastMoveValid = false;
			}
		} // end of in.ready()
	} // end of getRowCol
	
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