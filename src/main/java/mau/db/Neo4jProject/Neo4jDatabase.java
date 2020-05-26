package mau.db.Neo4jProject;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;

public class Neo4jDatabase implements AutoCloseable {
	private final Driver driver;

	public Neo4jDatabase(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
	
	public void addArticle(String name, int year) {
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("CREATE (a:Article {name: $name, year: $year})", parameters("name", name, "year", year)));
		}
	}
	
	/*public void mergeArticle(Article article) {
		try (Session session = driver.session()){
			var p = parameters("name", article.name, "year", article.year, "topics", article.topics);
			session.writeTransaction(tx -> tx.run("MERGE (a:Article {name: $name, year: $year, topics: $topics})", p));
		}
	}*/

	public void mergeArticle(Article article){
		try (Session session = driver.session()){
			var p = parameters("name", article.name, "year", article.year, "topics", article.topics);
			session.writeTransaction(tx -> tx.run("MERGE (a:Article {name: $name}) SET a.year = $year, a.topics = $topics", p));
		}
	}
	
	public void addReference(String article1, String article2) {
		try(Session session = driver.session()){
			writeReference(session, article1, article2);
		}
	}
	
	public void addReferences(String article, ArrayList<String> articles) {
		try(Session session = driver.session()){
			for (String ref : articles) {
				writeReference(session, article, ref);
			}
		}
	}
	
	private void writeReference(Session session, String article1, String article2) {
		var p = parameters("article1", article1, "article2", article2);
		session.writeTransaction(tx -> tx.run("MATCH (a:Article), (b:Article) WHERE a.name = $article1 AND b.name = $article2 CREATE (a)-[:References]->(b)", p));
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
	
	public Article getArticle(String name) {
		try(Session session = driver.session()){
			var p = parameters("name", name);
			Result result = session.run("MATCH (a:Article) WHERE a.name = $name RETURN properties(a) AS props", p);
			while(result.hasNext()) {
				Record record = result.next();
				return new Article(record.get("props").asMap());
			}
		}
		return null;
	}
	
	public ArrayList<String> getAllArticleNames() {
		ArrayList<String> articles = new ArrayList<String>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Article) RETURN a.name AS name");
			while(result.hasNext()) {
				Record record = result.next();
				articles.add(record.get("name").asString());
			}
		}
		return articles;
	}

	public ArrayList<Article> getAllArticles(){
		ArrayList<Article> articles = new ArrayList<>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Article) RETURN properties(a) AS props");
			articles = getArticlesFromResult(result);
		}
		return articles;
	}
	
	public ArrayList<Article> getReferences(String article) {
		/*ArrayList<String> articles = new ArrayList<String>();
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)-[:References]->(b) return b.name AS name", p);	
			while(result.hasNext()) {
				Record record = result.next();
				articles.add(record.get("name").asString());
			}
		}
		return articles;*/
		return getReferences(article, 1);
	}

	public ArrayList<Article> getReferences(String article, int steps){
		ArrayList<Article> references;
		try(Session session = driver.session()){
			var p = parameters("article", article);
			String query = "MATCH (:Article {name: $article})";
			for (int i = 0; i < steps; i++) {
				if(i < steps - 1){
					query += "-[:References]->(:Article)";
				} else {
					//Last step in the loop
					query += "-[:References]->(a:Article) RETURN properties(a) AS props";
				}
			}
			Result result = session.run(query, p);
			references = getArticlesFromResult(result);
		}
		return references;
	}

	public ArrayList<String> getSharedReferencers(String article){
		ArrayList<String> articles = new ArrayList<String>();
		try(Session session = driver.session()){
			var p = parameters("name", article);
			Result result = session.run("MATCH (a:Article {name: $name})-[:References]->()<-[:References]-(b:Article) return b.name AS name", p);
			while(result.hasNext()) {
				Record record = result.next();
				articles.add(record.get("name").asString());
			}
		}
		return articles;
	}
	
	public ArrayList<Article> getReferencers(String article) {
		ArrayList<Article> articles;
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)<-[:References]-(b) return b.name AS name", p);
			articles = getArticlesFromResult(result);
		}
		return articles;
	}
	
	public ArrayList<String> getAllAuthorNames(){
		ArrayList<String> authors = new ArrayList<String>();
		try(Session session = driver.session()){
			Result result = session.run("MATCH (a:Author) RETURN a.name AS name");
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
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)<-[:Wrote]-(b) RETURN b.name AS name", parameters("article", article));
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
			articles.add(new Article(record.get("props").asMap()));
		}
		return articles;
	}
	
}

