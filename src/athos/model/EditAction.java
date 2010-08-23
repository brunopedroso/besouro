package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.Rete;




/**
 * Implements edit action on files.
 * 
 * @author Hongbing Kou
 * @version $Id: EditAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class EditAction extends FileAction {
  /** Duration in seconds. */
  private int duration;
  /** File size with this edit. */
  protected int fileSize = 0;
  /** File size increase. It could be either positive or negative. */
  protected int fileSizeIncrease = 0;
  
  private String operation;
  private String unitName;
private int currentTestMethods;
private int currentTestAssertions;
private int currentStatements;
private int currentMethods;
private int currentSize;
  
  /**
   * Constructs an edit action on a file.
   * 
   * @param clock Time action occurs.
   * @param workspaceFile Associated file.
   * @param duration Duration of edit on the given file.
   */
  public EditAction(Clock clock, File workspaceFile, int duration) {
    super(clock, workspaceFile);
    this.duration = duration;
  }

  /**
   * Set the size of the file beding edited. 
   * 
   * @param fileSize Size of the file being edited.
   */
  public void setFileSize(int fileSize) {
    this.fileSize = fileSize;
  }
  
  /**
   * Gets edited file size.
   *  
   * @return Size of the file is edited.
   */
  public int getFileSize() {
    return this.fileSize;
  }
  
  /**
   * Sets file size increase with this edit action. 
   * 
   * @param increase Increase of file size.
   */
  public void setFileSizeIncrease(int increase) {
    this.fileSizeIncrease = increase; 
  }
  
  /**
   * Gets file size increase with this edit action.
   * 
   * @return File size increase.
   */
  public int getFileSizeIncrease() {
    return this.fileSizeIncrease;
  }
  
  /**
   * Sets duration of this action. 
   * 
   * @param duration Duration of this action in number of seconds.
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Gets duration of action in seconds.
   * 
   * @return Duration in seconds.
   */
  public int getDuration() {
    return this.duration;
  }
  
  /**
   * Gets edit action string.
   * 
   * @return Edit action string. 
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append(super.toString());
    buf.append(" EDIT ");
    buf.append(this.duration  + "s ");
    buf.append(this.operation + " ");
    buf.append(this.unitName  + " ");
    return buf.toString();
  }
  
  /**
   * Gets metric name=value string pair.
   * @param name Metric name
   * @param value value of the metric.
   * @param total Total value of the metric.
   * @return Metric name=value(total) pair.
   */
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

@Override
public Fact assertJessFact(int index, Rete engine) throws JessException {
	throw new RuntimeException("implement this");
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

public void setCurrentSize(int value) {
	currentSize = value;
	
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
