package athos.listeners.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResourceFactory {

	public static IResourceChangeEvent createTestEditAction() throws CoreException {
		IResourceChangeEvent event = mock(IResourceChangeEvent.class);
		when(event.getType()).thenReturn(IResourceChangeEvent.POST_CHANGE);
		IResourceDelta createMockResourceDelta = createChangeTestDelta();
		when(event.getDelta()).thenReturn(createMockResourceDelta);
		return event;
	}

	public static IResourceDelta createChangeTestDelta() throws CoreException {
		
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
