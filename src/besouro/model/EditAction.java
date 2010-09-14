package besouro.model;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

import org.eclipse.core.resources.IResource;

/**
 * Implements edit action on files.
 * @author Hongbing Kou
 */
public class EditAction extends JavaFileAction {

	private String operation;

	public String getOperation() {
		return operation;
	}

	private int methodIncrease = 0;
	private int statementIncrease = 0;

	private int testMethodIncrease = 0;
	private int testAssertionIncrease = 0;

	private boolean isTestEdit;

	private int fileSizeIncrease;

	public EditAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getClock());

		if (this.isTestEdit()) {
			buf.append(" SAVE TEST ");
			buf.append(getResource().getName());
			buf.append(" {");
			buf.append(makeMetricPair("TI", getTestMethodIncrease(), getTestMethodsCount())).append(", ");
			buf.append(makeMetricPair("AI", getTestAssertionIncrease(), getTestAssertionsCount()));

		} else {
			buf.append(" SAVE PRODUCTION ");
			buf.append(getResource().getName());
			buf.append(" {");
		}

		buf.append(makeMetricPair("MI", getMethodIncrease(), getMethodsCount())).append(", ");
		buf.append(makeMetricPair("SI", getStatementIncrease(),getStatementsCount())).append(", ");
		buf.append(", ");
		buf.append(makeMetricPair("FI", getFileSizeIncrease(), getFileSize()));

		buf.append("}");
		return buf.toString();
	}

	protected String makeMetricPair(String name, int value, int total) {
		StringBuffer buf = new StringBuffer();
		buf.append(name);
		buf.append("=");
		if (value > 0) {
			buf.append("+");
		}
		buf.append(value);

		buf.append("(").append(total).append(")");
		return buf.toString();
	}

	public boolean isSubstantial() {

		if (isTestEdit) {
			return (this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0
					|| this.getTestMethodIncrease() != 0 || this.getTestAssertionIncrease() != 0);

		} else {
			return (this.getFileSizeIncrease() != 0 || this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0);
		}

	}

	// TODO [mod] assert facts in a 'classifier' object to 

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		
		Fact assertedFact = null;
		
		if (isSubstantial()) {
			Fact f;

			if (this.isTestEdit()) {
				f = new Fact("UnitTestEditAction", engine);
				f.setSlotValue("testChange", new Value(this.getTestMethodIncrease(), RU.INTEGER));
				f.setSlotValue("assertionChange", new Value(this.getTestAssertionIncrease(), RU.INTEGER));

			} else {
				f = new Fact("ProductionEditAction", engine);
				f.setSlotValue("methodChange", new Value(this.getMethodIncrease(), RU.INTEGER));
				f.setSlotValue("statementChange", new Value(this.getStatementIncrease(), RU.INTEGER));
			}

			f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
			f.setSlotValue(FILE_SLOT, new Value(this.getResource().getName(), RU.STRING));
			f.setSlotValue("byteChange", new Value(this.getFileSizeIncrease(), RU.INTEGER));

			assertedFact = engine.assertFact(f);
		}

		return assertedFact;
	}

	public boolean isTestEdit() {
		return isTestEdit;
	}

	public void setIsTestEdit(boolean isTestEdit) {
		this.isTestEdit = isTestEdit;
	}

	public int getFileSizeIncrease() {
		if (getPreviousAction() != null) {
			return this.getFileSize() - getPreviousAction().getFileSize();
		} else {
			return fileSizeIncrease;

		}
	}

	// usefull for test
	public void setFileSizeIncrease(int size) {
		fileSizeIncrease = size;
	}

	public void setOperation(String op) {
		this.operation = op;

	}

	public void setTestMethodIncrease(int value) {
		this.testMethodIncrease = value;
	}

	public int getTestMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestMethodsCount() - getPreviousAction().getTestMethodsCount();
		}
		return this.testMethodIncrease;
	}

	public void setTestAssertionIncrease(int value) {
		this.testAssertionIncrease = value;
	}

	public int getTestAssertionIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestAssertionsCount() - getPreviousAction().getTestAssertionsCount();
		}
		return this.testAssertionIncrease;
	}

	public void setMethodIncrease(int methodIncrease) {
		this.methodIncrease = methodIncrease;
	}

	public int getMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getMethodsCount() - getPreviousAction().getMethodsCount();
		}
		return this.methodIncrease;
	}

	public void setStatementIncrease(int statementIncrease) {
		this.statementIncrease = statementIncrease;
	}

	public int getStatementIncrease() {
		if (getPreviousAction() != null) {
			return this.getStatementsCount() - getPreviousAction().getStatementsCount();
		}
		return this.statementIncrease;
	}

}