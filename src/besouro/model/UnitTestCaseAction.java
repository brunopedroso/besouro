package besouro.model;

import java.util.Date;
import java.util.StringTokenizer;

public class UnitTestCaseAction extends UnitTestAction {

	private String testcase;
	protected String failureMessage;
	
	public UnitTestCaseAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public UnitTestCaseAction(StringTokenizer tok) {
		super(tok);
	}

	public UnitTestCaseAction(Date date, String string, boolean b) {
		super(date, string, b);
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
	
}
