package athos.model;

import java.io.File;

public abstract class UnitTestAction extends FileAction {

	private boolean success = true;

	public UnitTestAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setSuccessValue(boolean success) {
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}

}