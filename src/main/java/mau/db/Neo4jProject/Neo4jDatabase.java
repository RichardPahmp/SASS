package mau.db.Neo4jProject;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
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
			session.writeTransaction(tx -> tx.run("CREATE (a:Article {name: $name, year: $year)", parameters("name", name, "year", year)));
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
	
	public ArrayList<String> printAllArticles() {
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
	
	public ArrayList<String> getReferences(String article) {
		ArrayList<String> articles = new ArrayList<String>();
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)-[:References]->(b) return b.name AS name", p);	
			while(result.hasNext()) {
				Record record = result.next();
				articles.add(record.get("name").asString());
			}
		}
		return articles;
	}
	
	public ArrayList<String> getReferencers(String article) {
		ArrayList<String> articles = new ArrayList<String>();
		try(Session session = driver.session()){
			var p = parameters("article", article);
			Result result = session.run("MATCH (a:Article) WHERE a.name = $article MATCH (a)<-[:References]-(b) return b.name AS name", p);	
			while(result.hasNext()) {
				Record record = result.next();
				articles.add(record.get("name").asString());
			}
		}
		return articles;
	}
	
}

