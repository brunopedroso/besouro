package besouro.integration.besouro;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;
import besouro.integration.IntegrationTestBaseClass;


public class TestLastRecognition extends BesouroBaseIntegrationTest {

	@Test 
	public void testLastCategory1() throws Exception {
		
		addTestLast1Actions();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void testLastCategory2() throws Exception {
		
		addTestLast2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-last", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

}
