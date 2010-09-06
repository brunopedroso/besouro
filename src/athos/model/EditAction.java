package athos.model;

import java.io.File;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

/**
 * Implements edit action on files.
 * 
 * @author Hongbing Kou
 * @version $Id: EditAction.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class EditAction extends JavaFileAction {

	private String operation;

	public String getOperation() {
		return operation;
	}

	private String unitName;

	private int methodIncrease = 0;
	private int statementIncrease = 0;

	private int testMethodIncrease = 0;
	private int testAssertionIncrease = 0;

	private boolean isTestEdit;

	private int fileSizeIncrease;

	private long duration;

	public EditAction(Clock clock, File workspaceFile) {
		super(clock, workspaceFile);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getClock());

		if (this.isTestEdit()) {
			buf.append(" SAVE TEST ");
			buf.append(getFile().getName());
			buf.append(" {");
			buf.append(makeMetricPair("TI", getTestMethodIncrease(), getTestMethodsCount())).append(", ");
			buf.append(makeMetricPair("AI", getTestAssertionIncrease(), getTestAssertionsCount()));

		} else {
			buf.append(" SAVE PRODUCTION ");
			buf.append(getFile().getName());
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
			// ignoring the duration. Look a litle down
			// return this.getDuration() > 0 &&
			return (this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0
					|| this.getTestMethodIncrease() != 0 || this.getTestAssertionIncrease() != 0);

		} else {
			// ignoring the duration. Look a litle down
			// return this.getDuration() > 0 &&
			return (this.getFileSizeIncrease() != 0 || this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0);
		}

	}

	// TODO [mod] create a classifier interface

	public Fact assertJessFact(int index, Rete engine) throws JessException {
		Fact assertedFact = null;
		if (isSubstantial()) {
			Fact f;

			if (this.isTestEdit()) {
				f = new Fact("UnitTestEditAction", engine);
				f.setSlotValue("testChange",
						new Value(this.getTestMethodIncrease(), RU.INTEGER));
				f.setSlotValue("assertionChange",
						new Value(this.getTestAssertionIncrease(), RU.INTEGER));

			} else {
				f = new Fact("ProductionEditAction", engine);
				f.setSlotValue("methodChange",
						new Value(this.getMethodIncrease(), RU.INTEGER));
				f.setSlotValue("statementChange",
						new Value(this.getStatementIncrease(), RU.INTEGER));
			}

			f.setSlotValue(INDEX_SLOT, new Value(index, RU.INTEGER));
			f.setSlotValue(FILE_SLOT, new Value(this.getFile().getName(),
					RU.STRING));
			// f.setSlotValue("duration", new Value(this.getDuration(),
			// RU.INTEGER));
			f.setSlotValue("byteChange", new Value(this.getFileSizeIncrease(),
					RU.INTEGER));

			assertedFact = engine.assertFact(f);
		}

		return assertedFact;
	}

	// I've droped this concept. Hongbing used it just for classifying an action
	// as substancial...
	// Im gonna consider all durations as substantial...

	// @Override
	// public int getDuration(){
	//
	// if (previousAction != null) {
	// long thisTimestamp = getClock().getDate().getTime();
	// long previousTimestamp = previousAction.getClock().getDate().getTime();
	// duration = (thisTimestamp - previousTimestamp)/1000;
	// previousTimestamp = thisTimestamp;
	// }
	//
	// return (int) duration;
	//
	// }
	//
	// @Override
	// public void setDuration(int d){
	// this.duration = d;
	// }

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

	public void setUnitName(String name) {
		this.unitName = name;

	}

	public void setTestMethodIncrease(int value) {
		this.testMethodIncrease = value;
	}

	public int getTestMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestMethodsCount()
					- getPreviousAction().getTestMethodsCount();
		}
		return this.testMethodIncrease;
	}

	public void setTestAssertionIncrease(int value) {
		this.testAssertionIncrease = value;
	}

	public int getTestAssertionIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestAssertionsCount()
					- getPreviousAction().getTestAssertionsCount();
		}
		return this.testAssertionIncrease;
	}

	public void setMethodIncrease(int methodIncrease) {
		this.methodIncrease = methodIncrease;
	}

	public int getMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getMethodsCount()
					- getPreviousAction().getMethodsCount();
		}
		return this.methodIncrease;
	}

	public void setStatementIncrease(int statementIncrease) {
		this.statementIncrease = statementIncrease;
	}

	public int getStatementIncrease() {
		if (getPreviousAction() != null) {
			return this.getStatementsCount()
					- getPreviousAction().getStatementsCount();
		}
		return this.statementIncrease;
	}

	/**
	 * Checks whether this edit work makes any progress.
	 * 
	 * @return True if there is any or false otherwise.
	 */
	// public abstract boolean hasProgress();

	/**
	 * Checks whether this edit work is significant.
	 * 
	 * @return True if it is and false otherwise.
	 */
	// public abstract boolean isSubstantial();
}
