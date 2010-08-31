package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

/**
 * Implements compilation error action.
 * 
 * @author Hongbing Kou
 * @version $Id: CompilationAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class CompilationAction extends FileAction {
	/** Compilation error. */
	private String errMsg;

	/**
	 * Instantiates an instance.
	 * 
	 * @param clock
	 *            Timestamp of the compliation action.
	 * @param workspaceFile
	 *            Active file.
	 */
	public CompilationAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);
	}

	/**
	 * Sets error message.
	 * 
	 * @param errMsg
	 *            Error message.
	 */
	public void setErrorMessage(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * Gets the compilation error message.
	 * 
	 * @return Compilation error message.
	 */
	public String getErrorMessage() {
		return this.errMsg;
	}

	/**
	 * Makes Jess facts in the given Jess rete engine.
	 * 
	 * @param index
	 *            Action index in episode.
	 * @param engine
	 *            Jess rete engine.
	 * @throws JessException
	 *             If error while constructing jess action.
	 * @return Jess fact of this action.
	 */
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact f = new Fact("CompilationAction", engine);
		f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
		f.setSlotValue(FILE_SLOT,
				new Value(this.getFile().getName(), RU.STRING));
		f.setSlotValue("message", new Value(this.getErrorMessage(), RU.STRING));
		Fact assertedFact = engine.assertFact(f);

		return assertedFact;
	}

	/**
	 * Gets the compilation action string.
	 * 
	 * @return Compilation action string.
	 */
	public String toString() {
		return super.toString() + " COMPILE {" + this.errMsg + "}";
	}

	/**
	 * Encode comiplation action with yellow as a warning sign.
	 * 
	 * @return Yellow.
	 */
	public String getActionColorEncoding() {
		return "yellow";
	}

	/**
	 * Action description is the compilation error message.
	 * 
	 * @return Compilation error message.
	 */
	public String getActionDesc() {
		return this.errMsg;
	}

	/**
	 * Action type is compilation.
	 * 
	 * @return Compilation error.
	 */
	public String getActionType() {
		return "COMPILE";
	}
}
