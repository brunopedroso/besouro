package besouro.model;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Defines unary refactoring action.
 * 
 * @author Hongbing Kou
 */
public class RefactoringAction extends ResourceAction {

	private String op;
	private String subjectType;
	private String subjectName;
	
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

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public String getSubjectName() {
		return this.subjectName;
	}

	public RefactoringAction(Date clock, String workspaceFile) {
		super(clock, workspaceFile);
	}

	public RefactoringAction(StringTokenizer tok) {
		super(tok);
		setOperator(tok.nextToken());
		setSubjectName(tok.nextToken());
		setSubjectType(tok.nextToken());
	}


	public String toString() {
		return super.toString() + " " + getOperator() + " " + getSubjectName() + " " + getSubjectType(); 
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
