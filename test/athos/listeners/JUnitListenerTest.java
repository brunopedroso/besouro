package athos.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.junit.Before;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.JUnitEventFactory;
import athos.model.Action;
import athos.model.FileAction;
import athos.model.UnitTestAction;
import athos.stream.ActionOutputStream;

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
	public void shouleGenerateAPassingUnitTestEvent() {
		
		listener.sessionFinished(JUnitEventFactory.createJunitSession("packageName", "MyTest.java", Result.OK));
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(true, action.isSuccessful());
		Assert.assertEquals(null, action.getFailureMessage());
		
	}

	@Test
	public void shouleGenerateAFailingUnitTestEvent() {
		
		// invoke the listener
		listener.sessionFinished(JUnitEventFactory.createJunitSession("packageName", "MyTest.java", Result.ERROR));
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getFile().getName());
		
		//TODO [data] do we need junit failure messages?
//		Assert.assertNotNull(action.getFailureMessage());
		
	}

	@Test
	public void shouldGetTheFileNameFromOnlyTestCaseInTheHierarchy() {
		
		// invoke the listener
		listener.sessionFinished(JUnitEventFactory.createDeepJunitExecutionHierarchy( "MyTest.java", Result.ERROR));
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getFile().getName());		
	}
	
	@Test
	public void shouldGenerateOneActionForTwoTestCasesInTheSameFile() {
		
		// invoke the listener
		listener.sessionFinished(JUnitEventFactory.createTwoTestCases("MyTest.java",true, "MyTest.java",true));
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
//		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getFile().getName());		

	}

	@Test
	public void shouldGenerateTwoActionsForTwoTestCasesInDiferentFiles() {
		
		listener.sessionFinished(JUnitEventFactory.createTwoTestCases("MyTest1.java",true , "MyTest2.java",true));
		

		Assert.assertEquals(2, generatedActions.size());
		
		Set<String> names = new HashSet<String>();
		for(Action action: generatedActions){
			names.add(((FileAction)action).getFile().getName());
		}
		Assert.assertTrue(names.contains("MyTest1.java"));
		Assert.assertTrue(names.contains("MyTest2.java"));
				
	}

	@Test
	public void shouldGenerateTwoActionsThatRespectsResults() {
		
		ITestRunSession session = JUnitEventFactory.createTwoTestCases("MyTest1.java",false, "MyTest2.java",true);

		listener.sessionFinished(session);
		

		Assert.assertEquals(2, generatedActions.size());
		
		//TODO [test] assertions depend on the inverse order...
		
		Assert.assertEquals("MyTest1.java", ((UnitTestAction) generatedActions.get(1)).getFile().getName());
		Assert.assertFalse(((UnitTestAction) generatedActions.get(1)).isSuccessful());
		
		Assert.assertEquals("MyTest2.java", ((UnitTestAction) generatedActions.get(0)).getFile().getName());
		Assert.assertTrue(((UnitTestAction) generatedActions.get(0)).isSuccessful());
		
				
	}
	
	
	//TODO   should generate actions for each test file executed
//			(in case package or folder is execued)
	
}









