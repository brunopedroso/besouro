package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;


/**
 * Tests unit test action.
 * 
 * @author Hongbing Kou
 * @version $Id: UnitTestAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class UnitTestAction extends FileAction {
  /** Success value. */
  private String success = "true";
  /** Test-case name. */
  private String testcase;
  /** Failure message. */
  private String failureMessage;
  
  /**
   * Instantiates an instance.
   * 
   * @param clock Timestamp of the unit test action.
   * @param workspaceFile Test case name.
   */
  public UnitTestAction(Clock clock, File workspaceFile) {
    super(clock, workspaceFile);
  }

  /**
   * Sets unit test execution result. 
   *  
   * @param success Success value string.
   */
  public void setSuccessValue(String success) {
    this.success = success;    
  }

  /**
   * Check test succeeds or not.
   * 
   * @return True if test succeeds.
   */
  public boolean isSuccessful() {
    return "true".equals(this.success) || "pass".equals(this.success);
  }

  /**
   * Sets test case name.
   * 
   * @param testcase Name of the test case. 
   */
  public void setTestCase(String testcase) {
    this.testcase = testcase;
  }
  
  /**
   * Gets test case name.
   * 
   * @return Name of the test case.
   */
  public String getTestCase() {
    return this.testcase;
  }
  
  /**
   * Sets unit test failure message. 
   * 
   * @param failureMessage Failure message if this unit test fails.
   */
  public void setFailureMessage(String failureMessage) {
    this.success = "failure";
    this.failureMessage = failureMessage;
  }
  
  /**
   * Gets unit test failure message. 
   * 
   * @return Failure message if this unit test unit fails.
   */
  public String getFailureMessage() {
    return this.failureMessage;
  }
  
  /**
   * Makes Jess facts in the given Jess rete engine.
   * 
   * @param index Action index in episode. 
   * @param engine Jess rete engine.
   * @throws JessException If error while constructing jess action.
   * @return Jess fact of this action.
   */
  public Fact assertJessFact(int index, Rete engine) throws JessException  {
    Fact f = new Fact("UnitTestAction", engine);
    f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
    f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));
    if (!this.isSuccessful()) {
      f.setSlotValue("errmsg", new Value(this.success, RU.STRING));
    }
    
    Fact assertedFact = engine.assertFact(f);
    return assertedFact;
  }  
  
  /**
   * Gets unit test action string.
   * 
   * @return Unit test action string. 
   */
  public String toString() {
    if (this.isSuccessful()) {
      return super.toString() + " TEST OK";
    }
    else {
      return super.toString() + " TEST FAILED";
    }
  }

  /**
   * Encode unit test success in green and failure in red.
   * 
   * @return Green or red.
   */
  public String getActionColorEncoding() {
    if (this.isSuccessful()) {
      return "green";
    }
    else {
      return "red";
    }  
  }
  
  /**
   * Gets unit test invocation result..
   * 
   * @return OK or failure.
   */
  public String getActionDesc() {
    if (this.isSuccessful()) {
      return "TEST OK";
    }
    else {
      return "TEST FAILED";
    }
  }

  /**
   * Action type is unit test.
   * 
   * @return "UNIT TEST"
   */
  public String getActionType() {
    return "UNIT TEST";
  }
}
