package mau.db.Neo4jProject;

import javafx.fxml.Initializable;

public abstract class Controller{

	protected App app;
	protected Neo4jDatabase database;
	
	public void initialize(App app, Neo4jDatabase database) {
		this.app = app;
		this.database = database;
	}
	
}
