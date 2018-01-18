package besouro.classification.randomHeuristic;

import org.junit.Assert;
import org.junit.Test;

import besouro.model.Episode;


public class RandomHeuristicTDDConformanceTest {
	
	
	@Test
	public void testFirstIsConformant() throws Exception {
		
		RandomHeuristicTDDConformance conformance = new RandomHeuristicTDDConformance();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		conformance.episodeRecognized(e1);
		
		Assert.assertTrue(e1.isTDD());
		
	}
	
	@Test
	public void testLastIsNonconformant() throws Exception {
		
		RandomHeuristicTDDConformance conformance = new RandomHeuristicTDDConformance();
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1");
		conformance.episodeRecognized(e3);
		
		Assert.assertFalse(e3.isTDD());
		
	}
	
	@Test
	public void otherClassificationsAreRandom() throws Exception {
		isRandom("regression");
		isRandom("test-addition");
		isRandom("production");
		isRandom("refactoring");
	}

	private void isRandom(String classification) {
		
		Episode e3 = new Episode();
		e3.setClassification(classification, "1");

		RandomHeuristicTDDConformance conformance = new RandomHeuristicTDDConformance();

		int conformantCount = 0;
		int nonconformantCount = 0;
		
		for (int i = 0; i < 10; i++) {
			conformance.episodeRecognized(e3);
			if (e3.isTDD())
				conformantCount++;
			else
				nonconformantCount++;
		}
		
		// in 10 classification at least one should be classified with each one
		Assert.assertTrue(conformantCount>0);
		Assert.assertTrue(nonconformantCount>0);
	}


}
