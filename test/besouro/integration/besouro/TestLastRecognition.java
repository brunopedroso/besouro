package besouro.integration.besouro;

import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
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
