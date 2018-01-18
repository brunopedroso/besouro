package besouro.integration.besouro;

import org.junit.Assert;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
public class ProductionRecognition extends BesouroBaseIntegrationTest {

	
	@Test 
	public void productionCategory1() throws Exception {
		
		addProductionCategory1Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}

	
	@Test 
	public void productionCategory1WithTestBreak() throws Exception {
		
		addProductionCategory1WithTestBreakEvents();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		addProductionCategory2Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		addProductionCategory2_2_events();
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
//		this one was not considered by hingbings test
//		Assert.assertEquals("refactoring", stream.getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2A", stream.getRecognizedEpisodes().get(1).getSubtype());
		
	}


	@Test 
	public void productionCategory3() throws Exception {
		
		addProductionCategory3Events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("production", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
	}
	
	
}
