package athos.model.refactor;

import java.util.HashMap;

/**
 * Defines refactoring subject.
 * 
 * @author Hongbing Kou
 * @version $Id: RefactorSubjectType.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class RefactorSubjectType {
	/** Refactor on class. */
	public static final RefactorSubjectType CLASS = new RefactorSubjectType(
			"CLASS");
	/** Refactor on method. */
	public static final RefactorSubjectType METHOD = new RefactorSubjectType(
			"METHOD");
	/** Refactor on field. */
	public static final RefactorSubjectType FIELD = new RefactorSubjectType(
			"FIELD");
	/** Refactor on imports. */
	public static final RefactorSubjectType IMPORT = new RefactorSubjectType(
			"IMPORT");
	public static final RefactorSubjectType PACKAGE = new RefactorSubjectType(
			"PACKAGE");

	/** Gets the set of refactor subject. */
	private static HashMap<String, RefactorSubjectType> subjects;

	/** Subject name. */
	private String name;

	/**
	 * Constructs a RefactorSubject.
	 * 
	 * @param name
	 *            Subject name.
	 */
	private RefactorSubjectType(String name) {
		this.name = name;
		if (RefactorSubjectType.subjects == null) {
			RefactorSubjectType.subjects = new HashMap<String, RefactorSubjectType>();
		}

		RefactorSubjectType.subjects.put(name, this);
	}

	/**
	 * Gets refactor subject name.
	 * 
	 * @return Refactor subject name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets refactor subject instance.
	 * 
	 * @param type
	 *            Refactor subject type string.
	 * @return Refactor subject type.
	 */
	public static RefactorSubjectType getInstance(String type) {
		String upperType = type.toUpperCase();
		if (!RefactorSubjectType.subjects.containsKey(upperType)) {
			RefactorSubjectType subjectType = new RefactorSubjectType(upperType);
			RefactorSubjectType.subjects.put(upperType, subjectType);
		}

		return (RefactorSubjectType) RefactorSubjectType.subjects
				.get(upperType);
	}

	/**
	 * String representation of subject type.
	 * 
	 * @return Subject type string.
	 */
	public String toString() {
		return this.name;
	}
}
