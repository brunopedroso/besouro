package athos.model;

public class Episode {

	private String category;
	private String subtype;
	private boolean isTDD;

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

}
