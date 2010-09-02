package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);

		setFileSize((int) workspaceFile.length());

		// should calculate java metrics here?

	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

	@Override
	public String toString() {
		return "FileOpened: " + getFile() + " (size: " + getFileSize() + ")"
				+ "(m: "  + getMethodsCount() + ")" 
				+ "(s: "  + getStatementsCount() + ")" 
				+ "(ta: " + getTestAssertionsCount() + ")" 
				+ "(ts: " + getTestMethodsCount() + ")";
	}

}
