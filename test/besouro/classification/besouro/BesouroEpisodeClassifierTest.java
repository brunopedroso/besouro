package besouro.classification.besouro;

import static org.mockito.Mockito.when;
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
		
		//TODO [rule]  redundancy prod2/refact2A
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

		// TODO [rule] redundancy! Its loosing the prod2 classification
		// need to apply integration tests to both classifications

		
		Assert.assertTrue("Type 1 TDD episode can be classified", result.next());
		Assert.assertEquals("should be production", "production", result.getString("cat"));
		Assert.assertEquals("should be 2", "2", result.getString("tp"));
		
		while(result.next()) {
			System.out.println("loosing " + result.getString("cat") + " " + result.getString("tp"));
		}
	}

}
