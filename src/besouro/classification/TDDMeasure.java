package besouro.classification;

import java.util.ArrayList;
import java.util.List;

import besouro.model.Episode;

public class TDDMeasure {
	
	private float numberOfTDDEpisodes;
	private float numberOfNonTDDEpisodes;

	private float durationOfTDDEpisodes;
	private float durationOfNonTDDEpisodes;

	private List<Episode> episodes = new ArrayList<Episode>();
	
	public TDDMeasure() {
		
	}

	public void addEpisode(Episode e) {
		
		this.episodes.add(e);
			
		// measures all the stream again
		execute();
		
	}
	
	private void execute() {
		
		numberOfNonTDDEpisodes = 0;
		numberOfTDDEpisodes = 0;
		durationOfNonTDDEpisodes = 0;
		durationOfTDDEpisodes = 0;
		
		for (int i=0 ; i< episodes.size() ; i++) {
			
			if (episodes.get(i).isTDD()) {
				numberOfTDDEpisodes += 1;
				durationOfTDDEpisodes += episodes.get(i).getDuration();
				
			} else {
				numberOfNonTDDEpisodes += 1;
				durationOfNonTDDEpisodes += episodes.get(i).getDuration();
			}
			
		}
			
	}

	public float getTDDPercentageByNumber() {
		execute();
		float totalEpisodes = numberOfNonTDDEpisodes + numberOfTDDEpisodes;
		if (totalEpisodes == 0) return 0;
		else return numberOfTDDEpisodes / totalEpisodes;
	}

	public float getTDDPercentageByDuration() {
		execute();
		float totalDuration = durationOfNonTDDEpisodes + durationOfTDDEpisodes;
		if (totalDuration == 0) return 0;
		else return durationOfTDDEpisodes / totalDuration;
	}

	public int countEpisodes() {
		return episodes.size();
	}
	
	
}
