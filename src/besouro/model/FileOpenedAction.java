package besouro.model;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class FileOpenedAction extends JavaFileAction {

	public FileOpenedAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public FileOpenedAction(StringTokenizer tok) {
		super(tok);
	}

	@Override
	public String toString() {
		return  super.toString();
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
