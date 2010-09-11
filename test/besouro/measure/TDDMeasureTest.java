package besouro.measure;

import junit.framework.Assert;

import org.junit.Test;

import besouro.measure.TDDMeasure;
import besouro.model.Episode;


public class TDDMeasureTest {
	
	
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
		
		TDDMeasure measure = new TDDMeasure();
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
		
		TDDMeasure measure = new TDDMeasure();
		measure.measure(episodes);
		
		Assert.assertEquals(0.75, measure.getTDDPercentageByNumber(), 0.0001);
		
	}
	
	@Test
	public void contextDependentIncrementalCase() throws Exception {
		
		TDDMeasure measure = new TDDMeasure();
		
		Episode e1 = new Episode();
		e1.setClassification("test-first", "1"); // TDD
		measure.addEpisode(e1);
		Assert.assertEquals(1, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1"); // TDD
		measure.addEpisode(e2);
		Assert.assertEquals(1, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e3 = new Episode();
		e3.setClassification("test-last", "1"); // not-TDD
		measure.addEpisode(e3);
		Assert.assertEquals(2f/3f, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e4 = new Episode();
		e4.setClassification("test-first", "1"); // TDD
		measure.addEpisode(e4);
		Assert.assertEquals(3f/4f, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e5 = new Episode();
		e5.setClassification("test-addition", "1"); // TDD
		measure.addEpisode(e5);
		Assert.assertEquals(4f/5f, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e6 = new Episode();
		e6.setClassification("test-first", "1"); // TDD
		measure.addEpisode(e6);
		Assert.assertEquals(5f/6f, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e7 = new Episode();
		e7.setClassification("test-last", "1"); // TDD
		measure.addEpisode(e7);
		Assert.assertEquals(5f/7f, measure.getTDDPercentageByNumber(), 0.0001);
		
		Episode e8 = new Episode();
		e8.setClassification("test-addition", "1"); // not-TDD (do not follow a tdd-episode)
		measure.addEpisode(e8);
		Assert.assertEquals(5f/8f, measure.getTDDPercentageByNumber(), 0.0001);
		
	}
	
	
}
