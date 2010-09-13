package besouro.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Before;
import org.junit.Test;

import besouro.listeners.JavaStatementMeter;
import besouro.listeners.ResourceChangeListener;
import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.model.Action;
import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.stream.ActionOutputStream;



public class ResourceListenerTest {

	private ArrayList<Action> generatedActions;
	private ActionOutputStream stream;
	private ResourceChangeListener listener;
	private JavaStatementMeter testCounter;

	@Before
	public void setup() {
		generatedActions = new ArrayList<Action>();
		stream = new FakeActionStream(generatedActions);
		listener = new ResourceChangeListener(stream);
		testCounter = mock(JavaStatementMeter.class);
		listener.setTestCounter(testCounter);
	}

	
	@Test
	public void shouldRecognizeTestEditsByNumberOfTestsAndAsserts() throws Exception {
		
		// its how test edits are identified 
		when(testCounter.isTest()).thenReturn(Boolean.TRUE);
		when(testCounter.getNumOfMethods()       ).thenReturn(1);
		when(testCounter.getNumOfStatements()    ).thenReturn(2);
		when(testCounter.getNumOfTestAssertions()).thenReturn(3);
		when(testCounter.getNumOfTestMethods()   ).thenReturn(4);
		
		IResourceChangeEvent event = ResourceChangeEventFactory.createEditAction("AJavaFile.java",33);
		
		listener.resourceChanged(event);
		
		Assert.assertEquals(1, generatedActions.size());
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals("AJavaFile.java", action.getFile().getName());
		Assert.assertEquals(true, action.isTestEdit());
		
		Assert.assertEquals(1, action.getMethodsCount());
		Assert.assertEquals(2, action.getStatementsCount());
		Assert.assertEquals(3, action.getTestAssertionsCount());
		Assert.assertEquals(4, action.getTestMethodsCount());
		
		Assert.assertEquals(33, action.getFileSize());
		
	}
	
	@Test
	public void shouldRecognizeProductionEdits() throws Exception {
		
		// it depends on the implementation of JavaStatementMeter (is not being testet yet)
		when(testCounter.isTest()).thenReturn(Boolean.FALSE);
		IResourceChangeEvent event = ResourceChangeEventFactory.createEditAction("ATestJavaFile.java", 33);
		
		listener.resourceChanged(event);
		
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals(false, action.isTestEdit());

	}

	
	@Test
	public void shouldRecognizeTestEditsByTesInTheNameOfTheClass() throws Exception {
		
		// it depends on the implementation of JavaStatementMeter (is not being testet yet)
		when(testCounter.isTest()).thenReturn(Boolean.TRUE);
		IResourceChangeEvent event = ResourceChangeEventFactory.createEditAction("ATestJavaFile.java", 33);
		
		listener.resourceChanged(event);
		
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals(true, action.isTestEdit());
		
	}
	
	@Test
	public void shouldRegisterABuildErrorActon() throws Exception {
		
		String filename = "afile.java";
		String errorMessage = "any build error message";
		listener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent(filename, errorMessage));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof CompilationAction);
		CompilationAction action = (CompilationAction) generatedActions.get(0);
		
		Assert.assertEquals(filename, action.getFile().getName());
		Assert.assertEquals(errorMessage, action.getErrorMessage());
	}

	// TODO [test] more unit tests for ResourceListener?

	
}
