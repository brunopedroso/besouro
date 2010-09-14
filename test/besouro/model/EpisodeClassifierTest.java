package besouro.model;


import java.text.SimpleDateFormat;
import java.util.Date;

import jess.Batch;
import jess.QueryResult;
import jess.Rete;
import jess.ValueVector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests test-pass episode classification.
 * 
 * @author Hongbing Kou
 * @version $Id$
 */
public class EpisodeClassifierTest {

  private Rete engine;
  private Date clock;
  
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
  
  @Before
  public void setUp() throws Exception {
    this.engine = new Rete();

    Batch.batch("besouro/model/Episode.clp", this.engine);
    Batch.batch("besouro/model/Actions.clp", this.engine);
    Batch.batch("besouro/model/EpisodeClassifier.clp", this.engine);
    
    this.clock = dateFormat.parse("01/01/2005 08:30:45");

    engine.reset();
  }
  
  @Test 
  public void testPassEpisodeClassifierTest() throws Exception {
	  Batch.batch("besouro/model/EpisodeClassifierTest.clp", this.engine);
	  this.engine.run();
  }

  @Test 
  public void testTDDEpisodeCategory1() throws Exception {
    TestEpisodesFactory.addTDDType1Facts(engine, clock);
    engine.run();
    
    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
    
    Assert.assertTrue("Type 1 TDD episode can be classified", result.next());
    Assert.assertEquals("Test TDD type 1 episode category name", "test-first", result.getString("cat"));
    Assert.assertEquals("Test TDD type 1 episode cateory type", "1", result.getString("tp"));
    
  }

  @Test 
  public void testTDDEpisodeCategory2() throws Exception {
  	TestEpisodesFactory.addTDDType2Facts(engine, clock);
  	engine.run();
      
    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
    
    Assert.assertTrue("Type 2 TDD episode can be classified", result.next());
    Assert.assertEquals("Test TDD type 1 episode category name", "test-first", result.getString("cat"));
    Assert.assertEquals("Test TDD type 1 episode cateory type", "2", result.getString("tp")); 
  }
  
  @Test 
  public void testTDDEpisodeCategory3() throws Exception {
  	TestEpisodesFactory.addTDDType3Facts(engine, clock);
    engine.run();
      
    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
    
    Assert.assertTrue("Type 1 TDD episode can be classified", result.next());
    Assert.assertEquals("Test TDD type 1 episode category name", "test-first", result.getString("cat"));
    Assert.assertEquals("Test TDD type 1 episode cateory type", "3", result.getString("tp"));  
  }

  @Test 
  public void testTestCodeRefact() throws Exception {
  	TestEpisodesFactory.addTestCodeRefactoFacts(engine, clock);
    engine.run();
    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
    
    Assert.assertTrue("Type 1 refactor episode can be classified", result.next());
    Assert.assertEquals("Test refactoring type 2 episode category name", "refactoring", result.getString("cat"));
    Assert.assertEquals("Test refactoring type 2 episode categoty type", "1A", result.getString("tp"));  
    
//	 Iterator it = engine.listFacts();
//	 while (it.hasNext()) {
//	 System.out.println(it.next());
//	 }

  }

  @Test 
  public void testProductionCodeRefact() throws Exception {
  	TestEpisodesFactory.addProductionCodeRefactoFacts(engine, clock);
    engine.run();
    
    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());

    Assert.assertTrue("Type 1 refactor episode can be classified", result.next());
    Assert.assertEquals("Test refactoring type 2 episode category name", "refactoring", result.getString("cat"));
    Assert.assertEquals("Test refactoring type 2 episode categoty type", "2A", result.getString("tp"));  
  }
}
