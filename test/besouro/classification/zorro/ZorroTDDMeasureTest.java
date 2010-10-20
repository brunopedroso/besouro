package besouro.classification.zorro;

import junit.framework.Assert;

import org.junit.Test;

import besouro.classification.zorro.ZorroTDDMeasure;
import besouro.model.Episode;


public class ZorroTDDMeasureTest {
	
	
	@Test
	public void contextIndependentCase() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		e1.setDuration(10);
		
		Episode e2 = new Episode();
		e2.setClassification("test-first", "1");
		e2.setDuration(10);
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1");
		e3.setDuration(30);
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1");
		e4.setDuration(10);
		
		Episode[] episodes = new Episode[]{e1,e2,e3,e4};
		
		ZorroTDDMeasure measure = new ZorroTDDMeasure();
		measure.measure(episodes);
		
		Assert.assertEquals(0.75, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertEquals(0.5, measure.getTDDPercentageByDuration(), 0.0001);
		
	}
	
	@Test
	public void contextDependentCase() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1");
		
		Episode e3 = new Episode();
		e3.setClassification("test-adition", "1");
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1");
		
		Episode[] episodes = new Episode[]{e1,e2,e3,e4};
		
		ZorroTDDMeasure measure = new ZorroTDDMeasure();
		measure.measure(episodes);
		
		Assert.assertEquals(0.75, measure.getTDDPercentageByNumber(), 0.0001);
		
	}
	
	@Test
	public void contextDependentIncrementalCase() throws Exception {
		
		ZorroTDDMeasure measure = new ZorroTDDMeasure();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		Assert.assertNull(e1.isTDD()); // not tdd yet..
		
		measure.addEpisode(e1);
		
		Assert.assertEquals(1, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertTrue(e1.isTDD()); // TDD
		
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1");
		measure.addEpisode(e2);
		Assert.assertEquals(1, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertTrue(e2.isTDD()); // TDD
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1");
		measure.addEpisode(e3);
		Assert.assertEquals(2f/3f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertFalse(e3.isTDD()); // NOT-tdd
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1"); // TDD
		measure.addEpisode(e4);
		Assert.assertEquals(3f/4f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertTrue(e4.isTDD()); // TDD
		
		Episode e5 = new Episode();
		e5.setClassification("test-addition", "1"); // TDD
		measure.addEpisode(e5);
		Assert.assertEquals(4f/5f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertTrue(e5.isTDD()); // TDD
		
		Episode e6 = new Episode();
		e6.setClassification("test-first", "1"); // TDD
		measure.addEpisode(e6);
		Assert.assertEquals(5f/6f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertTrue(e6.isTDD()); // TDD
		
		Episode e7 = new Episode();
		e7.setClassification("test-last", "1");
		measure.addEpisode(e7);
		Assert.assertEquals(5f/7f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertFalse(e7.isTDD()); // NOT-tdd
		
		Episode e8 = new Episode();
		e8.setClassification("test-addition", "1"); // not-TDD (do not follow a tdd-episode)
		measure.addEpisode(e8);
		Assert.assertEquals(5f/8f, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertFalse(e8.isTDD()); // NOT-tdd
		
	}
	
	@Test
	public void contextDependencyIsRecurrent() throws Exception {
		
		ZorroTDDMeasure measure = new ZorroTDDMeasure();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		measure.addEpisode(e1);
		Assert.assertTrue(e1.isTDD()); // TDD
		
		// every context-sensitive will be tdd from now on.
		for (int i=0 ; i<5 ; i++) {
			Episode e3 = new Episode();
			e3.setClassification("production", "1");
			measure.addEpisode(e3);
			Assert.assertTrue(e3.isTDD()); // TDD because follows a tdd
		}
		
	}
	
	@Test
	public void contextDependencyClassifiesBackwardsAndRecurrently() throws Exception {
		
		ZorroTDDMeasure measure = new ZorroTDDMeasure();
		
		Episode e2 = new Episode();
		e2.setClassification("production", "1");
		measure.addEpisode(e2);
		Assert.assertFalse(e2.isTDD()); // not TDD yet
		
		Episode e3 = new Episode();
		e3.setClassification("production", "1");
		measure.addEpisode(e3);
		Assert.assertFalse(e3.isTDD()); // not TDD yet
		
		Episode e4 = new Episode();
		e4.setClassification("production", "1");
		measure.addEpisode(e4);
		Assert.assertFalse(e4.isTDD()); // not TDD yet
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1");
		measure.addEpisode(e1);
		Assert.assertTrue(e1.isTDD()); // TDD
		
		Assert.assertTrue(e2.isTDD()); // TDD now!
		Assert.assertTrue(e3.isTDD()); // TDD now!
		Assert.assertTrue(e4.isTDD()); // TDD now!
		
	}
	
	
}
