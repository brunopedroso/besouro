package athos.model;

import java.io.File;

/**
 * Defines generic software development actions, which is an independent
 * activity taken by software developer. For instance, unit test creation,
 * invocation, file edit, refactoring etc are all actions in Zorro system.
 * 
 * @author Hongbing Kou
 * @version $Id: FileAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public abstract class FileAction extends Action implements Comparable {
	/** Action workspace file. */
	private File workspaceFile;

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
	public FileAction(Clock clock, File workspaceFile) {
		super(clock);
		this.workspaceFile = workspaceFile;
	}

	/**
	 * Gets the target file.
	 * 
	 * @return Target file.
	 */
	public File getFile() {
		return this.workspaceFile;
	}

	/**
	 * Gets action string.
	 * 
	 * @return Action string.
	 */
	public String toString() {
		return super.getClock() + " " + this.workspaceFile.getName();
	}

	/**
	 * Action value if file name to FileAction.
	 * 
	 * @return File name.
	 */
	public String getActionValue() {
		return this.workspaceFile.getName();
	}
}
