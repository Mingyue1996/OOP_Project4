package oop.view;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	private static int gameVersion;
	private int numberOfCells;
	private static int timeout = 0;
	public static Label turnLabel = new Label();
	
	private ArrayList<String> username = new ArrayList<>();
	private ArrayList<String> marker = new ArrayList<>();
	private static int humanPlayerID = 1;
	public static Timeline timer;
	private static boolean isAIMove = false;
	
	private boolean emptyErrors = false;
	private boolean duplicateErrors = false;
	
    private final int windowWidth = 1200; 
    private final int windowHeight = 900;
    private Text emptyInputsText = new Text ("Fill in user name and marker. Computer cannot be username.");	
    private Text timeStringErrorText = new Text ("Time out must be an integer");
    private Text duplicateInputsText = new Text ("Two players cannot use the same username or marker.");
    private Text emptySelectionFirstScene = new Text ("Please select all the fields below.");
    
    
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
		isAIMove = false;
		username1 = username2 = marker1 = marker2 ="";
		checkOldUser.setText("Check previous game results");
	}
	
	
	public StackPane getNumPlayers() {
		this.pane = new StackPane();
		
		// create a pane for the game menu
		VBox vBoxForButtons = new VBox(20);
		
		/*********************Ask # of players **********************/
		// provide options for users
		ObservableList<String> numberOfPlayersOption = 
			    FXCollections.observableArrayList(
			        "Play against computer",
			        "Play with another human"
		);
		
		// create a comboBox to ask the # of players
		ComboBox<String> selectNumberOfPlayers = new ComboBox<>(numberOfPlayersOption);
		selectNumberOfPlayers.setPromptText("Select who you want to play with");
		selectNumberOfPlayers.setEditable(false);
		
		
		
		/*************************Ask version of games ********************/
		// provide game versions for users
				ObservableList<String> versionOfGamesOption = 
					    FXCollections.observableArrayList(
					        "3*3 basic game",
					        "ultimate game",
					        "4*4 basic game",
					        "5*5 basic game",
					        "Territory Tic-Tac-Toe",
					        "6*6 basic game"
				);
				
		// create a comboBox to ask the # of players
		ComboBox<String> selectVersionOfGames = new ComboBox<>(versionOfGamesOption);
		selectVersionOfGames.setPromptText("Select which game you want to play");
		selectVersionOfGames.setEditable(false);
		
		
		// create two buttons: continue and quit
		Button continueButton = new Button("Continue");
		Button quitButton = new Button("Quit the game");
		
		/****************Add 1st game menu to GUI********************/
		// add buttons to the pane
		vBoxForButtons.getChildren().addAll(selectNumberOfPlayers, selectVersionOfGames, continueButton, quitButton);	
		vBoxForButtons.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(vBoxForButtons);
		
		//quit the game if "quit" is clicked
		quitButton.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
		});
		
		// update the # of players
		selectNumberOfPlayers.valueProperty().addListener((obs, old, n) -> {
			if (n.equals("Play against computer")) {
				numPlayer = 1;
			} else if (n.equals("Play with another human")) {			
				numPlayer = 2;
			}
			
		});
		
		
		// update the game version
		selectVersionOfGames.valueProperty().addListener((obs, old, n) -> {
			if (n.equals("3*3 basic game")) {
				gameVersion = 1;
				numberOfCells = 3;
			} else if (n.equals("ultimate game")) {			
				gameVersion = 2;
				numberOfCells = 3;
			} else if (n.equals("4*4 basic game")) {
				gameVersion = 3;
				numberOfCells = 4;
			}else if (n.equals("5*5 basic game")) {
				gameVersion = 4;
				numberOfCells = 5;
			}else if (n.equals("Territory Tic-Tac-Toe")) {
				gameVersion = 5;
				numberOfCells = 5;
			}else if (n.equals("6*6 basic game")) {
				gameVersion = 6;
				numberOfCells = 6;
			}
			
		});
		
		
		// check whether the player can move on to the next game menu
		continueButton.setOnAction(e -> {
			if (!selectNumberOfPlayers.getSelectionModel().isEmpty() && !selectVersionOfGames.getSelectionModel().isEmpty()) {
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

		VBox VBoxRootLeft = new VBox(10);
		// add a button to check previous game results
		if (!VBoxRootLeft.getChildren().contains(checkOldUser)) {
			VBoxRootLeft.getChildren().add(checkOldUser);
		}
		
		root.setLeft(VBoxRootLeft);
		displayInfo();
		// a list for previous game results
		lvUsername = new ListView<> (FXCollections.observableArrayList(userInfoArrayList)); // userInfoArrayList
		
		// show/hide previous game results
		checkOldUser.setOnAction (e -> {
			if (checkOldUser.getText().contains("Check")) {
				VBoxRootLeft.getChildren().add(lvUsername);
				
				checkOldUser.setText("Hide previous game results");
			}
			else {
				if (VBoxRootLeft.getChildren().contains(lvUsername)) {
					VBoxRootLeft.getChildren().remove(lvUsername);
				}
				checkOldUser.setText("Check previous game results");
			}
			
		});
		
		// create two buttons in the second game menu: continue and quit 
		Button startButton = new Button("Start the Game");
		Button quitButton = new Button("Quit the game");
		
		// add buttons to the pane
		vBoxForButtons.getChildren().addAll(startButton, quitButton);		
		vBoxForButtons.setAlignment(Pos.CENTER);
		
		// create a new grid pane for player info
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
		
		// add labels
		// when there is one player
		if (numPlayer == 1) {
			
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
		
		
		/*****************************************Select/Ask user name 1**************************/ 
		// create a list for all the previous user names
		displayInfo();
		ObservableList<String> usernameOption = FXCollections.observableArrayList(usernameArrayList);
		ComboBox<String> enterUsername1 = new ComboBox<>(usernameOption);
		enterUsername1.setEditable(true);
		
		// add combox box to the GUI
		gridPaneForInfo.add(enterUsername1, 1, 1);
		
		// get user name 1
		enterUsername1.valueProperty().addListener((obs, old, n) -> {
			username1 = n; 		
		});
		
		
		/************************************Select/Ask user name 2*********************************/
		ComboBox<String> enterUsername2 = new ComboBox<>(usernameOption);
		enterUsername2.setEditable(true);
		
	
		// get user name 2
		enterUsername2.valueProperty().addListener((obs, old, n) -> {
			username2 = n; 		
		});
		
		// add "enter user name 2" to GUI
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
			
		
		// quit the game if "quit" is clicked
		quitButton.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
			});		
		
		// get timeout, user name, marker when game starts
		startButton.setOnAction(e -> {			
		try {

			// remove previous errors
			if (vBoxForButtons.getChildren().contains(emptyInputsText)) {
				vBoxForButtons.getChildren().remove(emptyInputsText);
				
			} // end of remove errors
			if (vBoxForButtons.getChildren().contains(duplicateInputsText)) {
				vBoxForButtons.getChildren().remove(duplicateInputsText);
			}
			// remove timeout is not an integer error
			if (gridPaneForInfo.getChildren().contains(timeStringErrorText)) {
				gridPaneForInfo.getChildren().remove(timeStringErrorText);
			}

			// reset all the errors to false
			emptyErrors = false;
			duplicateErrors = false;
	
			// Computer cannot be the user name
			if (username1.trim().equals("Computer") || username2.trim().equals("Computer")) {
				emptyErrors = true;
			}
			
			// user names and markers cannot be empty
			if  (username1.trim().length() == 0 || marker1.trim().length() == 0 ||
				(numPlayer == 2 && (username2.trim().length() == 0 || marker2.trim().length() == 0))) {
				emptyErrors = true;
				
			} // end of emptyErrors
			
			if (emptyErrors) {
				vBoxForButtons.getChildren().add(1, emptyInputsText);
			}
			
			// user names and markers cannot be the same
			if (numPlayer == 2) {
				if ((username1.trim().length() != 0 && username1.equals(username2)) || ( marker1.trim().length() != 0 && marker1.equals(marker2))) {
					duplicateErrors = true;
					if (emptyErrors) {
							vBoxForButtons.getChildren().add(2, duplicateInputsText);
					}// end of emptyErrors
					else {
							vBoxForButtons.getChildren().add(1, duplicateInputsText);
					}
					
				} // end of duplicateErrors
			} // end of numPlayer == 2

			// get time out
			timeout =Integer.parseInt(fieldTimeOut.getText());

			if (!duplicateErrors && !emptyErrors) {

				if (numPlayer == 1) {
						humanPlayerID = 1;
						// add user name and marker
						username.add(username1);
						marker.add(marker1);
						username.add("Computer");
						if (!marker1.equals("X"))
							marker.add("X");
						else
							marker.add("O");
				} // end of one player
				
				
				// two players		
				else {
					username.add(username1);
					marker.add(marker1);
					username.add(username2);
					marker.add(marker2);
				}
				
				// create a game
				ticTacToe.startNewGame(numPlayer, timeout, gameVersion, numberOfCells);
				
				// create players	
				// one player
				if (numPlayer == 1) {
					// human first
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
					// create a computer player
					ticTacToe.setIsHumanPlayer(false);
					ticTacToe.createPlayer(username.get(1), marker.get(1), 2);
					
				} // end of create one human player
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
					
				} // end of create two human players
				
				// clear all the elements in pane
				pane.getChildren().clear();
				gridPaneForInfo.getChildren().clear();
				root.getChildren().clear();
				
				// play game
				playGame();
			} // end of error exists
		} catch (NumberFormatException error){

			// add "time out is a string" error
			if (gridPaneForInfo.getChildren().contains(timeStringErrorText)) {
				gridPaneForInfo.getChildren().remove(timeStringErrorText);
			}
							
			gridPaneForInfo.add(timeStringErrorText, 2, 0);
			
		} // end of catch
		
			
			
		}); // end of try block 
	} // end of getPlayerInfo
	
	
	// playGame
	public void playGame() {
		// add a  game title
		root.setTop(new CustomPane("Welcome to Tic Tac Toe!"));
		
		// show the game board
		root.setCenter(ticTacToe.getGameDisplay());		
						
		// show whose turn now
		turnLabel = new Label (username.get(ticTacToe.getPlayerID()-1) + "'s turn to play.");
		
		// create a quit button in the middle of the game
		quitBtn = new Button ("Quit the game");
		
		vBoxForGame.getChildren().addAll(turnLabel, quitBtn);
		vBoxForGame.setAlignment(Pos.CENTER);
		
		quitBtn.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
		});
		
		/********************** add time out *************************/
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
						System.out.println("Human lost");
						Square.playSound("src/loseSound.mp3");
						
						turnLabel.setText(username.get(0) + " lost the game.");
						// update the lose condition for the human player and store results
						ticTacToe.player.get(0).setLose();
						ticTacToe.getHashMap().put(username.get(0), ticTacToe.player.get(0));
						
						// remove the quit button for the middle of game when the game is over
						if (vBoxForGame.getChildren().contains(quitBtn)) {
							vBoxForGame.getChildren().remove(quitBtn);
						}
						
						// show the two buttons (play again and quit) after the game is over
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
			if (gameVersion != 2) {
				timer.setCycleCount(numberOfCells * numberOfCells);
			}else {
				timer.setCycleCount(numberOfCells * numberOfCells * numberOfCells * numberOfCells);
			}
			
			timer.play();
		} // end of timeout
		
			
		/**************************add check user play results**************************/
			
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
	
	// computer makes a random move
	public void AIMove() {
		Random rand = new Random(); 
		int computerRow = rand.nextInt(numberOfCells); 
		int computerCol = rand.nextInt(numberOfCells);
		while (!MainView.ticTacToe.updatePlayerMove(computerRow, computerCol, 2)) {
			computerRow = rand.nextInt(numberOfCells); 
			computerCol = rand.nextInt(numberOfCells);
		}
		
		// when the game is over
		if (ticTacToe.getGameState() == 0) {
			// update valid game boards
			if (gameVersion == 2) {
				ticTacToe.updateValidBoard(ticTacToe.transformIntoSquareID(computerRow, computerCol, -1, -1, false));
			}
			
		}
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
	
	public static int getGameVersion () {
		return gameVersion;
	}
	
	public int getNumberOfCells() {
		return numberOfCells;
	}
	
	// display user information
	public void displayInfo() {
		 Set set = ticTacToe.getHashMap().entrySet();
	      Iterator iterator = set.iterator();
	      userInfoArrayList.clear();
	      usernameArrayList.clear();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();	
	         
	         // store info into the array list
	         userInfoArrayList.add(((Player) mentry.getValue()).getUsername()  + " (" + ((Player) mentry.getValue()).getMarker() + ") Win-Lose: " 
	         + ((Player) mentry.getValue()).getWin() + "-"+ ((Player) mentry.getValue()).getLose());
	         
	         usernameArrayList.add(((Player) mentry.getValue()).getUsername());
	      }
	} // end of displayInfo
} // end of class MainView



// customePane for "Welcome to Tic Tac Toe"
class CustomPane extends StackPane {
	public CustomPane(String title) {
		Text textTitle = new Text(title);
		getChildren().add(textTitle);
		textTitle.getStyleClass().add("textTitle");
	}
}
