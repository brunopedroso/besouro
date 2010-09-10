package besouro.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.junit.Test;

import besouro.listeners.JavaStructureChangeListener;
import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.model.Action;
import besouro.model.EditAction;
import besouro.model.refactor.RefactorOperator;
import besouro.model.refactor.RefactorSubjectType;
import besouro.model.refactor.UnaryRefactorAction;


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
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		Assert.assertEquals(RefactorOperator.ADD, ((UnaryRefactorAction)action).getOperator());
		Assert.assertEquals(RefactorSubjectType.METHOD, ((UnaryRefactorAction)action).getSubjectType());
		Assert.assertEquals("AnyClass/aMethod", ((UnaryRefactorAction)action).getSubjectName());
		
	}

	@Test
	public void shouldGenerateARemoveEvent() {
		
		IJavaElement parentElement = JavaStructureChangeEventFactory.createJavaElement(null,"AnyClass.java","AnyClass", IJavaElement.CLASS_FILE);
		IJavaElement fieldElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,"AnyClass.java","AnyClass#aMethod", IJavaElement.FIELD);

		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(fieldElement,IJavaElementDelta.REMOVED);
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
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
		
		ElementChangedEvent event = JavaStructureChangeEventFactory.createRenameMethodEvent("AnyClass.java", "AnyClass", "aMethod", "anotherMethod");
		
		// create listener
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		JavaStructureChangeListener listener = new JavaStructureChangeListener(new FakeActionStream(generatedActions));
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [data] do we need to diferentiate unaries from binaries refactorings?
		
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof UnaryRefactorAction);
		Assert.assertEquals(RefactorOperator.RENAME, ((UnaryRefactorAction)action).getOperator());
		Assert.assertEquals("AnyClass/aMethod => AnyClass/anotherMethod", ((UnaryRefactorAction)action).getSubjectName());
		Assert.assertEquals(RefactorSubjectType.FIELD, ((UnaryRefactorAction)action).getSubjectType());
		
	}

	@Test
	public void shouldGenerateAMoveEvent() {
		
		IJavaElement parentElementFrom = JavaStructureChangeEventFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass", IJavaElement.CLASS_FILE);
		IJavaElement parentElementTo = JavaStructureChangeEventFactory.createJavaElement(null,"AnyOtherClass.java", "AnyOtherClass",IJavaElement.CLASS_FILE);
		IJavaElement fromElement = JavaStructureChangeEventFactory.createJavaElement(parentElementFrom,"AnyClass.java", "aMethod",IJavaElement.FIELD);
		IJavaElement toElement = JavaStructureChangeEventFactory.createJavaElement(parentElementTo,"AnyOtherClass.java", "aMethod",IJavaElement.FIELD);
		
		IJavaElementDelta childDelta1 = JavaStructureChangeEventFactory.createJavaChangeDelta(fromElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta childDelta2 = JavaStructureChangeEventFactory.createJavaChangeDelta(toElement, IJavaElementDelta.ADDED);
		
		IJavaElementDelta classDelta  = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta1, childDelta2});
		
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElementFrom, IJavaElementDelta.CHANGED);
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
