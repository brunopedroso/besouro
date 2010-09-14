package besouro.model;

import java.io.File;

import org.eclipse.core.resources.IResource;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Clock clock, IResource workspaceFile) {
		super(clock, workspaceFile);

		setFileSize((int) workspaceFile.getLocation().toFile().length());

		// should calculate java metrics here?

	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

	@Override
	public String toString() {
		return getClock() + " OPEN " + getResource().getName() 
				+ "(size: " + getFileSize() + ")"
				+ "(m: "  + getMethodsCount() + ")" 
				+ "(s: "  + getStatementsCount() + ")" 
				+ "(ta: " + getTestAssertionsCount() + ")" 
				+ "(ts: " + getTestMethodsCount() + ")";
	}

}
