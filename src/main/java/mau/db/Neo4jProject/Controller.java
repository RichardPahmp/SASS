package mau.db.Neo4jProject;

import javafx.fxml.Initializable;

public abstract class Controller{

	protected App app;
	protected Neo4jDatabase database;
	protected String username;
	
	public void initialize(App app, Neo4jDatabase database) {
		this.app = app;
		this.database = database;
		this.username = username;
	}
	
}
