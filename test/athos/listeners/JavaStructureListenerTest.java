package athos.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.junit.Test;

import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.JavaElementsFactory;
import athos.model.Action;
import athos.model.EditAction;
import athos.model.refactor.RefactorOperator;
import athos.model.refactor.RefactorSubjectType;
import athos.model.refactor.UnaryRefactorAction;

public class JavaStructureListenerTest {

	@Test
	public void shouldGenerateAnAddEvent() {
		
		ElementChangedEvent event = JavaElementsFactory.createAddMethodAction();
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		Assert.assertEquals(RefactorOperator.ADD, ((UnaryRefactorAction)action).getOperator());
		Assert.assertEquals(RefactorSubjectType.METHOD, ((UnaryRefactorAction)action).getSubjectType());
		Assert.assertEquals("AnyClass/aMethod", ((UnaryRefactorAction)action).getSubjectName());
		
	}



	@Test
	public void shouldGenerateARemoveEvent() {
		
		IJavaElement parentElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java","AnyClass", IJavaElement.CLASS_FILE);
		IJavaElement fieldElement = JavaElementsFactory.createJavaElement(parentElement,"AnyClass.java","AnyClass#aMethod", IJavaElement.FIELD);

		IJavaElementDelta childDelta = JavaElementsFactory.createJavaChangeDelta(fieldElement,IJavaElementDelta.REMOVED);
		IJavaElementDelta parentDelta = JavaElementsFactory.createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
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
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		Assert.assertEquals(RefactorOperator.REMOVE, ((UnaryRefactorAction)action).getOperator());
		Assert.assertEquals("AnyClass/aMethod", ((UnaryRefactorAction)action).getSubjectName());
		Assert.assertEquals(RefactorSubjectType.FIELD, ((UnaryRefactorAction)action).getSubjectType());
		
	}

	@Test
	public void shouldGenerateARenameEvent() {
		
		IJavaElement parentElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java", "AnyClass", IJavaElement.CLASS_FILE);
		IJavaElement renamedFromElement = JavaElementsFactory.createJavaElement(parentElement,"AnyClass.java", "AnyClass#aMethod", IJavaElement.FIELD);
		IJavaElement renamedToElement = JavaElementsFactory.createJavaElement(parentElement,"AnyClass.java", "AnyClass#anotherMethod", IJavaElement.FIELD);
		
		IJavaElementDelta removedDelta = JavaElementsFactory.createJavaChangeDelta(renamedFromElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta addedDelta = JavaElementsFactory.createJavaChangeDelta(renamedToElement, IJavaElementDelta.ADDED);

		IJavaElementDelta classDelta = JavaElementsFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{removedDelta, addedDelta});
		
		IJavaElementDelta parentDelta = JavaElementsFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
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
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		Assert.assertEquals(RefactorOperator.RENAME, ((UnaryRefactorAction)action).getOperator());
		Assert.assertEquals("AnyClass/aMethod => AnyClass/anotherMethod", ((UnaryRefactorAction)action).getSubjectName());
		Assert.assertEquals(RefactorSubjectType.FIELD, ((UnaryRefactorAction)action).getSubjectType());
		
	}
	
	@Test
	public void shouldGenerateAMoveEvent() {
		
		IJavaElement parentElementFrom = JavaElementsFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass", IJavaElement.CLASS_FILE);
		IJavaElement parentElementTo = JavaElementsFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass",IJavaElement.CLASS_FILE);
		IJavaElement fromElement = JavaElementsFactory.createJavaElement(parentElementFrom,"AnyClass.java", "aMethod",IJavaElement.FIELD);
		IJavaElement toElement = JavaElementsFactory.createJavaElement(parentElementTo,"AnyOtherClass.java", "aMethod",IJavaElement.FIELD);
		
		IJavaElementDelta childDelta1 = JavaElementsFactory.createJavaChangeDelta(fromElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta childDelta2 = JavaElementsFactory.createJavaChangeDelta(toElement, IJavaElementDelta.ADDED);
		
		IJavaElementDelta classDelta  = JavaElementsFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		IJavaElementDelta parentDelta = JavaElementsFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED);
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
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		UnaryRefactorAction refactorAction = (UnaryRefactorAction)action;
		Assert.assertEquals(RefactorOperator.MOVE, refactorAction.getOperator());
		Assert.assertEquals(RefactorSubjectType.FIELD, ((UnaryRefactorAction)action).getSubjectType());
		
	}

	
	
	
}
