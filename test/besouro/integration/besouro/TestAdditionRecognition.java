package besouro.integration.besouro;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
public class TestAdditionRecognition extends BesouroBaseIntegrationTest {
	
	
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
		
		Assert.assertEquals(2, stream.getEpisodes().length);
		Assert.assertEquals("test-addition", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2", stream.getEpisodes()[0].getSubtype());
		
		// TODO [rule]  redundancy: 2 test-addition
//		Assert.assertEquals("test-addition", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}

}
