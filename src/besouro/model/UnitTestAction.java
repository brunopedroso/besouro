package besouro.model;

import java.util.Date;
import java.util.StringTokenizer;

public abstract class UnitTestAction extends ResourceAction {

	private boolean success = true;

	public UnitTestAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public UnitTestAction(StringTokenizer tok) {
		super(tok);
	}
	
	

	public void setSuccessValue(boolean success) {
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}

}