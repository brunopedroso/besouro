package besouro.model;

import java.util.Date;

/**
 * Implements edit action on files.
 * @author Hongbing Kou
 */
public class EditAction extends JavaFileAction {

	public EditAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		if (this.isTestEdit()) {
			buf.append("SAVE TEST ");

		} else {
			buf.append("SAVE PRODUCTION ");
		}

		buf.append(getResource());
		
		return buf.toString();
	}
	
}
