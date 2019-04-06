package oop.view;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import oop.board.BasicGameBoard;
import oop.board.square.Square;
import oop.controller.*;
import oop.player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

//import main.course.oop.tictactoe.util.TwoDArray;

public class MainView {
	public static BorderPane root;
	private Scene scene; 
	private StackPane pane = new StackPane();
	
	public static VBox vBoxForGame = new VBox(20);
	public static TTTControllerImpl ticTacToe = new TTTControllerImpl();
	
	private int numPlayer;
	private static int timeout = 0;
	public static Label turnLabel = new Label();
	
	private ArrayList<String> username = new ArrayList<>();
	private ArrayList<String> marker = new ArrayList<>();
	private static int humanPlayerID = 1;
	public static Timeline timer;
	private static boolean isAIMove = false;
	
	private boolean emptyErrors = false;
	private boolean duplicateErrors = false;
	private boolean duplicateErrors1 = false;
	
    private final int windowWidth = 1200; 
    private final int windowHeight = 900;
    private Text emptyInputsText = new Text ("Fill in user name and marker. Computer cannot be username.");	
    private Text timeStringErrorText = new Text ("Time out must be an integer");
    private Text duplicateInputsText = new Text ("Two players cannot use the same username or marker.");
    private Text duplicateInputsText1 = new Text("User name is in the list already.");
    private Text emptySelectionFirstScene = new Text ("Please select all the fields below.");
    
    private static boolean isImageMarker1 = false;
    private static boolean isImageMarker2 = false;
    
    private String username1, username2, marker1, marker2;
    public static Timeline timerSquare;
    public static Button quitBtn;
    private ArrayList<String> userInfoArrayList = new ArrayList<String>();
    private ArrayList<String> usernameArrayList = new ArrayList<String>();
    
    private String[] imageOption = {"♫", "☀", "✈", "♕"};
    private Button checkOldUser = new Button("Check previous game results");
    
    private ListView<String> lvUsername;
    
    //public static Timeline timerSquare;
    
	public MainView() {
		
		MainView.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);

		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

		getMainView();		
		
	} //end of MainView
	
	public void getMainView() {
		root.setPadding(new Insets(50));
		// add a  game title
		root.setTop(new CustomPane("Welcome to Tic Tac Toe!"));		
		// remove play again buttons and whoseTurn label
		vBoxForGame.getChildren().clear();		
		// add radio buttons
		root.setCenter(getNumPlayers());
		//clear user name and marker lists
		username.clear();
		marker.clear();
		isImageMarker1 = false;
		isImageMarker2 = false;
		isAIMove = false;
		username1 = username2 = marker1 = marker2 ="";
		checkOldUser.setText("Check previous game results");
	}
	
	
	public StackPane getNumPlayers() {
		this.pane = new StackPane();
		
		// create a pane for buttons
		VBox vBoxForButtons = new VBox(30);
		
		
		ObservableList<String> numberOfPlayersOption = 
			    FXCollections.observableArrayList(
			        "Play against computer",
			        "Play with another human"
			    );
		
		ComboBox<String> selectNumberOfPlayers = new ComboBox<>(numberOfPlayersOption);
		selectNumberOfPlayers.setPromptText("Select who you want to play with");
		selectNumberOfPlayers.setEditable(false);
		
		// create two buttons: continue and quit
		Button continueButton = new Button("Continue");
		Button quitButton = new Button("Quit the game");
		
		
		// add buttons to the pane
		vBoxForButtons.getChildren().addAll(selectNumberOfPlayers, continueButton, quitButton);	
		vBoxForButtons.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(vBoxForButtons);
		
		//quit the game if "quit" is clicked
		quitButton.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
		});
		
		
		selectNumberOfPlayers.valueProperty().addListener((obs, old, n) -> {
			if (n.equals("Play against computer")) {
				numPlayer = 1;
			} else if (n.equals("Play with another human")) {			
				numPlayer = 2;
			}
			
		});
		
		// get the number of players
		continueButton.setOnAction(e -> {
			if (!selectNumberOfPlayers.getSelectionModel().isEmpty()) {
				// remove "ask number of players" 
				vBoxForButtons.getChildren().clear();
				
				// call another function to get timeout, user names, and markers 
				getPlayerInfo(vBoxForButtons);
			}
			// does not select an option
			else {
				if (!vBoxForButtons.getChildren().contains(emptySelectionFirstScene)) {
					vBoxForButtons.getChildren().add(0, emptySelectionFirstScene);
				}			
			}
					
		});
				
		return pane;
	}
	
	
	// get timeout, user names, and markers
	public void getPlayerInfo(VBox vBoxForButtons) {

		displayInfo();
		// newly added create a list view for old users  for player 1
		lvUsername = new ListView<> (FXCollections.observableArrayList(userInfoArrayList)); // userInfoArrayList
		
		checkOldUser.setOnAction (e -> {
			if (checkOldUser.getText().contains("Check")) {
				root.setLeft(lvUsername);
				checkOldUser.setText("Hide previous game results");
			}
			else {
				if (root.getChildren().contains(lvUsername)) {
					root.getChildren().remove(lvUsername);
				}
				checkOldUser.setText("Check previous game results");
			}
			
		});
		
		// create two buttons: continue and quit
		Button startButton = new Button("Start the Game");
		Button quitButton = new Button("Quit the game");
		
		// add buttons to the pane
		vBoxForButtons.getChildren().addAll(checkOldUser, startButton, quitButton);		
		vBoxForButtons.setAlignment(Pos.CENTER);
		
		GridPane gridPaneForInfo = new GridPane();

		gridPaneForInfo.setHgap(20);
		gridPaneForInfo.setVgap(20);
		gridPaneForInfo.setAlignment(Pos.CENTER);
		
		Label timeoutLabel = new Label("Time limit (in second): ");
		gridPaneForInfo.add(timeoutLabel, 0, 0);
		
		// add a textField for timeoutLabel
		TextField fieldTimeOut = new TextField();
		fieldTimeOut.setPrefColumnCount(4);
		
		gridPaneForInfo.add(fieldTimeOut, 1, 0);
		vBoxForButtons.getChildren().add(0, gridPaneForInfo);
		
		TextField fieldUsername1;
		TextField fieldUsername2 = new TextField();
		TextField fieldMarker1;
		TextField fieldMarker2 = new TextField();
		
		// create new buttons
		RadioButton playFirst = new RadioButton("Yes");
		RadioButton playSecond = new RadioButton("No");
		
		// when there is one player
		if (numPlayer == 1) {
			
			// ask whether the human wants to play the first
			//Label wantGoFirstLabel = new Label ("Do you want to play first?");
			
				
			// group the buttons together
			//ToggleGroup radioButtonsGroup = new ToggleGroup();
			//playFirst.setToggleGroup(radioButtonsGroup);
		//	playSecond.setToggleGroup(radioButtonsGroup);
			
			// add buttons to the pane
			//gridPaneForInfo.add(wantGoFirstLabel, 0, 4);
			//gridPaneForInfo.add(playFirst, 1, 4);
			//gridPaneForInfo.add(playSecond, 2, 4);
			// set a default selection
			playFirst.setSelected(true);
			
			Label humanPlayerUsernameLabel = new Label ("Give yourself a user name: ");
			gridPaneForInfo.add(humanPlayerUsernameLabel, 0, 1);
			
			Label humanPlayerMarkerLabel = new Label ("Put your marker: ");
			gridPaneForInfo.add(humanPlayerMarkerLabel, 0, 2);
	
		} 
		// when we have two players
		else {
			// add player 1's user name
			Label player1UsernameLabel = new Label("Enter palyer 1's user name");
			gridPaneForInfo.add(player1UsernameLabel, 0, 1);
			
			// add player 2's user name
			Label player1MarkerLabel = new Label("Enter palyer 1's marker");
			gridPaneForInfo.add(player1MarkerLabel, 0, 2);
			
			// add player 2's user name
			Label player2UsernameLabel = new Label("Enter palyer 2's user name");
			gridPaneForInfo.add(player2UsernameLabel, 0, 3);
			
			// add player 2's user name
			Label player2MarkerLabel = new Label("Enter palyer 2's marker");
		    gridPaneForInfo.add(player2MarkerLabel, 0, 4);
	
		} // end of two users
		
		// add a textField for player1 user name
		fieldUsername1 = new TextField();
		
		// add username1 to field
		//gridPaneForInfo.add(fieldUsername1, 3, 1);
		
		
		// newly added
		// add radio buttons for users to choose to enter a new user name or choose an old one
		
		/*****************************************Select/Ask user name1**************************/ 
		displayInfo();
		ObservableList<String> usernameOption = FXCollections.observableArrayList(usernameArrayList);
		ComboBox<String> enterUsername1 = new ComboBox<>(usernameOption);
		enterUsername1.setEditable(true);
		
		// add combox box to the GUI
		gridPaneForInfo.add(enterUsername1, 1, 1);
		
		// get user's input for user name 1
		enterUsername1.valueProperty().addListener((obs, old, n) -> {
			username1 = n; 		
		});
		
		
		/************************************Select/Ask user name 2*********************************/
		ComboBox<String> enterUsername2 = new ComboBox<>(usernameOption);
		enterUsername2.setEditable(true);
		
	
		// get user's input for user name 1
		enterUsername2.valueProperty().addListener((obs, old, n) -> {
			username2 = n; 		
		});
		
	
		if (numPlayer == 2) {
			
			// add combox box to the GUI
			gridPaneForInfo.add(enterUsername2, 1, 3);
			
		}
			
	

		/********************Enter your marker1************************/
		ObservableList<String> markerOption = FXCollections.observableArrayList(imageOption);
		ComboBox<String> enterMarker1 = new ComboBox<>(markerOption);
		enterMarker1.setEditable(true);
		gridPaneForInfo.add(enterMarker1, 1, 2);
		
		
		enterMarker1.valueProperty().addListener((obs, old, n) -> {
			marker1 = n;		
		});
		
		
		
		/********************Enter your marker2*******************************/
		ComboBox<String> enterMarker2 = new ComboBox<>(markerOption);
		enterMarker2.setEditable(true);
		
		
		
		enterMarker2.valueProperty().addListener((obs, old, n) -> {
			marker2 = n;		
		});
		
		
		
		if (numPlayer == 2) {
			
			gridPaneForInfo.add(enterMarker2, 1, 4);
	
		} // end of numPlayer == 2
		
		/**********************************************************/
		
		
		//quit the game if "quit" is clicked
		quitButton.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
			});
		
		
		
		// get timeout, user name, marker when game starts
		startButton.setOnAction(e -> {			
		try {

			
			if (gridPaneForInfo.getChildren().contains(emptyInputsText)) {
				gridPaneForInfo.getChildren().remove(emptyInputsText);
				
			} // end of remove errors
			if (gridPaneForInfo.getChildren().contains(duplicateInputsText)) {
				gridPaneForInfo.getChildren().remove(duplicateInputsText);
			}
			if (gridPaneForInfo.getChildren().contains(duplicateInputsText1)) {
				gridPaneForInfo.getChildren().remove(duplicateInputsText1);
			}
			
			
			emptyErrors = false;
			duplicateErrors = false;
			duplicateErrors1 = false;
	

			if (username1.trim().equals("Computer") || username2.trim().equals("Computer")) {
				emptyErrors = true;
			}
			
			// user names and markers cannot be empty
			if  ( marker1.trim().length() == 0 ||( numPlayer == 2 && marker2.trim().length() == 0) ) {
				emptyErrors = true;
				
			} // end of emptyErrors
			else {
				if (username1.trim().length() == 0) {
					//System.out.println("emptyError1: " + emptyErrors);
					emptyErrors = true;				
				}
				if (numPlayer == 2  && username2.trim().length() == 0 ) {
					//System.out.println("emptyError2: " + emptyErrors);
					emptyErrors = true;
				}
			}
			
			if (emptyErrors) {
				gridPaneForInfo.add(emptyInputsText, 0, 7);
			}
			
			
			if (numPlayer == 1 && duplicateErrors1) {
				gridPaneForInfo.add(duplicateInputsText1, 0, 8);
			}
			
			if (numPlayer == 2 && duplicateErrors1) {
				gridPaneForInfo.add(duplicateInputsText1, 0, 9);
			}
			// user names and markers cannot be the same
			if (numPlayer == 2) {
				if ((username1.trim().length() != 0 && username1.equals(username2)) || ( marker1.trim().length() != 0 && marker1.equals(marker2) )) {
					duplicateErrors = true;
					if (emptyErrors) {
						if (numPlayer == 2) {
							gridPaneForInfo.add(duplicateInputsText, 0, 8);
						}
						
						
					}// end of emptyErrors
					else {

						if (numPlayer == 2) {
							gridPaneForInfo.add(duplicateInputsText, 0, 7);
						}
						
					}
					
				} // end of duplicateErrors
			} // end of numPlayer == 2
			
			//System.out.println("empty errors: " + emptyErrors + " duplicateErrors: " + duplicateErrors);
			
//			System.out.println(emptyErrors + " " + duplicateErrors + "  " + duplicateErrors1);
			
			timeout =Integer.parseInt(fieldTimeOut.getText());
			//System.out.println(username1);
			if (!duplicateErrors && !emptyErrors && !duplicateErrors1) {
				// one player, she/he goes first
				if (numPlayer == 1) {
					if (playFirst.isSelected()) {
						humanPlayerID = 1;
						
						username.add(username1);
						marker.add(marker1);
						username.add("Computer");
						if (!marker1.equals("X"))
							marker.add("X");
						else
							marker.add("O");
						
					} else {
						humanPlayerID = 2;
						// one player, she/he goes second
						if (!marker1.equals("X"))
							marker.add("X");
						else
							marker.add("O");
						username.add("Computer");
						username.add(username1);
						marker.add(marker1);
					}
					
				} // end of one player
				
				
				// two players		
				else {
					username.add(username1);
					marker.add(marker1);
					username.add(username2);
					marker.add(marker2);
				}
				
				// create a game
				ticTacToe.startNewGame(numPlayer, timeout);
				
				// create players	
				// one player
				if (numPlayer == 1) {
					// human first
					if (humanPlayerID == 1) {
						ticTacToe.setIsHumanPlayer(true);
					
						
						// if we choose a user name, use the original player object				
						if (ticTacToe.getHashMap().containsKey(username1)) {
							// update the marker
							ticTacToe.getHashMap().get(username1).setMarker(marker1);
							// add the player object to the player list
							ticTacToe.player.add(0,ticTacToe.getHashMap().get(username1));
						}
						else {
							ticTacToe.createPlayer(username.get(humanPlayerID-1), marker.get(humanPlayerID-1), humanPlayerID);	
						}
						
						ticTacToe.setIsHumanPlayer(false);
						ticTacToe.createPlayer(username.get(2-humanPlayerID), marker.get(2-humanPlayerID), 3-humanPlayerID);
					}
					// human second
					else {
						ticTacToe.setIsHumanPlayer(false);
						ticTacToe.createPlayer(username.get(2-humanPlayerID), marker.get(2-humanPlayerID), 3-humanPlayerID);
						ticTacToe.setIsHumanPlayer(true);
						ticTacToe.createPlayer(username.get(humanPlayerID-1), marker.get(humanPlayerID-1), humanPlayerID);		
					}
					
				} // end of one player
				// two players
				else {
					
					ticTacToe.setIsHumanPlayer(true);
					
					// if we choose a user name, use the original player object				
					if (ticTacToe.getHashMap().containsKey(username1)) {
						// update the marker
						ticTacToe.getHashMap().get(username1).setMarker(marker1);
						// add the player object to the player list
						ticTacToe.player.add(0,ticTacToe.getHashMap().get(username1));
					}
					else {
						ticTacToe.createPlayer(username.get(0), marker.get(0), 1);
					}
					
					// if we choose a user name2, use the original player object				
					if (ticTacToe.getHashMap().containsKey(username2)) {
						// update the marker
						ticTacToe.getHashMap().get(username2).setMarker(marker2);
						// add the player object to the player list
						ticTacToe.player.add(1,ticTacToe.getHashMap().get(username2));
					}
					else {
						// create a player
						ticTacToe.createPlayer(username.get(1), marker.get(1), 2);
					}
					
				}
				
				// clear all the elements in pane
				pane.getChildren().clear();
				gridPaneForInfo.getChildren().clear();
				root.getChildren().clear();
				
				// play game
				playGame();
			} // end of error exists
		} catch (NumberFormatException error){

			
			if (gridPaneForInfo.getChildren().contains(timeStringErrorText)) {
				gridPaneForInfo.getChildren().remove(timeStringErrorText);
			}
							
			gridPaneForInfo.add(timeStringErrorText, 2, 0);
			
		} // end of catch
		
			
			
		});
	} // end of getPlayerInfo
	
	
	// playGame
	public void playGame() {
		// add a  game title
		root.setTop(new CustomPane("Welcome to Tic Tac Toe!"));
		
		// show the game board
		root.setCenter(ticTacToe.getGameDisplay());
		
		
		// computer makes first moves if necessary
		if (humanPlayerID == 2 && numPlayer == 1) {
			isAIMove = true;
			// generate row & column, call updatePlayerMove
			AIMove();
			
		} // end of  computer first move
		
					
		// show whose turn now
		turnLabel = new Label (username.get(ticTacToe.getPlayerID()-1) + "'s turn to play.");
		 quitBtn = new Button ("Quit the game");
		
		vBoxForGame.getChildren().addAll(turnLabel, quitBtn);
		
		
		vBoxForGame.setAlignment(Pos.CENTER);
		
		quitBtn.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
		});
		
		
		
		
		// add timeout
		if (timeout > 0) {
			// start the timer, when time is up, change playerID and restart animation
			// when a valid move is made, change playerID and restart animation
			timer = new Timeline(new KeyFrame(Duration.millis(timeout*1000), e-> {
				// when the timer is called, the previous player does not make a move, so we need to change the player ID here
				int curPlayerID = ticTacToe.setCurrentPlayer(ticTacToe.getPlayerID());
				// show whose turn now
				turnLabel.setText(username.get(curPlayerID-1) + "'s turn to play.");
				// computer makes a move (If a human moves, then this timer would not be called)
				// generate row & column, call updatePlayerMove
				if (numPlayer == 1) {
					isAIMove = true;
					AIMove();
					
					// when the game is over
					if (ticTacToe.getGameState() != 0) {
						// stop the timer when game is over
						timer.stop();
						// show the human lost the game

						Square.playSound("src/loseSound.mp3");
						
						turnLabel.setText(username.get(humanPlayerID-1) + " lost the game.");
						// update win/lose
						ticTacToe.player.get(0).setLose();
						ticTacToe.getHashMap().put(username.get(0), ticTacToe.player.get(0));
						
						
						if (vBoxForGame.getChildren().contains(quitBtn)) {
							vBoxForGame.getChildren().remove(quitBtn);
						}
						
						// show the two buttons (play again and quit)
						Square.hBox.setAlignment(Pos.CENTER);
						MainView.vBoxForGame.getChildren().add(Square.hBox);
					} // end of game over
					
					// get Current Player (must be a human since computer already changes player ID)
					curPlayerID = ticTacToe.getPlayerID();
				} // end of numPlayer == 1
				
				// show whose turn now
				if (ticTacToe.getGameState() == 0) {
					turnLabel.setText(username.get(curPlayerID-1) + "'s turn to play.");
				}
			
			}));
			
			timer.setCycleCount(9);
			timer.play();
		} // end of timeout
		
			
		/**************************add check user play results**************************/
			
		
//		root.setBottom(vBoxForGame);
//		root.setPadding(new Insets(50));
		
		root.setBottom(vBoxForGame);
		
		
		// create two buttons
		Button playAgainButton = new Button("Play Again");
		Button quitButton = new Button("Quit the game");
		
		if (Square.hBox.getChildren().size() == 0) {
			Square.hBox.getChildren().addAll(playAgainButton, quitButton);

		}
		
		// restart the game when "Play Again" works
		((ButtonBase) Square.hBox.getChildren().get(0)).setOnAction(e -> {
			// restart a game
			ticTacToe.setIsReplay(true);
			getMainView();
		});
		
		// quit the game
		((ButtonBase) Square.hBox.getChildren().get(1)).setOnAction(e -> {
			ticTacToe.saveInfo();
			// quit the game
			System.exit(0);
		});
	} // end of playGame
	
	

	public Scene getMainScene() {
		return this.scene;
	}
	
	public static int getHumanPlyaerID() {
		return humanPlayerID;
	}
	
	public void AIMove() {
		Random rand = new Random(); 
		int computerRow = rand.nextInt(3); 
		int computerCol = rand.nextInt(3);
		while (!MainView.ticTacToe.updatePlayerMove(computerRow, computerCol, 3-humanPlayerID)) {
			computerRow = rand.nextInt(3); 
			computerCol = rand.nextInt(3);
		}
		//System.out.println("computer marker in MainView: " + marker.get(1));
		BasicGameBoard.basicTwoD[computerRow][computerCol].setMarker(marker.get(2-humanPlayerID), false);
	}
	
	public static int getTimeout() {
		return timeout;
	}

	public static boolean getIsAIMove() {
		return isAIMove;
	}
	
	public static void setIsAIMove(boolean isMove) {
		isAIMove = isMove;
	}
	
	public static boolean getIsImageMarker1 () {
		return isImageMarker1;
	}
	
	public static boolean getIsImageMarker2 () {
		return isImageMarker2;
	}
	
	// display user information
	public void displayInfo() {
		 Set set = ticTacToe.getHashMap().entrySet();
	      Iterator iterator = set.iterator();
	      userInfoArrayList.clear();
	      usernameArrayList.clear();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
//	         System.out.print("key: "+ mentry.getKey() + " & Value: ");
//	         System.out.println(mentry.getValue());
	         
	         // store info into the array list
	         
	         userInfoArrayList.add(((Player) mentry.getValue()).getUsername()  + " (" + ((Player) mentry.getValue()).getMarker() + ") Win-Lose: " 
	         + ((Player) mentry.getValue()).getWin() + "-"+ ((Player) mentry.getValue()).getLose());
	         
	         usernameArrayList.add(((Player) mentry.getValue()).getUsername());
	      }
	} // end of displayInfo
}



// customePane
class CustomPane extends StackPane {
	public CustomPane(String title) {
		Text textTitle = new Text(title);
		getChildren().add(textTitle);
		textTitle.getStyleClass().add("textTitle");
	}
}

