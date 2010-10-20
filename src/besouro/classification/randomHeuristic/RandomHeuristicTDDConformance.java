package besouro.classification.randomHeuristic;

import besouro.model.Episode;
import besouro.stream.EpisodeListener;

public class RandomHeuristicTDDConformance implements EpisodeListener {

	public void episodeRecognized(Episode e1) {
		if ("test-first".equals(e1.getCategory())) {
			e1.setIsTDD(true);
			
		} else if ("test-last".equals(e1.getCategory())) {
			e1.setIsTDD(false);
			
		} else {
			e1.setIsTDD(Math.random()>=0.5);
			
		}
	}

}
