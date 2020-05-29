package mau.db.Neo4jProject;

import java.util.*;

import static java.lang.Math.toIntExact;

import org.neo4j.driver.Record;

public class Article implements Comparable<Article>{
	
	public String name;
	public int year;
	public ArrayList<String> topics;
	public ArrayList<String> authors;
	
	public Article(Map<String, Object> map) {
		this.name = map.get("name").toString();
		this.year = toIntExact((long)map.get("year"));
		this.topics = new ArrayList<String>((Collection<? extends String>) map.get("topics"));
		this.authors = new ArrayList<>();
	}

	public Article(Map<String, Object> map, List<String> authors){
		this(map);
		this.authors = new ArrayList<String>(authors);
	}
	
	public Article(String name, int year, String[] topics) {
		this.name = name;
		this.year = year;
		this.topics = new ArrayList<String>(Arrays.asList(topics));
	}

	public void setAuthors(Collection<? extends String> authors){
		this.authors = new ArrayList<>(authors);
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

	public boolean containsTopics(String[] topics){
		return this.topics.containsAll(Arrays.asList(topics));
	}

	public boolean containsTopics(String topics){
		String[] split = topics.split(", *");
		return containsTopics(split);
	}

	public boolean containsAuthors(String[] authors){
		return this.authors.containsAll(Arrays.asList(authors));
	}

	public boolean containsAuthors(String authors){
		String[] split = authors.split(", *");
		return containsAuthors(split);
	}

	@Override
	public int compareTo(Article other){
		if(name == other.name && year == other.year && topics == other.topics){
			return 0;
		} else {
			return 1;
		}
	}

	public boolean equals(Article other){
		return this.name == other.name;
	}
}
