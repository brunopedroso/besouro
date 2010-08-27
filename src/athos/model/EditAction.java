package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;




/**
 * Implements edit action on files.
 * 
 * @author Hongbing Kou
 * @version $Id: EditAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class EditAction extends FileAction {
	
  private int fileSize = 0;
  //TODO [1] increases
  private int fileSizeIncrease = 0;
  
  private String operation;
  public String getOperation() {
	return operation;
}

private String unitName;
  
  private int methodIncrease = 0;
  private int statementIncrease = 0;
  
  private int testMethodIncrease = 0;
  private int testAssertionIncrease = 0;

  private int currentTestMethods;
  private int currentTestAssertions;
  private int currentStatements;
  private int currentMethods;

  private boolean isTestEdit;
  

  public EditAction(Clock clock, File workspaceFile, int duration) {
    super(clock, workspaceFile, duration);
  }
  
  	  
  	  public String toString() {
  	    StringBuffer buf = new StringBuffer();
  	    buf.append(super.toString());
  	    
        if (this.isTestEdit()) {
        	buf.append(" TEST {");
        	buf.append(makeMetricPair("TI", this.testMethodIncrease, getCurrentTestMethods())).append(", ");
        	buf.append(makeMetricPair("AI", this.testAssertionIncrease, getCurrentTestAssertions()));
        	
        } else {
        	buf.append(" PRODUCTION {");
        }
  	    
  	    buf.append(makeMetricPair("MI", getMethodIncrease(), getCurrentMethods())).append(", ");
  	    buf.append(makeMetricPair("SI", getStatementIncrease(), getCurrentStatements())).append(", ");
  	    buf.append(", ");
  	    buf.append(makeMetricPair("FI", getFileSizeIncrease(), getFileSize()));

  	    buf.append("}");    
  	    return  buf.toString();
  	  }


  	  protected String makeMetricPair(String name, int value, int total) {
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    if (value > 0) {
	      buf.append("+");
	    }
	    buf.append(value);
	    
	    buf.append("(").append(total).append(")");    
	    return buf.toString();
	  }
  	  
  	  
  	  public boolean isSubstantial() {
  		  
  		  //TODO [1] increases
//  		  return true;
//  		  return getMethodIncrease() != 0 || getStatementIncrease() != 0 || 
//  	         this.testMethodIncrease != 0 || this.testAssertionIncrease != 0;

  		  if (isTestEdit)
  			  return this.getDuration() > 0 && 
  			  	(this.methodIncrease != 0 || this.statementIncrease != 0 || 
  			  	 this.testMethodIncrease != 0 || this.testAssertionIncrease != 0);
  			  
  		  else
  			  return this.getDuration() > 0 && 
  			  	(this.getFileSizeIncrease() != 0 || this.methodIncrease != 0 || 
  			     this.statementIncrease != 0);
  		   
  		  
  		  

  		  
  	  }


	  //TODO [api] create a classifier interface
	  
	  public Fact assertJessFact(int index, Rete engine) throws JessException  {
	    Fact assertedFact = null;
	    if (isSubstantial()) {
	      Fact f;
	      
	      if (this.isTestEdit()) {
	    	  f = new Fact("UnitTestEditAction", engine);
	          f.setSlotValue("testChange", new Value(this.getTestMethodIncrease(), RU.INTEGER));
	          f.setSlotValue("assertionChange", new Value(this.getTestAssertionIncrease(), RU.INTEGER));

	      } else {
	    	  f = new Fact("ProductionEditAction", engine);
	    	  f.setSlotValue("methodChange", new Value(this.getMethodIncrease(), RU.INTEGER));
	    	  f.setSlotValue("statementChange", new Value(this.getStatementIncrease(), RU.INTEGER));
	      }
	      
	      f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
	      f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(), RU.STRING));
	      f.setSlotValue("duration", new Value(this.getDuration(), RU.INTEGER));
	      f.setSlotValue("byteChange", new Value(this.getFileSizeIncrease(), RU.INTEGER));
	      
	      assertedFact = engine.assertFact(f);
	    }
	    
	    return assertedFact;
	  }  
	  
	  
  public boolean isTestEdit() {
	return isTestEdit;
  }

  public void setIsTestEdit(boolean isTestEdit) {
		this.isTestEdit = isTestEdit;
	}

	  
	  
  public void setFileSize(int fileSize) {
    this.fileSize = fileSize;
  }
  
  public int getFileSize() {
    return this.fileSize;
  }
  
  public void setFileSizeIncrease(int increase) {
    this.fileSizeIncrease = increase; 
  }
  
  public int getFileSizeIncrease() {
    return this.fileSizeIncrease;
  }
  
  

public void setOperation(String op) {
	this.operation = op;
	
}

public void setUnitName(String name) {
	this.unitName = name;
	
}



public void setCurrentTestMethods(int value) {
	currentTestMethods = value;
}

public void setCurrentTestAssertions(int value) {
	currentTestAssertions = value;
	
}

public void setCurrentStatements(int value) {
	currentStatements = value;
	
}

public void setCurrentMethods(int value) {
	currentMethods = value;
	
}


public int getCurrentTestMethods() {
	return currentTestMethods;
}

public int getCurrentTestAssertions() {
	return currentTestAssertions;
}

public int getCurrentStatements() {
	return currentStatements;
}

public int getCurrentMethods() {
	return currentMethods;
}


public void setTestMethodIncrease(int value) {
    this.testMethodIncrease = value;
  }
  
  public int getTestMethodIncrease() {
    return this.testMethodIncrease;
  }
  
  public void setTestAssertionIncrease(int value) {
    this.testAssertionIncrease = value;
  }
  
  public int getTestAssertionIncrease() {
    return this.testAssertionIncrease;
  }



public void setMethodIncrease(int methodIncrease) {
  this.methodIncrease = methodIncrease;    
}

public int getMethodIncrease() {
  return this.methodIncrease;
}

public void setStatementIncrease(int statementIncrease) {
  this.statementIncrease = statementIncrease;    
}

public int getStatementIncrease() {
  return this.statementIncrease; 
}

  /**
   * Checks whether this edit work makes any progress.
   * 
   * @return True if there is any or false otherwise.
   */
//  public abstract boolean hasProgress();

  /**
   * Checks whether this edit work is significant.
   * 
   * @return True if it is and false otherwise.
   */
//  public abstract boolean isSubstantial();
}
