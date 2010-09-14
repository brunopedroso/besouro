package besouro.model;

import java.util.Date;

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
	
	
	public void setOperation(String op) {
		this.operation = op;

	}


}
