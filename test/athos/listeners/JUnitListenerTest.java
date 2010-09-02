package athos.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.JUnitEventFactory;
import athos.model.Action;
import athos.model.UnitTestAction;
import athos.stream.ActionOutputStream;

public class JUnitListenerTest {

	@Test
	public void shouleGenerateAPassingUnitTestEvent() {
		
		//mock things
		ITestRunSession session = JUnitEventFactory.createJunitSession("packageName", "MyTest.java", Result.OK);
				
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);

		// invoke the listener
		JUnitListener listener = new JUnitListener(stream);
		listener.sessionFinished(session);
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(true, action.isSuccessful());
		Assert.assertEquals(null, action.getFailureMessage());
		
	}

	@Test
	public void shouleGenerateAFailingUnitTestEvent() {
		
		//mock things
		ITestRunSession session = JUnitEventFactory.createJunitSession("packageName", "MyTest.java", Result.ERROR);
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		// invoke the listener
		JUnitListener listener = new JUnitListener(stream);
		listener.sessionFinished(session);
		
		// asserts.
		Assert.assertEquals(1, generatedActions.size());
		UnitTestAction action = (UnitTestAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isSuccessful());
		Assert.assertEquals("MyTest.java", action.getFile().getName());
		
		//TODO [data] do we need junit failure messages?
//		Assert.assertNotNull(action.getFailureMessage());
		
	}

	@Test
	public void shouldGetTheFileNameFromTestCaseClasses() {
		
		//TODO   test the general, recursive case
		
	}
	
}
