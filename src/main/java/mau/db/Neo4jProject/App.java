package mau.db.Neo4jProject;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Neo4jDatabase db = new Neo4jDatabase("bolt://localhost:7687", "neo4j", "asd123");
    	//db.addReference("Hur smurfarna tog Normandy", "Var kommer smurfarna ifrån");
    	//ArrayList<String> articles = db.getReferences("Var kommer smurfarna ifrån");
    	db.addReference("Var kommer smurfarna ifrån", "Vem är kleckson?");
    	ArrayList<String> articles = db.getReferencers("Vem är kleckson?");
    	for(String s : articles) {
    		System.out.println(s);
    	}
    	//db.printAllArticles();
    }
}
