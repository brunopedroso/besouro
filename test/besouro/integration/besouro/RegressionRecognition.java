package besouro.integration.besouro;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;
import besouro.integration.IntegrationTestBaseClass;


public class RegressionRecognition extends BesouroBaseIntegrationTest {
	
	@Test 
	public void regressionCategory1() throws Exception {
		
		addRegressionCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	@Test 
	public void regressionCategory1_2() throws Exception {
		
		addRegressionCategory1_2_events();
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
		Assert.assertEquals("regression", stream.getEpisodes()[1].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[1].getSubtype());
		
	}

	
	@Test 
	public void regressionCategory2() throws Exception {
		
		addRegressionCategory2Events();
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("regression", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
		// TODO [rule]  redundancy: 2 regressions.
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}

}
