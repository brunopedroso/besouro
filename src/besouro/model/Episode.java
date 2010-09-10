package besouro.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {

	private String category;
	private String subtype;
	private boolean isTDD;
	private int duration;
	private List<Action> actions = new ArrayList<Action>();
	

	private Episode previousEpisode;

	public void setPreviousEpisode(Episode previousEpisode) {
		this.previousEpisode = previousEpisode;
		
	}
	
	
	public void setClassification(String category, String subtype) {
		this.category = category;
		this.subtype = subtype;
	}
	
	public List<Action> getActions() {
		return actions;
	}

	public Action getLastAction() {
		return actions.get(actions.size()-1);
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
		
		long first;
		
		if (previousEpisode != null){
			first = previousEpisode.getLastAction().getClock().getDate().getTime();
			
		} else if (actions.size()>0) {
			first = actions.get(0).getClock().getDate().getTime();
			
		} else {
			// for testing
			return duration;
			
		}
		
		long last = actions.get(actions.size()-1).getClock().getDate().getTime();
		return (int) (last-first)/1000;
		
		
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
