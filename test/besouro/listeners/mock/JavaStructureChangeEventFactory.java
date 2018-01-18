package besouro.listeners.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.mockito.Matchers.any;


import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;



public class JavaStructureChangeEventFactory {
	
	public static final int EDITED_FLAG = 540673;
	public static final int METHOD_UPDATED_FLAG = 540680;

	public static IJavaElement createJavaElement(IJavaElement parentElement, String resourceName, String elementName, int type) {
		
		IJavaElement fromElement = mock(IJavaElement.class);
		
		IFile resource = createMockResource(resourceName, (long)33);

		when(fromElement.getParent()).thenReturn(parentElement);
		when(fromElement.toString()).thenReturn(elementName);
		when(fromElement.getElementType()).thenReturn(type);
		when(fromElement.getResource()).thenReturn(resource);
		
		return fromElement;
	}
	


	public static IJavaElementDelta createJavaChangeDelta(IJavaElement element, int op_type, int op_flag) {
		IJavaElementDelta delta = mock(IJavaElementDelta.class);
		when(delta.getElement()).thenReturn(element);
		when(delta.getKind()).thenReturn(op_type);
		when(delta.getFlags()).thenReturn(op_flag);
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{});
		return delta;
	}
	
	public static ElementChangedEvent createAddMethodAction(String filename, String className, String methodName) {
		
		IJavaElement classElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className,IJavaElement.CLASS_FILE);
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED, -1);
		
		IJavaElement addedElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className + "#" + methodName, IJavaElement.METHOD);
		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.ADDED, -1);
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		
		when(event.getDelta()).thenReturn(delta);
		return event;
	}
	
	public static ElementChangedEvent createRemoveMethodAction(String filename, String className, String methodName) {
		
		IJavaElement classElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className,IJavaElement.CLASS_FILE);
		IJavaElement addedElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,className + "#" + methodName, IJavaElement.METHOD);
		
		IJavaElementDelta childDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(addedElement,IJavaElementDelta.REMOVED, -1);
		
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED, -1);
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(delta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{childDelta});
		when(event.getDelta()).thenReturn(delta);
		
		return event;
	}
	
	public static ElementChangedEvent createRenameMethodEvent(String filename, String classname, String fromMethod, String toMethod) {
		
		IJavaElement parentElement = JavaStructureChangeEventFactory.createJavaElement(null,filename, classname, IJavaElement.CLASS_FILE);
		IJavaElement renamedFromElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,filename, classname + "#" + fromMethod, IJavaElement.FIELD);
		IJavaElement renamedToElement = JavaStructureChangeEventFactory.createJavaElement(parentElement,filename, classname + "#" + toMethod, IJavaElement.FIELD);
		
		IJavaElementDelta removedDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(renamedFromElement, IJavaElementDelta.REMOVED, -1);
		IJavaElementDelta addedDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(renamedToElement, IJavaElementDelta.ADDED, -1);

		IJavaElementDelta classDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED, -1);
		IJavaElementDelta parentDelta = JavaStructureChangeEventFactory.createJavaChangeDelta(parentElement,IJavaElementDelta.CHANGED, -1);
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		
		when(classDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{removedDelta, addedDelta});
		when(parentDelta.getAffectedChildren()).thenReturn(new IJavaElementDelta[]{classDelta});

		when(event.getDelta()).thenReturn(parentDelta);
		return event;
	}
	
	public static ElementChangedEvent createEditMethodAction(String filename, int fileSize) throws CoreException {
		IJavaElement classElement = JavaStructureChangeEventFactory.createJavaElement(null,filename,null,IJavaElement.CLASS_FILE);
		IJavaElementDelta delta = JavaStructureChangeEventFactory.createJavaChangeDelta(classElement,IJavaElementDelta.CHANGED, EDITED_FLAG);
		
		ElementChangedEvent event = mock(ElementChangedEvent.class);
		when(event.getDelta()).thenReturn(delta);
		
		return event;
	}
	
	public static IFile createMockResource(String filename, long fileSIze) {
		
		String[] split = filename.split("\\.");
		
		IFile resource = mock(IFile.class);
		
		IPath path = mock(IPath.class);
		when(path.toString()).thenReturn(filename);
		
		File file = mock(File.class);
		when(file.getName()).thenReturn(filename);
		when(file.getPath()).thenReturn(filename);
		when(file.length()).thenReturn(fileSIze);
		
		when(path.toFile()).thenReturn(file);
		
		if (split.length>1)
			when(path.getFileExtension()).thenReturn(split[1]);

		when(resource.getLocation()).thenReturn(path);

		// separates "filename" of ".java"
		when(resource.getName()).thenReturn(filename);
		
		if (split.length>1)
			when(resource.getFileExtension()).thenReturn(split[1]);
		
		when(resource.getFullPath()).thenReturn(path);
		
		return resource;
	}
	
	public static void eclipseMock(String filename, long fileSIze) {
		mockStatic(ResourcesPlugin.class);
        
		IWorkspace workspace = mock(IWorkspace.class);
		when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        
		IWorkspaceRoot workspaceRoot = mock(IWorkspaceRoot.class);
		when(workspace.getRoot()).thenReturn(workspaceRoot);
        
		IPath workspaceRootLocation = mock(IPath.class);
		when(workspaceRoot.getLocation()).thenReturn(workspaceRootLocation);
        
		IFile file = createMockResource(filename, (long)fileSIze);
		when(workspaceRoot.getFile(any(Path.class))).thenReturn(file);
    }
}
