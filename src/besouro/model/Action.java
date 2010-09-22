package besouro.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		return "action";
	}
	
	public List<String> getActionDetails() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(new SimpleDateFormat("HH:mm:ss").format(clock));
		return list;
	}
}
