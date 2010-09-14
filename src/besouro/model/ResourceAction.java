package besouro.model;

import java.util.Date;

import org.eclipse.core.resources.IResource;

/**
 * Defines generic software development actions, which is an independent
 * activity taken by software developer. For instance, unit test creation,
 * invocation, file edit, refactoring etc are all actions in Zorro system.
 * 
 * @author Hongbing Kou
 */
public abstract class ResourceAction extends Action {
	
	private IResource resource;

	protected static final String INDEX_SLOT = "index";
	protected static final String FILE_SLOT = "file";

	public ResourceAction(Date clock, IResource workspaceFile) {
		super(clock);
		this.resource = workspaceFile;
	}

	public IResource getResource() {
		return this.resource;
	}

	public String toString() {
		return super.getClock() + " " + this.resource.getName();
	}

	public String getActionValue() {
		return this.resource.getName();
	}
}
