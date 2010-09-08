package athos.model;

public class Episode {

	private String category;
	private String subtype;
	private boolean isTDD;
	private int duration;

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
	
}
