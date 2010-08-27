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
import athos.listeners.mock.FakeActionStream;
import athos.listeners.mock.JavaElementsFactory;
import athos.model.Action;
import athos.model.EditAction;

public class JavaStructureListenerTest {

	@Test
	public void shouldGenerateAnAddEvent() {
		
		ElementChangedEvent event = JavaElementsFactory.createAddMethodAction();
		
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
		
		IJavaElement parentElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java",IJavaElement.CLASS_FILE);
		IJavaElement fieldElement = JavaElementsFactory.createJavaElement(parentElement,"AnyClass.java",IJavaElement.FIELD);

		IJavaElementDelta childDelta = JavaElementsFactory.createJavaChangeDelta(fieldElement,IJavaElementDelta.REMOVED);
		IJavaElementDelta parentDelta = JavaElementsFactory.createJavaChangeDelta(parentElement, IJavaElementDelta.CHANGED);
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
		
		IJavaElement parentElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java", IJavaElement.CLASS_FILE);
		IJavaElement renamedElement = JavaElementsFactory.createJavaElement(parentElement,"AnyClass.java", IJavaElement.FIELD);
		
		IJavaElementDelta removedDelta = JavaElementsFactory.createJavaChangeDelta(renamedElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta addedDelta = JavaElementsFactory.createJavaChangeDelta(renamedElement, IJavaElementDelta.ADDED);

		IJavaElementDelta classDelta = JavaElementsFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{removedDelta, addedDelta});
		
		IJavaElementDelta parentDelta = JavaElementsFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
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
		
		IJavaElement parentElementFrom = JavaElementsFactory.createJavaElement(null,"AnyOtherClass.java", IJavaElement.CLASS_FILE);
		IJavaElement parentElementTo = JavaElementsFactory.createJavaElement(null,"AnyOtherClass.java", IJavaElement.CLASS_FILE);
		IJavaElement fromElement = JavaElementsFactory.createJavaElement(parentElementFrom,"AnyClass.java", IJavaElement.FIELD);
		IJavaElement toElement = JavaElementsFactory.createJavaElement(parentElementTo,"AnyOtherClass.java", IJavaElement.FIELD);
		
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
		
		JavaStatementMeter meter = mock(JavaStatementMeter.class);
		when(meter.getNumOfMethods()).thenReturn(11);
		when(meter.getNumOfStatements()).thenReturn(22);
		when(meter.getNumOfTestAssertions()).thenReturn(33);
		when(meter.getNumOfTestMethods()).thenReturn(44);
		listener.setTestCounter(meter);
		
		int fileSize = 55;
		WindowListener.setActiveBufferSize(fileSize);
		
		// invoke listener
		listener.elementChanged(event);
		
		// verify event data
		Assert.assertEquals(1, generatedActions.size());
		
		//TODO [1] should be a UnaryAction
		Action action = generatedActions.get(0);
		Assert.assertTrue(action instanceof EditAction);
		
		
		EditAction editAction = (EditAction)action;
		Assert.assertEquals("Move", editAction.getOperation());

		Assert.assertEquals(meter.getNumOfMethods(), editAction.getCurrentMethods());
		Assert.assertEquals(meter.getNumOfStatements(), editAction.getCurrentStatements());
		Assert.assertEquals(meter.getNumOfTestAssertions(), editAction.getCurrentTestAssertions());
		Assert.assertEquals(meter.getNumOfTestMethods(), editAction.getCurrentTestMethods());
		
		Assert.assertEquals(fileSize, editAction.getFileSize());
		
		// duration is calculated by the EpisodeClassifierStream
		Assert.assertEquals(0, editAction.getDuration());
		
		//TODO [1] increases
		Assert.assertEquals(0, editAction.getFileSizeIncrease());
		Assert.assertEquals(0, editAction.getMethodIncrease());
		Assert.assertEquals(0, editAction.getStatementIncrease());
		Assert.assertEquals(0, editAction.getTestAssertionIncrease());
		Assert.assertEquals(0, editAction.getTestMethodIncrease());

		
	}

	
	
	
}
