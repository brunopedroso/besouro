package besouro.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {

	private String category;
	private String subtype;
	private Boolean isTDD;
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
		if (actions.size()==0) return null;
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

	public Boolean isTDD() {
		return isTDD;
	}

	public void setDuration(int i) {
		this.duration = i;
		
	}

	public int getDuration() {
		
		long first;
		
		if (previousEpisode != null && previousEpisode.getLastAction()!=null){
			first = previousEpisode.getLastAction().getClock().getTime();
			
		} else if (actions.size()>0) {
			first = actions.get(0).getClock().getTime();
			
		} else {
			// used for testing
			return duration;
			
		}
		
		long last = actions.get(actions.size()-1).getClock().getTime();
		return (int) (last-first)/1000;
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		
		sb.append(getCategory());
		sb.append(" ");
		sb.append(getSubtype());
		
		sb.append(" ");
		sb.append("(");
		sb.append(getDuration());
		sb.append("s) ")
		;
		return sb.toString();
	}

	public void addActions(List<Action> actions) {
		this.actions.addAll(actions);
		
	}
	
}
