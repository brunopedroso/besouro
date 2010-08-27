package athos.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

import athos.listeners.ResourceListenerTest;

public class JavaElementsFactory {

	public static IJavaElement createJavaElement(IJavaElement parentElement, String resourceName, int type) {
		IJavaElement fromElement = mock(IJavaElement.class);
		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn(resourceName);
		when(fromElement.getElementType()).thenReturn(type);
		IFile resource = ResourceFactory.createMockResource(resourceName);
		when(fromElement.getResource()).thenReturn(resource);
//		when(resource.toString())
		return fromElement;
	}
	


	public static IJavaElementDelta createJavaChangeDelta(IJavaElement element, int op_type) {
		IJavaElementDelta delta = mock(IJavaElementDelta.class);
		when(delta.getElement()).thenReturn(element);
		when(delta.getKind()).thenReturn(op_type);
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{});
		return delta;
	}
	
	public static ElementChangedEvent createAddMethodAction() {
		IJavaElement classElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java",IJavaElement.CLASS_FILE);
		IJavaElement addedElement = JavaElementsFactory.createJavaElement(null,"AnyClass.java",IJavaElement.METHOD);
		
		IJavaElementDelta childDelta = JavaElementsFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.ADDED);
		
		IJavaElementDelta delta = JavaElementsFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED);
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta);
		return event;
	}
	
}
