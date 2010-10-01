package besouro.model;

import java.util.Date;
import java.util.StringTokenizer;

public class UnitTestSessionAction extends UnitTestAction {

	public UnitTestSessionAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public UnitTestSessionAction(StringTokenizer tok) {
		super(tok);
	}

	@Override
	public String toString() {
		return super.toString() + " " + (this.isSuccessful()?"OK":"FAIL");
	}
	
}
