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

  private boolean success = true;
  private String testcase;
  private String failureMessage;
  
  public UnitTestAction(Clock clock, File workspaceFile) {
    super(clock, workspaceFile);
  }

  public void setSuccessValue(boolean success) {
    this.success = success;    
  }

  public boolean isSuccessful() {
    return this.success;
  }

  public void setTestCase(String testcase) {
    this.testcase = testcase;
  }
  
  public String getTestCase() {
    return this.testcase;
  }

  public void setFailureMessage(String failureMessage) {
    this.success = false;
    this.failureMessage = failureMessage;
  }
  
  public String getFailureMessage() {
    return this.failureMessage;
  }
  
  public Fact assertJessFact(int index, Rete engine) throws JessException  {
    Fact f = new Fact("UnitTestAction", engine);
    f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
    
    //TODO [2] organize the file representation all over the program
    f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));
    
    
    if (!this.isSuccessful()) {
      f.setSlotValue("errmsg", new Value("" + (this.success?"true":"failure"), RU.STRING));
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
