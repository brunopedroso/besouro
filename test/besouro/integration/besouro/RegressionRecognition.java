package besouro.integration.besouro;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
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
