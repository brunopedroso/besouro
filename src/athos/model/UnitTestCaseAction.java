package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

public class UnitTestCaseAction extends UnitTestAction {

	private String testcase;
	protected String failureMessage;
	
	public UnitTestCaseAction(Clock clock, File workspaceFile) {
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

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact f = new Fact("UnitTestAction", engine);
		f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));

		// TODO [clean] organize the file representation all over the program
		f.setSlotValue(FILE_SLOT,
				new Value(this.getFile().getName(), RU.STRING));

		if (!this.isSuccessful()) {
			f.setSlotValue("errmsg", new Value(""
					+ (this.isSuccessful() ? "true" : "failure"), RU.STRING));
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
		} else {
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
		} else {
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
		} else {
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