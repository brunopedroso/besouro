package besouro.model;
import java.util.Date;
public  class Jacoco {

	private Date clock;

	public Jacoco(Date clock) {
		this.clock = clock;
	}
	
	public Jacoco(){
		this.clock = new Date();
	}

	public Date getClock() {
		return this.clock;
	}

	//TODO clean up unecessary methods from classes.
	public int compareTo(Jacoco o) {
		return this.clock.compareTo(o.clock);
	}

	public String toString() {
		return getClass().getSimpleName() + " " + getClock().getTime();
	}





}



