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
		setSuccessValue("OK".equals(tok.nextToken()));
	}
	
	public UnitTestAction(Date date, String string, boolean result) {
		this(date, string);
		setSuccessValue(result);
	}

	public void setSuccessValue(boolean success) {
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + (this.isSuccessful()?"OK":"FAIL");
	}

}