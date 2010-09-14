package besouro.model;

import java.io.File;
import java.util.Date;

import org.eclipse.core.resources.IResource;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

/**
 * Implements compilation error action.
 * @author Hongbing Kou
 */
public class CompilationAction extends ResourceAction {

	private String errMsg;

	public CompilationAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setErrorMessage(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrorMessage() {
		return this.errMsg;
	}

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact f = new Fact("CompilationAction", engine);
		f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
		f.setSlotValue(FILE_SLOT,new Value(this.getResource().getName(), RU.STRING));
		f.setSlotValue("message", new Value(this.getErrorMessage(), RU.STRING));
		Fact assertedFact = engine.assertFact(f);

		return assertedFact;
	}

	public String toString() {
		return getClock() + " COMPILE FAIL " + getResource() + "{" + this.errMsg + "}";
	}

	public String getActionColorEncoding() {
		return "yellow";
	}

	public String getActionDesc() {
		return this.errMsg;
	}

	public String getActionType() {
		return "COMPILE";
	}
}
