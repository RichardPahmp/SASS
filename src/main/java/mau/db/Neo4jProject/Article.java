package mau.db.Neo4jProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import static java.lang.Math.toIntExact;

import org.neo4j.driver.Record;

public class Article {
	
	public String name;
	public int year;
	public ArrayList<String> topics;
	
	public Article(Map<String, Object> map) {
		this.name = map.get("name").toString();
		this.year = toIntExact((long)map.get("year"));
		this.topics = new ArrayList<String>((Collection<? extends String>) map.get("topics"));
	}
	
	public Article(String name, int year, String[] topics) {
		this.name = name;
		this.year = year;
		this.topics = new ArrayList<String>(Arrays.asList(topics));
	}
	
	public String toString() {
		return name;
	}

	public static ArrayList<String> toNameList(ArrayList<Article> articles){
		ArrayList<String> names = new ArrayList<>();
		for(Article article : articles){
			names.add(article.name);
		}
		return names;
	}
}
