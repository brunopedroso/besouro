package besouro.integration.besouro;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import besouro.classification.besouro.BesouroEpisodeClassifierStream;
import besouro.integration.IntegrationTestBaseClass;

public class TestAdditionRecognition extends IntegrationTestBaseClass {
	
	@Before 
	@Override
	public void setup() throws Exception {
		BesouroEpisodeClassifierStream stream = new BesouroEpisodeClassifierStream();
		setup(stream);
	}	
	
	@Test 
	public void testAditionCategory1() throws Exception {
		
		addTestAddCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-addition", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	
	@Test 
	public void testAditionCategory2() throws Exception {
		
		addTestAddCategory2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("test-addition", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
		// TODO [rule]  redundancy: 2 test-addition
//		Assert.assertEquals("test-addition", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}

}
