package besouro.classification.zorro;

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

public class ZorroTDDConformance {

	private Rete engine;
	
	private Episode previsousEpisode;
	
	private List<Episode> episodes = new ArrayList<Episode>();


	public ZorroTDDConformance() {
		this.engine = new Rete();
	    try {
			Batch.batch("besouro/classification/zorro/EpisodeTDDConformance.clp", this.engine);
			Batch.batch("besouro/classification/zorro/TwoWayTDDHeuristicAlgorithm.clp", this.engine);
		} catch (JessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addEpisode(Episode e) {
		
		linkEpisodes(e);
		this.episodes.add(e);
			
		// measures all the stream again
		execute();
		
	}

	private void linkEpisodes(Episode episode) {
		if (previsousEpisode!=null) 
			episode.setPreviousEpisode(previsousEpisode);
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
					
				}
				
			}
			
		} catch (JessException e) {
			throw new RuntimeException(e);
		}
			
	}

	public List<Episode> getRecognizedEpisodes() {
		return episodes;
	}

}
