package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IResource;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);

		setFileSize((int) workspaceFile.getLocation().toFile().length());

		// should calculate java metrics here?

	}

	@Override
	public String toString() {
		return  "OPEN " + getResource().getName();
	}
	
	@Override
	public List<String> getActionDetails() {
		List<String> details = super.getActionDetails();
		details.add("size: " + getFileSize());
		details.add("methods: "  + getMethodsCount());
		details.add("statements: "  + getStatementsCount()); 
		details.add("tests: " + getTestMethodsCount());
		details.add("asserts: " + getTestAssertionsCount()); 
		return details;
	}

}
