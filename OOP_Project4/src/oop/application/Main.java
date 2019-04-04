package oop.application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import oop.view.MainView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import oop.view.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView myView = new MainView();
			BorderPane root = new BorderPane();
			Scene scene =  myView.getMainScene();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// set a title in GUI
			//root.setTop(new CustomPane("Welcome to Tic Tac Toe!"));
			
			// give a title of Java app
			primaryStage.setTitle("TicTacToe - Mingyue Chen");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

