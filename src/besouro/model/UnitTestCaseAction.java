package besouro.model;

import java.util.Date;

public class UnitTestCaseAction extends UnitTestAction {

	private String testcase;
	protected String failureMessage;
	
	public UnitTestCaseAction(Date clock, String workspaceFile) {
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
		if (this.isSuccessful()) {
			return "TEST CASE - OK " + getResource();
		} else {
			return "TEST CASE - FAILED " + getResource();
		}
	}
	
}
