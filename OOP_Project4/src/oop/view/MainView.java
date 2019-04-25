package oop.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import oop.board.square.Square;
import oop.controller.*;
import oop.player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

//import main.course.oop.tictactoe.util.TwoDArray;

public class MainView {
	private int size;
	
	private Scene scene; 
	private StackPane pane = new StackPane();
	
	private int numPlayer;
	private static int gameVersion;
	private int numberOfCells;
	private static int timeout = 0;
	
	private ArrayList<String> username = new ArrayList<>();
	private ArrayList<String> marker = new ArrayList<>();
	private static int humanPlayerID = 1;
	
	private static boolean isAIMove = false;
	
	private boolean emptyErrors = false;
	private boolean duplicateErrors = false;
	
    private final int windowWidth = 1200; 
    private final int windowHeight = 900;
    private VBox VBoxRootLeft = new VBox(10);
    private VBox VBoxRootTop = new VBox(5);
    
    private Text emptyInputsText = new Text ("Fill in user name and marker. Computer cannot be username.");	
    private Text timeStringErrorText = new Text ("Time out must be an integer");
    private Text duplicateInputsText = new Text ("Two players cannot use the same username or marker.");
    private Text emptySelectionFirstScene = new Text ("Please select all the fields below.");
    private Text lackWinsErrorText = new Text ("You don't have enough wins to redeem all the selected prizes.");
    private Text lackUsernameErrorText = new Text ("Please select a user name.");
    private Text shopHintText = new Text ("Press Ctrl when select/deselect item(s).");
    
    private boolean hasUniqueTile;
    private String username_gift, username1, username2, marker1, marker2;
    
    private ArrayList<String> userInfoArrayList = new ArrayList<String>();
    private ArrayList<String> usernameArrayList = new ArrayList<String>();
    private ArrayList<String> username_WinArrayList = new ArrayList<String>();
    private ArrayList<String> chosenMarkerWins = new ArrayList<String> ();
    private ArrayList<String> wholeGiftList = new ArrayList<String>();
	// Create a gift list
	private ObservableList<String> giftList;
	private ObservableList<String> markerList1;
	private ObservableList<String> markerList2;
	// create a list view for players to choose multiple markers
	private ListView<String> giftsLV = new ListView<>(giftList);
    
    private ArrayList<String> imageOption = new ArrayList<String>();
    private Button checkOldUser = new Button("Check previous game results");
    
    private ListView<String> lvUsername;
    private int numberOfWins;
    private int chosenMaxNumberOfWins;
    private int index;
    
    public static BorderPane root;
	public static VBox vBoxForGame = new VBox(20);
	public static TTTControllerImpl ticTacToe = new TTTControllerImpl();
	public static Label turnLabel = new Label();
	public static Timeline timer;
	public static Timeline timerSquare;
    public static Button quitBtn;
	
    //public static Timeline timerSquare;
    
	public MainView() {
		imageOption.add("♫");
		imageOption.add("☀");
		imageOption.add("✈");
		imageOption.add("♕");
		imageOption.add("❀");

		MainView.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);

		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

		getMainView();		
		
	} //end of MainView
	
	public void getMainView() {
		root.setPadding(new Insets(50));
		// add a  game title
		root.setTop(new CustomPane("Welcome to Tic Tac Toe!", "textTitle"));		
		// remove play again buttons and whoseTurn label
		vBoxForGame.getChildren().clear();		
		// add radio buttons
		root.setCenter(getNumPlayers());
		root.setStyle("-fx-background-color: lightblue;");
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
		
		
		// add a button to check previous game results
		if (!VBoxRootLeft.getChildren().contains(checkOldUser)) {
			VBoxRootLeft.getChildren().add(checkOldUser);
		}
		
		if (VBoxRootLeft.getChildren().contains(lvUsername) ) {
			checkOldUser.setText("Check previous game results");
			VBoxRootLeft.getChildren().remove(lvUsername);
		}
		
		root.setLeft(VBoxRootLeft);
		displayInfo();
		
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
		
		// add a check box to ask whether the user wants to contain a unique tile
		CheckBox uniqueTileCheckBox = new CheckBox ("Contain a trap tile");
		uniqueTileCheckBox.getStyleClass().add("uniqueTileCheckBox");
		
		// add a button to giver users a chance to get gifts
		Button shopButton = new Button ("shop");
		shopButton.getStyleClass().add("shopButton");
		
		// create two buttons: continue and quit
		Button continueButton = new Button("Continue");
		Button quitButton = new Button("Quit the game");
		
		continueButton.getStyleClass().add("continueButton");
		quitButton.getStyleClass().add("quitButton");
		
		/****************Add 1st game menu to GUI********************/
		// add buttons to the pane
		vBoxForButtons.getChildren().addAll(selectNumberOfPlayers, selectVersionOfGames, continueButton, shopButton, quitButton);	
		vBoxForButtons.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(vBoxForButtons);
		
		// quit the game if "quit" is clicked
		quitButton.setOnAction(e -> {
			ticTacToe.saveInfo();
			System.exit(0);
		});
		
		// go to the gift shop to buy gifts
		shopButton.setOnAction ( e->{
			vBoxForButtons.getChildren().clear();
			shop(vBoxForButtons);
			addWholeGiftList();
			giftList = FXCollections.observableArrayList(wholeGiftList);
			//System.out.println(giftList);
			giftsLV.setItems(giftList);
			giftsLV.getSelectionModel().clearSelection(); 
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
				updateCheckBoxForTrapTile(true, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames );
			} else if (n.equals("ultimate game")) {			
				gameVersion = 2;
				numberOfCells = 3;
				updateCheckBoxForTrapTile(false, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames);
			} else if (n.equals("4*4 basic game")) {
				gameVersion = 3;
				numberOfCells = 4;
				updateCheckBoxForTrapTile(true, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames);
			}else if (n.equals("5*5 basic game")) {
				gameVersion = 4;
				numberOfCells = 5;
				updateCheckBoxForTrapTile(true, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames);
			}else if (n.equals("Territory Tic-Tac-Toe")) {
				gameVersion = 5;
				numberOfCells = 5;
				updateCheckBoxForTrapTile(false, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames);
			}else if (n.equals("6*6 basic game")) {
				gameVersion = 6;
				numberOfCells = 6;
				updateCheckBoxForTrapTile(true, vBoxForButtons, uniqueTileCheckBox, selectVersionOfGames);
			}
			
		});
		
		
		// check whether the player can move on to the next game menu
		continueButton.setOnAction(e -> {
			if (!selectNumberOfPlayers.getSelectionModel().isEmpty() && !selectVersionOfGames.getSelectionModel().isEmpty()) {
				// remove "ask number of players" 
				vBoxForButtons.getChildren().clear();
				// check whether the player wants to play with unique tiles
				if (uniqueTileCheckBox.isSelected()) {
					hasUniqueTile = true;
				}else {
					hasUniqueTile = false;
				}
				
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
	} // end of get # of players and the version of games
	
	
	public void shop (VBox vbox) {
		if (VBoxRootLeft.getChildren().contains(checkOldUser)) {
			VBoxRootLeft.getChildren().remove(checkOldUser);
		}
		
		if (!vbox.getChildren().contains(shopHintText)) {
			vbox.getChildren().add(shopHintText);
		}
		
		// create a list for all the previous user names
		displayInfo();
		ObservableList<String> username_WinOption = FXCollections.observableArrayList(username_WinArrayList);
		ComboBox<String> chooseUsername = new ComboBox<>(username_WinOption);
	
		chooseUsername.setPromptText("Select your user name");
		

		// get user name 
		chooseUsername.valueProperty().addListener((obs, old, n) -> {
			addWholeGiftList();
			username_gift = n; 	
			index = username_WinArrayList.indexOf(username_gift);
			// update number of wins for this user			
			String s = username_gift.substring(username_gift.indexOf("(") + 1);
			numberOfWins = Integer.parseInt(s.substring(0, s.indexOf(" ")));
			//System.out.println("numberOfWins " + numberOfWins);
			
			// update the list
			String username_only = usernameArrayList.get(index);
			//System.out.println("username only: " + username_only);
			ArrayList<String> temp_pMarkerWins = ticTacToe.getHashMap().get(username_only).getPersonalMarkerWins();
			//System.out.println(ticTacToe.getHashMap().get(username_only).getPersonalMarkerWins());
			wholeGiftList.removeAll(temp_pMarkerWins);
	
			giftList = FXCollections.observableArrayList(wholeGiftList);
			//System.out.println(giftList);
			giftsLV.setItems(giftList);

		});
		
		addWholeGiftList();
		giftList = FXCollections.observableArrayList(wholeGiftList);
		giftsLV.setItems(giftList);
		giftsLV.setMaxHeight(200);
		giftsLV.setMaxWidth(200);
		giftsLV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		giftsLV.setEditable(false);
		
		// get players' choice (gifts)		
		giftsLV.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends String> c)  -> {
			chosenMarkerWins.clear();
			verifySelectedItems(giftsLV.getSelectionModel());
			size = giftsLV.getSelectionModel().getSelectedIndices().size();
			if (size != 0) {
				String gift = giftsLV.getSelectionModel().getSelectedItems().get(size-1);
				String s = gift.substring(gift.indexOf("d") + 2);
				chosenMaxNumberOfWins = Integer.parseInt(s.substring(0, s.indexOf(" ")));
				chosenMarkerWins.addAll(giftsLV.getSelectionModel().getSelectedItems());
			}else {
				chosenMaxNumberOfWins = 0;
			}
			
			//System.out.println("after: "+giftsLV.getSelectionModel().getSelectedItems());
			
			
		});
		
		
		/* create a confirm button to 
			1. go back to the menu 
			2. check if users redeem successfully
			3. update users' marker list
		*/
		Button confirmButton = new Button ("Confirm");		
		confirmButton.getStyleClass().add("confirmButton");
		
		confirmButton.setOnAction(e -> {
			if (vbox.getChildren().contains(lackUsernameErrorText)) {
				vbox.getChildren().remove(lackUsernameErrorText);
			}
			if (vbox.getChildren().contains(lackWinsErrorText)) {
				vbox.getChildren().remove(lackWinsErrorText);
			}
			
			if (chooseUsername.getItems().size() == 0) {
				// go back to the menu scene
				getMainView();
			}
			
			if (chooseUsername.getSelectionModel().isEmpty() && chosenMaxNumberOfWins != 0) {
				if (!vbox.getChildren().contains(lackUsernameErrorText)) {
					vbox.getChildren().add(0, lackUsernameErrorText);
				}
			}
			else if (chosenMaxNumberOfWins == 0) {
				// go back to the menu scene
				getMainView();
			}
			else {
				if(numberOfWins < chosenMaxNumberOfWins) 
				{
					if (!vbox.getChildren().contains(lackWinsErrorText)) {
						vbox.getChildren().add(0, lackWinsErrorText);
					}
				}else {
					
					// update the list
					index = username_WinArrayList.indexOf(username_gift);
					String username_only = usernameArrayList.get(index);
					//System.out.println("username only: " + username_only);
					// update the player's markers with/without wins
					ticTacToe.getHashMap().get(username_only).setPersonalMarkerWins(chosenMarkerWins);
					
					ArrayList<String> temp_markersOnly = new ArrayList<>();
					// extract markers now
					for(String s: chosenMarkerWins){
						temp_markersOnly.add(Character.toString(s.charAt(0)));
					}
					if (temp_markersOnly.size() > 0) {
						ticTacToe.getHashMap().get(username_only).setPersonalMarkers(temp_markersOnly);
					}
						//System.out.println("temp: " + temp_markersOnly.get(0));
					
					
					// go back to the menu scene
					getMainView();
				}
			}
			
			
		});
		
		// add drop-down lists and buttons to the scene
		vbox.getChildren().addAll(chooseUsername, giftsLV, confirmButton);
	} // end of shop class
	
	
	// get timeout, user names, and markers
	public void getPlayerInfo(VBox vBoxForButtons) {

		
		// add a button to check previous game results
		if (!VBoxRootLeft.getChildren().contains(checkOldUser)) {
			VBoxRootLeft.getChildren().add(checkOldUser);
		}
		
		if (VBoxRootLeft.getChildren().contains(lvUsername) ) {
			checkOldUser.setText("Check previous game results");
			VBoxRootLeft.getChildren().remove(lvUsername);
		}
		
		root.setLeft(VBoxRootLeft);
		//displayInfo();
		// a list for previous game results
		//lvUsername = new ListView<> (FXCollections.observableArrayList(userInfoArrayList)); // userInfoArrayList
		
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
		
		startButton.getStyleClass().add("continueButton");
		quitButton.getStyleClass().add("quitButton");
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
			
			// check if the user name is a previous one
			if (ticTacToe.getHashMap().containsKey(username1)) {
				markerList1 = FXCollections.observableArrayList(ticTacToe.getHashMap().get(username1).getPersonalMarkers());
				enterMarker1.setItems(markerList1);
			} // end of user name is a previous one
			else {
				markerList1 = FXCollections.observableArrayList (imageOption);
				enterMarker1.setItems(markerList1);
			}
		});
		
		
		/************************************Select/Ask user name 2*********************************/
		ComboBox<String> enterUsername2 = new ComboBox<>(usernameOption);
		enterUsername2.setEditable(true);
		
	
		// get user name 2
		enterUsername2.valueProperty().addListener((obs, old, n) -> {
			username2 = n; 	
			
			// check if the user name is a previous one
			if (ticTacToe.getHashMap().containsKey(username2)) {
				markerList2 = FXCollections.observableArrayList(ticTacToe.getHashMap().get(username2).getPersonalMarkers());
				enterMarker2.setItems(markerList2);
			} // end of user name is a previous one
			else {
				markerList2 = FXCollections.observableArrayList (imageOption);
				enterMarker2.setItems(markerList2);
			}
		});
		
		// add "enter user name 2" to GUI
		if (numPlayer == 2) {	
			// add combo box to the GUI
			gridPaneForInfo.add(enterUsername2, 1, 3);
			
		}
			


	
			
		
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
			//System.out.println("marker: " + marker1);
			
			// user names and markers cannot be empty
			if  (marker1 == null || username1.trim().length() == 0 || marker1.trim().length() == 0 ||  
				(numPlayer == 2 && (marker2 == null ||  username2.trim().length() == 0 || marker2.trim().length() == 0)) ) {
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
					username2 = "Computer";
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
				ticTacToe.startNewGame(numPlayer, timeout, gameVersion, numberOfCells, hasUniqueTile);
				
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
						ticTacToe.player.get(0).setPersonalMarkers(imageOption);
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
						ticTacToe.player.get(0).setPersonalMarkers(imageOption);
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
						ticTacToe.player.get(1).setPersonalMarkers(imageOption);
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
		//root.setStyle("-fx-background-color: wheat");
		VBoxRootTop.getChildren().clear();
		
		CustomPane title = new CustomPane("Welcome to Tic Tac Toe!", "textTitle");
		
		HBox hBoxForPlayerVS = new HBox(5);
		Label playerUsername1 = new Label (username1);
		Label playerVS = new Label (" VS ");
		Label playerUsername2 = new Label (username2);
		
		playerUsername1.getStyleClass().add("playerVSInfo");
		playerUsername1.getStyleClass().add("player1BGC");
		
		playerVS.getStyleClass().add("playerVSInfo");
		playerUsername2.getStyleClass().add("playerVSInfo");
		playerUsername2.getStyleClass().add("player2BGC");
		
		hBoxForPlayerVS.getChildren().addAll(playerUsername1, playerVS, playerUsername2);
		hBoxForPlayerVS.setAlignment(Pos.CENTER);
		hBoxForPlayerVS.getStyleClass().add("vBoxRootTop");
		
		VBoxRootTop.getChildren().addAll(title, hBoxForPlayerVS);
		// add a  game title
	
		root.setTop(VBoxRootTop);
		
		// show the game board
		root.setCenter(ticTacToe.getGameDisplay());		
						
		// show whose turn now
		turnLabel = new Label (username.get(ticTacToe.getPlayerID()-1) + "'s turn to play.");
		
		// create a quit button in the middle of the game
		quitBtn = new Button ("Quit the game");

		quitBtn.getStyleClass().add("quitButton");
		
		vBoxForGame.getChildren().addAll(turnLabel, quitBtn);
		vBoxForGame.setAlignment(Pos.CENTER);
		vBoxForGame.getStyleClass().add("vBoxForGame");
		
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
						//System.out.println("Human lost");
						
						
						if (hasUniqueTile && MainView.ticTacToe.getUniqueTileClicked()) {
							turnLabel.setText(username.get(0) + " won the game. Computer clicked on the trap tile.");
							Square.playSound("src/winSound.mp3");
						}else {
							turnLabel.setText(username.get(0) + " lost the game.");
							Square.playSound("src/loseSound.mp3");
						}
						
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
			if (ticTacToe.getNumberPlayers() == 1) {
				if (gameVersion != 2) {
					timer.setCycleCount((int) (Math.pow(MainView.ticTacToe.getNumberOfCells(), 2)));
				}else {
					timer.setCycleCount((int) (Math.pow(MainView.ticTacToe.getNumberOfCells(), 4)));
				}
			}
			else {
				timer.setCycleCount(Timeline.INDEFINITE);
			}
			
			
			timer.play();
		} // end of timeout
		
			
		/**************************add check user play results**************************/
			
		root.setBottom(vBoxForGame);
		
		
		// create two buttons
		Button playAgainButton = new Button("Play Again");
		Button quitButton = new Button("Quit the game");
		
		playAgainButton.getStyleClass().add("continueButton");
		quitButton.getStyleClass().add("quitButton");
		
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
	
	private void updateCheckBoxForTrapTile(boolean allow, VBox vb, CheckBox cb, ComboBox<String> comb) {
		int properLocationForCB = vb.getChildren().indexOf(comb);
		if (allow) {
			if (!vb.getChildren().contains(cb)) {
				vb.getChildren().add (properLocationForCB + 1, cb);
			}
		} 			
		else {
				 if (vb.getChildren().contains(cb)) {
						vb.getChildren().remove (cb);
				}
		}
	}

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
	
	public void addWholeGiftList () {
		wholeGiftList.clear();
		wholeGiftList.add("✰  (need 2 wins)");
		wholeGiftList.add("❤  (need 5 wins)");
		wholeGiftList.add("⌘ (need 10 wins)");
		wholeGiftList.add("✞  (need 15 wins)");
		wholeGiftList.add("☎  (need 20 wins)");
		wholeGiftList.add("✂  (need 30 wins)");
		wholeGiftList.add("➹  (need 40 wins)");
		wholeGiftList.add("☂   (need 55 wins)");
		wholeGiftList.add("❄  (need 70 wins)");
		wholeGiftList.add("☯  (need 85 wins)");
		wholeGiftList.add("✌ (need 100 wins)");
	}
	
	
	protected <T> void verifySelectedItems(MultipleSelectionModel<T> selectionModel) { 
        boolean problem = false; 
        for (T item : selectionModel.getSelectedItems()) { 
            if (item == null) { 
                problem = true; 
            } 
        } 
        if (problem) { 
            ArrayList<Integer> selectedIndices = new ArrayList<>(selectionModel.getSelectedIndices()); 
           // System.out.println("in function, selectedIndices: " );
            for (int indice : selectedIndices) { 
                selectionModel.select(indice); 
            } 
        } 
    }
	
	
	// display user information
	public void displayInfo() {
		String username;
		 Set set = ticTacToe.getHashMap().entrySet();
	      Iterator iterator = set.iterator();
	      userInfoArrayList.clear();
	      usernameArrayList.clear();
	      username_WinArrayList.clear();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();	
	         username = ((Player) mentry.getValue()).getUsername();
	         // store info into the array list
	         userInfoArrayList.add(username  + " (" + ((Player) mentry.getValue()).getMarker() + ") Win-Lose: " 
	         + ((Player) mentry.getValue()).getWin() + "-"+ ((Player) mentry.getValue()).getLose());
	         
	         usernameArrayList.add(((Player) mentry.getValue()).getUsername());
	         username_WinArrayList.add(username + " (" +((Player) mentry.getValue()).getWin() + " wins)");
	      }
	} // end of displayInfo
} // end of class MainView



// customePane for "Welcome to Tic Tac Toe"
class CustomPane extends StackPane {
	public CustomPane(String title, String titleStyle) {
		Text textTitle = new Text(title);
		getChildren().add(textTitle);
		textTitle.getStyleClass().add(titleStyle);
	}
}
