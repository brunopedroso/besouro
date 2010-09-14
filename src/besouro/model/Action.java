package besouro.model;

import java.util.Date;

import jess.Fact;
import jess.JessException;
import jess.Rete;

/**
 * Implements abstract command for build data or cli data.
 * 
 * @author Hongbing Kou
 */
public abstract class Action implements Comparable<Action> {

	private Date clock;

	public Action(Date clock) {
		this.clock = clock;
	}

	public Date getClock() {
		return this.clock;
	}

	public int compareTo(Action o) {
		return this.clock.compareTo(o.clock);
	}

	public String toString() {
		return this.clock.toString();
	}

	public abstract Fact assertJessFact(int index, Rete engine) throws JessException;
}
