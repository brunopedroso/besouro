package besouro.model;

import java.io.File;
import java.util.Date;

import org.eclipse.core.resources.IResource;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class JavaFileAction extends ResourceAction {

	private int methodsCount;
	private int statementsCount;
	private int testAssertionsCount;
	private int testMethodsCount;
	private int fileSize = 0;
	private JavaFileAction previousAction;

	public JavaFileAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}


	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileSize() {
		return this.fileSize;
	}

	public int getMethodsCount() {
		return methodsCount;
	}

	public void setMethodsCount(int methodsCount) {
		this.methodsCount = methodsCount;
	}

	public int getStatementsCount() {
		return statementsCount;
	}

	public void setStatementsCount(int statementsCount) {
		this.statementsCount = statementsCount;
	}

	public int getTestAssertionsCount() {
		return testAssertionsCount;
	}

	public void setTestAssertionsCount(int testAssertionsCount) {
		this.testAssertionsCount = testAssertionsCount;
	}

	public int getTestMethodsCount() {
		return testMethodsCount;
	}

	public void setTestMethodsCount(int testMethodsCount) {
		this.testMethodsCount = testMethodsCount;
	}

	public void setPreviousAction(JavaFileAction previousAction) {
		this.previousAction = previousAction;
	}

	public JavaFileAction getPreviousAction() {
		return this.previousAction;
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

}
