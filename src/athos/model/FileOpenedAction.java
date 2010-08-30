package athos.model;

import java.io.File;

import athos.listeners.JavaStatementMeter;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class FileOpenedAction extends FileAction {

	private int numOfMethods;
	private int numOfStatements;
	private int numOfTestAssertions;
	private int numOfTestMethods;

	public FileOpenedAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);
		
		// should calculate java metrics here?
		
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

	public int getNumOfMethods() {
		return numOfMethods;
	}

	public int getNumOfStatements() {
		return numOfStatements;
	}

	public int getNumOfTestAssertions() {
		return numOfTestAssertions;
	}

	public int getNumOfTestMethods() {
		return this.numOfTestMethods;
	}

	public void setNumOfMethods(int numOfMethods) {
		this.numOfMethods = numOfMethods;
	}

	public void setNumOfStatements(int numOfStatements) {
		this.numOfStatements = numOfStatements;
	}

	public void setNumOfTestAssertions(int numOfTestAssertions) {
		this.numOfTestAssertions = numOfTestAssertions;
	}

	public void setNumOfTestMethods(int numOfTestMethods) {
		this.numOfTestMethods = numOfTestMethods;
	}


}
