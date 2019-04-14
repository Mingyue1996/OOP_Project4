package oop.controller;
import oop.board.BasicGameBoard;
import oop.board.Board;
import oop.board.UltimateGameBoard;
import oop.board.square.Square;
import oop.player.ComputerPlayer;
import oop.player.HumanPlayer;
import oop.player.Player;
import oop.view.MainView;

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
	private int numberOfCells = 0;
	private BasicGameBoard basicBoard;
	private UltimateGameBoard ultimateBoard;
	private int playerID = 1;
	private int newMoveRow = -1;
	private int newMoveCol = -1;
	private int currentBasicBoardLocationRow = -1;
	private int currentBasicBoardLocationCol = -1;
	private String marker;
	private int gameState = 0;
	private boolean isReplay = false;
	private boolean isHumanPlayer = true;
	private boolean isLastMoveValid = true;
	public ArrayList<Player> player = new ArrayList<>();
	private HashMap<String, Player> userInfo = new HashMap<String, Player>();
	private ArrayList<BasicGameBoard> basicGameBoardList = new ArrayList<>();
	public HashMap<Integer, Square[][]> validBasicGameBoardMap= new HashMap<>(); 
	private HashMap<Integer, BasicGameBoard> basicGameBoardMap = new HashMap<>();
	
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
	public void startNewGame(int numPlayers, int timeoutInSecs, int gameVersion, int numberOfCells) {
		this.gameVersion = gameVersion;
		this.numberOfCells = numberOfCells;
		validBasicGameBoardMap.clear();
		basicGameBoardList.clear();
		if (numPlayers != 2 && numPlayers != 1) {
			System.out.println("The number of players is invalid.");
		}
		else {
			numberPlayer = numPlayers;
			timeout = timeoutInSecs;
			// create a new board if this is the first time to play
			if (!isReplay) {
				if (gameVersion != 2) {					
					basicBoard = new BasicGameBoard(0, numberOfCells, "   ");	
					setBasicGameBoardList(basicBoard);
					basicGameBoardMap.put(gameVersion, basicBoard);
				}else {
					ultimateBoard = new UltimateGameBoard(numberOfCells);
				}
				
			}
			// reset the board if play again
			else if (isReplay) {
				if (gameVersion != 2) {
					if (basicGameBoardMap.containsKey(gameVersion)) {
						basicBoard = basicGameBoardMap.get(gameVersion);
						basicBoard.reset();
						setBasicGameBoardList(basicBoard);
						validBasicGameBoardMap.put(0, basicBoard.basicTwoD);
					}else {
						basicBoard = new BasicGameBoard(0, numberOfCells, "   ");	
						setBasicGameBoardList(basicBoard);
						basicGameBoardMap.put(gameVersion, basicBoard);
					}
					
				}
				else {
					if (ultimateBoard != null) {
						ultimateBoard.reset();	
					}else {
						ultimateBoard = new UltimateGameBoard(numberOfCells);
					}
					
					
				}
					
				//basicGameBoardList.clear();
				//validBasicGameBoardMap.clear();
				gameState = 0;
				playerID = 1;
				newMoveRow = -1;
				newMoveCol = -1;
				currentBasicBoardLocationRow = -1;
				currentBasicBoardLocationCol = -1;
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
		if (gameVersion != 2) {
			return decideWinner(basicBoard);
		}else {
			return decideWinner(ultimateBoard);
		}
		
	}
	
	private int decideWinner(Board board) {
		marker = player.get(playerID-1).getMarker();
		// check if there is a winner
		
		if (board.hasWon(newMoveRow, newMoveCol, marker)) {
			if (MainView.getGameVersion() == 2) {
				for (int i = 0; i< 9; i++) {
					UltimateGameBoard.gridPane.getChildren().get(i).getStyleClass().removeIf(style -> style.equals("validBoard"));
				}
			}
			
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
			if (MainView.getGameVersion() == 2) {
				for (int i = 0; i< 9; i++) {
					UltimateGameBoard.gridPane.getChildren().get(i).getStyleClass().removeIf(style -> style.equals("validBoard"));
				}
			}
			return 0;
		}
		
		// else, there is a tie
		gameState = 3;
		return 3;
	}
	
	public Pane getGameDisplay() {
		if (gameVersion != 2) {
			return basicBoard.display();
		}else {
			return ultimateBoard.display();
		}
		
	}
	
	/*
	 * call makeMove() in player method and update moves
	 * */
	
	public boolean updatePlayerMove(int row, int col, int playerID) {
		marker = player.get(playerID-1).getMarker();
		this.playerID = playerID;
		// get a valid board number
		int validBoardNumber = (int) validBasicGameBoardMap.keySet().toArray()[0];
		
		// use basic game board list to return the valid game board
		// System.out.println("valid board number: " + validBoardNumber);
		if (getBasicGameBoardList(validBoardNumber).markBoard(row, col, marker)) {
			// REMOVE style of previous moves
			
			// ADD style to the square to show the latest move
			
			
			
			player.get(playerID-1).makeMove(row, col);
			// check win
			// change turn if the game is in progress
			newMoveRow = row;
			newMoveCol = col;
			
			// call transformIntoSquareID
			int currentBoardID = (int) validBasicGameBoardMap.keySet().toArray()[0];
			//System.out.println("current Board ID: " + currentBoardID);
			transformIntoSquareID(newMoveRow, newMoveCol, -1, currentBoardID, true);
			
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
	
	
	public void updateValidBoard (int squareID) {
		validBasicGameBoardMap.clear();
		for (int i = 0; i< 9; i++) {
			UltimateGameBoard.gridPane.getChildren().get(i).getStyleClass().removeIf(style -> style.equals("validBoard"));
		}
		
		// check if the next board is valid
		if (getBasicGameBoardList(squareID).getIsMarked()) {
		
			// if not valid, mark all the available board valid
			for (int i = 0; i < 9; i++) {
				if (!getBasicGameBoardList(i).getIsMarked()) {
					validBasicGameBoardMap.put(i, MainView.ticTacToe.getBasicGameBoardList(i).basicTwoD);
					UltimateGameBoard.gridPane.getChildren().get(i).getStyleClass().add("validBoard");
				}
			}
			
		} // end of if
		else {
			validBasicGameBoardMap.put(squareID, MainView.ticTacToe.getBasicGameBoardList(squareID).basicTwoD);
			UltimateGameBoard.gridPane.getChildren().get(squareID).getStyleClass().add("validBoard");
			//System.out.println("Square check " + MainView.ticTacToe.validBasicGameBoardMap.get(squareID)[0][0].getBoardID());
			//System.out.println("this.squareID: " + this.squareID);
		}
	} // end of updateValidBoard
	
	// row, column transforms into squareID
	public int transformIntoSquareID (int rowNum, int colNum, int square_id, int board_id, boolean squareIDToRowCol) {
		int squareID = 0, boardID = 0;
		int [][] squareIDArray = new int [numberOfCells][numberOfCells];
		for (int i = 0; i < squareIDArray.length; i++) {
			for (int j = 0; j < squareIDArray.length; j++, squareID++, boardID++) {
				squareIDArray[i][j] = squareID;
				if (squareIDToRowCol && square_id == squareID) {
					newMoveRow = i;
					newMoveCol = j;
				}
				
				if (squareIDToRowCol && board_id == boardID) {
					currentBasicBoardLocationRow = i;
					currentBasicBoardLocationCol = j;
				}
			} // end of inner for
		}
		
		return squareIDArray[rowNum][colNum];
	}
	
	
	// return new row, new column
	public int getNewMoveRow() {
		return newMoveRow;
	}
	
	public int getNewMoveCol() {
		return newMoveCol;
	}
	
	public int getCurrentBasicBoardLocationRow() {
		return currentBasicBoardLocationRow;
	}
	
	public int getCurrentBasicBoardLocationCol() {
		return currentBasicBoardLocationCol;
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

	public int getNumberOfCells() {
		// TODO Auto-generated method stub
		return numberOfCells;
	}
	
}