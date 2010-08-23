package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;


/**
 * Implements production code edit action. Production is the actual code
 * which is not test.
 * 
 * @author Hongbing Kou
 * @version $Id: ProductionEditAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class ProductionEditAction extends EditAction {
  /** Number of methods. */
  private int numOfMethods = 0;
  /** Number of statements. */
  private int numOfStatements = 0;
  /** Number of method change. */
  private int methodIncrease = 0;
  /** Number of statement change. */
  private int statementIncrease = 0;
  
  //TODO z comments cleanup
  
  /**
   * Constructs a production edit action instance.
   * 
   * @param clock Time action occurs.
   * @param productFile Production file.
   * @param duration Editing duration. 
   */
  public ProductionEditAction(Clock clock, File productFile, int duration) {
    super(clock, productFile, duration);
  }

  /**
   * Makes Jess facts in the given Jess rete engine.
   * 
   * @param index Action index in episode. 
   * @param engine Jess rete engine.
   * @throws JessException If error while constructing jess action.
   * @return The asserted fact.
   */
  
  //TODO [api] create a classifier interface
  
  public Fact assertJessFact(int index, Rete engine) throws JessException  {
    Fact assertedFact = null;
    if (isSubstantial()) {
      Fact f = new Fact("ProductionEditAction", engine);
      f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
      f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));
      f.setSlotValue("duration", new Value(this.getDuration(), RU.INTEGER));
      f.setSlotValue("methodChange", new Value(this.getMethodIncrease(), RU.INTEGER));
      f.setSlotValue("statementChange", new Value(this.getStatementIncrease(), RU.INTEGER));
      f.setSlotValue("byteChange", new Value(this.getFileSizeIncrease(), RU.INTEGER));
      
      assertedFact = engine.assertFact(f);
    }
    
    return assertedFact;
  }
  
  
  /**
   * Sets number of statement change.
   * 
   * @param value Number of statement. 
   */
  public void setStatementIncrease(int value) {
    this.statementIncrease = value;
  }
  /**
   * Gets number of statement change.
   * 
   * @return Number of statement change. 
   */
  public int getStatementIncrease() {
    return this.statementIncrease;
  }

  /**
   * Sets the number of method change.
   * 
   * @param value Number of method change.
   */
  public void setMethodIncrease(int value) {
    this.methodIncrease = value;
  }
  
  /**
   * Gets number of method change.
   * 
   * @return Number of method change.
   */
  public int getMethodIncrease() {
    return this.methodIncrease;
  }

  /**
   * Sets number of method in this file.
   * 
   * @param value Number of method.
   */
  public void setNumOfMethods(int value) {
    this.numOfMethods = value;    
  }
  
  /**
   * Gets number of methods. 
   * 
   * @return Number of methods.
   */
  public int getNumOfMethods() {
    return this.numOfMethods;
  }
  
  /**
   * Sets number of statements. 
   * 
   * @param value Number of statements.
   */
  public void setNumOfStatements(int value) {
    this.numOfStatements = value; 
  }
  
  /**
   * Gets number of statements.
   * 
   * @return Number of statements.
   */
  public int getNumOfStatements() {
    return this.numOfStatements;
  }
  
  /**
   * Gets production code edit string.
   * 
   * @return Production code edit string. 
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append(super.toString());
    buf.append(" PRODUCTION {");
    
    buf.append(makeMetricPair("MI", this.methodIncrease, this.numOfMethods)).append(", ");    
    buf.append(makeMetricPair("SI", this.statementIncrease, this.numOfStatements)).append(", ");
    buf.append(makeMetricPair("FI", this.fileSizeIncrease, this.fileSize));
    
    buf.append("}");
    
    return  buf.toString();
  }  

  /**
   * Checks production code edit.
   * 
   * @return True if edit time is positive.
   */
  public boolean hasProgress() {
    return this.getDuration() > 0 || this.getFileSizeIncrease() != 0;
  }  
  

  /**
   * Checks whether this edit work is significant or not.
   * 
   * @return True if the edit is substantial and false otherwise.
   */
  public boolean isSubstantial() {
//    return this.getDuration() > 0 && 
//      (this.getFileSizeIncrease() != 0 || this.methodIncrease != 0 || 
//       this.statementIncrease != 0);  
    return this.getFileSizeIncrease() != 0 || this.methodIncrease != 0 || 
           this.statementIncrease != 0;  
  }
  
  /**
   * Actions description is object metrics change.
   * 
   * @return Object metric changes.
   */
  public String getActionDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(super.getDuration()).append("sec ");
    
    buf.append(makeMetricPair("MI", this.methodIncrease, this.numOfMethods)).append(", ");    
    buf.append(makeMetricPair("SI", this.statementIncrease, this.numOfStatements)).append(", ");
    buf.append(makeMetricPair("FI", this.fileSizeIncrease, this.fileSize));
    
    return buf.toString();
  }

  /**
   * Action type is production edit.
   * 
   * @return Production edit.
   */
  public String getActionType() {
    return "PRODUCTION EDIT";
  }
}
