package besouro.model;

import java.util.Date;

import org.eclipse.core.resources.IResource;

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
