package besouro.measure;

import besouro.model.Episode;
import jess.Batch;
import jess.Fact;
import jess.QueryResult;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;

public class TDDMeasure {

	private Rete engine;
	
	private float numberOfTDDEpisodes;
	private float numberOfNonTDDEpisodes;

	private float durationOfTDDEpisodes;
	private float durationOfNonTDDEpisodes;

	public TDDMeasure() throws Exception {
		this.engine = new Rete();
	    Batch.batch("besouro/measure/EpisodeTDDConformance.clp", this.engine);
	    Batch.batch("besouro/measure/OneWayTDDHeuristicAlgorithm.clp", this.engine);

	}
	
	public void measure(Episode[] episodes) throws Exception {
		
		for (int i=0 ; i< episodes.length ; i++) {
			Episode e = episodes[i];
		    Fact f = new Fact("EpisodeTDDConformance", engine);
		    f.setSlotValue("index", new Value(i, RU.INTEGER));
		    f.setSlotValue("category", new Value(e.getCategory(), RU.STRING));
		    f.setSlotValue("subtype", new Value(e.getSubtype(), RU.STRING));

			engine.assertFact(f);
		}
		
		engine.run();
		
		numberOfNonTDDEpisodes = 0;
		numberOfTDDEpisodes = 0;
		durationOfNonTDDEpisodes = 0;
		durationOfTDDEpisodes = 0;

		for (int i=0 ; i< episodes.length ; i++) {
			
			QueryResult result = engine.runQueryStar("episode-tdd-conformance-query-by-index", 
					(new ValueVector()).add(new Value(i, RU.INTEGER)));
			
			
			if (result.next()) {
				episodes[i].setIsTDD("True".equals(result.getString("isTDD")));
				
				if (episodes[i].isTDD()) {
					numberOfTDDEpisodes += 1;
					durationOfTDDEpisodes += episodes[i].getDuration();
					
				} else {
					numberOfNonTDDEpisodes += 1;
					durationOfNonTDDEpisodes += episodes[i].getDuration();
				}
			}
			
				
			
		}
		
	}

	public float getTDDPercentageByNumber() {
		return numberOfTDDEpisodes / (numberOfNonTDDEpisodes + numberOfTDDEpisodes);
	}

	public float getTDDPercentageByDuration() {
		return durationOfTDDEpisodes / (durationOfNonTDDEpisodes + durationOfTDDEpisodes);
	}

}
