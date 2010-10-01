package besouro.model;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Implements edit action on files.
 * @author Hongbing Kou
 */
public class EditAction extends JavaFileAction {

	public EditAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public EditAction(StringTokenizer tok) {
		super(tok);
	}

	public String toString() {
		
		return super.toString();
		
//		StringBuffer buf = new StringBuffer();
//
//		if (this.isTestEdit()) {
//			buf.append("SAVE TEST ");
//
//		} else {
//			buf.append("SAVE PRODUCTION ");
//		}
//
//		buf.append(getResource());
//		
//		return buf.toString();
	}
	
}
