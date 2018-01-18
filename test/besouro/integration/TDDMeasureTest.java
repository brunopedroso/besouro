package besouro.integration;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.eclipse.core.resources.ResourcesPlugin;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import besouro.classification.zorro.ZorroEpisodeClassifierStream;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourcesPlugin.class)
public class TDDMeasureTest extends IntegrationTestBaseClass {

	@Test
	public void testIncrementalMeasure() throws Exception {
	
		addTestFirst1Actions();
		Assert.assertEquals(1, ((ZorroEpisodeClassifierStream)stream).getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addRefactoring1A_Actions();
		Assert.assertEquals(1, ((ZorroEpisodeClassifierStream)stream).getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addTestLast1Actions();
		Assert.assertEquals(2f/3f, ((ZorroEpisodeClassifierStream)stream).getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addRefactoring1A_Actions();
		Assert.assertEquals(2f/4f, ((ZorroEpisodeClassifierStream)stream).getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		
	}

	
}
