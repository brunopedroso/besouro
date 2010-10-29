package besouro.classification.besouro;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;

import org.eclipse.core.resources.IResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.model.UnitTestCaseAction;



public class JessTemplatesTest {

	private Date clock;
	private BesouroEpisodeClassification besouro;
	private Rete engine;

	private String productionFile;
	private String testFile;

	@Before
	public void setup() throws JessException {
		clock = new Date();
		testFile = "testFile";
		productionFile = "productionFile";
		
		besouro = new BesouroEpisodeClassification();
		engine = besouro.getEngine();
	}

	@Test
	public void episodeTemplateTest() throws Exception {

		Fact f = new Fact("episode", engine);
		f.setSlotValue("category", new Value("regression", RU.STRING));
		f.setSlotValue("type", new Value("1", RU.STRING));

		engine.assertFact(f);
		engine.run();

		QueryResult result = engine.runQueryStar("episode-classification-query", new ValueVector());
		Assert.assertTrue("Type 1 TDD episode can be classified", result.next());

		Assert.assertEquals("Test TDD type 1 episode category name","regression", result.getString("cat"));
		Assert.assertEquals("Test TDD type 1 episode cateory type", "1",result.getString("tp"));

	}

	/**
	 * Test whether we can assert actions into JESS working memory.
	 */
	@Test
	public void actionTemplateTest() throws Exception {
		
		// Add test method
		RefactoringAction unaryAction = new RefactoringAction(this.clock, this.testFile);
		unaryAction.setOperator("ADD");
		unaryAction.setSubjectType("METHOD");
		unaryAction.setSubjectName("void testEquilateral()");
//		unaryAction.assertJessFact(1, engine);
		besouro.assertJessFact(1, unaryAction);
		
		// Edit on test
		EditAction editAction = new EditAction(this.clock, this.testFile);
		
		
//		ignoring duration
//		editAction.setDuration(123);
		
		editAction.setIsTestEdit(true);
		besouro.assertJessFact(2, editAction);
//		editAction.assertJessFact(2, engine);

		// Compile error on test
		CompilationAction compilationAction = new CompilationAction(this.clock, this.testFile);
		compilationAction.setErrorMessage("Unknown data type");
		besouro.assertJessFact(3, compilationAction);
//		compilationAction.assertJessFact(3, engine);

		// Work on production code
		editAction = new EditAction(this.clock, this.productionFile);
		
//		ignoring duration
//		editAction.setDuration(200);
		
		editAction.setIsTestEdit(false);
		besouro.assertJessFact(4, editAction);
//		editAction.assertJessFact(4, engine);

		// Unit test failue
		UnitTestCaseAction unitTestAction = new UnitTestCaseAction(this.clock, this.testFile);
		unitTestAction.setFailureMessage("Failed to import");
		besouro.assertJessFact(5, unitTestAction);
//		unitTestAction.assertJessFact(5, engine);

		// Edit on prodction code
		editAction = new EditAction(this.clock, this.productionFile);
		
//		ignoring duration
//		editAction.setDuration(199);
		
		editAction.setIsTestEdit(false);
		besouro.assertJessFact(6, editAction);
//		editAction.assertJessFact(6, engine);

		// Unit test pass
		unitTestAction = new UnitTestCaseAction(this.clock, this.testFile);
		besouro.assertJessFact(7, unitTestAction);
//		unitTestAction.assertJessFact(7, engine);
	}

}
