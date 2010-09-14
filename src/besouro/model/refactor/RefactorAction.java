package besouro.model.refactor;

import java.util.Date;

import org.eclipse.core.resources.IResource;

import besouro.model.ResourceAction;


/**
 * Implements action regarding to refactoring on java class, method etc.
 * @author Hongbing Kou
 */
public abstract class RefactorAction extends ResourceAction {

	private String op;
	private String subjectType;

	public RefactorAction(Date clock, IResource workspceFile) {
		super(clock, workspceFile);
	}

	public void setOperator(String op) {
		this.op = op;
	}

	public String getOperator() {
		return this.op;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public String toString() {
		return super.toString() + " REFACTOR " + this.getOperator() + " " + this.getSubjectType();
	}
}
