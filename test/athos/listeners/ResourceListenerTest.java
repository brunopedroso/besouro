package athos.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import athos.listeners.JavaStatementMeter;
import athos.listeners.ResourceChangeListener;
import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.ResourceFactory;
import athos.model.Action;
import athos.model.EditAction;
import athos.stream.ActionOutputStream;

//TODO [0] unit test buid error sensor

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
		
		IResourceChangeEvent event = ResourceFactory.createTestEditAction();
		
		listener.resourceChanged(event);
		
		Assert.assertEquals(1, generatedActions.size());
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals(new File("TestFile.java"), action.getFile());
		Assert.assertEquals(true, action.isTestEdit());
		
	}


	
}
