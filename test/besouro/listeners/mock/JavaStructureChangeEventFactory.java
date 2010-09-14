package besouro.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

public class JavaStructureChangeEventFactory {

	public static IJavaElement createJavaElement(IJavaElement parentElement, String resourceName, String elementName, int type) {
		
		IJavaElement fromElement = mock(IJavaElement.class);
		IFile resource = ResourceChangeEventFactory.createMockResource(resourceName, (long)33);

		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn(elementName);
		when(fromElement.getElementType()).thenReturn(type);
		when(fromElement.getResource()).thenReturn(resource);
		
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
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED);
		
		IJavaElement addedElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className + "#" + methodName, IJavaElement.METHOD);
		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.ADDED);
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		when(event.getDelta()).thenReturn(delta);
		return event;
	}
	
	public static ElementChangedEvent createRemoveMethodAction(String filename, String className, String methodName) {
		
		IJavaElement classElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className,IJavaElement.CLASS_FILE);
		IJavaElement addedElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className + "#" + methodName, IJavaElement.METHOD);
		
		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.REMOVED);
		
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED);
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		when(event.getDelta()).thenReturn(delta);
		
		return event;
	}
	
	public static ElementChangedEvent createRenameMethodEvent(String filename, String classname, String fromMethod, String toMethod) {
		
		IJavaElement parentElement = JavaStructureChangeEventFactory.createJavaElement(null,filename, classname, IJavaElement.CLASS_FILE);
		IJavaElement renamedFromElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,filename, classname + "#" + fromMethod, IJavaElement.FIELD);
		IJavaElement renamedToElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,filename, classname + "#" + toMethod, IJavaElement.FIELD);
		
		IJavaElementDelta removedDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(renamedFromElement, IJavaElementDelta.REMOVED);
		IJavaElementDelta addedDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(renamedToElement, IJavaElementDelta.ADDED);

		IJavaElementDelta classDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED);
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{removedDelta, addedDelta});
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});

		when(event.getDelta()).thenReturn(parentDelta);
		return event;
	}
	
}
