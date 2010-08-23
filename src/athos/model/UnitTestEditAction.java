package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;


/**
 * Implements edit action on test code.
 * 
 * @author Hongbing Kou
 * @version $Id: UnitTestEditAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class UnitTestEditAction extends EditAction {
  /** Number of test methods. */
  private int numOfTestMethods = 0;
  /** Number of test assertions. */
  private int numOfTestAssertions = 0;
  /** Number of test method changes. */
  private int testMethodIncrease = 0;
  /** Number of test assertion change. */
  private int testAssertionIncrease = 0;
  /** Number of methods in test class. */
  
  private int numOfMethods = 0;
  /** Number of method increase. */
  private int methodIncrease = 0;
  
  /** Number of statements in test class. */
  private int numOfStatements = 0;
  /** Number of statement increase. */
  private int statementIncrease = 0;
  
  /**
   * Constructs an edit action on a file.
   * 
   * @param clock Time action occurs.
   * @param testFile Associated file.
   * @param duration Edit duration.
   */
  public UnitTestEditAction(Clock clock, File testFile, int duration) {
    super(clock, testFile, duration);
  }
  
  /**
   * Makes Jess facts in the given Jess rete engine.
   * 
   * @param index Action index in episode.
   * @param engine Jess rete engine.
   * @throws JessException If error while constructing jess action.
   * @return The asserted fact.
   */
  public Fact assertJessFact(int index, Rete engine) throws JessException  {
    Fact assertedFact = null;
    if (isSubstantial()) {
      Fact f = new Fact("UnitTestEditAction", engine);
      f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
      f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));
      f.setSlotValue("duration", new Value(this.getDuration(), RU.INTEGER));
      f.setSlotValue("testChange", new Value(this.getTestMethodIncrease(), RU.INTEGER));
      f.setSlotValue("assertionChange", new Value(this.getTestAssertionIncrease(), RU.INTEGER));
      f.setSlotValue("byteChange", new Value(this.getFileSizeIncrease(), RU.INTEGER));

      assertedFact = engine.assertFact(f);
    }
    
    return assertedFact;
  }

  /**
   * Sets number of test methods to the edited test file.
   * 
   * @param value
   *          Number of test methods.
   */
  public void setNumOfTestMethods(int value) {
    this.numOfTestMethods = value;    
  }
  
  /**
   * Sets number of test assertions to the edited file.
   * 
   * @param value
   *          Number of test assertions.
   */
  public void setNumOfTestAssertions(int value) {
    this.numOfTestAssertions = value;
  }

  /**
   * Gets number of test methods.
   * 
   * @return Number of test methods.
   */
  public int getNumOfTestMethods() {
    return this.numOfTestMethods;
  }
  
  /**
   * Gets number of test assertions.
   * 
   * @return Number of test assertions.
   */
  public int getNumOfTestAssertions() {
    return this.numOfTestAssertions;
  }

  /**
   * Sets number of test method changes.
   * 
   * @param value Number of method changes.
   */
  public void setTestMethodIncrease(int value) {
    this.testMethodIncrease = value;
  }
  
  /**
   * Gets the number of test method change.
   * 
   * @return Number of test method change.
   */
  public int getTestMethodIncrease() {
    return this.testMethodIncrease;
  }
  
  /**
   * Sets number of test assertion change.
   * 
   * @param value
   *          Number of test assertions change.
   */
  public void setTestAssertionIncrease(int value) {
    this.testAssertionIncrease = value;
  }
  
  /**
   * Gets number of test assertion change.
   * 
   * @return Number of test assertion changes.
   */
  public int getTestAssertionIncrease() {
    return this.testAssertionIncrease;
  }
  
  /**
   * Gets unit test edit action string.
   * 
   * @return Unit test edit action string.
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append(super.toString());
    buf.append(" TEST {");
    
    // Method increase
    buf.append(makeMetricPair("MI", this.methodIncrease, this.numOfMethods)).append(", ");
    // Statement increase
    buf.append(makeMetricPair("SI", this.statementIncrease, this.numOfStatements)).append(", ");
    // Test method increase
    buf.append(makeMetricPair("TI", this.testMethodIncrease, this.numOfTestMethods)).append(", ");
    // Assertion increase
    buf.append(makeMetricPair("AI", this.testAssertionIncrease, this.numOfTestAssertions));
    buf.append(", ");
    // File size change.
    buf.append(super.makeMetricPair("FI", this.fileSizeIncrease, this.fileSize));

    buf.append("}");    
    return  buf.toString();
  }

  /**
   * Sets number of methods in test class.
   * 
   * @param numOfMethods
   *          Number of methods in test class.
   */
  public void setNumOfMethods(int numOfMethods) {
    this.numOfMethods = numOfMethods;    
  }

  /**
   * Gets number of methods in test class.
   * 
   * @return Number of methods in test class.
   */
  public int getNumOfMethods() {
    return this.numOfMethods;    
  }

  /**
   * Sets number of statements.
   * 
   * @param numOfStatements
   *          Number of statement in test class.
   */
  public void setNumOfStatements(int numOfStatements) {
    this.numOfStatements = numOfStatements;    
  }
  
  /**
   * Gets number of statements in test class.
   * 
   * @return Number of statements in test class.
   */
  public int getNumOfStatements() {
    return this.numOfStatements;
  }

  /**
   * Sets number of method increase in test class.
   * 
   * @param methodIncrease
   *          Number of method increase.
   */
  public void setMethodIncrease(int methodIncrease) {
    this.methodIncrease = methodIncrease;    
  }
  
  /**
   * Gets number of method increase in test class.
   * 
   * @return Method increase.
   */
  public int getMethodIncrease() {
    return this.methodIncrease;
  }

  /**
   * Sets statement increase for test class.
   * 
   * @param statementIncrease
   *          Number of statement increase.
   */
  public void setStatementIncrease(int statementIncrease) {
    this.statementIncrease = statementIncrease;    
  }
  
  /**
   * Gets number of statement increase for test class.
   * 
   * @return Statement increase.
   */
  public int getStatementIncrease() {
    return this.statementIncrease; 
  }
  
  /**
   * Checks unit test code edit.
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
//          (this.methodIncrease != 0 || this.statementIncrease != 0 || 
//           this.testMethodIncrease != 0 || this.testAssertionIncrease != 0);  
  return this.methodIncrease != 0 || this.statementIncrease != 0 || 
         this.testMethodIncrease != 0 || this.testAssertionIncrease != 0;  
  }

  
  /**
   * Action description is unit test file metrics.
   * 
   * @return Metric changes.
   */
  public String getActionDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(super.getDuration()).append("sec ");
    
    // Method increase
    buf.append(makeMetricPair("MI", this.methodIncrease, this.numOfMethods)).append(", ");
    // Statement increase
    buf.append(makeMetricPair("SI", this.statementIncrease, this.numOfStatements)).append(", ");
    // Test method increase
    buf.append(makeMetricPair("TI", this.testMethodIncrease, this.numOfTestMethods)).append(", ");
    // Assertion increase
    buf.append(makeMetricPair("AI", this.testAssertionIncrease, this.numOfTestAssertions));
    buf.append(", ");
    // File size change.
    buf.append(super.makeMetricPair("FI", this.fileSizeIncrease, this.fileSize));
    
    return buf.toString();
  }

  /**
   * Gets action type.
   * 
   * @return "TEST EDIT"
   */
  public String getActionType() {
    return "TEST EDIT";
  }
}