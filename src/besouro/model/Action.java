package besouro.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
	
	//TODO clean up unecessary methods from classes.
	public int compareTo(Action o) {
		return this.clock.compareTo(o.clock);
	}

	public String toString() {
		return getClass().getSimpleName() + " " + getClock().getTime();
	}
	
	public List<String> getActionDetails() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(new SimpleDateFormat("HH:mm:ss").format(clock));
		return list;
	}

	public static Action fromString(String line) {
		Action action = null;
		
		StringTokenizer tok = new StringTokenizer(line," ");
		String className = tok.nextToken();
		
		if (className.equals("EditAction")) {
			action = new EditAction(tok);
		
		} else if (className.equals("UnitTestCaseAction")) {
			action = new UnitTestCaseAction(tok);
		
		} else if (className.equals("UnitTestSessionAction")) {
			action = new UnitTestSessionAction(tok);
			
		} else if (className.equals("RefactoringAction")) {
			action = new RefactoringAction(tok);
			
		} else if (className.equals("FileOpenedAction")) {
			action = new FileOpenedAction(tok);
			
		} else if (className.equals("CompilationAction")) {
			action = new CompilationAction(tok);
			
		}
		
		return action;
	}

}
