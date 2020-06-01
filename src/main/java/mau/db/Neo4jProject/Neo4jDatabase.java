package mau.db.Neo4jProject;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Neo4jDatabase implements AutoCloseable {
	private final Driver driver;

	public Neo4jDatabase(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	public void mergeArticle(Article article){
		try (Session session = driver.session()){
			var p = parameters("name", article.name, "year", article.year, "topics", article.topics);
			session.writeTransaction(tx -> tx.run("MERGE (a:Article {name: $name}) SET a.year = $year, a.topics = $topics", p));
		}
	}
	
	public void mergeReference(String article1, String article2) {
		try(Session session = driver.session()){
			var p = parameters("article1", article1, "article2", article2);
			session.writeTransaction(tx -> tx.run("MATCH (a:Article), (b:Article) WHERE a.name = $article1 AND b.name = $article2 MERGE (a)-[:References]->(b)", p));
		}
	}
	
	public void deleteReference(String article1, String article2) {
		try(Session session = driver.session()){
			var p = parameters("article1", article1, "article2", article2);
			session.writeTransaction(tx -> tx.run("MATCH (a:Article {name: $article1})-[r:References]->(b:Article {name: $article2}) DELETE r", p));
		}
	}
	
	public void deleteReferences(String article, ArrayList<String> references) {
		for(String ref : references) {
			deleteReference(article, ref);
		}
	}
	
	public void deleteAuthored(String author, String article) {
		try(Session session = driver.session()){
			var p = parameters("article", article, "author", author);
			session.writeTransaction(tx -> tx.run("MATCH (a:Author {name: $author})-[r:Wrote]->(b:Article {name: $article}) DELETE r", p));
		}
	}
	
	public void deleteAuthored(String article, ArrayList<String> authors) {
		for(String author : authors) {
			deleteAuthored(author, article);
		}
	}
	
	public void mergeAuthored(String author, String article) {
		try(Session session = driver.session()){
			var p = parameters("article", article, "author", author);
			session.writeTransaction(tx -> tx.run("MATCH (a:Author), (b:Article) WHERE a.name = $author AND b.name = $article MERGE (a)-[:Wrote]->(b)", p));
		}
	}
	
	public void mergeAuthored(String article, ArrayList<String> authors) {
		for(String author : authors) {
			mergeAuthored(author, article);
		}
	}
	
	public void mergeReferences(String article, ArrayList<String> references) {
		for(String ref : references) {
			mergeReference(article, ref);
		}
	}

	public ArrayList<Article> getAllArticles(){
		ArrayList<Article> articles = new ArrayList<>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Article) OPTIONAL MATCH (a)<-[:Wrote]-(b:Author) RETURN properties(a) AS props, collect(b.name) AS authors ORDER BY props.name");
			articles = getArticlesFromResult(result);
		}
		return articles;
	}

	public ArrayList<Article> getReferences(String article, int steps){
		ArrayList<Article> references;
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (:Article {name: $article})-[:References*" + steps + "]->(a:Article) OPTIONAL MATCH (a)<-[:Wrote]-(b:Author) RETURN DISTINCT properties(a) AS props, collect(b.name) AS authors ORDER BY props.name", p);
			references = getArticlesFromResult(result);
		}
		return references;
	}

	public ArrayList<Article> getReferencers(String article, int steps){
		ArrayList<Article> articles;
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (:Article {name: $article})<-[:References*" + steps + "]-(a:Article) OPTIONAL MATCH (a)<-[:Wrote]-(b:Author) RETURN DISTINCT properties(a) AS props, collect(b.name) AS authors ORDER BY props.name", p);
			articles = getArticlesFromResult(result);
		}
		return articles;
	}

	public ArrayList<Article> getSharedReferencers(String article) {
		ArrayList<Article> articles;
		try (Session session = driver.session()) {
			var p = parameters("name", article);
			Result result = session.run("MATCH (a:Article {name: $name})-[:References]->()<-[:References]-(b:Article) OPTIONAL MATCH (b)<-[:Wrote]-(c:Author) return properties(b) AS props, collect(c.name) AS authors", p);
			articles = getArticlesFromResult(result);
		}
		return articles;
	}
	
	public ArrayList<String> getAllAuthorNames(){
		ArrayList<String> authors = new ArrayList<String>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Author) RETURN a.name AS name ORDER BY name");
			while(result.hasNext()) {
				Record record = result.next();
				authors.add(record.get("name").asString());
			}
		}
		return authors;
	}
	
	public ArrayList<String> getAuthors(String article){
		ArrayList<String> authors = new ArrayList<String>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)<-[:Wrote]-(b) RETURN b.name AS name ORDER BY name", parameters("article", article));
			while(result.hasNext()) {
				Record record = result.next();
				authors.add(record.get("name").asString());
			}
		}
		return authors;
	}
	
	public void deleteAuthor(String name) {
		try(Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MATCH (a:Author) WHERE a.name = $name DETACH DELETE a", parameters("name", name)));
		}
	}
	
	public void mergeAuthor(String name) {
		try(Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Author {name: $name}) ", parameters("name", name)));
		}
	}
	
	public void deleteArticle(String article) {
		try(Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MATCH (a:Article) WHERE a.name = $article DETACH DELETE a", parameters("article", article)));
		}
	}

	private ArrayList<Article> getArticlesFromResult(Result result){
		ArrayList<Article> articles = new ArrayList<>();
		while(result.hasNext()) {
			Record record = result.next();
			Article article = new Article(record.get("props").asMap());
			try {
				List<String> authors = (List<String>) record.get("authors").asObject();
				if(authors != null){
					article.setAuthors(authors);
				}
			} catch (NoSuchRecordException e){

			}
			articles.add(article);
		}
		return articles;
	}
	
}

