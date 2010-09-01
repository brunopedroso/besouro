package athos.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

public class JavaStructureChangeEventFactory {

	public static IJavaElement createJavaElement(IJavaElement parentElement, String resourceName, String elementName, int type) {
		IJavaElement fromElement = mock(IJavaElement.class);
		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn(elementName);
		when(fromElement.getElementType()).thenReturn(type);
		IFile resource = ResourceChangeEventFactory.createMockResource(resourceName, (long)33);
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
	
	public static ElementChangedEvent createAddMethodAction(String filename, String className, String methodName) {
		IJavaElement classElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className,IJavaElement.CLASS_FILE);
		IJavaElement addedElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className + "#" + methodName, IJavaElement.METHOD);
		
		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.ADDED);
		
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED);
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta);
		return event;
	}
	
}
