package mau.db.Neo4jProject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void containsTopics(){
        String[] topics = {"a", "b", "c"};
        Article article = new Article("", 0, topics);
        assertTrue(article.containsTopics("a, b, c"));
        assertTrue(article.containsTopics("b"));
        assertFalse(article.containsTopics("d, e, f"));

    }
}
