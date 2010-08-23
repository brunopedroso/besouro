package athos.model.refactor;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

import athos.model.Clock;

/**
 * Defines unary refactoring action.
 * 
 * @author Hongbing Kou
 * @version $Id: UnaryRefactorAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class UnaryRefactorAction extends RefactorAction {
  /** Refactor subject name. */
  private String subjectName;

  /**
   * Constructs an unary refactor action.
   * 
   * @param clock Time stamp.
   * @param workspaceFile Associated file.
   */
  public UnaryRefactorAction(Clock clock, File workspaceFile) {
    super(clock, workspaceFile);
  }

  /**
   * Sets refactoring subject name.
   * 
   * @param subjectName Refactoring subject name.
   */
  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  /**
   * Gets subject name.
   * 
   * @return Subject name.
   */
  public String getSubjectName() {
    return this.subjectName;
  }  

  /**
   * Makes Jess facts in the given Jess rete engine.
   * 
   * @param index Index of action in episode. 
   * @param engine Jess rete engine.
   * @throws JessException If error while constructing jess action.
   * @return Jess fact of this action.
   */
  public Fact assertJessFact(int index, Rete engine) throws JessException  {
    Fact f = new Fact("UnaryRefactorAction", engine);
    f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
    f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));

    f.setSlotValue("operation", new Value(this.getOperator().getName(), RU.STRING));
    f.setSlotValue("type", new Value(this.getSubjectType().getName(), RU.STRING));
    f.setSlotValue("data", new Value(this.getSubjectName(), RU.STRING));
    
    Fact assertedFact = engine.assertFact(f);
    return assertedFact;
  }  
  
  
  /**
   * Gets refactor action string.
   * 
   * @return Refactor action string.
   */
  public String toString() {
    return super.toString() + " {" + this.subjectName + "}";
  }

  /**
   * Gets refactored subject as descrion for unary refactoring action.
   * 
   * @return Refactoring subject name.
   */
  public String getActionDesc() {
    return this.getSubjectName();
  }

  /**
   * Refacroring action name and its subtype.
   * 
   * @return Unary rfactoring action name and its subtype.
   */
  public String getActionType() {
    return  this.getOperator().getName() + " " + this.getSubjectType().getName();
  }
}
