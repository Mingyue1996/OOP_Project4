package oop.board.square;

import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oop.board.BasicGameBoard;

import oop.view.*;

public class Square extends BorderPane{
	private int squareID;
	private String marker;
	private boolean isMarked = false;
	// create a HBox
	public static HBox hBox = new HBox(20);
	
	private String computerMarker;
	private int currentPlayerID;
	

	
	private ArrayList<Timeline> timerLists = new ArrayList<>();
	
	public Square (int id, String marker) {
		squareID = id;
		this.marker = marker;
		setStyle("-fx-border-color: black");
		this.setPrefSize(180,180);
		this.setOnMouseClicked(e->handleMouseClick());
		
	}
	
	// return squareID
	public int getSquareID() {
		return squareID;
	}
	
	
	//display each marker
	public String display() {
		return marker;
	}
	
	// update marker
	public void setMarker(String marker, boolean isReset) {
		this.marker = marker;

		if (!isReset) {
			isMarked = true;
			//System.out.println("computer marker outside: " + marker);
			//System.out.println("computer move: " + MainView.getIsAIMove());
			// display a text when computer moves
			if (MainView.getIsAIMove()) {
				//System.out.println("computer marker: " + marker);
				this.setCenter(new Text(marker));
				if (marker.equals("cn.png") || marker.equals("us.png") || marker.equals("gb.png")) {
					//System.out.println(" my marker");
					
					this.setCenter(new ImageView(Paths.get("src/" + marker).toUri().toString()));
				}
			}
			// display a text when a human with text marker moves
			else if (!MainView.getIsAIMove()) {
				//System.out.println("currentPlayerID:  " + currentPlayerID);
				
				// check if the human has a text marker
				if (currentPlayerID == 1 && !MainView.getIsImageMarker1()) {
					this.setCenter(new Text(marker));
					if (marker.equals("cn.png") || marker.equals("us.png") || marker.equals("gb.png")) {
						//System.out.println(" my marker");
						
						this.setCenter(new ImageView(Paths.get("src/" + marker).toUri().toString()));
					}
				}
				else if (currentPlayerID == 2 && !MainView.getIsImageMarker2()) {
					
					this.setCenter(new Text(marker));
					if (marker.equals("cn.png") || marker.equals("us.png") || marker.equals("gb.png")) {
						//System.out.println(" my marker");
						
						this.setCenter(new ImageView(Paths.get("src/" + marker).toUri().toString()));
					}
				}
				else if (currentPlayerID == 1 && MainView.getIsImageMarker1()) {
					this.setCenter(new ImageView(Paths.get("src/" + marker).toUri().toString()));
				}
				else if (currentPlayerID == 2 && MainView.getIsImageMarker2()) {
					this.setCenter(new ImageView(Paths.get("src/" + marker).toUri().toString()));
				}
				
			} // end of human moves display marker
			
			
		} // end of !isReset
		else {
			isMarked = false;
			this.setCenter(new Text(marker));
		} // end of isReset
		
		
	}
	
	
	// return square state
	public boolean getIsMarked() {
		return isMarked;
	}
	
	// handle a mouse click event
	private void handleMouseClick() {
		MainView.setIsAIMove(false);

		if (MainView.ticTacToe.getGameState() == 0) {
			// get currentPlayerID
			currentPlayerID = MainView.ticTacToe.getPlayerID();
			System.out.println(MainView.ticTacToe.player.get(currentPlayerID -1));
			String currentMarker = MainView.ticTacToe.player.get(currentPlayerID -1).getMarker();
			computerMarker = "X";
			if (MainView.ticTacToe.getNumberPlayers() == 1) {
				computerMarker = (currentMarker.equals("X")) ? "O" : "X";
			}
			
			
			// mark the square if the square is available 
			if (!this.getIsMarked()) {
				
				
				
				//if (MainView.getTimeout() > 0) {
				if (MainView.getTimeout() > 0) {
					MainView.timer.stop();
				}		
					// just removed old codes for timer
				
				
				setMarker(currentMarker, false);
				int gameState = MainView.ticTacToe.determineWinner();
				//System.out.println(timerSquare + "   human player");
				// check game status
				if (gameState != 0) {
					// stop the timer when the game is over
					if (MainView.timerSquare != null ) {
						MainView.timerSquare.stop();
						//System.out.println(timerSquare +" stops after human plays");
					}
						
					checkGameIsOver(gameState);
				}
				// game is going on
				else {
					// change turn
					// change player if a human makes a move
					if (!MainView.getIsAIMove()) {
						// newly add
						currentPlayerID = MainView.ticTacToe.setCurrentPlayer(currentPlayerID);

					}
					// newly remove
					//currentPlayerID = MainView.ticTacToe.setCurrentPlayer(currentPlayerID);
					MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID -1).getUsername() + "'s turn to play.");
					
					
					// if we have two players
					if (MainView.ticTacToe.getNumberPlayers() == 2 ) {
						
						// if we have a timeout
						if (MainView.getTimeout() > 0) {
							if (MainView.ticTacToe.getGameState() == 0) {
								// always stop a previous timer
								if (MainView.timerSquare != null) {
									MainView.timerSquare.stop();
								} // stop MainView.timerSquare
								
								MainView.timerSquare = new Timeline(new KeyFrame(Duration.millis(MainView.getTimeout()*1000), e-> {
									// when the timer is called, the previous player does not make a move, so we need to change the player ID here
									currentPlayerID = MainView.ticTacToe.setCurrentPlayer(currentPlayerID);
									
									// show whose turn now
									MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID-1).getUsername() + "'s turn to play.");
									
									MainView.setIsAIMove(false);
									
								}));
								MainView.timerSquare.setCycleCount(Timeline.INDEFINITE);
								MainView.timerSquare.play();
							} // end of game is going in two players
						} // end of we have a timeout two players
						
						
					} // end of "if we have two players"
					
					
					
					// check if it is computer's turn. If it is, generate moves 
					//System.out.println("currentID when going on: " + currentPlayerID);
					if (MainView.ticTacToe.getNumberPlayers() == 1 && currentPlayerID == 3-MainView.getHumanPlyaerID()) {
						// computer makes move and check game results
						AI_Move_CheckWin();		
						MainView.setIsAIMove(true);
						
						//add a timer
//						// add timeout
						if (MainView.getTimeout() > 0) {
							if (MainView.ticTacToe.getGameState() == 0) {

							// start the timer, when time is up, change playerID and restart animation
							// when a valid move is made, change playerID and restart animation
							
								if (MainView.timerSquare != null) {
									MainView.timerSquare.stop();
									//System.out.println(MainView.timerSquare +" stops after computer plays");
								}
								

								MainView.timerSquare = new Timeline(new KeyFrame(Duration.millis(MainView.getTimeout()*1000), e-> {
								timerLists.add(MainView.timerSquare);
								//System.out.println(MainView.timerSquare +" starts");
									// when the timer is called, the previous player does not make a move, so we need to change the player ID here
									currentPlayerID = MainView.ticTacToe.setCurrentPlayer(currentPlayerID);
									// show whose turn now
									MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID-1).getUsername() + "'s turn to play.");
									// computer makes a move (If a human moves, then this timer would not be called)
									// generate row & column, call updatePlayerMove
									if (MainView.ticTacToe.getNumberPlayers() == 1) {
										if (MainView.ticTacToe.getGameState() == 0) {
											AI_Move_CheckWin();
										}
										MainView.setIsAIMove(true);
										// when the game is over
										if (MainView.ticTacToe.getGameState() != 0) {
											// stop the timer when game is over
											if (MainView.timerSquare != null) {
												MainView.timerSquare.stop();
	
											}
//											if (MainView.ticTacToe.getGameState() == 2) {
//												// show the human lost the game
//												MainView.turnLabel.setText(MainView.ticTacToe.player.get(MainView.getHumanPlyaerID()-1).getUsername() + " lost the game.");
//											}
//											else if (MainView.ticTacToe.getGameState() == 3) {
//												MainView.turnLabel.setText("There is a tie.");
//											}
											
											// show the two buttons (play again and quit)
											Square.hBox.setAlignment(Pos.CENTER);
											if (!MainView.vBoxForGame.getChildren().contains(hBox)) {
												MainView.vBoxForGame.getChildren().add(Square.hBox);
											}
										} // end of game over
										
		
									} // end of numPlayer == 1
									
									// show whose turn now
									if (MainView.ticTacToe.getGameState() == 0) {
										MainView.timerSquare.stop();
										MainView.timerSquare.play();
										MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID-1).getUsername() + "'s turn to play.");
									}
								
								}));
								MainView.timerSquare.setCycleCount(9);
								MainView.timerSquare.play();
							
								
							}
						} // end of timeout
						
						
					}// end of computer plays
				
					
				}// end of game is going on
				
				
			}// end of getIsMarked()
			
		} // end of gameState == 0
	} // end of handleMouseClick
	
	private void AI_Move_CheckWin() {
		// generate row & column, call updatePlayerMove
		Random rand = new Random(); 
		
		int computerRow = rand.nextInt(3); 
		int computerCol = rand.nextInt(3);
		while (!MainView.ticTacToe.updatePlayerMove(computerRow, computerCol, 3-MainView.getHumanPlyaerID())) {
			computerRow = rand.nextInt(3); 
			computerCol = rand.nextInt(3);
		}
			MainView.setIsAIMove(true);
			BasicGameBoard.basicTwoD[computerRow][computerCol].setMarker(computerMarker, false);

		// newly add
		currentPlayerID = MainView.ticTacToe.getPlayerID();
		// get game state
		int gameState1 = MainView.ticTacToe.getGameState();
		// check game status
		if (gameState1 != 0) {

			if (MainView.getTimeout() > 0) {
				if (MainView.timerSquare != null) {
					//System.out.println(MainView.timerSquare + "stops");					
					MainView.timerSquare.stop();

					//clearTimer();
				}
					
			}
			checkGameIsOver(gameState1);
		}

		else {
			// change turn newly changes  MainView.turnLabel.setText(MainView.ticTacToe.player.get(MainView.ticTacToe.setCurrentPlayer(currentPlayerID)-1).getUsername() + "'s turn to play.");
			MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID-1).getUsername() + "'s turn to play.");
		}
	}
	
	
	private void checkGameIsOver(int gameState) {
		// someone won the game
		if ( gameState == 1 || gameState == 2) {
			MainView.turnLabel.setText(MainView.ticTacToe.player.get(currentPlayerID-1).getUsername() + " won the game.");

			if ( MainView.ticTacToe.getNumberPlayers() == 2 || (MainView.ticTacToe.getNumberPlayers() == 1 && currentPlayerID == MainView.getHumanPlyaerID() ) ) {
				playSound("src/winSound.mp3");
			}
			else if (MainView.ticTacToe.getNumberPlayers() == 1 && currentPlayerID != MainView.getHumanPlyaerID()) {
				playSound("src/loseSound.mp3");
			}
			
	
			// update user info in hash map
			if (MainView.ticTacToe.getNumberPlayers() == 1 ) {
				if (gameState == 1) {
					MainView.ticTacToe.player.get(0).setWin();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
				}
				else {
					MainView.ticTacToe.player.get(0).setLose();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
				}
			} // end of update user info in hash map for one player
			else {
				if (gameState == 1) {
					MainView.ticTacToe.player.get(0).setWin();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
					
					MainView.ticTacToe.player.get(1).setLose();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(1).getUsername(), MainView.ticTacToe.player.get(1));
				}else if (gameState == 2) {
					MainView.ticTacToe.player.get(1).setWin();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(1).getUsername(), MainView.ticTacToe.player.get(1));
					
					MainView.ticTacToe.player.get(0).setLose();
					MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
				}
			}// end of update user info in hash map for two players
		} // end of win/loss
		// there is a tie
		else if (gameState == 3) {
			MainView.turnLabel.setText("There is a tie.");
			
			if (MainView.ticTacToe.getNumberPlayers()  == 1) {
				MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
			}
			else {
				MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(0).getUsername(), MainView.ticTacToe.player.get(0));
				MainView.ticTacToe.getHashMap().put(MainView.ticTacToe.player.get(1).getUsername(), MainView.ticTacToe.player.get(1));
			}
		} // end of a tie
		hBox.setAlignment(Pos.CENTER);
		if (!MainView.vBoxForGame.getChildren().contains(hBox)) {
			MainView.vBoxForGame.getChildren().add(hBox);
		}
		
		if (MainView.vBoxForGame.getChildren().contains(MainView.quitBtn)) {
			MainView.vBoxForGame.getChildren().remove(MainView.quitBtn);
		}
		
	} // end of checkGameIsOver
	
	
	public static void playSound(final String FILE_PATH) {
		Media media_win = new Media (Paths.get(FILE_PATH).toUri().toString()); //https://vocaroo.com/i/s1Ho8LVVYJRD
		MediaPlayer mediaPlayer = new MediaPlayer(media_win);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.play();
	} // end of playSound()
	
	

} // end of Square class