package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IResource;

public class UnitTestCaseAction extends UnitTestAction {

	private String testcase;
	protected String failureMessage;
	
	public UnitTestCaseAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setTestCase(String testcase) {
		this.testcase = testcase;
	}

	public String getTestCase() {
		return this.testcase;
	}

	public void setFailureMessage(String failureMessage) {
		this.setSuccessValue(false);
		this.failureMessage = failureMessage;
	}

	public String getFailureMessage() {
		return this.failureMessage;
	}

	public String toString() {
		String formatedDate = new SimpleDateFormat("HH:mm:ss").format(getClock());
		if (this.isSuccessful()) {
			return formatedDate + " TEST CASE - OK " + getResource();
		} else {
			return formatedDate + " TEST CASE - FAILED " + getResource();
		}
	}
	
}
