package besouro.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.junit.Assert;
import org.junit.Test;

import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.model.Action;
import besouro.model.RefactoringAction;


public class JavaStructureListenerTest {

	@Test
	public void shouldGenerateAnAddEvent() {
		
		ElementChangedEvent event = JavaStructureChangeEventFactory.createAddMethodAction("AnyClass.java", "AnyClass", "aMethod");
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof RefactoringAction);
		Assert.assertEquals("ADD", ((RefactoringAction)action).getOperator());
		Assert.assertEquals("METHOD", ((RefactoringAction)action).getSubjectType());
		Assert.assertEquals("AnyClass/aMethod", ((RefactoringAction)action).getSubjectName());
		
	}

	@Test
	public void shouldGenerateARemoveEvent() {
		
		IJavaElement parentElement = JavaStructureChangeEventFactory.createJavaElement(null,"AnyClass.java","AnyClass", IJavaElement.CLASS_FILE);
		IJavaElement fieldElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,"AnyClass.java","AnyClass#aMethod", IJavaElement.FIELD);

		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(fieldElement,IJavaElementDelta.REMOVED, -1);
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED, -1);
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(parentDelta);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof RefactoringAction);
		Assert.assertEquals("REMOVE", ((RefactoringAction)action).getOperator());
		Assert.assertEquals("AnyClass/aMethod", ((RefactoringAction)action).getSubjectName());
		Assert.assertEquals("FIELD", ((RefactoringAction)action).getSubjectType());
		
	}

	@Test
	public void shouldGenerateARenameEvent() {
		
		ElementChangedEvent event = JavaStructureChangeEventFactory.createRenameMethodEvent("AnyClass.java", "AnyClass", "aMethod", "anotherMethod");
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof RefactoringAction);
		Assert.assertEquals("RENAME", ((RefactoringAction)action).getOperator());
		Assert.assertEquals("AnyClass/aMethod=>AnyClass/anotherMethod", ((RefactoringAction)action).getSubjectName());
		Assert.assertEquals("FIELD", ((RefactoringAction)action).getSubjectType());
		
	}

	@Test
	public void shouldGenerateAMoveEvent() {
		
		IJavaElement parentElementFrom = JavaStructureChangeEventFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass", IJavaElement.CLASS_FILE);
		IJavaElement parentElementTo = JavaStructureChangeEventFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass",IJavaElement.CLASS_FILE);
		IJavaElement fromElement = JavaStructureChangeEventFactory.createJavaElement(parentElementFrom,"AnyClass.java", "aMethod",IJavaElement.FIELD);
		IJavaElement toElement = JavaStructureChangeEventFactory.createJavaElement(parentElementTo,"AnyOtherClass.java", "aMethod",IJavaElement.FIELD);
		
		IJavaElementDelta childDelta1 = JavaStructureChangeEventFactory.createJavaChangeDelta(fromElement, IJavaElementDelta.REMOVED, -1);
		IJavaElementDelta childDelta2 = JavaStructureChangeEventFactory.createJavaChangeDelta(toElement, IJavaElementDelta.ADDED, -1);
		
		IJavaElementDelta classDelta  = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED, -1);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED, -1);
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});

		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(parentDelta);
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof RefactoringAction);
		RefactoringAction refactorAction = (RefactoringAction)action;
		Assert.assertEquals("MOVE", refactorAction.getOperator());
		Assert.assertEquals("FIELD", ((RefactoringAction)action).getSubjectType());
		
	}

	
	
	
}
