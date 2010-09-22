package besouro.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

		if (this.isTestEdit()) {
			buf.append("SAVE TEST ");

		} else {
			buf.append("SAVE PRODUCTION ");
		}

		buf.append(getResource().getName());
		
		return buf.toString();
	}
	
	@Override
	public java.util.List<String> getActionDetails() {
		List<String> list = super.getActionDetails();
		if (this.isTestEdit()) {
			list.add(makeMetricPair("TestIncrease: ", getTestMethodIncrease(), getTestMethodsCount()));
			list.add(makeMetricPair("AssertionIncrease: ", getTestAssertionIncrease(), getTestAssertionsCount()));
			
		}
		list.add(makeMetricPair("MethodsIncrease: ", getMethodIncrease(), getMethodsCount()));
		list.add(makeMetricPair("StatementsIncrease: ", getStatementIncrease(),getStatementsCount()));
		list.add(makeMetricPair("FileSizeIncrease: ", getFileSizeIncrease(), getFileSize()));
		return list;
	};

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
