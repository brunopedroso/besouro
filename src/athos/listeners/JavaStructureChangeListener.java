package athos.listeners;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

import athos.model.Clock;
import athos.model.EditAction;
import athos.stream.ActionOutputStream;


/**
 * Listens to the java element change events to get incremental work on java objects and collect 
 * refactoring information for test-driven development analysis purpose. It's declared as package
 * private so that it can only be instantiated by Eclise sensor.
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
  
  private ActionOutputStream sensor;

/**
   * Instantiates the JavaStructureDetector instance with Eclipse sensor.
   * 
   * @param sensor Eclipse sensor.
   */
  public JavaStructureChangeListener(ActionOutputStream sensor) {
    this.sensor = sensor;
  }

  /**
   * Implements the element change response.
   * 
   * @param event Element change event.
   */
  public void elementChanged(ElementChangedEvent event) {
    //IJavaElementDelta jed = event.getDelta().getAffectedChildren()[0];
    IJavaElementDelta[] childrenChanges = event.getDelta().getAffectedChildren(); 
    
    if (childrenChanges != null && childrenChanges.length > 0) {
      javaObjectChange(childrenChanges[0]);
    }
  }
  
  /**
   * Process the editng on java element changes.
   * 
   * @param jed Java element delta change.
   */
  private void javaObjectChange(IJavaElementDelta jed) {
    List<IJavaElementDelta> additions = new ArrayList<IJavaElementDelta>();
    List<IJavaElementDelta> deletions = new ArrayList<IJavaElementDelta>();
    
    // Traverse the delta change tree for refactoring activity
    traverse(jed, additions, deletions);
    
    //  Gets the location of java file.
    IPath javaFile = jed.getElement().getResource().getLocation();
        
    // No java structure change
    if (additions.isEmpty() && deletions.isEmpty()) {
      return;      
    }
    // Addition, deletion, renaming activity.
    else if (additions.size() == 1 || deletions.size() == 1) {
      if (deletions.isEmpty()) {
        processUnary(javaFile, "Add", (IJavaElementDelta) additions.get(0));        
      }
      else if (additions.isEmpty()) {
        processUnary(javaFile, "Remove", (IJavaElementDelta) deletions.get(0));
      }
      else if (deletions.size() == 1) {
        IJavaElementDelta fromDelta = (IJavaElementDelta) deletions.get(0);
        IJavaElementDelta toDelta = (IJavaElementDelta) additions.get(0);
        if (fromDelta.getElement().getParent().equals(toDelta.getElement().getParent())) { 
          processRenameRefactor(javaFile, fromDelta, toDelta); 
        }
        else {
          javaFile = fromDelta.getElement().getResource().getLocation();
          processMoveRefactor(javaFile, fromDelta.getElement(), fromDelta.getElement().getParent(), 
              toDelta.getElement().getParent());
        }
      }
    }    
    // Massive addition by copying
    else if (additions.size() > 1) {
      for (Iterator i = additions.iterator(); i.hasNext();) {
        processUnary(javaFile, "Add", (IJavaElementDelta) i.next());
      }
    }
    // Massive block deletion
    else if (deletions.size() > 1) {
      for (Iterator i = deletions.iterator(); i.hasNext();) {
        processUnary(javaFile, "Remove", (IJavaElementDelta) i.next());
      }
    }    
  }

  /**
   * Constructs and sends the java element change data.
   * 
   * @param javaFile Associated file.
   * @param op Operation
   * @param delta Delta change element
   */
  private void processUnary(IPath javaFile, String op, IJavaElementDelta delta) {
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
    
    IPath classFileName = javaFile;
    if (CLASS.equals(type)) {
      classFileName = element.getResource().getLocation();
    }  
    
    // Only deal with java file.
    if (!JAVA.equals(classFileName.getFileExtension())) {
      return;
    }
    
    String name = retrieveName(element);
    if (name != null && !"".equals(name)) {
      
    	//TODO [1]  this should be a refactor action... unary, binary...
    	EditAction action = new EditAction(new Clock(new Date()), classFileName.toFile(), 0);
    	
    	
		IFile changedFile = (IFile) element.getResource();
		
		// TODO [3] measures and classification be made in another place?
		JavaStatementMeter testCounter = JavaStatementMeter.measureJavaFile(changedFile);
//		System.out.println("\t measured " + testCounter);
		
		//TODO classificar edits nos listeners de java structure (extrair classificador pra depois?)
//		action.setIsTestEdit(testCounter.hasTest());
		action.setIsTestEdit(name.toLowerCase().contains("test"));
		
		action.setFileSize(WindowListener.getActiveBufferSize());
		action.setCurrentMethods(testCounter.getNumOfMethods());
		action.setCurrentStatements(testCounter.getNumOfStatements());
		action.setCurrentTestMethods(testCounter.getNumOfTestMethods());
		action.setCurrentTestAssertions(testCounter.getNumOfTestMethods());
    		
      action.setOperation(op);
      action.setUnitName(name);
      
      this.sensor.addAction(action);
    		  
    }
  }


  /**
   * Constructs and send of the java element change data.
   * 
   * @param javaFile Associated file.
   * @param fromDelta Change from delta.
   * @param toDelta Change to delta.
   */
  private void processRenameRefactor(IPath javaFile, IJavaElementDelta fromDelta, 
      IJavaElementDelta toDelta) {
    String typeName = retrieveType(toDelta.getElement());
    
    IPath classFileName = javaFile;
    if (CLASS.equals(typeName)) {
      classFileName = fromDelta.getElement().getResource().getLocation();
    }
    else if ("Package".equals(typeName)) {
      classFileName = fromDelta.getElement().getResource().getLocation();
    }

    // Only deal with java file.
    if (!JAVA.equals(classFileName.getFileExtension())) {
      return;
    }
    
    String fromName = retrieveName(fromDelta.getElement());
    String toName = retrieveName(toDelta.getElement());
    
    if (fromName != null && !"".equals(fromName) && toName != null && !"".equals(toName)) {
            
//      msgBuf.append("Refactor : Rename#").append(typeName).append('#').append(fromName).append(" -> ").append(toName);
      
      //TODO [1] duration 
      EditAction action = new EditAction(new Clock(new Date()), classFileName.toFile(), 0);
      action.setOperation("Rename");
      action.setUnitName(fromName + " => " + toName);
      
      action.setIsTestEdit(classFileName.toString().toLowerCase().contains("test"));
      
      this.sensor.addAction(action);

    }
  }
  
  /**
   * Constructs and send of the java element change data.
   * 
   * @param javaFile Associated file.
   * @param element  Java Element to be moved.
   * @param from Change from element.
   * @param to Change to element.
   */
  private void processMoveRefactor(IPath javaFile, IJavaElement element, IJavaElement from, IJavaElement to) {
	  
    // Only deal with java file.
    if (!JAVA.equals(javaFile.getFileExtension())) {
      return;
    }
    
//    String name = retrieveName(element);
    String fromName = retrieveName(from);
    String toName = retrieveName(to);
    
    // Put refactor data together with pound sigh separation and send it to Hackystat server as activity data.
    if (fromName != null && !"".equals(fromName) && toName != null && !"".equals(toName)) {

//      msgBuf.append("Refactor : Move#").append(typeName).append('#').append(name).append('#').append(fromName).append(" -> ").append(toName);
      
      //TODO [1] duration
      EditAction action = new EditAction(new Clock(new Date()), javaFile.toFile(), 0);
      action.setOperation("Move");
      action.setUnitName(fromName + " => " + toName);
      
      action.setIsTestEdit(javaFile.toString().toLowerCase().contains("test"));
      
      this.sensor.addAction(action);

      
    }    
  }
  
  /**
   * Gets the element type.
   * 
   * @param element Java element object
   * @return Element type string (class, method, field or import).
   */
  private String retrieveType(IJavaElement element) {
    int eType = element.getElementType();
    
    switch (eType) {
      case IJavaElement.FIELD:
        return "Field";
      case IJavaElement.METHOD:
        return "Method";
      case IJavaElement.IMPORT_DECLARATION:
        return "Import";
            case IJavaElement.IMPORT_CONTAINER:
        return "Import";
      case IJavaElement.COMPILATION_UNIT:
        return CLASS;
      case IJavaElement.JAVA_PROJECT:
        return CLASS;
      case IJavaElement.PACKAGE_FRAGMENT:
        return "Package";
      default:
        return null;
    }
  }
  
  /**
   * Gets the element name with signature.
   * 
   * @param element Java element, which could be class, method, field or import.
   * @return Brief element name.
   */
  private String retrieveName(IJavaElement element) {
    String name = element.toString();
    try {
      name = name.substring(0, name.indexOf('['));
    }
    catch (IndexOutOfBoundsException e) {
      System.out.println("Where is the [ ? " + name);
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
   * Traverses the delta change tree on java element to look for addition and deletion on
   * java element.
   * 
   * @param delta Delta element change.
   * @param additions Added element holder.
   * @param deletions Deleted element holder.
   */
  private void traverse(IJavaElementDelta delta, List<IJavaElementDelta> additions, 
      List<IJavaElementDelta> deletions) {    
    // Saves the addition and deletion.
    if (delta.getKind() == IJavaElementDelta.ADDED) {
       additions.add(delta);
    }
    else if (delta.getKind() == IJavaElementDelta.REMOVED) {
      deletions.add(delta);
    }
    
    // Recursively look for changes on children elements.
    IJavaElementDelta[] children = delta.getAffectedChildren();
    for (int i = 0; i < children.length; i++) {
      traverse(children[i], additions, deletions);
    }
  }
}