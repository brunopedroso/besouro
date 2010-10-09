package besouro.listeners;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.junit.Before;
import org.junit.Test;

import besouro.listeners.ResourceChangeListener;
import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.measure.JavaStatementMeter;
import besouro.model.Action;
import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.stream.ActionOutputStream;



public class ResourceListenerTest {

	private ArrayList<Action> generatedActions;
	private ActionOutputStream stream;
	private ResourceChangeListener listener;
	
	private JavaStatementMeter javaMetrics;
	private JavaStatementMeter measurer;

	@Before
	public void setup() {
		generatedActions = new ArrayList<Action>();
		stream = new FakeActionStream(generatedActions);
		listener = new ResourceChangeListener(stream);
		
		// its strange yet, i know
		javaMetrics = mock(JavaStatementMeter.class);
		measurer = mock(JavaStatementMeter.class);
		when(measurer.measureJavaFile(any(IFile.class))).thenReturn(javaMetrics);
		
	}

	
	@Test
	public void shouldRegisterABuildErrorActon() throws Exception {
		
		String filename = "afile.java";
		String errorMessage = "any build error message";
		listener.resourceChanged(ResourceChangeEventFactory.createBuildErrorEvent(filename, errorMessage));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof CompilationAction);
		CompilationAction action = (CompilationAction) generatedActions.get(0);
		
		Assert.assertEquals(filename, action.getResource());
		Assert.assertEquals(errorMessage, action.getErrorMessage());
	}

	// TODO [test] more unit tests for ResourceListener?

	
}
