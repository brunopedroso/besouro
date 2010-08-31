package athos.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.ResourceChangeEventFactory;
import athos.model.Action;
import athos.model.BuildErrorAction;
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
		
		listener.setTestCounter(testCounter);
		
		IResourceChangeEvent event = ResourceChangeEventFactory.createTestEditAction();
		
		listener.resourceChanged(event);
		
		Assert.assertEquals(1, generatedActions.size());
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals(new File("TestFile.java"), action.getFile());
		Assert.assertEquals(true, action.isTestEdit());
		
	}
	
	// TODO  [test] more unit tests for ResourceListener?
	
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
		Assert.assertTrue(generatedActions.get(0) instanceof BuildErrorAction);
		BuildErrorAction action = (BuildErrorAction) generatedActions.get(0);
		
		Assert.assertEquals(filename, action.getFile().getName());
		Assert.assertEquals(errorMessage, action.getErrorMessage());
	}

	
}
