package besouro.listeners;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.junit.Before;
import org.junit.Test;

import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.JUnitEventFactory;
import besouro.model.Action;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.stream.ActionOutputStream;


public class JUnitListenerTest {

	private ArrayList<Action> generatedActions;
	private ActionOutputStream stream;
	private JUnitListener listener;

	@Before
	public void setup() {
		generatedActions = new ArrayList<Action>();
		stream = new FakeActionStream(generatedActions);
		listener = new JUnitListener(stream);
	}

	@Test
	public void shouleGenerateAPassingUnitTestEvent() throws Exception {
		
		listener.sessionFinished(JUnitEventFactory.createJunitSession("packageName", "MyTest.java", Result.OK));
		
		// asserts.
		Assert.assertEquals(2, generatedActions.size());
		UnitTestCaseAction action = (UnitTestCaseAction) generatedActions.get(0);
		Assert.assertEquals(true, action.isSuccessful());
		Assert.assertEquals(null, action.getFailureMessage());
		
		Assert.assertEquals(true, ((UnitTestSessionAction) generatedActions.get(1)).isSuccessful());
	}

	@Test
	public void shouleGenerateAFailingUnitTestEvent() throws Exception {
		
		// invoke the listener
		listener.sessionFinished(JUnitEventFactory.createJunitSession("packageName", "MyTest", Result.ERROR));
		
		// asserts.
		Assert.assertEquals(2, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getResource().getName());
		
		Assert.assertEquals(false, ((UnitTestSessionAction) generatedActions.get(1)).isSuccessful());
		
		//TODO [data] do we need junit failure messages?
//		Assert.assertNotNull(action.getFailureMessage());
		
	}

	@Test
	public void shouldGetTheFileNameFromOnlyTestCaseInTheHierarchy() throws Exception {
		
		// invoke the listener
		listener.sessionFinished(JUnitEventFactory.createDeepJunitExecutionHierarchy( "MyTest", Result.ERROR));
		
		// asserts.
		Assert.assertEquals(2, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getResource().getName());
		
		Assert.assertEquals(false, ((UnitTestSessionAction) generatedActions.get(1)).isSuccessful());
	}
	

	@Test
	public void shouldGenerateTwoActionsForTwoTestCasesInDiferentFiles() throws Exception {
		
		listener.sessionFinished(JUnitEventFactory.createTwoTestCases("MyTest1",true , "MyTest2",true));
		
		Assert.assertEquals(3, generatedActions.size());
		
		Assert.assertEquals("MyTest1.java", ((UnitTestAction) generatedActions.get(0)).getResource().getName());
		Assert.assertEquals("MyTest2.java", ((UnitTestAction) generatedActions.get(1)).getResource().getName());
		
		Assert.assertEquals(true, ((UnitTestSessionAction) generatedActions.get(2)).isSuccessful());
	}

	@Test
	public void shouldGenerateTwoActionsThatRespectsResults() throws Exception {
		
		ITestRunSession session = JUnitEventFactory.createTwoTestCases("MyTest1",false, "MyTest2",true);

		listener.sessionFinished(session);

		Assert.assertEquals(3, generatedActions.size());
		
		Assert.assertEquals("MyTest1.java", ((UnitTestAction) generatedActions.get(0)).getResource().getName());
		Assert.assertFalse(((UnitTestAction) generatedActions.get(0)).isSuccessful());
		
		Assert.assertEquals("MyTest2.java", ((UnitTestAction) generatedActions.get(1)).getResource().getName());
		Assert.assertTrue(((UnitTestAction) generatedActions.get(1)).isSuccessful());
		
		Assert.assertEquals(false, ((UnitTestSessionAction) generatedActions.get(2)).isSuccessful());
		
	}
	
	
	/**
	 * this case happens when one executes a single test method
	 * @throws Exception 
	 */
	@Test
	public void shouldGenerateTestCaseActionWithTheCorrectFileName() throws Exception {
		
		listener.sessionFinished(JUnitEventFactory.createJunitSessionForSingleMethod("MyTest.myMethod", "MyTest", Result.ERROR));
		
		Assert.assertEquals(2, generatedActions.size());
		
		Assert.assertEquals("MyTest.java", ((UnitTestAction) generatedActions.get(0)).getResource().getName());
	}
	

	
}









