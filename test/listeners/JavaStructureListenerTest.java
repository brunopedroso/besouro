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
import athos.model.EditAction;

public class JavaStructureListenerTest {

	@Test
	public void shouldGenerateAnAddEvent() {
		
		IJavaElement parentElement = mock(IJavaElement.class);
		IJavaElementDelta delta = createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyClass.java"));
		IJavaElementDelta childDelta = createJavaChangeDelta(parentElement,IJavaElementDelta.ADDED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("AnyClass.java"));
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		listener.setTestCounter(mock(JavaStatementMeter.class));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [1] should be a UnaryAction
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof EditAction);
		Assert.assertEquals("Add", ((EditAction)action).getOperation());
		
		
	}

	@Test
	public void shouldGenerateARemoveEvent() {
		
		IJavaElement parentElement = mock(IJavaElement.class);
		IJavaElementDelta delta = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyClass.java"));
		IJavaElementDelta childDelta = createJavaChangeDelta(parentElement,IJavaElementDelta.REMOVED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("AnyClass.java"));
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		listener.setTestCounter(mock(JavaStatementMeter.class));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [1] should be a UnaryAction
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof EditAction);
		Assert.assertEquals("Remove", ((EditAction)action).getOperation());
		
	}

	@Test
	public void shouldGenerateARenameEvent() {
		
		IJavaElement parentElement = mock(IJavaElement.class);
		IJavaElement renamedElement = mock(IJavaElement.class);
		when(renamedElement.getParent()).thenReturn(parentElement);
		
		
		IJavaElementDelta delta1 = createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyClass.java"));
		IJavaElementDelta delta2 = createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyClass.java"));
		IJavaElementDelta childDelta1 = createJavaChangeDelta(renamedElement, IJavaElementDelta.REMOVED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("aField"));
		IJavaElementDelta childDelta2 = createJavaChangeDelta(renamedElement, IJavaElementDelta.ADDED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("anotherField"));
		when(delta1.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{delta2});
		when(delta2.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta1);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		listener.setTestCounter(mock(JavaStatementMeter.class));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [1] should be a UnaryAction
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof EditAction);
		Assert.assertEquals("Rename", ((EditAction)action).getOperation());
		
	}
	
	@Test
	public void shouldGenerateAMoveEvent() {
		
		IJavaElement parentElement = mock(IJavaElement.class);
		IJavaElement fromElement = mock(IJavaElement.class);
		IJavaElement toElement = mock(IJavaElement.class);
		
		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn("AnyClass.java");
		when(toElement.getParent()).thenReturn(parentElement);
		when(toElement.toString()).thenReturn("AnyOtherClass.java");
		
		IJavaElementDelta classDelta = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyOtherClass.java"));
		IJavaElementDelta childDelta1 = createJavaChangeDelta(fromElement, IJavaElementDelta.REMOVED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("AnyClass.java"));
		IJavaElementDelta childDelta2 = createJavaChangeDelta(toElement, IJavaElementDelta.ADDED, IJavaElement.FIELD, ResourceListenerTest.createMockResource("AnyOtherClass.java"));
		
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		IJavaElementDelta parentDelta = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED, IJavaElement.CLASS_FILE, ResourceListenerTest.createMockResource("AnyClass.java"));
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});
		when(event.getDelta()).thenReturn(parentDelta);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		listener.setTestCounter(mock(JavaStatementMeter.class));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [1] should be a UnaryAction
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof EditAction);
		Assert.assertEquals("Rename", ((EditAction)action).getOperation());
		
	}
	


	private IJavaElementDelta createJavaChangeDelta(IJavaElement element, int op_type, int element_type, IFile resource) {
		
		IJavaElementDelta delta = mock(IJavaElementDelta.class);
		
		when(element.getResource()).thenReturn(resource);
		when(element.getElementType()).thenReturn(element_type);
		
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{});
		when(delta.getElement()).thenReturn(element);
		when(delta.getKind()).thenReturn(op_type);
		
		return delta;
	}
	
	
}
