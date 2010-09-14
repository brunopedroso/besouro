package besouro.model;

import java.io.File;
import java.util.Date;

import org.eclipse.core.resources.IResource;

/**
 * Defines generic software development actions, which is an independent
 * activity taken by software developer. For instance, unit test creation,
 * invocation, file edit, refactoring etc are all actions in Zorro system.
 * 
 * @author Hongbing Kou
 * @version $Id: FileAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public abstract class ResourceAction extends Action implements Comparable {
	/** Action workspace file. */
	private IResource resource;

	/** Action index. */
	protected static final String INDEX_SLOT = "index";
	/** Action file. */
	protected static final String FILE_SLOT = "file";

	/**
	 * Constructs an action associated with file in certain time.
	 * 
	 * @param clock
	 *            Clock when action happens.
	 * @param workspaceFile
	 *            Workspace file being worked on.
	 */
	public ResourceAction(Date clock, IResource workspaceFile) {
		super(clock);
		this.resource = workspaceFile;
	}

	/**
	 * Gets the target file.
	 * 
	 * @return Target file.
	 */
	public IResource getResource() {
		return this.resource;
	}

	/**
	 * Gets action string.
	 * 
	 * @return Action string.
	 */
	public String toString() {
		return super.getClock() + " " + this.resource.getName();
	}

	/**
	 * Action value if file name to FileAction.
	 * 
	 * @return File name.
	 */
	public String getActionValue() {
		return this.resource.getName();
	}
}
