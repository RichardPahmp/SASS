package mau.db.Neo4jProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.neo4j.driver.exceptions.ServiceUnavailableException;

import java.io.File;
import java.io.FileReader;
import java.util.Optional;
import java.util.Properties;

public class App extends Application
{
	private MainViewController mainViewController;
	public Stage primaryStage;
	private Neo4jDatabase database;

	private static String USERNAME;
	private static String ADMIN = "admin";
	
    @Override
	public void start(Stage primaryStage) throws Exception {
    	File file = new File("properties.txt");
		FileReader reader = new FileReader(file);
		Properties props = new Properties();
		props.load(reader);
		reader.close();

		String url = props.getProperty("DatabaseURL");
		String user = props.getProperty("DatabaseUser");
		String password = props.getProperty("DatabasePassword");

		App.USERNAME = openLoginDialog();
		//TODO: Fix multiple selection shit

		try {
			database = new Neo4jDatabase(url, user, password);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/MainView.fxml"));
			BorderPane pane = (BorderPane) loader.load();
			mainViewController = (MainViewController) loader.getController();
			mainViewController.initialize(this, database);
			Scene scene = new Scene(pane, 800, 600);
			this.primaryStage = primaryStage;
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Scientific Article Snowballing System");
			this.primaryStage.setOnCloseRequest(e -> {
				try {
					database.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			this.primaryStage.show();
		} catch (ServiceUnavailableException e){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Service unavailable");
			alert.setContentText("The database service is unavailable or the connection was refused.");

			alert.showAndWait();
			database.close();
			primaryStage.close();
		}
	}

	public String openLoginDialog(){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Login");
		dialog.setHeaderText("Enter a username to login");
		dialog.setContentText("Username:");

		Optional<String> result = dialog.showAndWait();
		String username;
		if(result.isPresent()){
			username = result.get();
		} else {
			username = "";
		}

		return username;
	}

	public static boolean isAdmin(){
    	return App.USERNAME.equals(ADMIN);
	}

	public static boolean isAnon(){
    	return App.USERNAME.isBlank();
	}

	public static boolean isUser(){
    	return !App.isAnon() && !isAdmin();
	}

	public static final String getUsername(){
    	return App.USERNAME;
	}

	public static void main(String[] args){
    	launch(args);
    }
}
