package besouro.model;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

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

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact f = new Fact("UnitTestAction", engine);
		f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));

		f.setSlotValue(FILE_SLOT,new Value(this.getResource().getName(), RU.STRING));

		if (!this.isSuccessful()) {
			f.setSlotValue("errmsg", new Value(this.isSuccessful() ? "true" : "failure", RU.STRING));
		}

		Fact assertedFact = engine.assertFact(f);
		return assertedFact;
	}

	public String toString() {
		if (this.isSuccessful()) {
			return getClock() + " TEST CASE - OK " + getResource();
		} else {
			return getClock() + " TEST CASE - FAILED " + getResource();
		}
	}
	
}
