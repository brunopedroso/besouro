package besouro.model;

import java.util.Date;

public class UnitTestSessionAction extends UnitTestAction {

	public UnitTestSessionAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public String toString() {
		return super.toString() + (this.isSuccessful()?"OK":"FAIL");
	}
	
}
