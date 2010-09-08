package athos.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {

	private String category;
	private String subtype;
	private boolean isTDD;
	private int duration;
	private List<Action> actions = new ArrayList<Action>();

	public void setClassification(String category, String subtype) {
		this.category = category;
		this.subtype = subtype;
	}

	public String getCategory() {
		return category;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setIsTDD(boolean isTDD) {
		this.isTDD = isTDD;
	}

	public boolean isTDD() {
		return isTDD;
	}

	public void setDuration(int i) {
		this.duration = i;
		
	}

	public int getDuration() {
		if (actions.size()>0) {
			long first = actions.get(0).getClock().getDate().getTime();
			long last = actions.get(actions.size()-1).getClock().getDate().getTime();
			return (int) (last-first)/1000;
		}
		return duration;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[episode] ");
		
		sb.append("(");
		sb.append(getDuration());
		sb.append(") ");
		
		sb.append(getCategory());
		sb.append(" ");
		sb.append(getSubtype());
		
		return sb.toString();
	}

	public void addActions(List<Action> actions) {
		this.actions.addAll(actions);
		
	}
	
}
