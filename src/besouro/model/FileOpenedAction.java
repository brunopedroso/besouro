package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IResource;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);

		setFileSize((int) workspaceFile.getLocation().toFile().length());

		// should calculate java metrics here?

	}

	@Override
	public String toString() {
		return new SimpleDateFormat("HH:mm:ss").format(getClock())
				+ " OPEN " + getResource().getName() 
				+ "(size: " + getFileSize() + ")"
				+ "(m: "  + getMethodsCount() + ")" 
				+ "(s: "  + getStatementsCount() + ")" 
				+ "(ta: " + getTestAssertionsCount() + ")" 
				+ "(ts: " + getTestMethodsCount() + ")";
	}

}
