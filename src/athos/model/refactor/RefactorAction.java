package athos.model.refactor;

import java.io.File;


import athos.model.Clock;
import athos.model.FileAction;

/**
 * Implements action regarding to refactoring on java class, method etc.
 * 
 * @author Hongbing Kou
 * @version $Id: RefactorAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public abstract class RefactorAction extends FileAction {
	

	//TODO z[clean] inline these two classes here
	
	
  /** Refactoring operator. */ 
  private RefactorOperator op;
  /** Refactoring subject type*/
  private RefactorSubjectType subjectType;

  /**
   * Constructs refactor action.
   * 
   * @param clock Time stamp.
   * @param workspceFile Associated file.
   */
  public RefactorAction(Clock clock, File workspceFile) {
    super(clock, workspceFile);
  }

  /**
   * Sets the refactor operator.
   * 
   * @param op Refactoring operator.
   */
  public void setOperator(RefactorOperator op) {
    this.op = op;     
  }
  
  /**
   * Gets refactoring operator. 
   * 
   * @return Refactoring operator.
   */
  public RefactorOperator getOperator() {
    return this.op;
  }

  /**
   * Sets refactor subject type.
   * 
   * @param subjectType Refactor subject type. 
   */
  public void setSubjectType(RefactorSubjectType subjectType) {
    this.subjectType = subjectType;   
  }
  
  
  /**
   * Gets the refactoring subject type.
   * 
   * @return Type of this refactor action on.
   */
  public RefactorSubjectType getSubjectType() {
    return this.subjectType;
  }
  
  /**
   * Gets refactor action string.
   * 
   * @return  Refactor action string.
   */
  public String toString() {
    return super.toString() + " REFACTOR " + this.getOperator() + " " + this.getSubjectType();
  }
}
