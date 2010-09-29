package besouro.model;

import java.util.Date;
import java.util.List;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);

		
		//TODO   should calculate java metrics here?
//		setFileSize((int) workspaceFile.getLocation().toFile().length());


	}

	@Override
	public String toString() {
		return  "OPEN " + getResource();
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
