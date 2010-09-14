package besouro.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

import besouro.model.refactor.UnaryRefactorAction;
import besouro.stream.ActionOutputStream;


/**
 * Listens to the java element change events to get incremental work on java
 * objects and collect refactoring information for test-driven development
 * analysis purpose. It's declared as package private so that it can only be
 * instantiated by Eclise sensor.
 * 
 * @author Hongbing Kou
 * @version $Id$
 */
public class JavaStructureChangeListener implements IElementChangedListener {

	/** Literals of java. */
	public static final String JAVA = "java";
	/** Literals of class. */
	public static final String CLASS = "Class";

	protected static final String PROP_CURRENT_SIZE = "Current-Size";
	protected static final String PROP_CLASS_NAME = "Class-Name";
	protected static final String PROP_CURRENT_METHODS = "Current-Methods";
	protected static final String PROP_CURRENT_STATEMENTS = "Current-Statements";
	protected static final String PROP_CURRENT_TEST_METHODS = "Current-Test-Methods";
	protected static final String PROP_CURRENT_TEST_ASSERTIONS = "Current-Test-Assertions";

	private ActionOutputStream stream;

	/**
	 * Instantiates the JavaStructureDetector instance with Eclipse sensor.
	 * 
	 * @param sensor
	 *            Eclipse sensor.
	 */
	public JavaStructureChangeListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	/**
	 * Implements the element change response.
	 * 
	 * @param event
	 *            Element change event.
	 */
	public void elementChanged(ElementChangedEvent event) {
		// IJavaElementDelta jed = event.getDelta().getAffectedChildren()[0];
		IJavaElementDelta[] childrenChanges = event.getDelta()
				.getAffectedChildren();

		if (childrenChanges != null && childrenChanges.length > 0) {
			javaObjectChange(childrenChanges[0]);
		}
	}

	/**
	 * Process the editng on java element changes.
	 * 
	 * @param jed
	 *            Java element delta change.
	 */
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

	/**
	 * Constructs and sends the java element change data.
	 * 
	 * @param javaFile
	 *            Associated file.
	 * @param op
	 *            Operation
	 * @param delta
	 *            Delta change element
	 */
	private void processUnary(IResource javaFile, String op, IJavaElementDelta delta) {
		
		IJavaElement element = delta.getElement();

		// Stop if there is no associated element.
		if (javaFile == null || element == null
				|| element.getResource() == null) {
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

			UnaryRefactorAction action = new UnaryRefactorAction(new Date(), element.getResource());
			action.setOperator(op);
			action.setSubjectType(type);
			action.setSubjectName(name);

			this.stream.addAction(action);

		}
	}

	/**
	 * Constructs and send of the java element change data.
	 * 
	 * @param javaFile
	 *            Associated file.
	 * @param fromDelta
	 *            Change from delta.
	 * @param toDelta
	 *            Change to delta.
	 */
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

		if (fromName != null && !"".equals(fromName) && toName != null
				&& !"".equals(toName)) {

			// msgBuf.append("Refactor : Rename#").append(typeName).append('#').append(fromName).append(" -> ").append(toName);

			UnaryRefactorAction action = new UnaryRefactorAction(new Date(), javaFile);
			action.setOperator("RENAME");
			action.setSubjectName(fromName + " => " + toName);

			action.setSubjectType(type);

			this.stream.addAction(action);

		}
	}

	/**
	 * Constructs and send of the java element change data.
	 * 
	 * @param javaFile
	 *            Associated file.
	 * @param element
	 *            Java Element to be moved.
	 * @param from
	 *            Change from element.
	 * @param to
	 *            Change to element.
	 */
	private void processMoveRefactor(IJavaElementDelta fromDelta, IJavaElementDelta toDelta) {

		IResource javaFile = fromDelta.getElement().getResource();
		IJavaElement from = fromDelta.getElement();
		IJavaElement to = toDelta.getElement().getParent();

		// Only deal with java file.
		if (!JAVA.equals(javaFile.getFileExtension())) {
			return;
		}

//		System.out.println("///" + to + "///");

		// String name = retrieveName(element);
		String fromName = buildElementName(from.toString());
		String toName = buildElementName(to.toString());
		// System.out.println("--" + fromName + "======" + toName + "|");

		// Put refactor data together with pound sigh separation and send it to
		// Hackystat server as activity data.
		if (fromName != null && !"".equals(fromName) && toName != null
				&& !"".equals(toName)) {

			// msgBuf.append("Refactor : Move#").append(typeName).append('#').append(name).append('#').append(fromName).append(" -> ").append(toName);

			UnaryRefactorAction action = new UnaryRefactorAction(new Date(), javaFile);
			action.setOperator("MOVE");
			action.setSubjectName(fromName + " => " + toName);

			action.setSubjectType(retrieveType(toDelta.getElement()));

			this.stream.addAction(action);

		}
	}

	/**
	 * Gets the element type.
	 * 
	 * @param element
	 *            Java element object
	 * @return Element type string (class, method, field or import).
	 */
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

	/**
	 * Gets the element name with signature.
	 * 
	 * @param element
	 *            Java element, which could be class, method, field or import.
	 * @return Brief element name.
	 */
	private String buildElementName(String name) {

		try {
			name = name.substring(0, name.indexOf('['));
		} catch (IndexOutOfBoundsException e) {
//			System.out.println("Where is the [ ? " + name);
			
		}
		// Trim off the meaningless "(not open)" string
		int pos = name.indexOf("(not open)");
		if (pos > 0) {
			name = name.substring(0, pos);
		}

		// take off the '#' if it exists
		name = name.replace('#', '/');

		return name.trim();
	}

	/**
	 * Traverses the delta change tree on java element to look for addition and
	 * deletion on java element.
	 * 
	 * @param delta
	 *            Delta element change.
	 * @param additions
	 *            Added element holder.
	 * @param deletions
	 *            Deleted element holder.
	 */
	private void traverse(IJavaElementDelta delta,
			List<IJavaElementDelta> additions, List<IJavaElementDelta> deletions) {

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
}