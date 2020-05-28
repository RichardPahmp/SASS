package mau.db.Neo4jProject;

import java.util.Map;

public class Author {

	public String name;

	public Author(String name){
		this.name = name;
	}
	
	public Author(Map<String, Object> map) {
		this.name = map.get("name").toString();
	}
	
	public String toString() {
		return name;
	}
}
