package besouro.model;
import java.io.File;
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
import static org.mockito.Mockito.*;

import besouro.model.refactor.UnaryRefactorAction;


public class TemplatesTest {

	private Date clock;
	private Rete engine;

	private IResource productionFile;
	private IResource testFile;

	@Before
	public void setup() throws JessException {
		clock = new Date();
		testFile = mock(IResource.class);
		when(testFile.getName()).thenReturn("testFile");
		productionFile = mock(IResource.class);
		when(productionFile.getName()).thenReturn("productionFile");
		
		engine = new Rete();
		engine.reset();

		engine.batch("besouro/model/Episode.clp");
		engine.batch("besouro/model/Actions.clp");
	}

	@Test
	public void episodeTemplateTest() throws Exception {

		Fact f = new Fact("episode", engine);
		f.setSlotValue("category", new Value("regression", RU.STRING));
		f.setSlotValue("type", new Value("1", RU.STRING));

		engine.assertFact(f);
		engine.run();

		QueryResult result = engine.runQueryStar(
				"episode-classification-query", new ValueVector());
		Assert.assertTrue("Type 1 TDD episode can be classified", result.next());

		Assert.assertEquals("Test TDD type 1 episode category name",
				"regression", result.getString("cat"));
		Assert.assertEquals("Test TDD type 1 episode cateory type", "1",
				result.getString("tp"));

	}

	/**
	 * Test whether we can assert actions into JESS working memory.
	 */
	@Test
	public void actionTemplateTest() throws Exception {
		
		// Add test method
		UnaryRefactorAction unaryAction = new UnaryRefactorAction(this.clock, this.testFile);
		unaryAction.setOperator("ADD");
		unaryAction.setSubjectType("METHOD");
		unaryAction.setSubjectName("void testEquilateral()");
		unaryAction.assertJessFact(1, engine);
		
		// Edit on test
		EditAction editAction = new EditAction(this.clock, this.testFile);
		
		
//		ignoring duration
//		editAction.setDuration(123);
		
		editAction.setIsTestEdit(true);
		editAction.assertJessFact(2, engine);

		// Compile error on test
		CompilationAction compilationAction = new CompilationAction(this.clock, this.testFile);
		compilationAction.setErrorMessage("Unknown data type");
		compilationAction.assertJessFact(3, engine);

		// Work on production code
		editAction = new EditAction(this.clock, this.productionFile);
		
//		ignoring duration
//		editAction.setDuration(200);
		
		editAction.setIsTestEdit(false);
		editAction.assertJessFact(4, engine);

		// Unit test failue
		UnitTestCaseAction unitTestAction = new UnitTestCaseAction(this.clock, this.testFile);
		unitTestAction.setFailureMessage("Failed to import");
		unitTestAction.assertJessFact(5, engine);

		// Edit on prodction code
		editAction = new EditAction(this.clock, this.productionFile);
		
//		ignoring duration
//		editAction.setDuration(199);
		
		editAction.setIsTestEdit(false);
		editAction.assertJessFact(6, engine);

		// Unit test pass
		unitTestAction = new UnitTestCaseAction(this.clock, this.testFile);
		unitTestAction.assertJessFact(7, engine);
	}

}
