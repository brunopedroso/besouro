package besouro.model.refactor;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

import org.eclipse.core.resources.IResource;

/**
 * Defines unary refactoring action.
 * 
 * @author Hongbing Kou
 */
public class UnaryRefactorAction extends RefactorAction {

	private String subjectName;

	public UnaryRefactorAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact f = new Fact("UnaryRefactorAction", engine);
		f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
		f.setSlotValue(FILE_SLOT,new Value(this.getResource().getName(), RU.STRING));

		f.setSlotValue("operation", new Value(this.getOperator(),RU.STRING));
		f.setSlotValue("type", new Value(this.getSubjectType(),RU.STRING));
		f.setSlotValue("data", new Value(this.getSubjectName(), RU.STRING));

		Fact assertedFact = engine.assertFact(f);
		return assertedFact;
	}

	public String toString() {
		return getClock() + " REFACTORNG " + getActionType() + " in " + getResource().getName() + " {" + this.subjectName + "}";
	}

	public String getActionDesc() {
		return this.getSubjectName();
	}

	public String getActionType() {
		return this.getOperator() + " " + this.getSubjectType();
	}
}
