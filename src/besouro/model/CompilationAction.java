package besouro.model;

import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IResource;

/**
 * Implements compilation error action.
 * @author Hongbing Kou
 */
public class CompilationAction extends ResourceAction {

	private String errMsg;

	public CompilationAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setErrorMessage(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrorMessage() {
		return this.errMsg;
	}

	public String toString() {
		return "COMPILE FAIL in " + getResource();
	}
	
	@Override
	public List<String> getActionDetails() {
		List<String> list = super.getActionDetails();
		list.add(this.errMsg);
		return list;
	};

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
