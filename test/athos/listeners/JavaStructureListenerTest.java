package athos.listeners;

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
		
		IJavaElement classElement = createJavaElement(null,"AnyClass.java",IJavaElement.CLASS_FILE);
		IJavaElement addedElement = createJavaElement(null,"AnyClass.java",IJavaElement.FIELD);
		
		IJavaElementDelta childDelta = createJavaChangeDelta(addedElement,IJavaElementDelta.ADDED);
		
		IJavaElementDelta delta = createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED);
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
		
		IJavaElement parentElement = createJavaElement(null,"AnyClass.java",IJavaElement.CLASS_FILE);
		IJavaElement fieldElement = createJavaElement(parentElement,"AnyClass.java",IJavaElement.FIELD);

		IJavaElementDelta childDelta = createJavaChangeDelta(fieldElement,IJavaElementDelta.REMOVED);
		IJavaElementDelta parentDelta = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
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
		Assert.assertEquals("Remove", ((EditAction)action).getOperation());
		
	}

	@Test
	public void shouldGenerateARenameEvent() {
		
		IJavaElement parentElement = createJavaElement(null,"AnyClass.java", IJavaElement.CLASS_FILE);
		IJavaElement renamedElement = createJavaElement(parentElement,"AnyClass.java", IJavaElement.FIELD);
		
		IJavaElementDelta removedDelta = createJavaChangeDelta(renamedElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta addedDelta = createJavaChangeDelta(renamedElement, IJavaElementDelta.ADDED);

		IJavaElementDelta classDelta = createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{removedDelta, addedDelta});
		
		IJavaElementDelta parentDelta = createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});

		ElementChangedEvent event = mock(ElementChangedEvent.class);
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
	
	@Test
	public void shouldGenerateAMoveEvent() {
		
		IJavaElement parentElement = createJavaElement(null,"AnyOtherClass.java", IJavaElement.CLASS_FILE);
		IJavaElement fromElement = createJavaElement(parentElement,"AnyClass.java", IJavaElement.FIELD);
		IJavaElement toElement = createJavaElement(parentElement,"AnyOtherClass.java", IJavaElement.FIELD);
		
		IJavaElementDelta childDelta1 = createJavaChangeDelta(fromElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta childDelta2 = createJavaChangeDelta(toElement, IJavaElementDelta.ADDED);
		
		IJavaElementDelta classDelta  = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		IJavaElementDelta parentDelta = createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});

		ElementChangedEvent event = mock(ElementChangedEvent.class);
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

	private IJavaElement createJavaElement(IJavaElement parentElement, String resourceName, int type) {
		IJavaElement fromElement = mock(IJavaElement.class);
		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn(resourceName);
		when(fromElement.getElementType()).thenReturn(type);
		IFile resource = ResourceListenerTest.createMockResource(resourceName);
		when(fromElement.getResource()).thenReturn(resource);
		return fromElement;
	}
	


	private IJavaElementDelta createJavaChangeDelta(IJavaElement element, int op_type) {
		IJavaElementDelta delta = mock(IJavaElementDelta.class);
		when(delta.getElement()).thenReturn(element);
		when(delta.getKind()).thenReturn(op_type);
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{});
		return delta;
	}
	
	
}
