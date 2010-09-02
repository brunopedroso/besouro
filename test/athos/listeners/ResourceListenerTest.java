package athos.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.ResourceChangeEventFactory;
import athos.model.Action;
import athos.model.CompilationAction;
import athos.model.EditAction;
import athos.stream.ActionOutputStream;


public class ResourceListenerTest {

	@Test
	public void shouldRegisterAnEditAction() throws Throwable {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		ResourceChangeListener listener = new ResourceChangeListener(stream);
		
		JavaStatementMeter testCounter = mock(JavaStatementMeter.class);
		// its how test edits are identified 
		when(testCounter.hasTest()).thenReturn(Boolean.TRUE);
		when(testCounter.getNumOfMethods()       ).thenReturn(1);
		when(testCounter.getNumOfStatements()    ).thenReturn(2);
		when(testCounter.getNumOfTestAssertions()).thenReturn(3);
		when(testCounter.getNumOfTestMethods()   ).thenReturn(4);
		
		listener.setTestCounter(testCounter);
		
		IResourceChangeEvent event = ResourceChangeEventFactory.createTestEditAction(33);
		
		listener.resourceChanged(event);
		
		Assert.assertEquals(1, generatedActions.size());
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals("TestFile.java", action.getFile().getName());
		Assert.assertEquals(true, action.isTestEdit());
		
		Assert.assertEquals(1, action.getMethodsCount());
		Assert.assertEquals(2, action.getStatementsCount());
		Assert.assertEquals(3, action.getTestAssertionsCount());
		Assert.assertEquals(4, action.getTestMethodsCount());
		
		Assert.assertEquals(33, action.getFileSize());
		
	}
	
	// TODO [test] more unit tests for ResourceListener?
	// what about test x prod changes?
	
	@Test
	public void shouldRegisterABuildErrorActon() throws Exception {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		ResourceChangeListener listener = new ResourceChangeListener(stream);
		
		JavaStatementMeter testMeter = mock(JavaStatementMeter.class);
		listener.setTestCounter(testMeter);
		
		String filename = "afile.java";
		String errorMessage = "any build error message";
		listener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent(filename, errorMessage));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof CompilationAction);
		CompilationAction action = (CompilationAction) generatedActions.get(0);
		
		Assert.assertEquals(filename, action.getFile().getName());
		Assert.assertEquals(errorMessage, action.getErrorMessage());
	}

	
}
