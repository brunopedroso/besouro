package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IResource;


/**
 * Defines unary refactoring action.
 * 
 * @author Hongbing Kou
 */
public class RefactoringAction extends ResourceAction {

	private String op;
	private String subjectType;
	
	public String getOperator() {
		return op;
	}

	public void setOperator(String op) {
		this.op = op;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	private String subjectName;

	public RefactoringAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public String toString() {
		return " REFACTORNG " + getActionType() + " in " + getResource().getName();
	}

	@Override
	public List<String> getActionDetails() {
		List<String> details = super.getActionDetails();
		details.add("subject: " + this.subjectName);
		details.add("type: " + getActionType());
		details.add("value: " + getActionValue());
		return details;
	}
	
	public String getActionDesc() {
		return this.getSubjectName();
	}

	public String getActionType() {
		return this.getOperator() + " " + this.getSubjectType();
	}
}
