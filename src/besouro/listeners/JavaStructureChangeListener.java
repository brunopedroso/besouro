package besouro.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

import besouro.measure.JavaStatementMeter;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.stream.ActionOutputStream;


/**
 * Listens to the java element change events to get incremental work on java
 * objects and collect refactoring information for test-driven development
 * analysis purpose. It's declared as package private so that it can only be
 * instantiated by Eclise sensor.
 * 
 * @author Hongbing Kou
 */
public class JavaStructureChangeListener implements IElementChangedListener {

	public static final String JAVA = "java";
	public static final String CLASS = "Class";

	protected static final String PROP_CURRENT_SIZE = "Current-Size";
	protected static final String PROP_CLASS_NAME = "Class-Name";
	protected static final String PROP_CURRENT_METHODS = "Current-Methods";
	protected static final String PROP_CURRENT_STATEMENTS = "Current-Statements";
	protected static final String PROP_CURRENT_TEST_METHODS = "Current-Test-Methods";
	protected static final String PROP_CURRENT_TEST_ASSERTIONS = "Current-Test-Assertions";

	private ActionOutputStream stream;
	private JavaStatementMeter measurer = new JavaStatementMeter();

	public JavaStructureChangeListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	public void elementChanged(ElementChangedEvent event) {
		
		addToEditedFiles(event.getDelta());
		
		// IJavaElementDelta jed = event.getDelta().getAffectedChildren()[0];
		IJavaElementDelta[] childrenChanges = event.getDelta().getAffectedChildren();

		if (childrenChanges != null && childrenChanges.length > 0) {
			javaObjectChange(childrenChanges[0]);
		}
	}

	private void javaObjectChange(IJavaElementDelta jed) {
		List<IJavaElementDelta> additions = new ArrayList<IJavaElementDelta>();
		List<IJavaElementDelta> deletions = new ArrayList<IJavaElementDelta>();

		// Traverse the delta change tree for refactoring activity
		traverse(jed, additions, deletions);

		// Gets the location of java file.
		IResource javaFile = jed.getElement().getResource();

		// No java structure change
		if (additions.isEmpty() && deletions.isEmpty()) {
			return;

			// Addition, deletion, renaming activity.
		} else if (additions.size() == 1 || deletions.size() == 1) {

			if (deletions.isEmpty()) {
				processUnary(javaFile, "ADD", (IJavaElementDelta) additions.get(0));

			} else if (additions.isEmpty()) {
				processUnary(javaFile, "REMOVE", (IJavaElementDelta) deletions.get(0));

			} else if (deletions.size() == 1) {

				IJavaElementDelta fromDelta = (IJavaElementDelta) deletions.get(0);
				IJavaElementDelta toDelta = (IJavaElementDelta) additions.get(0);

				if (fromDelta.getElement().getParent().equals(toDelta.getElement().getParent())) {
					processRenameRefactor(javaFile, fromDelta, toDelta);

				} else {
					processMoveRefactor(fromDelta, toDelta);
				}

			}
		}
		// Massive addition by copying
		else if (additions.size() > 1) {

			for (Iterator i = additions.iterator(); i.hasNext();) {
				processUnary(javaFile, "ADD",(IJavaElementDelta) i.next());
			}
		}
		// Massive block deletion
		else if (deletions.size() > 1) {
			for (Iterator i = deletions.iterator(); i.hasNext();) {
				processUnary(javaFile, "REMOVE",(IJavaElementDelta) i.next());
			}
		}
	}


	private void processUnary(IResource javaFile, String op, IJavaElementDelta delta) {
		
		IJavaElement element = delta.getElement();

		// Stop if there is no associated element.
		if (javaFile == null || element == null || element.getResource() == null) {
			return;
		}

		String type = retrieveType(element);
		// If type is not field, method, import and class do nothing.
		if (type == null) {
			return;
		}

		IPath classFileName = javaFile.getLocation();
		if ("CLASS".equals(type)) {
			classFileName = element.getResource().getLocation();
		}

		// Only deal with java file.
		if (!JAVA.equals(classFileName.getFileExtension())) {

			return;
		}

		String name = buildElementName(element.toString());
		if (name != null && !"".equals(name)) {

			RefactoringAction action = new RefactoringAction(new Date(), element.getResource().getName());
			action.setOperator(op);
			action.setSubjectType(type);
			action.setSubjectName(name);

			this.stream.addAction(action);

		}
	}

	private void processRenameRefactor(IResource javaFile, IJavaElementDelta fromDelta, IJavaElementDelta toDelta) {

		String type = retrieveType(toDelta.getElement());

		IPath classFileName = javaFile.getLocation();
		if ("CLASS".equals(type)) {
			classFileName = fromDelta.getElement().getResource().getLocation();

		} else if ("PACKAGE".equals(type)) {
			classFileName = fromDelta.getElement().getResource().getLocation();
		}

		// Only deal with java file.
		if (!JAVA.equals(classFileName.getFileExtension())) {
			return;
		}

		String fromName = buildElementName(fromDelta.getElement().toString());
		String toName = buildElementName(toDelta.getElement().toString());

		if (fromName != null && !"".equals(fromName) && toName != null && !"".equals(toName)) {

			RefactoringAction action = new RefactoringAction(new Date(), javaFile.getName());
			action.setOperator("RENAME");
			action.setSubjectName(fromName + "=>" + toName);

			action.setSubjectType(type);

			this.stream.addAction(action);

		}
	}

	private void processMoveRefactor(IJavaElementDelta fromDelta, IJavaElementDelta toDelta) {

		IResource javaFile = fromDelta.getElement().getResource();
		IJavaElement from = fromDelta.getElement();
		IJavaElement to = toDelta.getElement().getParent();

		// Only deal with java file.
		if (!JAVA.equals(javaFile.getFileExtension())) {
			return;
		}

		String fromName = buildElementName(from.toString());
		String toName = buildElementName(to.toString());

		if (fromName != null && !"".equals(fromName) && toName != null && !"".equals(toName)) {

			RefactoringAction action = new RefactoringAction(new Date(), javaFile.getName());
			action.setOperator("MOVE");
			action.setSubjectName(fromName + "=>" + toName);

			action.setSubjectType(retrieveType(toDelta.getElement()));

			this.stream.addAction(action);

		}
	}

	private String retrieveType(IJavaElement element) {
		int eType = element.getElementType();

		switch (eType) {
		case IJavaElement.FIELD:
			return "FIELD";
		case IJavaElement.METHOD:
			return "METHOD";
		case IJavaElement.IMPORT_DECLARATION:
			return "IMPORT";
		case IJavaElement.IMPORT_CONTAINER:
			return "IMPORT";
		case IJavaElement.COMPILATION_UNIT:
			return "CLASS";
		case IJavaElement.JAVA_PROJECT:
			return "CLASS";
		case IJavaElement.PACKAGE_FRAGMENT:
			return "PACKAGE";
		default:
			return null;
		}
	}

	private String buildElementName(String name) {

		int index = name.indexOf('[');
		if (index >=0)
			name = name.substring(0, index);
		
		// Trim off the meaningless "(not open)" string
		int pos = name.indexOf("(not open)");
		if (pos > 0) {
			name = name.substring(0, pos);
		}

		// take off the '#' if it exists
		name = name.replace('#', '/');

		return name.trim();
	}


	private void traverse(IJavaElementDelta delta, List<IJavaElementDelta> additions, List<IJavaElementDelta> deletions) {

		// Saves the addition and deletion.
		if (delta.getKind() == IJavaElementDelta.ADDED) {
			additions.add(delta);

		} else if (delta.getKind() == IJavaElementDelta.REMOVED) {
			deletions.add(delta);
		}

		// Recursively look for changes on children elements.
		IJavaElementDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			traverse(children[i], additions, deletions);
		}
	}
	
	private void addToEditedFiles(IJavaElementDelta delta) {
		IResource resource = delta.getElement().getResource();
		int flag = delta.getFlags();
		int kind = delta.getKind();
		
		// :RESOLVED: 28 Mar 2017
		// Author: Adonis Figueroa
		// Note that the 540673 and 540680 enumeration types are not listed in the IJavaElementDelta static filed.
		// However, these numbers are generated when a File is edited so that it is checked in the logical condition.
		int EDITED_FLAG = 540673;
		int METHOD_UPDATED_FLAG = 540680; //Method Name Updated | Added | Deleted
		
		if ((kind == IJavaElementDelta.CHANGED) && resource instanceof IFile && 
			(flag == EDITED_FLAG || flag == METHOD_UPDATED_FLAG)) {
			if (JAVA.equals(resource.getFileExtension())) {
				String fullPath = resource.getFullPath().toString();
				
				BesouroListenerSet listeners = BesouroListenerSet.getSingleton();
				String previousEditedFile = listeners.getActualEditedFile();
				
				// If edited file is different --> add Action
				if (!previousEditedFile.isEmpty() && !previousEditedFile.equals(fullPath)) {
					Date previousEditedDate = listeners.getActualEditedDate();
					Path path = new Path(previousEditedFile);
					
					IFile changedFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					EditAction action = new EditAction(previousEditedDate, changedFile.getName());
					
					JavaStatementMeter meter = this.measurer.measureJavaFile(changedFile);
					
					action.setFileSize((int) changedFile.getLocation().toFile().length());
					action.setIsTestEdit(meter.isTest());
					action.setMethodsCount(meter.getNumOfMethods());
					action.setStatementsCount(meter.getNumOfStatements());
					action.setTestMethodsCount(meter.getNumOfTestMethods());
					action.setTestAssertionsCount(meter.getNumOfTestAssertions());
					
					stream.addAction(action);
				}
				
				listeners.setActualEditedFile(fullPath);
				listeners.setActualEditedDate(new Date());
			}
		}
		
		// Recursively look for changes on children elements.
		IJavaElementDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			addToEditedFiles(children[i]);
		}
	}
	
	/**
	 * for testing purposes
	 */
	public void setMeasurer(JavaStatementMeter meter) {
		this.measurer = meter;
	}
}