package besouro.model;

import jess.Fact;
import jess.JessException;
import jess.Rete;

// plan:

/**
 * Implements abstract command for build data or cli data.
 * 
 * @author Hongbing Kou
 * @version $Id: Action.java 281 2005-11-10 22:25:19Z hongbing $
 */
public abstract class Action implements Comparable {
	/** Time stamp of the command. */
	private Clock clock;

	/**
	 * Instantiates a command with clock.
	 * 
	 * @param clock
	 *            Time stamp for command.
	 */
	public Action(Clock clock) {
		this.clock = clock;
	}

	/**
	 * Gets timestamp of the command.
	 * 
	 * @return Command time stamp.
	 */
	public Clock getClock() {
		return this.clock;
	}

	/**
	 * Compares two actions objects.
	 * 
	 * @param o
	 *            Other object.
	 * @return Positive if this object is bigger or negative if smaller.
	 */
	public int compareTo(Object o) {
		return this.clock.compareTo(((Action) o).clock);
	}

	// /**
	// * Hashcode of the action.
	// *
	// * @return Action hash code.
	// */
	// public int hashCode() {
	// return this.clock.hashCode();
	// }

	// /**
	// * Test whether two commands happened at the same time.
	// *
	// * @param o Another command object.
	// * @return True if two command objects are equal.
	// */
	// public boolean equals(Object o) {
	// if (o == null || !(o instanceof Action)) {
	// return false;
	// }
	//
	// return this.clock.equals(((Action) o).clock);
	// }
	//

	/**
	 * Gets command string.
	 * 
	 * @return Action string.
	 */
	public String toString() {
		return this.clock.toString();
	}

	/**
	 * Makes Jess facts in the given Jess rete engine.
	 * 
	 * @param index
	 *            Index if this action in episode.
	 * @param engine
	 *            Jess rete engine.
	 * @throws JessException
	 *             If error while constructing jess action.
	 * @return Jess fact of this action.
	 */
	public abstract Fact assertJessFact(int index, Rete engine)
			throws JessException;
}
