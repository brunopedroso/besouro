package besouro.classification.zorro;

import junit.framework.Assert;

import org.junit.Test;

import besouro.model.Episode;


public class ZorroTDDConformanceTest {
	
	
	@Test
	public void contextIndependentCase() throws Exception {
		
		ZorroTDDConformance conformance = new ZorroTDDConformance();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		e1.setDuration(10);
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("test-first", "1");
		e2.setDuration(10);
		conformance.episodeRecognized(e2);
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1");
		e3.setDuration(30);
		conformance.episodeRecognized(e3);
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1");
		e4.setDuration(10);
		conformance.episodeRecognized(e4);
		
		Assert.assertTrue(e1.isTDD());
		Assert.assertTrue(e2.isTDD());
		Assert.assertFalse(e3.isTDD());
		Assert.assertTrue(e4.isTDD());
		
	}
	
	@Test
	public void contextDependentCase() throws Exception {
		
		ZorroTDDConformance conformance = new ZorroTDDConformance();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1");
		conformance.episodeRecognized(e2);
		
		Episode e3 = new Episode();
		e3.setClassification("test-addition", "1");
		conformance.episodeRecognized(e3);
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1");
		conformance.episodeRecognized(e4);
		
		Assert.assertTrue(e1.isTDD());
		Assert.assertTrue(e2.isTDD());
		Assert.assertTrue(e3.isTDD());
		Assert.assertTrue(e4.isTDD());
		
	}
	
	@Test
	public void contextDependentIncrementalCase() throws Exception {
		
		ZorroTDDConformance conformance = new ZorroTDDConformance();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		Assert.assertNull(e1.isTDD()); // not tdd yet..
		
		conformance.episodeRecognized(e1);
		Assert.assertTrue(e1.isTDD()); // TDD

		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1");
		conformance.episodeRecognized(e2);
		Assert.assertTrue(e2.isTDD()); // TDD
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1");
		conformance.episodeRecognized(e3);
		Assert.assertFalse(e3.isTDD()); // NOT-tdd
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1"); // TDD
		conformance.episodeRecognized(e4);
		Assert.assertTrue(e4.isTDD()); // TDD
		
		Episode e5 = new Episode();
		e5.setClassification("test-addition", "1"); // TDD
		conformance.episodeRecognized(e5);
		Assert.assertTrue(e5.isTDD()); // TDD
		
		Episode e6 = new Episode();
		e6.setClassification("test-first", "1"); // TDD
		conformance.episodeRecognized(e6);
		Assert.assertTrue(e6.isTDD()); // TDD
		
		Episode e7 = new Episode();
		e7.setClassification("test-last", "1");
		conformance.episodeRecognized(e7);
		Assert.assertFalse(e7.isTDD()); // NOT-tdd
		
		Episode e8 = new Episode();
		e8.setClassification("test-addition", "1"); // not-TDD (do not follow a tdd-episode)
		conformance.episodeRecognized(e8);
		Assert.assertFalse(e8.isTDD()); // NOT-tdd
		
	}
	
	@Test
	public void contextDependencyIsRecurrent() throws Exception {
		
		ZorroTDDConformance conformance = new ZorroTDDConformance();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		conformance.episodeRecognized(e1);
		Assert.assertTrue(e1.isTDD()); // TDD
		
		// every context-sensitive will be tdd from now on.
		for (int i=0 ; i<5 ; i++) {
			Episode e3 = new Episode();
			e3.setClassification("production", "1");
			conformance.episodeRecognized(e3);
			Assert.assertTrue(e3.isTDD()); // TDD because follows a tdd
		}
		
	}
	
	@Test
	public void contextDependencyClassifiesBackwardsAndRecurrently() throws Exception {
		
		ZorroTDDConformance conformance = new ZorroTDDConformance();
		
		Episode e2 = new Episode();
		e2.setClassification("production", "1");
		conformance.episodeRecognized(e2);
		Assert.assertFalse(e2.isTDD()); // not TDD yet
		
		Episode e3 = new Episode();
		e3.setClassification("production", "1");
		conformance.episodeRecognized(e3);
		Assert.assertFalse(e3.isTDD()); // not TDD yet
		
		Episode e4 = new Episode();
		e4.setClassification("production", "1");
		conformance.episodeRecognized(e4);
		Assert.assertFalse(e4.isTDD()); // not TDD yet
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		conformance.episodeRecognized(e1);
		Assert.assertTrue(e1.isTDD()); // TDD
		
		Assert.assertTrue(e2.isTDD()); // TDD now!
		Assert.assertTrue(e3.isTDD()); // TDD now!
		Assert.assertTrue(e4.isTDD()); // TDD now!
		
	}
	
	
}
