package athos.model.refactor;

import java.util.HashMap;

/**
 * Defines refactor operators.
 * 
 * @author Hongbing Kou
 * @version $Id: RefactorOperator.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class RefactorOperator {
	/** Add operator. */
	public static final RefactorOperator ADD = new RefactorOperator("ADD");
	/** Remove operator. */
	public static final RefactorOperator REMOVE = new RefactorOperator("REMOVE");
	/** Move operator. */
	public static final RefactorOperator MOVE = new RefactorOperator("MOVE");
	/** Rename operator. */
	public static final RefactorOperator RENAME = new RefactorOperator("RENAME");

	private static HashMap<String, RefactorOperator> operators;

	/** Refactor operator. */
	private String name;

	/**
	 * Instantiates an operator.
	 * 
	 * @param name
	 *            Operator name.
	 */
	private RefactorOperator(String name) {
		this.name = name;

		if (RefactorOperator.operators == null) {
			RefactorOperator.operators = new HashMap<String, RefactorOperator>();
		}

		RefactorOperator.operators.put(name, this);
	}

	/**
	 * Gets the operator name
	 * 
	 * @return Refactor operator.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets predefined refactor operator with name.
	 * 
	 * @param name
	 *            Refactor operator name.
	 * @return Refactor operator instance.
	 */
	public static RefactorOperator getInstance(String name) {
		String upperName = name.toUpperCase();
		return (RefactorOperator) RefactorOperator.operators.get(upperName);
	}

	/**
	 * Checks whether this operation is unary operation.
	 * 
	 * @return True if it is ADD/REMOVE.
	 */
	public boolean isUnary() {
		return this == RefactorOperator.ADD || this == RefactorOperator.REMOVE;
	}

	/**
	 * Checks whether this operation is binary type.
	 * 
	 * @return True if it is RENAME/MOVE.
	 */
	public boolean isBinary() {
		return this == RefactorOperator.MOVE || this == RefactorOperator.RENAME;
	}

	/**
	 * Gets string representation of refactoring operator.
	 * 
	 * @return Refactor operator.
	 */
	public String toString() {
		return this.name;
	}
}
