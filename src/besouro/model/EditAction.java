package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IResource;

/**
 * Implements edit action on files.
 * @author Hongbing Kou
 */
public class EditAction extends JavaFileAction {

	public EditAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		if (this.isTestEdit()) {
			buf.append("SAVE TEST ");

		} else {
			buf.append("SAVE PRODUCTION ");
		}

		buf.append(getResource().getName());
		
		return buf.toString();
	}
	
}
