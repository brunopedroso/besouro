package besouro.classification.besouro;

import static org.mockito.Mockito.when;

import java.util.Iterator;

import jess.QueryResult;
import jess.ValueVector;
import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Before;
import org.junit.Test;

import besouro.classification.zorro.TestEpisodesFactory;
import besouro.classification.zorro.ZorroEpisodeClassifierTest;
import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.model.UnitTestCaseAction;

/**
 * Applies the same tests from zorro, but in the BesouroEngine
 * 
 * @author Bruno Pedroso
 */
public class BesouroEpisodeClassifierTest extends ZorroEpisodeClassifierTest {

	@Before
	public void setUp() throws Exception {

		super.setUp();

		this.zorro = new BesouroEpisodeClassification();
		this.engine = zorro.getEngine();

		engine.reset();
	}

	@Test
	public void productionCategory2WithTestBreak() throws Exception {

		// method increase but byte size decrease

		// Work on production code
		EditAction editAction = new EditAction(clock, TestEpisodesFactory.productionFile);
		editAction.setIsTestEdit(false);
		editAction.setFileSizeIncrease(-2);
		editAction.setMethodIncrease(2);
		
		//TODO [rule]   redundancy prod2/refact2A
		editAction.setStatementIncrease(1);
		
		zorro.assertJessFact(1, editAction);
		
		// Unit test failue
		UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, TestEpisodesFactory.testFile);
		unitTestAction.setFailureMessage("Failed to import");
		zorro.assertJessFact(2, unitTestAction);

		// Edit on production code (corrects the error)
		editAction = new EditAction(clock, TestEpisodesFactory.productionFile);
		editAction.setIsTestEdit(false);
		editAction.setFileSizeIncrease(-2);
		editAction.setMethodIncrease(2);
		zorro.assertJessFact(3, editAction);

		// Unit test pass
		unitTestAction = new UnitTestCaseAction(clock, TestEpisodesFactory.testFile);
		zorro.assertJessFact(4, unitTestAction);

		engine.run();
		
		QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());

		//   redundancy! Its loosing the prod2 classification
		// need to apply integration tests to both classifications

		
		Assert.assertTrue("Type 1 TDD episode can be classified", result.next());
		Assert.assertEquals("should be production", "production", result.getString("cat"));
		Assert.assertEquals("should be 2", "2", result.getString("tp"));
		
		while(result.next()) {
			System.out.println("loosing " + result.getString("cat") + " " + result.getString("tp"));
		}
	}

	  @Test 
	  public void testTDDEpisodeWithLittleChangeInTest() throws Exception {
	    TestEpisodesFactory.addTDDType1Facts(zorro, clock);
	    engine.run();
	    
	    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
	    
	    Assert.assertTrue("Type 1 TDD episode can be classified", result.next());
	    Assert.assertEquals("Test TDD type 1 episode category name", "test-first", result.getString("cat"));
	    Assert.assertEquals("Test TDD type 1 episode cateory type", "1", result.getString("tp"));
	    
	  }
	  
		@Test 
		public void productionCategory2_2() throws Exception {
			
			// method increase but byte statement decrease
			
			// Edit on production code
			EditAction editAction = new EditAction(clock, TestEpisodesFactory.productionFile);
			editAction.setIsTestEdit(false);
			editAction.setFileSizeIncrease(-5);
			editAction.setMethodIncrease(2);
			zorro.assertJessFact(1, editAction);
						
			
			//TODO [rule]   redundancy production x refactoring
			// if we remove this senseless fail, the test breakes.
			// redundancy is on statementChange == 0
			
			// Unit test fail
			UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, "TestFile.java");
			unitTestAction.setSuccessValue(false);
		    zorro.assertJessFact(4, unitTestAction);

			
			// Unit test pass
			unitTestAction = new UnitTestCaseAction(clock, "TestFile.java");
			unitTestAction.setSuccessValue(true);
		    zorro.assertJessFact(4, unitTestAction);
			
		    
		    engine.run();
		    
		    QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
		    
		    Iterator it = engine.listFacts();
		    while(it.hasNext()){
		    	System.out.println(it.next());
		    }
		    
		    Assert.assertTrue("is classified", result.next());
		    String cat = result.getString("cat");
		    String tp = result.getString("tp");
		    
		    boolean redundant = false;
		    // only to show what is beeing reduntantly classified
		    while(result.next()) {
		    	System.out.println("losing " + result.getString("cat") + " " + result.getString("tp"));
		    	redundant = true;
		    }
		    
		    Assert.assertEquals("category", "production", cat);
		    Assert.assertEquals("subtype", "2", tp);
		    
			
//			Assert.assertFalse("should not classify more than one", redundant);
		    
		}

	  
}
