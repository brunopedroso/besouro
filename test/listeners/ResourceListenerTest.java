package listeners;

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
import athos.model.Action;
import athos.model.EditAction;
import athos.stream.ActionOutputStream;

public class ResourceListenerTest {

	@Test
	public void shouldRegisterAnEditAction() throws Throwable {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		ResourceChangeListener listener = new ResourceChangeListener(stream);
		
		JavaStatementMeter testCounter = mock(JavaStatementMeter.class);
		listener.setTestCounter(testCounter);
		
		IResourceChangeEvent event = mock(IResourceChangeEvent.class);
		when(event.getType()).thenReturn(IResourceChangeEvent.POST_CHANGE);
		IResourceDelta createMockResourceDelta = createMockResourceDelta();
		when(event.getDelta()).thenReturn(createMockResourceDelta);
		
		listener.resourceChanged(event);
		
		Assert.assertEquals(1, generatedActions.size());
		EditAction action = (EditAction) generatedActions.get(0);
		Assert.assertEquals(new File("TestFile.java"), action.getFile());
		
	}

	private IResourceDelta createMockResourceDelta() throws CoreException {
		
		final IResourceDelta delta = mock(IResourceDelta.class);
		when(delta.getKind()).thenReturn(IResourceDelta.CHANGED);
		when(delta.getFlags()).thenReturn(IResourceDelta.CONTENT);
		IFile createMockResource = createMockResource("TestFile.java");
		when(delta.getResource()).thenReturn(createMockResource);
		
		doAnswer(new Answer() {

			public Object answer(InvocationOnMock invocation) throws Throwable {
				IResourceDeltaVisitor visitor = (IResourceDeltaVisitor) invocation.getArguments()[0];
				visitor.visit(delta);
				return null;
			}
		
		}).when(delta).accept(any(IResourceDeltaVisitor.class));
		
		return delta;
		
	}

	//TODO [0] move to a factory
	public static IFile createMockResource(String filename) {
		
		String[] split = filename.split("\\.");
		
		IFile resource = mock(IFile.class);
		
		IPath path = mock(IPath.class);
		when(path.toString()).thenReturn(filename);
		when(path.toFile()).thenReturn(new File(filename));
		
		if (split.length>1)
			when(path.getFileExtension()).thenReturn(split[1]);

		when(resource.getLocation()).thenReturn(path);

		// separates "filename" of ".java"
		when(resource.getName()).thenReturn(split[0]);
		
		if (split.length>1)
			when(resource.getFileExtension()).thenReturn(split[1]);
		
		return resource;
	}
	
}
