package listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.junit.Test;

import athos.listeners.JavaStatementMeter;
import athos.listeners.JavaStructureChangeListener;
import athos.model.Action;
import athos.stream.ActionOutputStream;

public class JavaStructureListenerTest {

	@Test
	public void shouldGenerateAnAddEvent() {
		
		//mock things
		
		//TODO [0] anything to extract?
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		IJavaElementDelta delta = mock(IJavaElementDelta.class);
		when(event.getDelta()).thenReturn(delta);
		
		IJavaElementDelta childDelta = mock(IJavaElementDelta.class);
		when(childDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{});
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		IJavaElement element = mock(IJavaElement.class);
		IFile resource = ResourceListenerTest.createMockResource("AnyClass.java");
		when(element.getResource()).thenReturn(resource);
		
		when(childDelta.getElement()).thenReturn(element);
		when(childDelta.getKind()).thenReturn(IJavaElementDelta.ADDED);

		when(element.getElementType()).thenReturn(IJavaElement.FIELD);
		
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		JavaStructureChangeListener listener = new JavaStructureChangeListener(stream);
		
		JavaStatementMeter testCounter = mock(JavaStatementMeter.class);
		listener.setTestCounter(testCounter);
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [0] assert more things
		
	}
	
	
}
