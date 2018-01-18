package besouro.classification.besouro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import besouro.model.Episode;


public class BesouroTDDConformanceTest {
	
	
	private BesouroTDDConformance conformance;

	@Before
	public void setup() {
		conformance = new BesouroTDDConformance();
	}
	
	@Test
	public void maintainsContextIndependentCasesFromZorro() throws Exception {
		
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
	public void refactoringsAreTDDConformant() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("refactoring", "1A");
		e1.setDuration(10);
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("refactoring", "1B");
		e2.setDuration(10);
		conformance.episodeRecognized(e2);
		
		Episode e3 = new Episode();
		e3.setClassification("refactoring", "2A");
		e3.setDuration(30);
		conformance.episodeRecognized(e3);
		
		Episode e4 = new Episode();
		e4.setClassification("refactoring", "2B");
		e4.setDuration(10);
		conformance.episodeRecognized(e4);
		
		Episode e5 = new Episode();
		e5.setClassification("refactoring", "4");
		e5.setDuration(10);
		conformance.episodeRecognized(e5);
		
		Assert.assertTrue(e1.isTDD());
		Assert.assertTrue(e2.isTDD());
		Assert.assertTrue(e3.isTDD());
		Assert.assertTrue(e4.isTDD());
		Assert.assertTrue(e5.isTDD());
		
	}
	
	@Test
	public void testAdditionsAreTDDConformant() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("test-addition", "1");
		e1.setDuration(10);
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("test-addition", "2");
		e2.setDuration(10);
		conformance.episodeRecognized(e2);
		
		Assert.assertTrue(e1.isTDD());
		Assert.assertTrue(e2.isTDD());
		
	}
	
	@Test
	public void RegressionsAreTDDConformant() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("regression", "1");
		e1.setDuration(10);
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("regression", "2");
		e2.setDuration(10);
		conformance.episodeRecognized(e2);
		
		Assert.assertTrue(e1.isTDD());
		Assert.assertTrue(e2.isTDD());
		
	}
	
	@Test
	public void productionsAreNOTTDDConformant() throws Exception {
		
		Episode e1 = new Episode();
		e1.setClassification("production", "1");
		e1.setDuration(10);
		conformance.episodeRecognized(e1);
		
		Episode e2 = new Episode();
		e2.setClassification("production", "2");
		e2.setDuration(10);
		conformance.episodeRecognized(e2);
		
		Episode e3 = new Episode();
		e3.setClassification("production", "3");
		e3.setDuration(10);
		conformance.episodeRecognized(e3);
		
		Assert.assertFalse(e1.isTDD());
		Assert.assertFalse(e2.isTDD());
		Assert.assertFalse(e2.isTDD());
	}
	
	
}
