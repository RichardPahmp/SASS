module mau.db.Neo4jProject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.neo4j.driver;

    opens mau.db.Neo4jProject to javafx.fxml;
    exports mau.db.Neo4jProject;
}