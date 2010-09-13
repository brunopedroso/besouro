package besouro.model.refactor;

import java.io.File;

import besouro.model.Clock;
import besouro.model.FileAction;


/**
 * Implements action regarding to refactoring on java class, method etc.
 * 
 * @author Hongbing Kou
 * @version $Id: RefactorAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public abstract class RefactorAction extends FileAction {

	private String op;
	private String subjectType;

	public RefactorAction(Clock clock, File workspceFile) {
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
