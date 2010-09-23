package besouro.zorro;

// TODO [rule] a single regression is not TDD?

import java.util.ArrayList;
import java.util.List;

import jess.Batch;
import jess.Fact;
import jess.JessException;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;
import besouro.model.Episode;

public class ZorroTDDMeasure {

	private Rete engine;
	
	private Episode previsousEpisode;
	
	private float numberOfTDDEpisodes;
	private float numberOfNonTDDEpisodes;

	private float durationOfTDDEpisodes;
	private float durationOfNonTDDEpisodes;

	private List<Episode> episodes = new ArrayList<Episode>();

	private boolean executed;


	public ZorroTDDMeasure() throws Exception {
		this.engine = new Rete();
	    Batch.batch("besouro/zorro/EpisodeTDDConformance.clp", this.engine);
	    Batch.batch("besouro/zorro/TwoWayTDDHeuristicAlgorithm.clp", this.engine);
	}
	
	public void measure(Episode[] eps) {
		for (Episode e : eps) {
			addEpisode(e);
		}
	}

	public void addEpisode(Episode e) {
		
		linkEpisodes(e);
		this.episodes.add(e);
			
		// measures all the stream again
		execute();
		
	}

	private void linkEpisodes(Episode episode) {
		if (previsousEpisode!=null) episode.setPreviousEpisode(previsousEpisode);
		previsousEpisode = episode;
	}

	
	private void assertJessFact(Episode e, int currentFactIndex) throws JessException {
		Fact f = new Fact("EpisodeTDDConformance", engine);
		f.setSlotValue("index", new Value(currentFactIndex, RU.INTEGER));
		f.setSlotValue("category", new Value(e.getCategory(), RU.STRING));
		f.setSlotValue("subtype", new Value(e.getSubtype(), RU.STRING));
		
		engine.assertFact(f);
	}

	private void execute() {
		
		numberOfNonTDDEpisodes = 0;
		numberOfTDDEpisodes = 0;
		durationOfNonTDDEpisodes = 0;
		durationOfTDDEpisodes = 0;
		
		try {
			
			engine.reset();
			
			for (int i=0 ; i< episodes.size() ; i++) {
				assertJessFact(this.episodes.get(i), i);
			}
			
			engine.run();
			
			for (int i=0 ; i< episodes.size() ; i++) {
				
				QueryResult result = engine.runQueryStar("episode-tdd-conformance-query-by-index", (new ValueVector()).add(new Value(i, RU.INTEGER)));
				
				if (result.next()) {
					
					if ("True".equals(result.getString("isTDD"))) {
						episodes.get(i).setIsTDD(true);
						
					} else if ("False".equals(result.getString("isTDD"))) {
						episodes.get(i).setIsTDD(false);
						
					} else {
						// unclassified remains isTdd? == null
					}
					
					
					if (episodes.get(i).isTDD()) {
						numberOfTDDEpisodes += 1;
						durationOfTDDEpisodes += episodes.get(i).getDuration();
						
					} else {
						numberOfNonTDDEpisodes += 1;
						durationOfNonTDDEpisodes += episodes.get(i).getDuration();
					}
				}
				
			}
			
		} catch (JessException e) {
			throw new RuntimeException(e);
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

	public List<Episode> getRecognizedEpisodes() {
		return episodes;
	}

}
