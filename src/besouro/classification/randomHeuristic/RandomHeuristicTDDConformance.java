package besouro.classification.randomHeuristic;

import besouro.model.Episode;

public class RandomHeuristicTDDConformance {

	public void addEpisode(Episode e1) {
		if ("test-first".equals(e1.getCategory())) {
			e1.setIsTDD(true);
			
		} else if ("test-last".equals(e1.getCategory())) {
			e1.setIsTDD(false);
			
		} else {
			e1.setIsTDD(Math.random()>=0.5);
			
		}
	}

}
