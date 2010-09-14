package besouro.model;

import java.io.File;

import org.eclipse.core.resources.IResource;

public abstract class UnitTestAction extends ResourceAction {

	private boolean success = true;

	public UnitTestAction(Clock clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setSuccessValue(boolean success) {
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}

}